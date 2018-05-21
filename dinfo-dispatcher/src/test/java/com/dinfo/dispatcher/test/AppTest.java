package com.dinfo.dispatcher.test;

import com.dinfo.dispatcher.controller.DispatcherController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author yangxf
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {

    @Resource
    private DispatcherController controller;

    @Test
    public void test() {

    }

}
