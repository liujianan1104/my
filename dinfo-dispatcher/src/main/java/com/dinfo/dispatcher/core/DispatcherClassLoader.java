package com.dinfo.dispatcher.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import static com.dinfo.dispatcher.util.ObjectUtil.isNull;

/**
 * @author yangxf
 * @see #getClassLoader(URL...)
 * @see #getClassLoader(File...)
 */
public class DispatcherClassLoader extends URLClassLoader {

    private static final String FILE_PREFIX = "file:";
    private static final String JAR_PREFIX = "jar:" + FILE_PREFIX;
    private static final String JAR_SUFFIX = ".jar";
    private static final String CLASS_DIR_SUFFIX = "classes/";

    private DispatcherClassLoader(URL[] urls) {
        super(urls);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        synchronized (getClassLoadingLock(name)) {
            Class<?> c = findLoadedClass(name);
            if (c == null) {

                try {
                    c = findClass(name);
                } catch (ClassNotFoundException e) {
                    // nothing
                }

                if (c == null)
                    c = super.loadClass(name, resolve);

            }

            if (resolve)
                resolveClass(c);

            return c;
        }
    }

    /**
     * Simple load urls,
     * more complex situation use {@link #getClassLoader(File...)} please.
     *
     * @param urls resource urls
     * @return custom classloader named to DispatcherClassLoader
     */
    public static ClassLoader getClassLoader(URL... urls) {
        return new DispatcherClassLoader(urls);
    }

    /**
     * @see #getClassLoader(File...)
     */
    public static ClassLoader getClassLoader(String... files) throws IOException {
        return getClassLoader(Stream.of(files).map(File::new).toArray(File[]::new));
    }

    /**
     * Get custom classloader
     *
     * @param files source files
     * @return custom classloader named to DispatcherClassLoader
     * @throws IOException           load jar file
     * @throws MalformedURLException file to URL
     */
    public static ClassLoader getClassLoader(File... files) throws IOException {
        List<URL> urls = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (isNull(file))
                continue;

            String name = file.getAbsolutePath();
            if (name.endsWith(JAR_SUFFIX))
                try (JarFile jf = new JarFile(file)) {
                    loadJar(jf, urls);
                }
            else
                urls.add(file.toURI().toURL());
        }

        return new DispatcherClassLoader(urls.toArray(new URL[0]));
    }

    private static void loadJar(JarFile jarFile, List<URL> urls) throws IOException {
        Enumeration<JarEntry> entries = jarFile.entries();

        String path = jarFile.getName() + "!/",
                urlPath = JAR_PREFIX + path;

        boolean hasClassDir = false;
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if (jarEntry.isDirectory() && !hasClassDir &&
                    (hasClassDir = name.endsWith(CLASS_DIR_SUFFIX)))
                urls.add(new URL(urlPath + name));
            else if (name.endsWith(JAR_SUFFIX))
                urls.add(new URL(urlPath + name));
        }

        if (!hasClassDir)
            urls.add(new URL(FILE_PREFIX + jarFile.getName()));
    }

}
