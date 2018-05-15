package com.wu.nlp;

import com.dinfo.ml.corpus.Document;
import com.dinfo.ml.model.ExtractInfoAnalysis;
import com.dinfo.ml.pipeline.DefaultTraining;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import static com.wu.nlp.SchemaTest.readFile;

/**
 * <p>Date:2017/11/1</p>
 * <p>Module:</p>
 * <p>Description: </p>
 * <p>Remark: </p>
 *
 * @author wuxiangbo
 * @version 1.0
 *          <p>------------------------------------------------------------</p>
 *          <p> Change history</p>
 *          <p> Serial number: date:modified person: modification reason:</p>
 */
public class ExtractInfoAnalysisTest {

    private SparkSession spark;
    private Dataset<Row> transformData;

    @Before
    public void setUp() throws Exception {
        //如果你有hadoop环境变量可以不加这个，没有hadoop环境可以去网上下（只需要bin和etc就可以）
        System.setProperty("hadoop.home.dir","D:\\hadoop-2.7.0");
        //创建sparkSession
        spark = SparkSession
                .builder()
                .master("local[2]")
                .appName("my first spark run")
                .config("spark.sql.crossJoin.enabled", "true")
                .getOrCreate();
        //创建测试数据
        transformData = spark.createDataFrame(Arrays.asList(
                new Document(1L, "彭于晏来自河南省"),
                new Document(2L, "犯罪嫌疑人李毅，男，17岁，身份证号411601199903285642，山东聊城人，汉族，初中文化程度，云林街菜鸟物流园职工，非人大代表或政协委员，租住雄楚市雄楚区金港一号小区11栋504室。")
        ), Document.class);


    }





    @Test
    public void testExtractInfoAnalysis(){
        //这就是一个读取xml的内容的文件读取方法
        String s = readFile("C:\\Users\\dell\\Desktop\\schema修改\\E\\信息抽取新.txt","utf-8");

        ExtractInfoAnalysis model = new ExtractInfoAnalysis();
        System.out.println(model);
        model.setSchema(s);

        model.transform(transformData).show(false);

    }

    @Test
    public void testExtractInfoAnalysisSave(){
        //这就是一个读取xml的内容的文件读取方法
        String s = readFile("C:\\Users\\dell\\Desktop\\schema修改\\E\\信息抽取新.txt","utf-8");

        ExtractInfoAnalysis model = new ExtractInfoAnalysis();
        model.setSchema(s);
        try {
            //可以存在hdfs地址或是本地地址，前提是有hadoop环境
            model.write().overwrite().save("hdfs://192.168.181.200:9000/ultra-nlp-model/E/");
        } catch (IOException e) {
            e.printStackTrace();

        }

        model.transform(transformData).show(false);

    }



    @Test
    public void testExtractInfoAnalysisload(){
        //通过该方法可以直接load出相应地址的模型
        ExtractInfoAnalysis model = ExtractInfoAnalysis.load("hdfs://192.168.181.200:9000/ultra-nlp-model/E/");
        model.setRootId(6274067173069659626L);
        Dataset<Row> result = model.transform(transformData);
        result.show(false);

    }

    @Test
    public void testDtUse(){
        //可以放在pipeline中使用
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("feature_selector.ExtractInfoAnalysis.rootId","6274067173069659626");
        ExtractInfoAnalysis eClasAnalysis = ExtractInfoAnalysis.load("hdfs://192.168.181.200:9000/ultra-nlp-model/E/");
        DefaultTraining dt = new DefaultTraining();
        dt.addAlgorithm(eClasAnalysis);
        PipelineModel fit = dt.fit(transformData, map);
        fit.transform(transformData).show(false);

    }
}