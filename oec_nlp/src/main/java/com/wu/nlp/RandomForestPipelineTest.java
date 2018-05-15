package com.wu.nlp;

import com.dinfo.ml.corpus.LabeledDocument;
import com.dinfo.ml.pipeline.randomforest.RandomForestPipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xln on 2017/10/9.
 */
public class RandomForestPipelineTest {

    Dataset<Row> testData,trainData;
    String path;
    RandomForestPipeline rfc;

    public RandomForestPipelineTest() throws IllegalAccessException, InstantiationException {
    }
    SparkSession spark;
    @Before
    public void setUp() throws Exception {
        spark = SparkSession
                .builder()
                .master("local[3]") // 本地1个线程          							//本地测试时加上
                .appName("my first spark run")	//
                .getOrCreate();
        List<Row> rows = new LinkedList<Row>();
        //  读取目录获取每个类别下的语料
        List<LabeledDocument> docs = LabeledDocument.readFromFile("/Users/wushaojun/Downloads/corpus/THUCNews/test/");
        for(LabeledDocument doc :docs){
            String text = doc.getText();
            rows.add(RowFactory.create(doc.getLableName(),text));
        }

        StructType schema = new StructType(new StructField[] {
                new StructField("lableName", DataTypes.StringType, false, Metadata.empty()),
                new StructField("text", DataTypes.StringType, false, Metadata.empty())
        });
        Dataset<Row> data = spark.createDataFrame(rows,schema);

        // 把输入数据分为测试集和训练集2个，2、8分配
        Dataset<Row>[] datasets = data.randomSplit(new double[]{0.8,0.2});
        //       datasets[0].show(20,20);
        testData = datasets[1];
        trainData = datasets[0];

        rfc = new RandomForestPipeline();
        System.out.println(rfc);
    }


    @Test
    public void trainWithParam() throws Exception {
        Map<String,String> map = new HashMap<String,String>();
        map.put("word2feature.CountVectorizer.minDF","3");
        map.put("feature_selector.ChiSquare.numTopFeatures","3000");
        map.put("classifier.RandomForestClassifier.numTrees","100");
        map.put("classifier.RandomForestClassifier.maxDepth","20");

        PipelineModel model = rfc.fit(trainData,map);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String path = "/Users/wushaojun/Downloads/corpus/THUCNews/test/rf.model."+formatter.format(new Date());
        // 模型保存到磁盘
        model.save(path);
        this.path = path;
    }

    //@Test
    public void crossTrain() throws Exception {
        Map<String,List<String>> map = new HashMap<String,List<String>>();
        map.put("word2feature.CountVectorizer.minDF",Arrays.asList(new String[]{"1","2","3","4","5"}));
        map.put("feature_selector.ChiSquare.numTopFeatures",Arrays.asList(new String[]{"800","1500","2000","3000","4000","5000"}));
        map.put("classifier.RandomForest.numTrees",Arrays.asList(new String[]{"5","10","20","30","40"}));
        map.put("classifier.RandomForest.maxDepth",Arrays.asList(new String[]{"1","3","5","6","8","10"}));
        map.put("classifier.RandomForest.maxBins",Arrays.asList(new String[]{"10","20","32","40","50"}));
        map.put("classifier.RandomForest.minInfoGain",Arrays.asList(new String[]{"0.0","0.01","0.05","0.1","0.2"}));
        PipelineModel model = rfc.crossedfit(trainData,map,true,2);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String path = "D://model//rfCross.model."+formatter.format(new Date());
        // 模型保存到磁盘
        model.save(path);
        this.path = path;
    }

    @After
    public void predict(){
        // 从磁盘文件恢复模型
        Dataset<Row> predicts;
        PipelineModel model2 = PipelineModel.load(path);
        // 使用模型预测结果
        predicts = model2.transform(testData);
        Dataset<Row> p = predicts.select(
                new Column[]{new Column("label"),new Column("segment_result"),new Column("features")
                        ,new Column("wordCountVector") ,new Column("chifeatures"),new Column("prediction")});
        p.show(40,40);
//        p.show(30,true);
        //计算预测的准确程度
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("label")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        double accuracy = evaluator.evaluate(predicts);
        System.out.println("Test set accuracy = " + accuracy);

        predicts = model2.transform(trainData);
        //计算预测的准确程度
        evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("label")
                .setPredictionCol("prediction")
                .setMetricName("accuracy");
        System.out.println("Train set accuracy = " + evaluator.evaluate(predicts));

        rfc.deplayCurrentParameter();

//        for(PipelineStage s:model2.stages()){
//            if( s instanceof ChiSqSelectorModel){
//                ChiSqSelectorModel cs = (ChiSqSelectorModel)s;
//                System.out.println("chi select dic len  = " + cs.selectedFeatures().length);
//            }
//        }
        spark.close();
    }
}