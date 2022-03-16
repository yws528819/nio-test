package com.yws.channel;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * 通道之间数据传输
 */
public class FileChannelDemo3 {

    public static void main (String[] args) throws Exception {
        //创建两个fileChannel
        RandomAccessFile aFile = new RandomAccessFile("E:\\ywsTest\\001.txt", "rw");
        FileChannel fromChannle = aFile.getChannel();

        RandomAccessFile bFile = new RandomAccessFile("E:\\ywsTest\\002.txt", "rw");
        FileChannel toChannle = bFile.getChannel();

        int position = 0;
        long size = fromChannle.size();

        //fromChannel 传输到 toChannle
        //文件通过通道之间进行传输

        //transferFrom
        toChannle.transferFrom(fromChannle, position, size);

        //transferTo
        //fromChannle.transferTo(position, size, toChannle);

        aFile.close();
        bFile.close();
    }
}
