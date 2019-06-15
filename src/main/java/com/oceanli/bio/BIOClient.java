package com.oceanli.bio;


import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;

public class BIOClient {

    public static void main(String[] args) {
        try {
            Socket client = new Socket("localhost", 8080);
            OutputStream outputStream = client.getOutputStream();
            String name = UUID.randomUUID().toString();
            System.out.println("客户端发送消息: " + name);
            outputStream.write(name.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
