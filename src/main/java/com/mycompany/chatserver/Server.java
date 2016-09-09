package com.mycompany.chatserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    //SINGLETON (Eksisterer pga. brug af arraylist)
    private static Server instance = null;

    private Server() {
    }

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    //ARRAYLIST
    private static ArrayList<SocketConnection> socketList;

    public ArrayList<SocketConnection> getList() {
        return socketList;
    }

    //SERVER
//    static String ip = "localhost";
//    static int portNum = 8080;
    public static void main(String[] args) throws IOException {
        if (args.length == 2) {
            String ip = args[0];
            int portNum = Integer.parseInt(args[1]);

            socketList = new ArrayList();

            ServerSocket ss = new ServerSocket();
            ss.bind(new InetSocketAddress(ip, portNum));

            System.out.println("Server started - listening on port " + portNum + " bound to ip " + ip);

            while (true) {
                Socket link = ss.accept();
                new SocketConnection(link).start();
            }
        }
    }

}
