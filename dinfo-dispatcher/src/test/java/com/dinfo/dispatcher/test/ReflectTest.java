package com.dinfo.dispatcher.test;

import com.dinfo.dispatcher.util.IOUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author yangxf
 */
public class ReflectTest {

    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException {
        System.out.println(System.getProperty("java.class.path"));
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL("file:/Users/yangxuefeng/Documents/lib/print1.jar")});
//        ClassLoader.getSystemClassLoader().loadClass("com.yxf.Main");
        urlClassLoader.loadClass("com.yxf.Main");
    }

    @Test
    public void test2() throws ClassNotFoundException {
        ClassLoader.getSystemClassLoader().loadClass("com.app.appserver.service.AppServer");
    }

    @Test
    public void test() throws Exception, URISyntaxException {
//        String p = "/Users/yangxf/Documents/lib/paas-service.jar!/BOOT-INF/classes/";
        URL url = new URL("jar:file:D:\\lib\\appserver-0.0.1-SNAPSHOT.jar!/BOOT-INF/lib/spark-sql_2.11-2.2.0.jar");
        URLClassLoader loader = new URLClassLoader(new URL[]{url});
//        loader.loadClass("org.apache.spark.sql.Dataset");
//        Path tempFile = Files.createTempFile("dir", "name");
        File tempFile = File.createTempFile("D:\\lib\\", "1.jar");
        InputStream inputStream = url.openStream();
        IOUtil.copy(inputStream, new FileOutputStream(tempFile));
        URL url1 = tempFile.toURI().toURL();
        URLClassLoader loader1 = new URLClassLoader(new URL[]{url1});
        loader1.loadClass("org.apache.spark.sql.Dataset");
//        loader.loadClass("javassist.ClassPool");
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
//        HashMap<Object, Object> map1 = new HashMap<>(), map2 = new HashMap<>(), map3 = new HashMap<>();
//        map1.put("name", "jjj");
//        map2.put("kkk", "ooo");
//        map3.put("llll", "qqq");
//        Object[] obj = new Object[]{map1, map2, map3};
//        URL url = new URL("jar:file:/Users/yangxf/Documents/lib/123.jar!/BOOT-INF/classes/");
//        URLClassLoader loader = new URLClassLoader(new URL[]{url});
//        ClassLoader loader = ClassLoader.getSystemClassLoader();
//        Class<?> aClass = loader.loadClass("com.app.appserver.service.AppServer");
//        Object o = aClass.newInstance();
////        Thread.currentThread().setContextClassLoader(loader);
//        ClassPool pool = ClassPool.getDefault();
//        System.out.println(pool.getClassLoader().getClass().getName());
//        URL url1 = new URL("jar:file:/Users/yangxuefeng/Documents/lib/123.jar!/BOOT-INF/classes/com/app/appserver/service/AppServer.class");
//
//        CtClass ctClass1 = pool.makeClass(url1.openStream());
//        System.out.println(ctClass1.toClass().getName());
//        CtClass ctClass = pool.makeClass("AppServer1");
//        CtClass inter = pool.get("com.dinfo.dispatcher.test.ReflectTest$Invoker");
//        ctClass.addInterface(inter);
//        CtMethod cm = CtMethod.make("public Object invoke(Object[] obj) {" +
//                "return new com.app.appserver.service.AppServer().httpLocalExecute((java.util.Map)obj[0], (java.util.Map)obj[1], (java.util.Map)obj[2]);" +
//                "}", ctClass);
//        ctClass.addMethod(cm);
//
//        Class aClass1 = ctClass.toClass();
//        Invoker invoker = (Invoker) aClass1.newInstance();
//
//        long t1 = System.nanoTime();
//        Method m = aClass.getMethod("httpLocalExecute", new Class[]{Map.class, Map.class, Map.class});
//        Object invoke = m.invoke(o, obj);
//        long t2 = System.nanoTime();
//
//        invoker.invoke(obj);
//
//        long t3 = System.nanoTime();
//
//        System.out.println("\nreflect: " + (t2 - t1));
//        System.out.println("javassist: " + (t3 - t2));
    }


    public interface Invoker {
        Object invoke(Object... args);
    }

}

