package com.yws.netty.dubborpc.netty;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable<String> {

    private ChannelHandlerContext contex;
    private String result;
    private String param;


    //与服务器创建连接后，就会被调用，这个方法第一个被调用(1)
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        contex = ctx;//其他方法会使用到ctx
    }

    //收到服务器的数据后，调用方法(4)
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = msg.toString();
        notify();//唤醒等待的线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //被代理对象调用，发送数据给服务器，-> wait -> 等待被唤醒(channelRead) -> 返回结果 -> 4 -> 5
    //(3)(5)
    @Override
    public synchronized String call() throws Exception {
        contex.writeAndFlush(param);
        wait();//等待channelRead方法获取服务器的结果后，唤醒
        return result;//服务方返回的结果
    }


    //(2)
    public void setParam(String param) {
        this.param = param;
    }
}
