package com.oceanli.bio.tomcat.servlet;

import com.oceanli.bio.tomcat.http.GPRequest;
import com.oceanli.bio.tomcat.http.GPResponse;
import com.oceanli.bio.tomcat.http.GPServlet;

public class SecondServlet extends GPServlet {

    @Override
    public void doGet(GPRequest request, GPResponse response) {
        doPost(request,response);
    }

    @Override
    public void doPost(GPRequest request, GPResponse response) {
        response.write("this is a second servlet");
    }
}
