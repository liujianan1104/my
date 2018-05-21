package com.dinfo.dispatcher.core;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.dinfo.dispatcher.util.ObjectUtil.checkNullAndThrow;
import static com.dinfo.dispatcher.util.ObjectUtil.isNull;

/**
 * @author yangxf
 */
public class DispatcherContext {

    private static final String JAR_SUFFIX = ".jar";
    private static final ConcurrentMap<String, ClassLoader> CLASS_LOADER_MAPPING = new ConcurrentHashMap<>();

    private static String prefix = "";

    private DispatcherContext() {
    }

    public static void initialize(String path) {
        checkNullAndThrow(path);
        prefix = path.endsWith("/") ? path : path + '/';
        try {
            parseFiles(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseFiles(File root) throws IOException {
        if (root.isDirectory()) {
            File[] files = root.listFiles(f -> f.isDirectory() || f.getName().endsWith(JAR_SUFFIX));
            if (isNull(files))
                return;

            for (int i = 0; i < files.length; i++)
                parseFiles(files[0]);

        } else {
            String name = root.getAbsolutePath();
            if (name.startsWith(prefix))
                getOrAddClassLoader(name.substring(prefix.length()));
        }
    }

    public static ClassLoader getOrAddClassLoader(String name) throws IOException {
        String path = prefix + name;
        if (!new File(path).exists())
            return null;

        ClassLoader loader = CLASS_LOADER_MAPPING.get(name);
        if (isNull(loader)) {
            ClassLoader newLoader = DispatcherClassLoader.getClassLoader(path);
            loader = CLASS_LOADER_MAPPING.putIfAbsent(name, newLoader);
            if (isNull(loader))
                loader = newLoader;
        }
        return loader;
    }

    public static void removeClassLoader(String name) {
        CLASS_LOADER_MAPPING.remove(name);
    }

}
