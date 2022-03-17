package com.yws.channel;


import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class DatagramChannelDemo {

    //发送的实现
    @Test
    public void sendDatagram() throws Exception{
        //打开DatagramChannel
        DatagramChannel datagramChannel = DatagramChannel.open();

        //发送
        while (true) {
            ByteBuffer buffer = ByteBuffer.wrap("发送yws".getBytes(StandardCharsets.UTF_8));

            datagramChannel.send(buffer, new InetSocketAddress("127.0.0.1", 9999));

            System.out.println("已经完成发送");

            TimeUnit.SECONDS.sleep(1);
        }
    }

    //接收的实现
    @Test
    public void receiveDatagram() throws Exception{
        //打开DatagramChannel
        DatagramChannel receiveChannel = DatagramChannel.open();

        //绑定
        receiveChannel.bind(new InetSocketAddress(9999));

        //buffer
        ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);

        while (true) {
            receiveBuffer.clear();

            SocketAddress address = receiveChannel.receive(receiveBuffer);

            receiveBuffer.flip();

            //发送方的地址
            System.out.println(address.toString());

            //接受的内容
            System.out.println(Charset.forName("UTF-8").decode(receiveBuffer));
        }
    }



    /**
     * 连接 read 和 write
     * 需要连接才能读到，即DatagramChannel.connect
     * 前面send 和 receive 不用connect (应该是send不用)
     */
    @Test
    public void testConnect() throws Exception{
        //打开DatagramChannel
        DatagramChannel connChannel = DatagramChannel.open();

        //绑定
        connChannel.bind(new InetSocketAddress(9999));

        //连接
        connChannel.connect(new InetSocketAddress("127.0.0.1", 9999));

        //write方法
        connChannel.write(ByteBuffer.wrap("hello!是我".getBytes(StandardCharsets.UTF_8)));

        //buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            buffer.clear();
            //read接收读取
            connChannel.read(buffer);
            buffer.flip();
            System.out.println(Charset.forName("UTF-8").decode(buffer));
        }
    }


}
