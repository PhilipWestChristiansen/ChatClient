/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.chatserver;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Casper
 */
public class SocketConnectionTest {

    //static String[] input = {"localhost", "8080"};
//    static Connect con = Connect.getInstance();
//    static SocketConnection connection;
//    static ArrayList<SocketConnection> clientList = Server.getInstance().getList();

    public SocketConnectionTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Server.main(null);
                } catch (IOException ex) {
                    Logger.getLogger(SocketConnectionTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
//        con.connect("localhost", 8080);
//        connection = new SocketConnection(con.getSocket());
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class SocketConnection.
     */
    @org.junit.Test
    public void testRun() {
        System.out.println("run");
//        connection.start();
    }

    /**
     * Test of chatroom method, of class SocketConnection.
     */
//    @org.junit.Test
//    public boolean testChatroom() {
//        System.out.println("chatroom");
//        Socket s = null;
//        SocketConnection instance = sc;
//        instance.chatroom(s);
//    }
    /**
     * Test of showClients method, of class SocketConnection.
     */
    @org.junit.Test
    public void testShowClients() {
        System.out.println("showClients");
//        connection.name = "casper";
        String expResult = "CLIENTLIST:casper,";
//        String result = connection.showClients();
//        assertEquals(expResult, result);
    }

    /**
     * Test of showListToAll method, of class SocketConnection.
     */
    @org.junit.Test
    public void testShowListToAll() throws Exception {
        System.out.println("showListToAll");
//        SocketConnection instance = connection;
//        instance.showListToAll();

    }

    /**
     * Test of sendMsgToAll method, of class SocketConnection.
     */
    @org.junit.Test
    public void testSendMsgToAll() {
        System.out.println("sendMsgToAll");
        String msg = "";
        SocketConnection instance = null;
        instance.sendMsgToAll(msg);
    }

    /**
     * Test of sendMsg method, of class SocketConnection.
     */
    @org.junit.Test
    public void testSendMsg() {
        System.out.println("sendMsg");
        String[] userList = null;
        String msg = "";
        SocketConnection instance = null;
        instance.sendMsg(userList, msg);
    }

}
