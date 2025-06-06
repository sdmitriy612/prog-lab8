package ru.sdmitriy612.server;

import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;
import ru.sdmitriy612.io.ByteSerializator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sdmitriy612.server.handling.Processor;

/**
 * Main server class for handling client requests and console input.
 * Listens for UDP requests and console commands.
 */
public class UDPServer extends Thread{
    private static final Logger logger = LogManager.getLogger(UDPServer.class);
    private static final ForkJoinPool readPool = new ForkJoinPool();
    private static final ForkJoinPool sendPool = new ForkJoinPool();

    private final int port;
    /**
     * Constructs the server with a specified port.
     *
     * @param p Port number to listen on
     */
    public UDPServer(int p){
        port = p;
    }

    /**
     * Starts the server, listening for UDP requests and console input.
     * Runs an infinite loop to process data.
     */
    public void run(){
        InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
        logger.info("Server started");

        try(DatagramChannel datagramChannel = DatagramChannel.open();
            Selector selector = Selector.open()) {

            datagramChannel.bind(inetSocketAddress);
            datagramChannel.configureBlocking(false);
            datagramChannel.register(selector, SelectionKey.OP_READ);

            while (true){
                try {
                    selector.select();
                    Set<SelectionKey> keys = selector.selectedKeys();

                    for(Iterator<SelectionKey> iterator = keys.iterator(); iterator.hasNext();){
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if(selectionKey.isValid() && selectionKey.isReadable()){
                            readPool.submit(() -> {
                                try {
                                    readData((DatagramChannel) selectionKey.channel());
                                } catch (IOException e) {
                                    logger.error("Handling input error: {}", e.getMessage());
                                }
                            });
                        }
                    }
                }catch (IOException e){
                    logger.error("Receiving error: {}", e.getMessage());
                }
            }

        }catch (Exception e){
            logger.error("UDPServer error: ", e.getMessage());
        }
    }

    private void readData(DatagramChannel datagramChannel) throws IOException {
        int bufferSize = 8192;
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        SocketAddress address = datagramChannel.receive(buffer);
        if (address == null) return;

        logger.debug("Server received data from {}", address);

        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        try {
            ByteSerializator<Request> requestSerializator = new ByteSerializator<>(Request.class);
            Request request = requestSerializator.deserialize(data, data.length);
            Thread processingThread = new Thread(() -> {
                Response response = Processor.process(request, false);
                sendPool.submit(() -> {
                    sendData(datagramChannel, address, response);
                });
            });
            processingThread.start();
        }catch (Exception e) {
            logger.error("Reading data error for {}: {}", address, e.getMessage(), e);
            throw new IOException(e);
        }
    }

    private void sendData(DatagramChannel datagramChannel, SocketAddress address, Response response){
        try {
            ByteSerializator<Response> responseSerializator = new ByteSerializator<>(Response.class);
            ByteBuffer responseBuffer = ByteBuffer.wrap(responseSerializator.serialise(response));

            datagramChannel.send(responseBuffer, address);
            logger.info("Server sent response to client");
        }catch (Exception e){
            logger.error("Sending data error for {}: {}", address, e.getMessage(), e);
        }

    }

}
