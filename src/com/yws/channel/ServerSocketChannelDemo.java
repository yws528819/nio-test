package com.yws.channel;


import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class ServerSocketChannelDemo {


    public static void main(String[] args) throws Exception{
        //端口号
        int port = 8888;

        //buffer
        ByteBuffer buffer = ByteBuffer.wrap("hello,yws".getBytes());

        //serverSocketChannel
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //绑定
        ssc.socket().bind(new InetSocketAddress(port));

        //设置非阻塞模式
        ssc.configureBlocking(false);

        //监听有新连接传入
        while (true) {
            System.out.println("waiting for connections...");
            SocketChannel sc = ssc.accept();
            if (sc == null) {
                System.out.println("null");
                TimeUnit.SECONDS.sleep(2);
            }else {
                System.out.println("Incoming connection from：" + sc.socket().getRemoteSocketAddress());
                buffer.rewind();
                sc.write(buffer);
                sc.close();
            }
        }
    }

}
