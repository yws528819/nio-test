package com.yws.netty.sample;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 说明：
 * 我们自定义一个Handler 需要继承netty规定好的某个HandlerAdapter（规范）
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取数据（读取客户端发送的消息）
     * @param ctx 上下文对象，含有管道pipeline、通道channel、地址
     * @param msg 客户端发送的数据，默认Object
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = " + ctx);
        //msg转成一个ByteBuf（netty提供的，非nio的ByteBuffer）
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送的消息是：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());

        //====taskQueue自定义任务=====
        //1.用户自定义的普通任务（如果以上逻辑耗时很久，可以将逻辑放入自定义的taskQueue里）
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~ 2", CharsetUtil.UTF_8));
                }catch (Exception e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }
        });
        //如果需要加多个任务，就写多个ctx.channel().eventLoop().execute
        //假如任务1睡眠5s，任务2睡眠5s，执行效果为：任务1 5s，任务2 会在任务1执行完，再执行所以为10s，原理可用看成是同一个线程里的队列任务


        //1.1这里如果有逻辑直接走，不会阻塞
        System.out.println("go on ...");


        //2.用户自定义定时任务（该任务是提交到scheduleTaskQueue中）
        //taskQueue任务执行完才会执行scheduleTaskQueue任务，且scheduleTask延迟时间，是在执行第一个任务就开始算时间（测了好像不是，延迟好像是在所有taskQueue执行完）
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~ 3", CharsetUtil.UTF_8));
                }catch (Exception e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }
        }, 5, TimeUnit.SECONDS);

    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是 write + flush
        //将数据写入缓存，并刷新
        //一般需要对发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端~", CharsetUtil.UTF_8));
    }

    //处理异常，一般需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
