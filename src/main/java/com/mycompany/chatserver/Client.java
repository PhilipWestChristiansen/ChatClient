package com.mycompany.chatserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends Thread {

    Socket s;
    Username user;
    ArrayList<Username> userList = Server.getInstance().getList();

    public Client(Socket s) {
        this.s = s;
    }

    public void run() {
        System.out.println("New client connection");
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
            boolean login = false;
            while (!login) {
                msg = scn.nextLine();
                if (msg.contains("LOGIN:")) {
                    String[] parts = msg.split(":");
                    user = new Username(parts[1]);
                    if (!userList.contains(user)) {
                        userList.add(user);
                        prnt.println(showClients());
                        login = true;
                    } else {
                        prnt.println("User already exists");
                        login = false;
                    }
                }
            }

            //Chatting phase
            if (login) {
                prnt.println("Welcome to the chatroom");
                while (!msg.contains("LOGOUT")) {
                    msg = scn.nextLine();

                    //If the user tries to login again.
                    if (msg.contains("LOGIN:")) {
                        prnt.println("You're already logged in as " + user.getUsername());
                    }

                    //To ensure that msg does not only contain these keywords.
                    if (msg.equals("MSG:") || msg.equals("LOGIN:")) {
                        prnt.println("Please specify your command");
                        continue;
                    }

                    //Message Part
                    if (msg.contains("MSG:")) {
                        //MSG:Casper,Philip,Hamza:Hej
                        //parts array: { "MSG" , "Casper,Philip,Hamza" , "Hej"}
                        String[] parts = msg.split(":");

                        //clients array: { "Casper" , "Hamza" , "Philip"}
                        String[] clients = parts[1].split(",");

                        //Check if client exists in our list of clients
                        for (int i = 0; i < clients.length; i++) {
                            String client = clients[i];
                            for (int j = 0; j < userList.size(); j++) {
                                if (userList.get(i).getUsername().equals(client)) {
                                    //Insert method here that sends a message to that specific user
                                    //Example method: send(String client, String message);
                                    //message = parts[2];
                                    continue;
                                }
                            }
                        }

                    }

                    //Message ALL part
                    if (msg.contains("MSG::")) {
                        String[] parts = msg.split(":");
                        for (Username user : userList) {
                            //send(client.getUser, parts[2]);
                        }
                    }

                    //Get list of clients
                    if (msg.equals("CLIENTLIST:")) {
                        prnt.println(showClients());
                    }

                }
            }

            //REMOVE CLIENT FROM CLIENTLIST
            userList.remove(user);
            System.out.println("Removed " + user.getUsername() + " from the list of clients");

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
        for (Username username : userList) {
            users += username.getUsername() + ",";
        }
        return users;
    }
}
