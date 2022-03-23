package com.yws.pipe;

import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * 管道
 */
public class PipeDemo {

    public static void main(String[] args) throws Exception{
        //1 获取管道
        Pipe pipe = Pipe.open();

        //2 获取sink通道
        Pipe.SinkChannel sink = pipe.sink();

        //3 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("yws".getBytes());
        byteBuffer.flip();

        //4 写入数据
        sink.write(byteBuffer);

        //5 获取source通道
        Pipe.SourceChannel source = pipe.source();

        //6 读取数据
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(1024);
        byteBuffer2.clear();
        int length = source.read(byteBuffer2);

        System.out.println(new String(byteBuffer2.array(), 0, length));

        //7 关闭通道
        sink.close();
        source.close();
    }

}
