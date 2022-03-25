package com.yws.filelock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *
 */
public class FileLockDemo1 {

    public static void main(String[] args) throws Exception{
        String filePath = "d:\\ywsTest\\01.txt";
        Path path = Paths.get(filePath);
        FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        fileChannel.position(fileChannel.size() - 1);

        ByteBuffer byteBuffer = ByteBuffer.wrap("hello".getBytes());

        //加锁：独占锁，不能同时读、同时写，体现在多个进程里，同一个线程没限制
        //FileLock lock = fileChannel.lock();

        //加锁：共享锁，能同时读，写时不能读
        FileLock lock = fileChannel.lock(0, Integer.MAX_VALUE, true);
        System.out.println("是否共享锁：" + lock.isShared());

        fileChannel.write(byteBuffer);
        fileChannel.close();

        //读文件
        readFile(filePath);
    }

    private static void readFile(String filePath) throws Exception{

        try(
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
        ) {
            System.out.println("读取内容：");
            String line = br.readLine();
            while (line != null) {
                System.out.println(" " + line);
                line = br.readLine();
            }
        }
    }

}
