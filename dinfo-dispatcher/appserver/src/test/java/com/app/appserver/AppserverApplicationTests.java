package com.app.appserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppserverApplicationTests {

    public static void main(String[] args) {
        System.out.println(1);
        ClassLoader c = Thread.currentThread().getContextClassLoader();
        System.out.println(c);
        c.setPackageAssertionStatus("sdf.jar",true);
    }

    @Test
    public void contextLoads() {
    }

}
