package com.mycompany.chatserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class SocketConnection extends Thread {

    Socket s;
    String name;
    boolean login = false;

    //Takes the arraylist created in the Server class.
    ArrayList<SocketConnection> clientList = Server.getInstance().getList();

    public SocketConnection(Socket s) {
        this.s = s;
    }

    //Thread start
    public void run() {
        System.out.println("New client connection");
        clientList.add(this);
        chatroom(s);
        System.out.println("Client disconnected");
    }

    public void chatroom(Socket s) {
        try {
            Scanner scn = new Scanner(s.getInputStream());
            PrintWriter prnt = new PrintWriter(s.getOutputStream(), true);
            String msg = "";
            prnt.println("Please login first with this format: LOGIN:<name>");

            //Login Phase
            while (!login) {
                msg = scn.nextLine();

                //Show list of clients
                if (msg.equals("CLIENTLIST:")) {
                    prnt.println(showClients());
                }

                //Wether or not the user already exists
                if (msg.contains("LOGIN:")) {
                    String[] parts = msg.split(":");
                    name = parts[1];
                    login = true;
                } else {
                    prnt.println("Error");
                    break;
                }
            }

            //Check if login is successful or not
            if (login) {
                prnt.println("Welcome to the chatroom");
                showListToAll();

                //Chatting phase
                while (!msg.contains("LOGOUT")) {
                    msg = scn.nextLine();

                    //If the user tries to login again.
                    if (msg.contains("LOGIN:")) {
                        prnt.println("You're already logged in as " + name);
                    }

                    //To ensure that msg does not only contain these keywords.
                    if (msg.equals("MSG:") || msg.equals("LOGIN:")) {
                        prnt.println("Please specify your command");
                    }

                    //Message to 1 or more
                    if (msg.contains("MSG:")) {
                        String[] parts = msg.split(":");
                        String[] users = parts[1].split(",");
                        sendMsg(users, parts[2]);
                    }

                    //Message ALL
                    if (msg.contains("MSG::")) {
                        String[] parts = msg.split(":");
                        sendMsgToAll(parts[2]);
                    }

                    //Get list of clients
                    if (msg.equals("CLIENTLIST:")) {
                        prnt.println(showClients());
                    }
                }
            }

            //Remove from arraylist
            clientList.remove(this);
            prnt.println("Connection closed");
            showListToAll();
            
            //Close Connection
            scn.close();
            prnt.close();
            s.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Show list of clients
    public String showClients() {
        String users = "CLIENTLIST:";
        for (SocketConnection socket : clientList) {
            users += socket.name + ",";
        }
        return users;
    }

    public void showListToAll() {
        try {
            for (SocketConnection socketconnection : clientList) {
                PrintWriter prnt = new PrintWriter(socketconnection.s.getOutputStream(), true);
                prnt.println(socketconnection.showClients());
            }
        } catch (IOException e) {
            //do nothing
        }
    }

    public void sendMsgToAll(String msg) {
        try {

            for (SocketConnection socketconnection : clientList) {
                if (!socketconnection.name.equals(this.name)) {
                    PrintWriter prnt = new PrintWriter(socketconnection.s.getOutputStream(), true);
                    prnt.println("MSGRES:" + this.name + ":" + msg);
                }
            }
        } catch (IOException e) {
            //do nothing
        }
    }

    public void sendMsg(String[] userList, String msg) {
        try {
            for (int i = 0; i < userList.length; i++) {
                String user = userList[i];
                for (SocketConnection client : clientList) {
                    if (client.name.equals(user)) {
                        PrintWriter prnt = new PrintWriter(client.s.getOutputStream(), true);
                        prnt.println("MSGRES:" + this.name + ":" + msg);
                    }
                }
            }
        } catch (IOException e) {
            //do nothing
        }
    }
}
