package com.mycompany.chatserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends Thread {

    Socket s;
    Username user;
    //Takes the arraylist created in the Server class.
    ArrayList<Username> userList = Server.getInstance().getList();

    public Client(Socket s) {
        this.s = s;
    }

    //Thread starter
    public void run() {
        System.out.println("New client connection");
        chatroom(s);
        removeClient();
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

                //Show list of clients
                if (msg.equals("CLIENTLIST:")) {
                    prnt.println(showClients());
                }

                //Wether or not the user already exists
                if (msg.contains("LOGIN:")) {
                    String[] parts = msg.split(":");
                    //authentication() is a method in the Client class (See line 146)
                    if (authentication(parts[1])) {
                        userList.add(user = new Username(parts[1]));
                        login = true;
                        prnt.println("Login Successful");
                    } else {
                        prnt.println("User already exists. Please reconnect.");
                        break;
                    }
                }
            }

            //Check if login is successful or not
            if (login) {
                prnt.println("Welcome to the chatroom");
                prnt.println(showClients());
                //Chatting phase
                while (!msg.contains("LOGOUT:")) {
                    msg = scn.nextLine();

                    //If the user tries to login again.
                    if (msg.contains("LOGIN:")) {
                        prnt.println("You're already logged in as " + user.getUsername());
                    }

                    //To ensure that msg does not only contain these keywords.
                    if (msg.equals("MSG:") || msg.equals("LOGIN:")) {
                        prnt.println("Please specify your command");
                    }

                    //Message Part
                    if (msg.contains("MSG:")) {
                        String[] parts = msg.split(":");
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

                    //Message ALL
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
            prnt.println("Connection closed");

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

    //Remove client from our list
    public void removeClient() {
        for (Username u : userList) {
            if (u.getUsername().equals(user.getUsername())) {
                userList.remove(u);
                System.out.println("Removed " + user.getUsername() + " from the list of clients");
                break;
            }
        }

    }

    //Check whether or not the user is in our list of clients.
    public boolean authentication(String user) {
        //If the user is not in the list, return true, otherwise false
        for (Username username : userList) {
            if (username.getUsername().equals(user)) {
                return false;
            }
        }
        return true;
    }
}
