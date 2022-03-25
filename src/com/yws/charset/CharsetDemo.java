package com.yws.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;


public class CharsetDemo {

    public static void main(String[] args) throws Exception{
        //1 获取charset对象
        Charset charset = Charset.forName("UTF-8");

        //2 创建buffer
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("你好，我是yws");
        charBuffer.flip();

        //3 获取编码器，编码
        CharsetEncoder charsetEncoder = charset.newEncoder();
        ByteBuffer byteBuffer = charsetEncoder.encode(charBuffer);

        System.out.println("编码之后的结果：");
        while (byteBuffer.hasRemaining()) {
            System.out.println(byteBuffer.get());
        }


        //4 获取解码器，解码
        byteBuffer.flip();
        CharsetDecoder charsetDecoder = charset.newDecoder();
        CharBuffer charBuffer1 = charsetDecoder.decode(byteBuffer);
        System.out.println("解码后的结果：" + charBuffer1.toString());

        //5 用GBK解码器解码
        byteBuffer.flip();
        Charset charsetGBK = Charset.forName("GBK");
        CharsetDecoder charsetGBkDecoder = charsetGBK.newDecoder();
        CharBuffer charBuffer2 = charsetGBkDecoder.decode(byteBuffer);
        System.out.println("GBK解码后的结果：" + charBuffer2.toString());
    }

}
