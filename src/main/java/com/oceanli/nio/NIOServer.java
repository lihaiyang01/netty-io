package com.oceanli.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    private int port = 8080;
    //轮询器 相当于大堂经理
    private Selector selector;
    //缓冲区，相当于等候大厅
    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    public NIOServer(int port) {
        this.port = port;
        try {
            //初始化轮询器
            //开门
            ServerSocketChannel server = ServerSocketChannel.open();
            //绑定IP端口
            server.bind(new InetSocketAddress(this.port));
            //为了兼容BIO ，NIO默认采用的是阻塞模式，此处设置为非阻塞模式
            server.configureBlocking(false);
            //大堂经理准备就绪
            this.selector = Selector.open();
            //在门口翻牌子，正在营业
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        System.out.println("bio listen on " + this.port + ".");
        try {
            //轮询主线程
            while (true) {
                //大堂经理叫号
                selector.select();
                //每次大堂经理都拿到所有的号码
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    //不断的迭代，轮询
                    //同步体现在此处，每次只能拿到一个key，每次只能处理一种状态
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    process(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(SelectionKey key) throws Exception{
        //针对每一种状态做不同的处理
        if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            //此处体现为非阻塞，即不管你有没有准备好，都需要给我反馈一个状态
            SocketChannel socketChannel = server.accept();
            //设置为非阻塞
            socketChannel.configureBlocking(false);
            socketChannel.register(this.selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
            //从多路复用器中拿到客户端的引用
            SocketChannel channel = (SocketChannel) key.channel();
            int length = channel.read(byteBuffer);
            if (length > 0) {
                byteBuffer.flip();
                String result = new String(byteBuffer.array(), 0 , length);
                System.out.println("服务端接收: " + result);
                key = channel.register(this.selector, SelectionKey.OP_WRITE);
                //在key上携带一个附件，一会在写出去
                key.attach(result);

            }
        } else if (key.isWritable()) {
            SocketChannel channel = (SocketChannel) key.channel();
            String content = (String)key.attachment();
            channel.write(ByteBuffer.wrap(("输出：" + content).getBytes()));
            channel.close();
        }
    }

    public static void main(String[] args) {
        new NIOServer(8080).listen();
    }
}
