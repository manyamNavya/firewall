package com.firewall.app.entity;

// Client.java
import java.net.Socket;

public class Client {

    private String clientIP;
    private Socket clientSocket;

    public Client(String clientIP, Socket clientSocket) {
        this.clientIP = clientIP;
        this.clientSocket = clientSocket;
    }

    public String getClientIP() {
        return clientIP;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
