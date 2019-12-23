package io.renren.modules.lamp.communication;

/**
 * @Date 2019/12/17 20:18
 * @
 */

import io.renren.modules.lamp.entity.LampEntity;
import io.renren.modules.lamp.service.LampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;

@Service("server")
public class Server  implements CommandLineRunner{
    @Autowired
    private LampService lampService;


    String data = "the initial start";
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

    @Override
    public void run(String... args) throws Exception {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        handler();
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
        ssChannel.bind(new InetSocketAddress(8000));

        // 打开选择器。
        Selector selector = Selector.open();

        // 用所给的选择器来注册这个通道，返回一个选择键。
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);  //每次在一个通道注册一个选择器时，创建一个选择键。??
        // OP_ACCEPT 套接字接受操作的操作设置位。

        LampEntity lamp;

        Date date= new Date();

        // 轮询式的获取选择器上已经准备就绪的事件
        while (selector.select() > 0) { // select() 返回的值表示
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                /** 定时安排任务 **/
//                if ()


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
                    ByteBuffer buffer = ByteBuffer.allocate(256);

                    int len=0;
                    while ((len = socketChannel.read(buffer)) > 0) {
                        buffer.flip();
                        // received the data from client
                        byte[] bytes = buffer.array();
//                        String str = new String(buffer.array(), 0, len);
                        String str = BinaryToHexString(bytes);

                        if (str.length() >= 68) {
                            String nodeID = str.substring(18, 29).replace(" ", "");// num

                            // 判断是否存在，不存在新增实体 路灯记录
                            lamp = lampService.judgeNum(nodeID);
                            if (lamp == null) {
                                lamp = new LampEntity();
                                lamp.setNum(nodeID);
                                lamp.setName("lamp");
                                lamp.setBrightness(0);
                                lamp.setStatus(0);
                                lamp.setOnline(1);
                                lamp.setDamage(0);
                                lampService.save(lamp);
                            }
                            String online = str.substring(66, 68);
                            // 00 在线  01 掉线
                            if (online.equals("00")) {
                                // 执行数据库操作
                                lampService.updateOnline(nodeID,Integer.valueOf(online));
                            } else if (online.equals("01")){
                                lampService.updateOnline(nodeID,Integer.valueOf(online));
                            } else {
                                System.err.println("传送的数据不对");
                                // TODO 此处应该抛出异常
                            }
                        }

                        System.out.println(str);

                        buffer.clear();
                    }

                } else if (key.isWritable()) { //测试此键的通道是否已准备好写入。

                    SocketChannel socketChannel = (SocketChannel) key.channel();
//                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    ByteBuffer src = null;
                    if (data != null){
//                      src = Charset.forName("utf8").encode(getData());
                        if (data.length() == 20) {
                            byte[] bytes = hexStringToBytes(data);
                            src = ByteBuffer.wrap(bytes);
                            socketChannel.write(src);
                            System.out.println(data);
                        } else {
                            System.err.println("接收到的数据格式不对！");
                        }
                        data = null;
                    }
                }
            }
        }
    }

    //将字节数组转换为16进制字符串
    public static String BinaryToHexString(byte[] bytes) {
        String hexStr = "0123456789ABCDEF";
        String result = "";
        String hex = "";
        for (byte b : bytes) {
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));
            hex += String.valueOf(hexStr.charAt(b & 0x0F));
            result += hex + " ";
        }
        return result;
    }


    // String 转 byte[]
// byte[] 转 ByteBuffer
    ByteBuffer encodeValue(byte[] value) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(value.length);
        byteBuffer.clear();
        byteBuffer.get(value, 0, value.length);
        return byteBuffer;
    }

    // 十六进制转化为字节数组
    public  byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        // toUpperCase将字符串中的所有字符转换为大写
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        // toCharArray将此字符串转换为一个新的字符数组。
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    //返回匹配字符
    private  byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}

