package com.oceanli.bio.tomcat.http;

import java.io.InputStream;

public class GPRequest {

    private String url;

    private String method;

    public GPRequest(InputStream is) {

        byte[] bytes = new byte[1024];
        int length = 0;
        try {

            if ((length = is.read(bytes)) > 0 ) {
                String content = new String(bytes);

                String firstLine = content.split("\\n")[0];
                String[] s = firstLine.split(" ");
                this.method = s[0];
                this.url = s[1].split("\\?")[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getUrl() {
        return this.url;

    }

    public String getMethod() {
        return this.method;
    }
}
