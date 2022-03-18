package com.yws.buffer;

import org.junit.Test;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

public class BufferDemo01 {

    @Test
    public void buffer01() throws Exception{
        //FileChannel
        RandomAccessFile aFile = new RandomAccessFile("d:\\ywsTest\\01.txt", "rw");
        FileChannel channel = aFile.getChannel();

        //创建buffer，大小
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //读取文件到buffer中
        int read = channel.read(buffer);
        while (read != -1) {
            //read模式
            buffer.flip();

            while (buffer.hasRemaining()) {
                System.out.println((char)  buffer.get());
            }

            buffer.clear();
            read = channel.read(buffer);
        }

        //关闭
        aFile.close();
    }

    @Test
    public void buffer02() {
        //创建buffer
        IntBuffer intBuffer = IntBuffer.allocate(8);

        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i * 2 + 1);
        }

        intBuffer.flip();

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }

        intBuffer.clear();
    }



}
