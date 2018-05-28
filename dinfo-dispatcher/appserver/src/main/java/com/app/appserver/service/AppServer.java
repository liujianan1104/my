package com.app.appserver.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Liu Jianan
 * Date: 2018/5/21 15:14
 * Desc:
 */
public class AppServer implements BaseCom {
    @Override
    public Dataset<Row> sparkExecute(Dataset<Row> dataset, Map<String, Object> map, Map<String, Object> map1) throws Exception {
        return null;
    }

    @Override
    public Dataset<Row> sparkResultWrapper(Dataset<Row> dataset, Map<String, Object> map) throws Exception {
        return null;
    }

    @Override
    public void appendComParamMap(Map<String, Object> map, Map<String, String> map1) throws Exception {

    }

    @Override
    public Map<String,List<Object>> httpLocalExecute(Map<String,List<Object>> input, Map<String, Object> paramMap, Map<String, Object> comContext) throws Exception{

        Map<String,List<Object>> outMap = new HashMap<>();

        //获取数据源并逐条处理
        List<String> data = (List)input.get("in");
        List<Object> result = new ArrayList<>();
        for(String str: data){
            result.add(convertString(str));
        }

        //输出
        outMap.put("out", result);
        return outMap;
    }

    @Override
    public Map<String, List<Object>> httpSparkResultWrapper(Map<String, List<Object>> map, Map<String, Object> map1) throws Exception {
        return null;
    }

    /**
     * 大小写字母转换
     * @param str
     * @return
     */
    private String convertString(String str) {
        char[] ch = str.toCharArray();
        StringBuffer sbf = new StringBuffer();
        for(int i=0; i< ch.length; i++){
            if (ch[i] >= 'A' && ch[i] <= 'Z') {
                sbf.append(ch[i]+=32);
            } else if (ch[i] >= 'a' && ch[i] <= 'z') {
                sbf.append((ch[i]-=32));
            } else {
                sbf.append(ch[i]);
            }
        }
        return sbf.toString();

    }
//    private static final Logger logger = LoggerFactory.getLogger(AppServer.class);

//    @Override
//    public Map<String,List<String>> httpLocalExecute(Map<String,List<String>> input, Map<String, Object> paramMap, Map<String, Object> comContext) throws Exception{
//        Map<String,List<String>> result = new HashMap<String,List<String>>();
////        logger.info("input:" + input);
////        logger.info("paramMap:" + paramMap);
////        logger.info("comContext:" + comContext);
//
//        System.out.println("input:" + input);
//        System.out.println("paramMap:" + paramMap);
//        System.out.println("comContext:" + comContext);
//
//        for (String key : input.keySet()) {
//            result.put(key,input.get(key));
//        }
//
//        for (String key : paramMap.keySet()) {
//            result.put(key,new ArrayList<String>(){{add(paramMap.get(key).toString());}});
//        }
//
//        for (String key : comContext.keySet()) {
//            result.put(key,new ArrayList<String>(){{add(comContext.get(key).toString());}});
//        }
//
//        return result;
//    }
}
