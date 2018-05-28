package com.app.appserver;

import com.app.appserver.service.AppServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Liu Jianan
 * Date: 2018/5/24 16:31
 * Desc:
 */
public class AppTest {
    public static void main(String[] args) throws Exception {
        AppServer a = new AppServer();
        List<Object> list = new ArrayList<Object>(){{add("aaa");add("bbb");}};
        Map<String,List<Object>> input = new HashMap<>();
        Map<String, Object> paramMap = new HashMap<>();
        Map<String, Object> comContext = new HashMap<>();
        input.put("in",list);

        System.out.println(a.httpLocalExecute(input,paramMap,comContext));


    }
}
