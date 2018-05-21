package com.app.appserver.service;

import java.util.List;
import java.util.Map;

/**
 * User: Liu Jianan
 * Date: 2018/5/21 15:14
 * Desc:
 */
public class AppServer {
    public Map<String,List<String>> httpLocalExecute(Map<String,List<String>> input, Map<String, Object> paramMap, Map<String, Object> comContext) throws Exception{
        System.out.print(input);
        System.out.print(paramMap);
        System.out.print(comContext);
        return null;
    }
}
