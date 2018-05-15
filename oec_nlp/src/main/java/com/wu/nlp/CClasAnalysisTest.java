package com.wu.nlp;

import com.dinfo.ml.common.AlgorithmInfo;
import com.dinfo.ml.corpus.Document;
import com.dinfo.ml.model.CClasAnalysis;
import com.dinfo.ml.pipeline.DefaultTraining;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

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
public class CClasAnalysisTest {

    private SparkSession spark;
    private Dataset<Row> transformData;

    @Before
    public void setUp() throws Exception {
//        org.apache.log4j.LogManager.resetConfiguration();
//        org.apache.log4j.PropertyConfigurator.configure("/Users/wushaojun/workspace/oec_nlp/src/main/resources/log4j.properties");
        //如果你有hadoop环境变量可以不加这个，没有hadoop环境可以去网上下（只需要bin和etc就可以）
        System.setProperty("hadoop.home.dir","/Users/wushaojun/Downloads/tmp/hadoop-2.7.0");
        Logger.getLogger("org").setLevel(Level.ERROR);
        //创建sparkSession
        spark = SparkSession
                .builder()
                .master("local[2]")
                .appName("my first spark run")
                .config("spark.sql.crossJoin.enabled", "true")
                .getOrCreate();
        //创建测试数据
//        spark.sparkContext().setLogLevel("ERROR");

        transformData = spark.createDataFrame(Arrays.asList(
                new Document(2L, "五、相关服务机构,（一）基金份额发售机构,1、直销机构,（1）银河基金管理有限公司,办公地址:中国（上海）自由贸易试验区世纪大道1568号15层,法定代表人:刘立达,公司网站:www.galaxyasset.com（支持网上交易）,客户服务电话:400-820-0860,直销业务电话:（021）38568981/38568507,传真交易电话:（021）38568985,联系人:赵冉、郑夫桦,（2）银河基金管理有限公司北京分公司,地址:北京市西城区月坛西街6号院a-f座三层（邮编:100045）,电话:（010）56086900,传真:（010）56086939,联系人:孙妍,（3）银河基")
        ), Document.class);
    }


    @Test
    public void testCClasAnalysis(){
        //这就是一个读取xml的内容的文件读取方法
        String s = SchemaTest.readFile("/Users/wushaojun/Downloads/tmp/企业风控20180504.xml","utf-8");

        CClasAnalysis model = new CClasAnalysis();
        //set模型数据
        model.setSchema(s);
        //分析
        Dataset<Row> result = model.transform(transformData);
        //结果展示
//        result.show(false);
        for (Row row : result.collectAsList()) {
            String output = row.getAs("result");
            System.out.println(output);

        }

    }

    @Test
    public void testCClasAnalysisSave(){

        String s = SchemaTest.readFile("E://C分类.txt");

        CClasAnalysis model = new CClasAnalysis();
        AlgorithmInfo algorithmInfo = model.getAlgorithmInfo();
        //这样可以输出算法相关信息
        System.out.println(algorithmInfo);
        model.setSchema(s);

        try {
            //可以存在hdfs地址或是本地地址，前提是有hadoop环境
            model.write().overwrite().save("hdfs://192.168.181.200:9000/ultra-nlp-model/C/");
        } catch (IOException e) {
            e.printStackTrace();

        }

    }


    @Test
    public void testCClasAnalysisload(){
        //通过该方法可以直接load出相应地址的模型
        CClasAnalysis model = CClasAnalysis.load("hdfs://192.168.181.200:9000/ultra-nlp-model/C/");

        Dataset<Row> result = model.transform(transformData);
        result.show(false);

    }

    @Test
    public void testDtUse(){
        //可以放在pipeline中使用
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("classifier.CClasAnalysis.rootId", "6327729995837939236");

        String s = SchemaTest.readFile("E://C分类.txt");

        CClasAnalysis model = new CClasAnalysis();
        model.setSchema(s);

        DefaultTraining dt = new DefaultTraining();
        dt.addAlgorithm(model);
        PipelineModel fit = dt.fit(transformData, map);

        fit.transform(transformData).show(false);

    }
}