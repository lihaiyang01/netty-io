package com.oceanli.bio;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 同步阻塞的IO
 */
public class BIOServer {

    private int port;

    public BIOServer(int port) {
        this.port = port;

    }

    public void listen() {

        try {
            ServerSocket server = new ServerSocket(port);
            //循环监听
            while (true) {
                //等待客户端连接，阻塞方法
                Socket client = server.accept();
                //客户端发送数据的IO流
                InputStream inputStream = client.getInputStream();
                byte[] bytes = new byte[1024];
                //网络客户端把数据发送到网卡，机器所得到的数据读到了JVM内中
                int length = inputStream.read(bytes);
                if (length > 0) {
                    String result = new String(bytes, 0 , length);
                    System.out.println("服务器接收: " + result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BIOServer server = new BIOServer(8080);
        server.listen();
    }
}
