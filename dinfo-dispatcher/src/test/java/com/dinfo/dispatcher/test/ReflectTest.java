package com.dinfo.dispatcher.test;

import com.dinfo.dispatcher.core.DispatcherClassLoader;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author yangxf
 */
public class ReflectTest {

    @Test
    public void test() throws Exception, URISyntaxException {
//        String p = "/Users/yangxuefeng/Documents/lib/app.jar!/BOOT-INF/lib/jaxb-api-2.3.0-b170201.1204.jar";
//        String p2 = "/Users/yangxuefeng/Documents/lib/app.jar";
//        String name = new JarFile("file:" + p).getName();
//        System.out.println(name);
//        JarFile jarFile = new JarFile( p2);
//        JarEntry jarEntry = jarFile.getJarEntry("BOOT-INF/lib/jaxb-api-2.3.0-b170201.1204.jar");
//        System.out.println(jarEntry);
//        jarFile.stream()
//                .forEach(j -> System.out.println(j.getName()));
//        URL url = new URL("file:" + p);
//        InputStream in = url.openStream();
//        String file = url.getFile();
//        System.out.println(file);
//        System.out.println(url.getPath());
//        System.out.println(url.openStream());
//        File file1 = new File(url.toURI());
//        System.out.println(file1.exists());

//        JarFile jarFile = new JarFile("/Users/yangxuefeng/Documents/lib/123.jar");
//        jarFile.stream().map(JarEntry::getName).forEach(System.out::println);
//        System.out.println(jarFile.getName());

//        URL url = new URL("jar:file:/Users/yangxuefeng/Documents/lib/123.jar!/BOOT-INF/classes/");
//        URL url = new URL("jar:file:///Users/yangxuefeng/Documents/lib/123.jar!/BOOT-INF/classes/");
//        URLClassLoader loader = new URLClassLoader(new URL[]{url});
//        loader.loadClass("com.app.appserver.service.AppServer");
    }

}

