package org.client.utils;

import java.io.*;
import java.net.Socket;
import javax.swing.JOptionPane;

public class SocketClient {
    private final String host;
    private final int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        System.out.println("Attempting to connect to " + host + ":" + port);
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Successfully connected to " + host + ":" + port);
        } catch (IOException e) {
            System.err.println("Failed to connect to " + host + ":" + port + " - " + e.getMessage());
            throw e; // Rethrow to be handled by the caller
        }
    }

    public String sendRequest(String request) {
        try {
            if (socket == null || socket.isClosed()) {
                System.out.println("Socket is null or closed, attempting to reconnect...");
                connect();
            }
            System.out.println("Sending request: " + request);
            out.println(request);
            String response = in.readLine();
            System.out.println("Received response: " + response);
            return response;
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
            String errorMessage = "Cannot connect to server at " + host + ":" + port + ".\n" +
                               "Please ensure:\n" +
                               "1. The server application is running\n" +
                               "2. The server's 'Start Server' button has been clicked\n" +
                               "3. There are no firewall or network issues\n" +
                               "4. The port number " + port + " is correct";
            
            JOptionPane.showMessageDialog(null, errorMessage, "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 