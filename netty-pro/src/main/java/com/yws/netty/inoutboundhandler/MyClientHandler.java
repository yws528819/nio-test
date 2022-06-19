package com.yws.netty.inoutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器的ip=" + ctx.channel().remoteAddress());
        System.out.println("收到服务器的消息=" + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        ctx.writeAndFlush(123456L);//发送的是一个long

        //分析
        //1."abcdabcdabcdabcd"是16个字节
        //2.该处理器的前一个handler是 MyLongToByteEncoder
        //3.MyLongToByteEncoder 父类是MessageToByteEncoder
        //4.父类MessageToByteEncoder的write方法有判断 acceptOutboundMessage方法 当前msg是不是应该处理的类型，如果是就处理，不是就跳过encode
        //5.因此我们编写encode时，要注意传入的数据类型和处理的数据类型一致
        //ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));
    }
}
