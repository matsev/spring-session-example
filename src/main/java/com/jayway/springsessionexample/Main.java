package com.jayway.springsessionexample;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {

    private static int port = 8080;
    private static final String WEBAPP_DIR = "src/main/webapp/";

    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);

        Context context = tomcat.addWebapp("", new File(WEBAPP_DIR).getAbsolutePath());

        File configFile = new File(WEBAPP_DIR + "WEB-INF/web.xml");
        context.setConfigFile(configFile.toURI().toURL());

        tomcat.start();
        tomcat.getServer().await();
    }

}
