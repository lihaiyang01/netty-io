package com.oceanli.bio.tomcat;

import com.oceanli.bio.tomcat.http.GPRequest;
import com.oceanli.bio.tomcat.http.GPResponse;
import com.oceanli.bio.tomcat.http.GPServlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class GPTomcat {

    private ServerSocket server;
    private int port = 8080;
    Properties xmlWeb = new Properties();
    public Map<String, GPServlet> servletMap = new HashMap<>();

    public GPTomcat() {

    }

    public void init() {
        try {
            //读取web.properties中的配置的servletClass和url键值对，并粗如servletMap中
            String webInfo = this.getClass().getResource("/").getPath();
            FileInputStream fis = new FileInputStream(webInfo + "web.properties");
            xmlWeb.load(fis);
            for (Object key : xmlWeb.keySet()) {
                String k = key.toString();
                if (k.endsWith(".url")) {
                    String url = xmlWeb.getProperty(k);
                    String servletName = k.replaceAll(".url$", "");
                    String className = xmlWeb.getProperty(servletName + ".className");
                    servletMap.put(url, (GPServlet)Class.forName(className).newInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start() {

        init();
        System.out.println("GPTomcat start, port:" + port);
        try {
            //创建一个ServerSocket
            server = new ServerSocket(port);
            while (true) {
                //接收客户端的请求
                Socket client = server.accept();
                process(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(Socket client) throws IOException {

        InputStream is = client.getInputStream();
        OutputStream os = client.getOutputStream();
        GPRequest request = new GPRequest(is);
        GPResponse response = new GPResponse(os);
        String url = request.getUrl();
        GPServlet gpServlet = servletMap.get(url);
        if (gpServlet == null) {
            response.write("404 -Not Found~~~");
        } else {
            gpServlet.service(request, response);
        }
        os.flush();
        os.close();
        is.close();
        client.close();
    }

    public static void main(String[] args) {
        new GPTomcat().start();
    }

}
