package com.yws;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class FileChannelDemo1 {

    //FileChannel读取数据到buffer中
    public static void main(String[] args) throws Exception {
        //创建FileChannel
        RandomAccessFile aFile = new RandomAccessFile("d:\\ywsTest\\01.txt", "rw");
        FileChannel channel = aFile.getChannel();

        //创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //读取数据到buffer中
        int byteRead = channel.read(buffer);
        while (byteRead != -1) {
            System.out.println("读取了：" + byteRead);
            buffer.flip();

            while(buffer.hasRemaining()) {
                System.out.println((char) buffer.get());
            }

            buffer.clear();
            byteRead = channel.read(buffer);
        }
        aFile.close();
        System.out.println("结束了");

    }

}
