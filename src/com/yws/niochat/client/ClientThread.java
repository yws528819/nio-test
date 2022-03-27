package com.yws.niochat.client;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class ClientThread implements Runnable{

    private Selector selector;

    public ClientThread(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            while (true) {
                //获取就绪的channel数量
                if (selector.select() == 0) {
                    continue;
                }

                //获取可用的channel
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                //遍历集合
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    SelectionKey selectionKey = it.next();

                    //根据就绪状态，调用对应方法实现具体业务操作
                    if (selectionKey.isReadable()) {
                        //如果可读状态
                        readOperator(selector, selectionKey);
                    }

                    //移除set集合当前selectionKey
                    it.remove();
                }
            }
        }catch (Exception e) {

        }
    }


    /**
     * 可读状态操作
     * @param selector
     * @param selectionKey
     * @throws Exception
     */
    private void readOperator(Selector selector, SelectionKey selectionKey) throws Exception{
        //1 从selectionKey获取到已经就绪的通道
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        //2 创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //3 循环读取服务端消息
        int read = socketChannel.read(byteBuffer);
        String message = "";
        while (read > 0) {
            byteBuffer.flip();
            message += Charset.forName("UTF-8").decode(byteBuffer);
            byteBuffer.clear();
            read = socketChannel.read(byteBuffer);
        }

        //4 将channel再次注册到选择器上，监听可读状态
        socketChannel.register(selector, SelectionKey.OP_READ);

        //5 把服务端发送及转发的消息输出
        if (message.length() > 0) {
            System.out.println(message);
        }
    }
}
