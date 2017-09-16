package com.cxy890.cxy.env;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
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

    /**
     * 获取某包下所有类
     *
     * @param packageName  包名
     */
    public static void load(String packageName) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL url = loader.getResource(".");
        if (url != null) {
            String type = url.getProtocol();
            if (type.equals("file")) {
                loadContextByFile(url.getPath());
            } else if (type.equals("jar")) {
                loadContextByJar(url.getPath());
            }
        } else {
            loadClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath);
        }
    }

    /**
     * 从项目文件获取某包下所有类
     *
     * @param filePath     文件路径
     * @return 类的完整名称
     */
    private static List<String> loadContextByFile(String filePath) {
        List<String> myClassName = new ArrayList<>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles == null)
            return myClassName;

        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                myClassName.addAll(loadContextByFile(childFile.getPath()));
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    myClassName.add(childFilePath);
                    try {
                        CxyContext.addClass(Class.forName(childFilePath));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (childFilePath.endsWith(".properties")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9);
                    childFilePath = childFilePath.replace("\\", ".");
                    myClassName.add(childFilePath);
                    ClassLoader loader = Thread.currentThread().getContextClassLoader();
                    CxyContext.addProp(loader.getResourceAsStream(childFilePath));
                }
            }
        }

        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     *
     * @param jarPath      jar文件路径
     */
    private static void loadContextByJar(String jarPath) {
        String[] jarInfo = jarPath.split("!");
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1);
        try {
            JarFile jarFile = new JarFile(jarFilePath);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String entryName = jarEntry.getName();
                if (entryName.endsWith(".class")) {
                    if (entryName.startsWith(packagePath)) {
                        entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                        CxyContext.addClass(Class.forName(entryName));
                    }
                } else if (entryName.endsWith(".properties")) {
                    ClassLoader loader = Thread.currentThread().getContextClassLoader();
                    CxyContext.addProp(loader.getResourceAsStream(entryName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从所有jar中搜索该包，并获取该包下所有类
     *
     * @param urls         URL集合
     * @param packagePath  包路径
     */
    private static void loadClassNameByJars(URL[] urls, String packagePath) {
        if (urls != null) {
            for (URL url : urls) {
                String urlPath = url.getPath();
                if (urlPath.endsWith("classes/")) continue;
                String jarPath = urlPath + "!/" + packagePath;
                loadContextByJar(jarPath);
            }
        }
    }
}
