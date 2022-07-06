package com.yws.netty.echo2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

/**
 * 说明：
 * 我们自定义一个Handler 需要继承netty规定好的某个HandlerAdapter（规范）
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    private static final EventExecutorGroup group = new DefaultEventExecutorGroup(16);

    /**
     * 读取数据（读取客户端发送的消息）
     * @param ctx 上下文对象，含有管道pipeline、通道channel、地址
     * @param msg 客户端发送的数据，默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("handler的线程是=" + Thread.currentThread().getName());

        //1.用户自定义的普通任务（如果以上逻辑耗时很久，可以将逻辑放入自定义的taskQueue里）
        //ctx.channel().eventLoop().execute(new Runnable() {
        //    @Override
        //    public void run() {
        //        try {
        //            TimeUnit.SECONDS.sleep(10);
        //            System.out.println("eventLoop.execute线程是=" + Thread.currentThread().getName());
        //            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~", CharsetUtil.UTF_8));
        //        }catch (Exception e) {
        //            System.out.println("发生异常" + e.getMessage());
        //        }
        //    }
        //});

        group.submit(() -> {
            TimeUnit.SECONDS.sleep(10);
            System.out.println("group.submit的call线程是=" + Thread.currentThread().getName());
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~2", CharsetUtil.UTF_8));
            return null;
        });


        //1.1这里如果有逻辑直接走，不会阻塞
        System.out.println("go on ...");


    }


    //处理异常，一般需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
