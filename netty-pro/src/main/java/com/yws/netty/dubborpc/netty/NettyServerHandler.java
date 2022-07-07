package com.yws.netty.dubborpc.netty;


import com.yws.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    public static final String PRE_FIX = "HelloService#hello#";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用服务
        System.out.println("msg=" + msg);
        //客户端在调用服务器的api时，我们需要定义一个协议
        //比如每次发消息都必须以某个字符串开头"HelloService#hello#你好"
        if (msg.toString().startsWith(PRE_FIX)) {
            String result = new HelloServiceImpl().hello(msg.toString().substring(PRE_FIX.length()));
            ctx.writeAndFlush(result);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
