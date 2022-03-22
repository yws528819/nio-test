package com.yws.selector;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *
 */
public class SelectorDemo01 {

    public static void main(String[] args) throws Exception{
        //创建selector
        Selector selector = Selector.open();

        //通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //非阻塞
        serverSocketChannel.configureBlocking(false);

        //绑定连接
        serverSocketChannel.bind(new InetSocketAddress(8888));

        //将通道注册到选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //查询已经就绪通道操作
        Set<SelectionKey> selectionKeys = selector.selectedKeys();

        //遍历集合
        Iterator<SelectionKey> it = selectionKeys.iterator();
        while (it.hasNext()) {
            SelectionKey selectionKey = it.next();
            //判断key就绪状态操作
            if (selectionKey.isAcceptable()) {
                // a connection was accepted by a ServerSocketChannel.
            } else if (selectionKey.isConnectable()) {
                // a connection was established with a remote server.
            } else if (selectionKey.isReadable()) {
                // a channel is ready for reading
            } else if (selectionKey.isWritable()) {
                // a channel is ready for writing
            }

            it.remove();
        }
    }
}
