package com.yws.niochat.client;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

//客户端
public class ChatClient {
    public static void main(String[] args) throws Exception{
        //new ChatClient().startClient("lisi");
        new ChatClient().startClient("zs");
    }


    public void startClient(String name) throws Exception{
        //连接服务器
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));

        //接收服务端响应数据
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        ClientThread clientThread = new ClientThread(selector);
        new Thread(clientThread).start();

        //向服务端发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.nextLine();
            if (message.length() > 0) {
                socketChannel.write(Charset.forName("UTF-8").encode(name + " : " + message));
            }
        }
    }
}
