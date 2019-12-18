package io.renren.modules.lamp.communication;

/**
 * @Date 2019/12/17 16:06
 * @
 *  Remember the inputStream/outputStream need to be wrapped by DataInputStream/DataOutputStream,
 *  then the data could be in.readUTF()/out.writeUTF()
 */

import java.net.*;
import java.io.*;

public class GreetingClient
{
    public static void main(String [] args)
    {
        String serverName = args[0];  //  IP address
        int port = Integer.parseInt(args[1]);  // port number
        try
        {
            // display the information of connecting
            System.out.println("连接到主机：" + serverName + " ，端口号：" + port);

            // create Socket object and connect the specified IP and port
            Socket client = new Socket(serverName, port);

            // return the address of the socket connecting (client)
            System.out.println("远程主机地址：" + client.getRemoteSocketAddress());

            // return the socket's outputStream
            OutputStream outToServer = client.getOutputStream();

            // send information to server
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("Hello from " + client.getLocalSocketAddress()); // display the IP of client(socket)

            // receive information from server
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            System.out.println("服务器响应： " + in.readUTF());

            // close socket
            client.close();

        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}

