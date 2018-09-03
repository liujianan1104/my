package com.dinfo.dispatcher.core;

import com.dinfo.dispatcher.util.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import static com.dinfo.dispatcher.util.ObjectUtil.checkNullAndThrow;
import static com.dinfo.dispatcher.util.ObjectUtil.isNull;

/**
 * @author yangxf
 */
public class DispatcherContext {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherContext.class);

    private static final String JAR_SUFFIX = ".jar";
    private static final String FILE_PREFIX = "file:";
    private static final String JAR_PREFIX = "jar:" + FILE_PREFIX;
    private static final String CLASS_DIR_SUFFIX = "classes/";
    private static final String TMP_FILE_PREFIX = "dispatcher";
    private static String prefix = "";
    private static List<URL> urls = new ArrayList<>();

    private static ClassLoader classLoader;

    private DispatcherContext() {
    }

    /**
     * 初始化加载路径下jar包
     *
     * @param path jar包文件路径
     */
    public static void initialize(String path) {
        logger.info("filePath of jar : " + path);
        checkNullAndThrow(path);
        prefix = path.endsWith(File.separator) ? path : path + File.separator;
        try {
            long t1 = System.currentTimeMillis();
            parseFiles(new File(path));
            logger.info("urls size : " + urls.size());
            classLoader = new URLClassLoader(urls.toArray(new URL[0]));
            long t2 = System.currentTimeMillis();
            logger.info("Initialize jar success ! Time consuming " + (t2 - t1) + " ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载根目录下文件
     *
     * @param root 根路径
     * @throws IOException
     */
    private static void parseFiles(File root) throws IOException {
        if (root.isDirectory()) {
            File[] files = root.listFiles(f -> f.isDirectory() || f.getName().endsWith(JAR_SUFFIX));
            if (isNull(files))
                return;

            for (int i = 0; i < files.length; i++)
                parseFiles(files[i]); //目录下存在文件夹，循环加载文件夹下jar包

        } else {
            String name = root.getAbsolutePath();
            if (name.startsWith(prefix))
                setUrls(name.substring(prefix.length()));
        }
    }

    /**
     * 获得jar包文件的url
     *
     * @param name
     * @throws IOException
     */
    public static void setUrls(String name) throws IOException {
        String path = prefix + name;
        if (!new File(path).exists())
            return;
        File[] files = Stream.of(path).map(File::new).toArray(File[]::new);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (isNull(file))
                continue;

            String fileName = file.getAbsolutePath();
            if (fileName.endsWith(JAR_SUFFIX))
                try (JarFile jf = new JarFile(file)) {
                    loadJar(jf, urls);
                }
            else
                urls.add(file.toURI().toURL());
        }
    }

    /**
     * 加载jar包
     *
     * @param jarFile
     * @param urls
     * @throws IOException
     */
    private static void loadJar(JarFile jarFile, List<URL> urls) throws IOException {
        Enumeration<JarEntry> entries = jarFile.entries();

        String path = jarFile.getName() + "!/",
                urlPath = JAR_PREFIX + path;

        boolean hasClassDir = false;
        while (entries.hasMoreElements()) {
//            URL url;
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if (jarEntry.isDirectory() && !hasClassDir &&
                    (hasClassDir = name.endsWith(CLASS_DIR_SUFFIX))) //加载classes文件目录
                urls.add(new URL(urlPath + name));
            else if (name.endsWith(JAR_SUFFIX)) //直接加载jar包
                urls.add(getJarUrl(urlPath + name));
        }

        if (!hasClassDir)
            urls.add(new URL(FILE_PREFIX + jarFile.getName()));
    }

    /**
     * 加载jar包中的依赖jar包时，先将依赖的jar包生成到临时文件中，再加载
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static URL getJarUrl(String file) throws IOException {
        URL url = new URL(file);
        File tempFile = File.createTempFile(TMP_FILE_PREFIX, JAR_SUFFIX);
        InputStream inputStream = new URL(file).openStream();
        IOUtil.copy(inputStream, new FileOutputStream(tempFile));
        return tempFile.toURI().toURL();
    }

    public static ClassLoader getClassLoader() {
        return classLoader;
    }

    public static void setClassLoader(ClassLoader classLoader) {
        DispatcherContext.classLoader = classLoader;
    }
}
