package com.yws.asyncfilechannel;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Future;

/**
 * 异步FileChannel
 */
public class AsyncFileChannelDemo {

    @Test
    public void readAsyncFileChannelFuture() throws Exception{
        //1 创建AsynchronousFileChannel
        AsynchronousFileChannel asynchronousFileChannel =
                AsynchronousFileChannel.open(Paths.get("d:\\ywsTest\\01.txt"), StandardOpenOption.READ);

        //2 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //3 调用channel的read方法得到Future
        Future<Integer> future = asynchronousFileChannel.read(byteBuffer, 0);

        //4 判断是否完成，返回true
        while (!future.isDone());

        //5 读取buffer里的数据
        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        byteBuffer.clear();

        System.out.println(new String(bytes));

        asynchronousFileChannel.close();
    }


    @Test
    public void readAsyncFileChannelComplete() throws Exception{
        //1 创建channel
        AsynchronousFileChannel asynchronousFileChannel =
                AsynchronousFileChannel.open(Paths.get("d:\\ywsTest\\01.txt"), StandardOpenOption.READ);

        //2 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //3 调用channle的read方法得到Future
        asynchronousFileChannel.read(byteBuffer, 0, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("result：" + result);

                attachment.flip();
                byte[] data = new byte[attachment.limit()];
                attachment.get(data);
                System.out.println(new String(data));
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {

            }
        });
    }



    @Test
    public void writeAsyncFileFuture() throws Exception{
        //1 创建channel
        AsynchronousFileChannel asynchronousFileChannel =
                AsynchronousFileChannel.open(Paths.get("d:\\ywsTest\\01.txt"), StandardOpenOption.WRITE);

        //2 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //3 write方法
        byteBuffer.put("helloni".getBytes());
        byteBuffer.flip();
        Future<Integer> future = asynchronousFileChannel.write(byteBuffer, 0);

        while (!future.isDone());

        byteBuffer.clear();

        System.out.println("write over");

    }


    @Test
    public void writeAsyncFileChannelComplete() throws Exception{
        //1 创建channel
        AsynchronousFileChannel asynchronousFileChannel =
                AsynchronousFileChannel.open(Paths.get("d:\\ywsTest\\01.txt"), StandardOpenOption.WRITE);

        //2 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //3 write方法
        byteBuffer.put("helloni".getBytes());
        byteBuffer.flip();
        asynchronousFileChannel.write(byteBuffer, 0, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                System.out.println("byte written：" + result);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {

            }
        });
    }


}
