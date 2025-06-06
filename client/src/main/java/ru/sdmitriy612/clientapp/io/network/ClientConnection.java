package ru.sdmitriy612.clientapp.io.network;

import ru.sdmitriy612.io.ByteSerializator;
import ru.sdmitriy612.interactors.Interactor;
import ru.sdmitriy612.io.IOController;

import java.io.*;
import java.net.*;

/**
 * Handles UDP network communication on the client side.
 * Responsible for sending serialized {@link Interactor} objects to the server
 * and receiving responses over a {@link DatagramSocket}.
 */
public class ClientConnection implements IOController<Interactor> {
    private DatagramSocket datagramSocket;
    private InetAddress host;
    private final int port = 8081;
    private final ByteSerializator<Interactor> byteSerializator = new ByteSerializator<>(Interactor.class);

    /**
     * Initializes the UDP connection to the server at localhost on the predefined port.
     */
    public ClientConnection() {
        try {
            datagramSocket = new DatagramSocket();
            host = InetAddress.getByName("localhost");
        } catch (Exception e) {
            System.out.println("Init error: " + e.getMessage());
        }
    }

    /**
     * Receives a UDP packet from the server and deserializes it into an {@link Interactor}.
     *
     * @return the received interactor from the server
     * @throws IOException if there is a problem receiving or deserializing the packet
     */
    @Override
    public Interactor read() throws IOException {
        byte[] bytes = new byte[8192];
        int len = bytes.length;
        DatagramPacket datagramPacket = new DatagramPacket(bytes, len);
        datagramSocket.setSoTimeout(3000);
        datagramSocket.receive(datagramPacket);

        return byteSerializator.deserialize(bytes, datagramPacket.getLength());

    }

    /**
     * Serializes and sends an {@link Interactor} object to the server using UDP.
     *
     * @param data the interactor to send
     * @throws IOException if there is a problem during sending
     */
    @Override
    public void write(Interactor data) throws IOException {

        byte[] bytes = byteSerializator.serialise(data);
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, host, port);
        datagramSocket.send(packet);

    }

    /**
     * Closes the underlying datagram socket.
     *
     * @throws Exception if an error occurs while closing the socket
     */
    @Override
    public void close() throws Exception {
        datagramSocket.close();
    }
}
