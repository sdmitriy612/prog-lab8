package ru.sdmitriy612.server.handling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sdmitriy612.io.ByteSerializator;
import ru.sdmitriy612.interactors.Request;
import ru.sdmitriy612.interactors.Response;

import java.io.*;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import java.util.concurrent.ForkJoinPool;

/**
 * {@code InputHandler} handles UDP communication with clients,
 * processes requests, and sends responses.
 *
 * <p>The class uses Log4j2 for logging server activity such as receiving and processing requests, and sending responses.</p>
 *
 */
public class InputHandler {
    private static final ForkJoinPool pool = new ForkJoinPool();

    private static final Logger logger = LogManager.getLogger(InputHandler.class);
    /**
     * Handles incoming data from a {@link DatagramChannel}, processes it, and sends a response.
     *
     * @param datagramChannel the channel through which data is received from the client
     * @throws IOException if an error occurs while processing the data or sending the response
     */
    public static void handleData(DatagramChannel datagramChannel) throws IOException {
        int bufferSize = 8192;
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        SocketAddress address = datagramChannel.receive(buffer);
        if (address == null) return;

        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);

        try {
            ByteSerializator<Request> requestSerializator = new ByteSerializator<>(Request.class);
            Request request = requestSerializator.deserialize(data, data.length);

            Response response = Processor.process(request, false);
            logger.info("Server processed data. Response: {}", response);

            ByteSerializator<Response> responseSerializator = new ByteSerializator<>(Response.class);
            ByteBuffer responseBuffer = ByteBuffer.wrap(responseSerializator.serialise(response));

            datagramChannel.send(responseBuffer, address);
            logger.info("Server sent response to client");
        } catch (Exception e) {
            logger.error("Processing error for {}: {}", address, e.getMessage(), e);
        }
    }


}
