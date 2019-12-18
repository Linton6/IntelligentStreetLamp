package io.renren.modules.lamp.communication;

/**
 * @Date 2019/12/17 20:18
 * @
 */

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

@Service("server")
public class Server {
    String data = "the initial data!";
    public void setData(String str) {
        this.data = str;
    }

    public String getData() {
        return data;
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.handler();
    }

    /**
     * 存在问题：当连接的设备一个断开了，就会导致服务抛出异常其他的客户端也断开
     * @return
     */
    public  String handler() {
        Thread server = new Thread(new HandlerConnection());
        server.start();
        return "连接成功！"; // 最好做个判断
    }

    private class HandlerConnection implements Runnable {

        @Override
        public void run() {
            try {
                server();
            } catch (IOException e) {
                System.err.println("与lora通信出现问题！");
                e.printStackTrace();
            }
        }
    }

    // 需要增加判断接受和写入的情况
    private   void server() throws IOException {
        // create the server channel 服务器套接字通道是通过调用该类的open方法创建。
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        // set non-blocking
        ssChannel.configureBlocking(false);

        // the serverChannel bind the port 一个新创建的服务器套接字通道是打开的，但尚未绑定。 结合通道的插座到本地地址和配置套接字监听连接。
        ssChannel.bind(new InetSocketAddress(6060));

        // 打开选择器。
        Selector selector = Selector.open();

        // 用所给的选择器来注册这个通道，返回一个选择键。
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);  //每次在一个通道注册一个选择器时，创建一个选择键。??
        // OP_ACCEPT 套接字接受操作的操作设置位。




        // 轮询式的获取选择器上已经准备就绪的事件
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 很重要！！！删除已选择的 key，以防重复处理
                iterator.remove();
                // 判断具体是什么事件准备就绪
                if (key.isAcceptable()) {  // 测试此键的通道是否准备接受一个新的套接字连接。
                    // 接收新客户端，获取客户端连接
                    SocketChannel socketChannel = ssChannel.accept();
                    // 设置为非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 将该通道注册到选择器上
                    int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
                    socketChannel.register(selector, interestSet);
                } else if (key.isReadable()) { //测试此键的通道是否已准备好阅读。
                    // Received the information from client
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len=0;
                    while ((len = socketChannel.read(buffer)) > 0) {
                        buffer.flip();
                        // received the data from client
                        System.out.println(new String(buffer.array(), 0, len));
                        buffer.clear();
                    }

                } else if (key.isWritable()) { //测试此键的通道是否已准备好写入。

                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    ByteBuffer src = null;
                    if (data != null){
                         src = Charset.forName("utf8").encode(getData());
                        socketChannel.write(src);
                        System.out.println(data);
//                        src = null;
                        data = null;
                    }


//                    if (src != null) {
//                        int length = 0;
//                        length = socketChannel.write(src);  // 放进buffer里吧
//                        System.out.println(data);
//                        src = null;
//                        data = null;
//                    }



                }
            }
        }
    }
}

