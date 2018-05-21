package com.dinfo.dispatcher.test;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * User: Liu Jianan
 * Date: 2018/5/21 17:00
 * Desc:
 */
public class JarTest {

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        URL url1 = new URL("file:D:/lib/123.jar");
        URLClassLoader myClassLoader1 = new URLClassLoader(new URL[] { url1 }, Thread.currentThread()
                .getContextClassLoader());
        Class<?> myClass1 = myClassLoader1.loadClass("com.app.appserver.service.AppServer");
        Object action1 =  myClass1.newInstance();
        System.out.println(action1);
    }
}
