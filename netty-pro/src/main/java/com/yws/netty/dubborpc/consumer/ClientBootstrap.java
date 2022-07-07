package com.yws.netty.dubborpc.consumer;


import com.yws.netty.dubborpc.common.HelloService;
import com.yws.netty.dubborpc.netty.NettyClient;

public class ClientBootstrap {
    //定义协议头
    private static final String PROVIDE_NAME = "HelloService#hello#";

    public static void main(String[] args) {
        //创建一个消费者
        NettyClient consumer = new NettyClient();

        //创建代理对象
        HelloService service = (HelloService) consumer.getBean(HelloService.class, PROVIDE_NAME);

        //通过代理对象调用服务提供者的方法（服务）
        String res = service.hello("你好 dubbo~");
        System.out.println("调用的结果 res=" + res);

    }
}
