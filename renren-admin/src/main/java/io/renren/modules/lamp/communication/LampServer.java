package io.renren.modules.lamp.communication;

/**
 * @Date 2019/12/17 16:07
 * @
 */

import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LampServer extends Thread
{
    // create the serverSocket
    private ServerSocket serverSocket;

    // constructor
    public LampServer(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
    }

    // the method run that inherited from Thread
    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                // display the information of server
                System.out.println("等待Client连接，服务器端口号为：" + serverSocket.getLocalPort() + "...");

                // 堵塞队列用来存放读到的数据
                BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>();
                while (true) {
                    // util the client connect the port of server
                    Socket server = serverSocket.accept();

                    // return the IP/address of the serverSocket connection  == it's the client's IP
                    System.out.println("Client地址：" + server.getRemoteSocketAddress());

                    // receive information from client
                    InputStream in = new DataInputStream(server.getInputStream());

                    BufferedReader inn  = new  BufferedReader(new InputStreamReader(in));
                    System.out.println(inn.readLine());

                    // send the information to client
                    //server.getLocalSocketAddress()
                    BufferedWriter out  = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
                    out.write("Received the information.：" + "\nGoodbye!");

//                    DataOutputStream out = new DataOutputStream(server.getOutputStream());
//                    out.writeUTF("Received the information.：" + server.getLocalSocketAddress() + "\nGoodbye!");

                    // close
                    server.close();
                }

            }catch(SocketTimeoutException s)
            {
                System.out.println("Socket timed out!");
                break;
            }catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }
    public static void main(String [] args)
    {
        int port = Integer.parseInt("6000");
        try
        {
            Thread t = new LampServer(port);
            t.start();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
