package com.app.appserver.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;
import java.util.Map;

/**
 * 接口组件抽象接口，组件接口实现的父类
 * Created by chentian on 2017/10/18.
 */
public interface BaseCom {
    /**
     * 组件执行主方法返回hashMap中dataset所在的hash key值
     */
//    public static final String HASH_KEY_DATASET="dataset";
    public static final String COMPARAMKEY_CLASSIFYNAME = "classifyName";
    /**
     * 组件执行主方法
     * @param input 输入的dataset
     * @param paramMap 参数列表
     * @param comContext 组件上下文，可以把组件中产生的词典等信息，放到上下文中，供下游组件使用
     * @return 组件返回的结果
     */
    public Dataset<Row> sparkExecute(Dataset<Row> input, Map<String, Object> paramMap, Map<String, Object> comContext) throws Exception;

    /**
     * spark算法执行结果封装类，debug及传递到结束组件前调用
     * @param unwrapperDs 算法执行结果（未封装）
     * @param comContext 组件上下文
     * @return
     * @throws Exception
     */
    public Dataset<Row> sparkResultWrapper(Dataset<Row> unwrapperDs, Map<String, Object> comContext) throws Exception;

    /**
     * 将算法对应的效果参数放到目标map里(输入输出参数除外)
     * @param sourceMap 来源参数map 【模型训练train方法或组件执行主方法sparkExecute中的输入参数】
     * @param targetMap 目标参数map 【调用nlp算法包pipeline需要传递的参数对象】
     * @throws Exception
     */
    public void appendComParamMap(Map<String, Object> sourceMap, Map<String, String> targetMap)throws Exception;

    /**
     * Http方式调用的组件方法
     * @param input 输入map
     * @param paramMap 参数列表
     * @param comContext 组件上下文，可以把组件中产生的词典等信息，放到上下文中，供下游组件使用
     * @return Http组件返回的结果
     */
    public Map<String,List<Object>> httpLocalExecute(Map<String, List<Object>> input, Map<String, Object> paramMap, Map<String, Object> comContext) throws Exception;

    /**
     * http方式 spark算法执行结果封装类，传递到结束组件前调用
     * @param unwrapperRs 算法执行结果（未封装）
     * @param comContext 组件上下文
     * @return
     * @throws Exception
     */
    public Map<String, List<Object>> httpSparkResultWrapper(Map<String, List<Object>> unwrapperRs, Map<String, Object> comContext) throws Exception;

}
