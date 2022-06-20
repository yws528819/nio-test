package com.yws.netty.tcp;

import com.yws.netty.inoutboundhandler.MyByteToLongDecoder2;
import com.yws.netty.inoutboundhandler.MyLongToByteEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //自定义的handler处理业务逻辑
        pipeline.addLast(new MyServerHandler());
    }
}
