package com.yws.netty.tcp;

import com.yws.netty.inoutboundhandler.MyByteToLongDecoder2;
import com.yws.netty.inoutboundhandler.MyLongToByteEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //加入一个自定义的handler，处理业务
        pipeline.addLast(new MyClientHandler());
    }
}
