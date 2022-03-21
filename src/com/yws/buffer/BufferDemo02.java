package com.yws.buffer;


import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class BufferDemo02 {

    /**
     * 缓冲区分片（子缓冲区）：
     * 子缓冲区和源缓冲区的数据是共通的，修改子缓冲区，源缓冲区也会修改
     */
    @Test
    public void b01() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        //源缓冲区存值(0-9)
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte) i);
        }

        //创建子缓冲区
        byteBuffer.position(3);
        byteBuffer.limit(7);
        ByteBuffer slice = byteBuffer.slice();
        //子缓冲区存值
        for (int i = 0; i < slice.capacity(); i++) {
            //取出值(3-6) * 10 重新存值为(30-60)
            byte b = slice.get(i);
            b *= 10;
            slice.put(i, b);
        }

        //输出源缓冲区内容，子缓冲区重新存值的内容生效
        byteBuffer.position(0);
        byteBuffer.limit(byteBuffer.capacity());
        while (byteBuffer.hasRemaining()) {
            System.out.println(byteBuffer.get());
        }

    }


    /**
     * 只读缓冲区：
     * 只能读，不能修改
     * 源缓冲区做了修改，只读缓冲区也会看到修改后的数据
     */
    @Test
    public void b02() {
        //创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte) i);
        }

        //生成只读缓冲区
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();

        //修改源换冲区内容
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byte b = byteBuffer.get(i);
            b *= 10;
            byteBuffer.put(i, b);
        }

        //输出只读缓冲区内容
        readOnlyBuffer.position(0);
        readOnlyBuffer.limit(byteBuffer.capacity());
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }
    }

    /**
     * 直接缓冲区：
     * 创建方法用allocateDirect，使用方式与普通缓冲区无区别
     * 直接操作IO，省去中间缓冲区操作
     */
    @Test
    public void b03() throws Exception {

        FileInputStream fis = new FileInputStream("d:\\ywsTest\\01.txt");
        FileChannel fisChannel = fis.getChannel();

        FileOutputStream fos = new FileOutputStream("d:\\ywsTest\\02.txt");
        FileChannel fosChannel = fos.getChannel();

        //创建直接缓冲区
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        while(true) {
            buffer.clear();
            int read = fisChannel.read(buffer);
            if (read == -1) {
                break;
            }

            buffer.flip();
            fosChannel.write(buffer);
        }

    }



    private static final int START = 0;
    private static final int SIZE = 1024;
    /**
     * 内存映射文件io:
     * 比常规的基于流或者基于通道的I/O快
     * 使文件中的数据出现为内存数组的内容来完成，只有文件中实际读取或者写入的部分才会映射到内存中
     */
    @Test
    public void b04() throws Exception{
        RandomAccessFile aFile = new RandomAccessFile("d:\\ywsTest\\01.txt", "rw");
        FileChannel channel = aFile.getChannel();

        MappedByteBuffer mbb = channel.map(FileChannel.MapMode.READ_WRITE, START, SIZE);

        mbb.put(0, (byte) 97);
        mbb.put(1023, (byte) 122);

        aFile.close();
    }

}
