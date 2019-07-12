package com.oceanli.bio.tomcat.http;

import java.io.OutputStream;

public class GPResponse {

    private OutputStream os;

    public GPResponse(OutputStream os) {
        this.os = os;
    }

    public void write(String s) {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append("HTTP/1.1 200 OK\n")
                    .append("Content-Type: text/html;\n")
                    .append("\r\n")
                    .append(s);
            this.os.write(sb.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
