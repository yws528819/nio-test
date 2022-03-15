package com.yws;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//FileChannel写操作
public class FileChannelDemo2 {

    public static void main(String[] args) throws Exception {

        try (RandomAccessFile aFile = new RandomAccessFile("d:\\ywsTest\\02.txt", "rw");) {
            //打开FileChannel
            FileChannel channel = aFile.getChannel();

            //创建buffer对象
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.clear();

            String data = "hello,yws";
            buffer.put(data.getBytes());

            buffer.flip();

            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
        }
    }
}
