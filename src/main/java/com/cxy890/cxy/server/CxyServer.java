package com.cxy890.cxy.server;

import com.cxy890.cxy.global.GlobalCxyConfig;
import com.cxy890.cxy.util.StringUtil;

import java.io.File;
import java.io.IOException;

/**
 * 启动Tomcat服务器
 *
 * Created by ChangXiaoyang on 2017/5/21.
 */
public class CxyServer {

    private String baseDir;

    public CxyServer(String baseDir) {
        this.baseDir = baseDir;
    }

    /**
     * 获取tomcat启动端口
     *
     * @return int
     */
    private int getPort() {
        String portConfig = GlobalCxyConfig.getConfig("server.port");
        return StringUtil.isNull(portConfig) ? 8080 : Integer.parseInt(portConfig);
    }

    /**
     * 启动tomcat
     */
//    public void start() {
//        try {
//            Tomcat tomcat = new Tomcat();
//            File baseDir =  createTempDir();
//            tomcat.setBaseDir(baseDir.getAbsolutePath());
////            Container[] container = tomcat.getHost().findChildren();
////            if (container == null || container.length == 0)
////                tomcat.addWebapp("", "");
//
//            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
//            tomcat.getService().addConnector(connector);
//            customizeConnector(connector);
//            tomcat.setConnector(connector);
//            tomcat.getHost().setAutoDeploy(false);
//            tomcat.getEngine().setBackgroundProcessorDelay(-1);
//            prepareContext(tomcat.getHost());
//            tomcat.start();
//            System.out.println("tomcat started");
//            tomcat.getServer().await();
//        }catch (Exception e) {
//            System.out.println("Start Tomcat error!");
//        }
//    }

    /**
     * 准备Content
     */
//    private void prepareContext(Host host){
//        StandardContext context = new StandardContext();
//        context.setParentClassLoader(this.getClass().getClassLoader());
//        WebappLoader loader = new WebappLoader(context.getParentClassLoader());
//        loader.setLoaderClass(this.getClass().getName());
//        loader.setDelegate(true);
//        context.setLoader(loader);
//        addDefaultServlet(context);
//        host.addChild(context);
//    }

    /**
     * 添加默认servlet
     *
     * @param context Context
     */
//    private void addDefaultServlet(Context context) {
//        Wrapper defaultServlet = context.createWrapper();
//        defaultServlet.setName("default");
//        defaultServlet.setServletClass("org.apache.catalina.servlets.DefaultServlet");
//        defaultServlet.addInitParameter("debug", "0");
//        defaultServlet.addInitParameter("listings", "false");
//        defaultServlet.setLoadOnStartup(1);
//        defaultServlet.setOverridable(true);
//        context.addChild(defaultServlet);
//        context.addServletMapping("/", "default");
//
//    }

    /**
     * 创建Tomcat容器临时文件夹
     *
     * @return The temp dir for given servlet container.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private File createTempDir() {
        try {
            File tempDir = File.createTempFile("SummerTomcat.", "." + getPort());
            tempDir.delete();
            tempDir.mkdir();
            tempDir.deleteOnExit();
            return tempDir;
        }
        catch (IOException ex) {
            System.out.println("");
            return null;
        }
    }

    /**
     * 定制 端口Connector
     *
     * @param connector Connector
     */
//    private void customizeConnector(Connector connector) {
//        connector.setPort(getPort());
//        connector.setURIEncoding("utf-8");
//        connector.setUseBodyEncodingForURI(true);
//        connector.setRedirectPort(7999);
//    }

}
