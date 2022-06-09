package com.yws.netty.buf;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyBuf01 {
    public static void main(String[] args) {
        //创建一个byteBuf
        //说明：
        //1.创建对象，该对象包含一个数组arr，是一个byte[10]
        //2.在netty的buffer中，不需要使用flip进行反转
        //  底层维护了readerIndex 和 writerIndex 和 capacity，讲buffer分为3个区域
        //  0--readerIndex 已经读取的区域
        //  readerIndex--writeIndex 可读的区域
        //  writerIndex--capacity 可写的区域
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i=0; i<10; i++) {
            buffer.writeByte(i);
        }

        System.out.println("capacity=" + buffer.capacity());

        for (int i=0; i<10; i++) {
            System.out.println(buffer.readByte());
        }
    }
}
