package com.yws.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

/**
 *
 */
public class NettyBuf02 {
    public static void main(String[] args) {
        //创建byteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", CharsetUtil.UTF_8);

        if (byteBuf.hasArray()) {
            byte[] array = byteBuf.array();
            System.out.println(new String(array, CharsetUtil.UTF_8));
            //System.out.println(new String(array, 0, byteBuf.readableBytes(), CharsetUtil.UTF_8));
            System.out.println("byteBuf = " + byteBuf);

            System.out.println(byteBuf.arrayOffset());//0
            System.out.println(byteBuf.readerIndex());//0
            System.out.println(byteBuf.writerIndex());//12
            System.out.println(byteBuf.capacity());//36

            System.out.println(byteBuf.readableBytes());//可读的字节数 12
        }
    }
}
