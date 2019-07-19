package com.cxy890.config.util;

import java.io.*;
import java.net.*;
import java.security.PermissionCollection;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author ChangXiaoyang
 */
public final class FileUtil {

    public static final String FILE = "file";

    public static final String JAR = "jar";

    private static URL url = null;

    private static String protocol = null;

    private static String projectPath = null;

    public static URL getUrl(Class clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation();
    }

    private static URL getUrl() {
        if (url == null) {
            url =  getUrl(FileUtil.class);
        }
        return url;
    }

    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    private static String getProtocol() {
        if (protocol == null)
            protocol = getUrl().getProtocol();
        return protocol;
    }

    public static boolean isJar() {
        return JAR.equals(getProtocol());
    }

    public static boolean isFile() {
        return FILE.equals(getProtocol());
    }

    public static String getProjectPath(Class<?> mainClass) {
        if (projectPath == null) {
            URL url = getUrl(mainClass);
            try {
                String filePath = URLDecoder.decode(url.getPath(), "utf-8");
                if (isJar()) {
                    File file = new File("");
                    filePath = file.getAbsolutePath();
                }
                projectPath =  filePath;
            } catch (UnsupportedEncodingException e) {
                projectPath =  url.getPath();
            }
        }
        return projectPath;
    }

    /**
     * 获取包下所有类名
     */
    public static List<String> getClassNames(String pkg) throws URISyntaxException, IOException {
        ClassLoader loader = FileUtil.class.getClassLoader();
        URL url = loader.getResource(pkg.replace(".", "/"));
        String protocol = null;
        if (url != null) {
            protocol = url.getProtocol();
        }
        if (FILE.equals(protocol)) {
            return findClassLocal(pkg, loader);
        } else if (JAR.equals(protocol)) {
            return findClassJar(pkg, loader);
        }
        return Collections.emptyList();
    }

    /**
     * 文件夹查找
     *
     * @param packName 包名
     */
    private static List<String> findClassLocal(final String packName, ClassLoader loader) throws URISyntaxException {
        List<String> names = new ArrayList<>();
        URL url = loader.getResource(packName.replace(".", "/"));
        assert url != null;
        URI uri = url.toURI();

        File file = new File(uri);
        File[] files = file.listFiles();
        if (files != null) {
            for (File chiFile : files) {
                if (chiFile.isDirectory()) {
                    names.addAll(findClassLocal(packName + "." + chiFile.getName(), loader));
                }
                if (chiFile.getName().endsWith(".class")) {
                    names.add(packName + "." + chiFile.getName().replace(".class", ""));
                }
            }
        }
        return names;
    }

    /**
     * jar包查找
     *
     * @param packName 包名
     */
    private static List<String> findClassJar(final String packName, ClassLoader loader) throws IOException {
        List<String> names = new ArrayList<>();
        String pathName = packName.replace(".", "/");
        URL url = loader.getResource(pathName);
        assert url != null;
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();

        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();
            if (jarEntryName.contains(pathName) && !jarEntryName.equals(pathName + "/")) {
                if (jarEntry.isDirectory()) {
                    String clazzName = jarEntry.getName().replace("/", ".");
                    int endIndex = clazzName.lastIndexOf(".");
                    String prefix = "";
                    if (endIndex > 0) {
                        prefix = clazzName.substring(0, endIndex);
                    }
                    names.addAll(findClassJar(prefix, loader));
                }
                if (jarEntry.getName().endsWith(".class")) {
                    names.add(jarEntry.getName().replace("/", ".").replace(".class", ""));
                }
            }

        }
        return names;
    }



}
