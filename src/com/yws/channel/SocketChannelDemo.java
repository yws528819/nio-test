package com.yws.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelDemo {
    public static void main(String[] args) throws Exception{
        //创建SocketChannel
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("www.baidu.com", 80));

        //设置阻塞和非阻塞
        socketChannel.configureBlocking(false);

        //读操作
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        socketChannel.read(byteBuffer);

        socketChannel.close();
        System.out.println("read over");
    }

}
