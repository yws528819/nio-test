package com.yws.niochat.server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

//服务端
public class ChatServer {

    //启动主方法
    public static void main(String[] args) throws Exception {
        new ChatServer().startServer();
    }

    public void startServer() throws Exception {
        //1 创建selector选择器
        Selector selector = Selector.open();

        //2 创建ServerSocketChannel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //3 通道绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress(8888));
        //设置非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //4 把channel通道注册到selector选择器上
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器已经启动成功了");

        //5 循环，等待有新连接介入
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

                //6 根据就绪状态，调用对应方法实现具体业务操作
                //6.1 如果accept状态
                if (selectionKey.isAcceptable()) {
                    acceptOperator(serverSocketChannel, selector);
                }else if (selectionKey.isReadable()) {
                    //6.2 如果可读状态
                    readOperator(selector, selectionKey);
                }

                //移除set集合当前selectionKey
                it.remove();
            }
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

        //3 循环读取客户端消息
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

        //5 把客户端发送消息，广播到其他客户端上
        if (message.length() > 0) {
            System.out.println(message);
            castOtherClient(message, selector, socketChannel);
        }
    }

    /**
     * 广播到其他客户端
     * @param message
     * @param selector
     * @param socketChannel
     * @throws Exception
     */
    private void castOtherClient(String message, Selector selector, SocketChannel socketChannel) throws Exception{
        //1 获取所有已经接入channel
        Set<SelectionKey> selectionKeys = selector.keys();

        //2 循环所有channel广播消息
        for (SelectionKey selectionKey : selectionKeys) {
            //获取每个channel
            Channel tarChannel = selectionKey.channel();
            //不需要给自己发送
            if (tarChannel instanceof SocketChannel && tarChannel !=  socketChannel) {
                ((SocketChannel) tarChannel).write(Charset.forName("UTF-8").encode(message));
            }

        }


    }

    /**
     * 处理接入状态操作
     * @param serverSocketChannel
     * @param selector
     * @throws Exception
     */
    private void acceptOperator(ServerSocketChannel serverSocketChannel, Selector selector) throws Exception{
        //1 接入状态，创建socketChannel
        SocketChannel socketChannel = serverSocketChannel.accept();

        //2 把socketChannel设置非阻塞模式
        socketChannel.configureBlocking(false);

        //3 把channel注册到selector选择器上，监听可读状态
        socketChannel.register(selector, SelectionKey.OP_READ);

        //4 客户端回复消息
        socketChannel.write(Charset.forName("UTF-8").encode("欢迎进入聊天室，请注意隐私安全"));

    }
}
