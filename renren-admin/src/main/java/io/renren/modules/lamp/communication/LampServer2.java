package io.renren.modules.lamp.communication;

import oracle.net.ns.Message;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @Date 2019/12/17 21:24
 * @ 废弃类
 */

public class LampServer2 {
    // serverSocket
    ServerSocket serverSocket = null;

    // restore has been initialed socket
    static HashMap<Integer, Socket> socketMap = new HashMap<Integer, Socket>();

    public void socket() throws IOException {
        // initiate serverSocket
        serverSocket = new ServerSocket(6060);
        System.out.println("LampServer is starting...");

        // the number of client
        int num = 1;

        // implement multiple clients connection
        while (true) {
            Socket socket = serverSocket.accept(); // create the socket of server
            System.out.println("client" + num + " Connected...");

            if (socket != null) {
                // put socket into map as value, and the num as key
                socketMap.put(num, socket);

                // create the thread to process the session
                Thread thread = new HandlerConnection(socket, socketMap);
                // setDaemon
//                thread.setDaemon(true);
                thread.start();
                num++;
            }
        }

    }

    public static void main(String[] args) throws IOException {
        new LampServer2().socket();
    }
}

/**
 * process the session
 */
class HandlerConnection extends Thread {
    Socket socket = null;
    InputStream in = null;
    OutputStream out = null;
    HashMap<Integer, Socket> socketMap = null;

    // constructor
    public HandlerConnection(Socket socket, HashMap<Integer, Socket> socketMap) {
        this.socket = socket;
        this.socketMap = socketMap;
    }

    @Override
    public void run() {
        try {

            System.out.println("===");
            while (true) {
                System.out.println("+++++");
                in = socket.getInputStream();

                if ( in != null) {
                    BufferedReader inBuffer  = new  BufferedReader(new InputStreamReader(new DataInputStream(in)));
                    String s = inBuffer.readLine();
                    System.out.println("-------");
                    System.out.println("Client Data:" + s);  // the date from client
                }
                System.out.println("************");
                if (socket.getOutputStream() != null) {
                    BufferedWriter out  = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    out.write("Received the information.：" + "\nGoodbye!"); // send to server
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

