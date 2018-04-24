package com.cxy890.config.loader.context;

import com.cxy890.config.util.ClassUtil;
import com.cxy890.server.filter.Filter;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包操作工具类
 *
 * Created by ChangXiaoyang on 2017/08/12
 */
public class ContextLoader {

    private static final String FILE = "file";

    private static final String JAR = "jar";

    /**
     * 加载框架配置
     */
    public static boolean loadFramework() {
        return true;
    }

    /**
     * 获取某包下所有类
     *
     * @param packageName  包名
     */
    public static boolean loadApplication(String packageName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL url = loader.getResource(".");
        if (url != null) {
            String type = url.getProtocol();
            if (type.equals("file")) {
                loadFromFile(url.getPath(), loader);
            } else if (type.equals("jar")) {
                loadFromJar(url, loader);
            }
        }
        return true;
    }

    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath     文件路径
     * @return 类的完整名称
     */
    private static List<String> loadFromFile(String filePath, ClassLoader loader) {
        List<String> myClassName = new ArrayList<>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles == null)
            return myClassName;

        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                myClassName.addAll(loadFromFile(childFile.getPath(), loader));
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    myClassName.add(childFilePath);
                    ClassUtil.ifExist(childFilePath, CxyContext::addClass);
                }
            }
        }

        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     *
     * @param url jar URL
     */
    private static void loadFromJar(URL url, ClassLoader loader) {
        try {
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            JarFile jarFile = jarURLConnection.getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (entryName.startsWith(url.getPath())) {
                        entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                        CxyContext.addClass(Class.forName(entryName));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* *//**
     * 从所有jar中搜索该包，并获取该包下所有类
     *
     * @param urls         URL集合
     * @param packagePath  包路径
     *//*
    private static void loadClassNameByJars(URL[] urls, String packagePath) {
        if (urls != null) {
            for (URL url : urls) {
                String urlPath = url.getPath();
                if (urlPath.endsWith("classes/")) continue;
                String jarPath = urlPath + "!/" + packagePath;
                loadFromJar(url, jarPath);
            }
        }
    }*/
}
