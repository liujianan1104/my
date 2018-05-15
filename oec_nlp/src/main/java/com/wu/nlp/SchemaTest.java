package com.wu.nlp;

import com.dinfo.ml.model.analysis.CClassficationAnalysis;
import com.dinfo.ml.model.analysis.ExtractAnalysis;
import com.dinfo.ml.model.analysis.MultitupleAnalysis;
import com.dinfo.ml.model.analysis.RelationAnalysis;
import com.dinfo.ml.model.core.model.OecCompleteModel;
import com.dinfo.ml.model.core.onto.bean.AnalysisResult;
import com.dinfo.ml.model.core.onto.bean.BaseResource;
import com.dinfo.ml.model.schema.ParseSchema;
import com.dinfo.ml.model.schema.SchemaModelTransfer;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Date:2017/11/5</p>
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
public class SchemaTest {

    @Test
    public void testJavaMulAna() throws Exception {

        String s = SchemaTest.readFile("C:\\Users\\dell\\Desktop\\schema修改\\信用卡投诉多元组.txt");

        MultitupleAnalysis multitupleAnalysis = new MultitupleAnalysis();

        OecCompleteModel oecCompleteModel = SchemaModelTransfer.getOecCompleteModel(s);

        AnalysisResult analysisResult = multitupleAnalysis.analyTextByMultiModel(oecCompleteModel, -1L, 0, "01号柜员在我办卡的时候服务不好");

        System.out.println(analysisResult);


    }



    @Test
    public void testJavaExtractAnan(){
        String s = readFile("C:\\Users\\dell\\Desktop\\schema修改\\入室盗窃信息抽取.txt","utf-8");

        long startModelTransfer = System.currentTimeMillis();
        OecCompleteModel oecCompleteModel = SchemaModelTransfer.getOecCompleteModel(s);
        long endModelTransfer = System.currentTimeMillis();
        System.out.println("SCHEMA:"+(endModelTransfer-startModelTransfer));


        long startJSONTransfer = System.currentTimeMillis();
        long endJSONTransfer = System.currentTimeMillis();
        System.out.println("JSON:"+(endJSONTransfer-startJSONTransfer));
        ExtractAnalysis extractAnalysis = new ExtractAnalysis();
        try {
            AnalysisResult analysisResult = extractAnalysis.analyTextByExModel(oecCompleteModel, -1L, "张某趁十一黄金周吴某全家旅游之际，撬开窗户进入吴某家中盗走现金20000元");
            ObjectMapper objectMapper = new ObjectMapper();
            String s1 = objectMapper.writeValueAsString(analysisResult);
            System.out.println(s1);
            System.out.println(s1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testJavaRelAna() throws Exception {

        String s = SchemaTest.readFile("C:\\Users\\dell\\Desktop\\schema修改\\企业关系关联分析.txt");

        RelationAnalysis relationAnalysis = new RelationAnalysis();

        OecCompleteModel oecCompleteModel = SchemaModelTransfer.getOecCompleteModel(s);

        AnalysisResult analysisResult = relationAnalysis.analyTextByRelModel(oecCompleteModel,-1L,"中国银行投资中国队");

        System.out.println(analysisResult);


    }


    @Test
    public void testJavaExtractAna(){



        String s = readFile("E://C分类.txt");

//        ParseSchema parseSchema = new ParseSchema();
//        parseSchema.parseSchema(s.toString());
//
//        BaseResource baseResource = parseSchema.loadBaseResource();
//        GetEontoRes getEontoRes = new GetEontoRes(parseSchema);
//        ExtractResource extractResource = new ExtractResource(getEontoRes.getRootNodeMap(baseResource));

        long startModelTransfer = System.currentTimeMillis();
        OecCompleteModel oecCompleteModel = SchemaModelTransfer.getOecCompleteModel(s);
        long endModelTransfer = System.currentTimeMillis();
        System.out.println("SCHEMA:"+(endModelTransfer-startModelTransfer));


        long startJSONTransfer = System.currentTimeMillis();
        long endJSONTransfer = System.currentTimeMillis();
        System.out.println("JSON:"+(endJSONTransfer-startJSONTransfer));
        ExtractAnalysis extractAnalysis = new ExtractAnalysis();
//        try {
//            AnalysisResult analysisResult = extractAnalysis.analyTextByExModel(oecCompleteModel, null, "张某趁房主不在入室行窃，拿走手机和3000元现金");
//            ObjectMapper objectMapper = new ObjectMapper();
//            String s1 = objectMapper.writeValueAsString(analysisResult);
//            System.out.println(s1);
//            System.out.println(s1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }


    @Test
    public void testJavaSAna(){



        String s = readFile("C:\\Users\\dell\\Desktop\\schema修改\\S\\schemamodel.xml");

//        ParseSchema parseSchema = new ParseSchema();
//        parseSchema.parseSchema(s.toString());
//
//        BaseResource baseResource = parseSchema.loadBaseResource();
//        GetEontoRes getEontoRes = new GetEontoRes(parseSchema);
//        ExtractResource extractResource = new ExtractResource(getEontoRes.getRootNodeMap(baseResource));

        long startModelTransfer = System.currentTimeMillis();
        OecCompleteModel oecCompleteModel = SchemaModelTransfer.getOecCompleteModel(s);
        ParseSchema parseSchema = new ParseSchema();
        BaseResource baseResource = parseSchema.parseSchema(s);
        long endModelTransfer = System.currentTimeMillis();
        System.out.println("SCHEMA:"+(endModelTransfer-startModelTransfer));




//        try {
//            AnalysisResult analysisResult = extractAnalysis.analyTextByExModel(oecCompleteModel, null, "张某趁房主不在入室行窃，拿走手机和3000元现金");
//            ObjectMapper objectMapper = new ObjectMapper();
//            String s1 = objectMapper.writeValueAsString(analysisResult);
//            System.out.println(s1);
//            System.out.println(s1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }



    @Test
    public void testJavaCAna(){



        String s = readFile("E://C分类.txt");

//        ParseSchema parseSchema = new ParseSchema();
//        parseSchema.parseSchema(s.toString());
//
//        BaseResource baseResource = parseSchema.loadBaseResource();
//        GetEontoRes getEontoRes = new GetEontoRes(parseSchema);
//        ExtractResource extractResource = new ExtractResource(getEontoRes.getRootNodeMap(baseResource));


        OecCompleteModel oecCompleteModel = SchemaModelTransfer.getOecCompleteModel(s);
        CClassficationAnalysis cClassficationAnalysis = new CClassficationAnalysis();
        AnalysisResult analysisResult = null;
        try {
            analysisResult = cClassficationAnalysis.analyTextByCclasModel(oecCompleteModel, -1L, -1, "呷哺呷哺服务很好");
        } catch (Exception e) {

            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonResult = objectMapper.writeValueAsString(analysisResult);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {




        //String s = readFile("C:\\Users\\dell\\Desktop\\schema修改\\招行投标信息抽取.txt");
        String s = readFile("C:\\Users\\dell\\Desktop\\schema修改\\swarts2.txt");
        //String s = readFile("C:\\Users\\dell\\Desktop\\schema修改\\测试前缀后缀.txt");

//        ParseSchema parseSchema = new ParseSchema();
//        parseSchema.parseSchema(s.toString());
//
//        BaseResource baseResource = parseSchema.loadBaseResource();
//        GetEontoRes getEontoRes = new GetEontoRes(parseSchema);
//        ExtractResource extractResource = new ExtractResource(getEontoRes.getRootNodeMap(baseResource));


        OecCompleteModel oecCompleteModel = SchemaModelTransfer.getOecCompleteModel(s);
        System.out.println("加载完成");
        ExtractAnalysis extractAnalysis = new ExtractAnalysis();
        AnalysisResult analysisResult = null;
        try {

            analysisResult = extractAnalysis.analyTextByExModel(oecCompleteModel,-1L,
                    "沈阳市浑南区人民法院\n" +
                            "民事裁定书\n" +
                            "（2015）浑南民一初字第00860号\n" +
                            "原告关某，女，汉族，住沈阳市东陵区。\n" +
                            "被告董某，男，汉族，住沈阳市东陵区。\n" +
                            "本院在审理原告关某与被告董某离婚后财产纠纷一案中，原告于2015年6月10日向本院提出撤诉申请。\n" +
                            "本院认为，原告自愿申请撤回起诉，符合有关法律规定，应予准许。依据《中华人民共和国民事诉讼法》第一百四十五条第一款的规定，裁定如下：\n" +
                            "准予原告关某撤回起诉。\n" +
                            "案件受理费2007元，减半收取1003.5元，由原告关某承担。\n" +
                            "代理审判员 卢政麒\n" +
                            "二〇一五年六月十日\n" +
                            "书记员 关月");

            System.out.println(analysisResult);
        } catch (Exception e) {

            e.printStackTrace();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonResult = objectMapper.writeValueAsString(analysisResult);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String readFile(String fileName)
    {
        String fileContent = "";
        try
        {
            File f = new File(fileName);
            if(f.isFile()&&f.exists())
            {
                InputStreamReader read = new InputStreamReader(new FileInputStream(f),"gbk");
                BufferedReader reader=new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null)
                {
                    fileContent += line;
                }
                read.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }


        String encoding = getEncoding(fileContent);
        System.out.println(encoding);

        return fileContent;
    }


    public static String readFile(String fileName,String encode)
    {
        String fileContent = "";
        try
        {
            File f = new File(fileName);
            if(f.isFile()&&f.exists())
            {
                InputStreamReader read = new InputStreamReader(new FileInputStream(f),encode);
                BufferedReader reader=new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null)
                {
                    fileContent += line;
                }
                read.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }


        String encoding = getEncoding(fileContent);
        System.out.println(encoding);

        return fileContent;
    }



    public static List<String> readFileLine(String fileName, String encode)
    {
        ArrayList<String> strings = new ArrayList<String>();
        String fileContent = "";
        try
        {
            File f = new File(fileName);
            if(f.isFile()&&f.exists())
            {
                InputStreamReader read = new InputStreamReader(new FileInputStream(f),encode);
                BufferedReader reader=new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null)
                {
                    strings.add(line);
                }
                read.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }


        String encoding = getEncoding(fileContent);
        System.out.println(encoding);

        return strings;
    }


    public static String getEncoding(String str) {
      String encode = "GB2312";  
      try {  
      if (str.equals(new String(str.getBytes(encode), encode))) {  
       String s = encode;  
       return s;  
        }  
        } catch (Exception exception) {
        }
      encode = "ISO-8859-1";  
      try {  
        if (str.equals(new String(str.getBytes(encode), encode))) {  
       String s1 = encode;  
       return s1;  
        }  
      } catch (Exception exception1) {  
      }  
      encode = "UTF-8";  
      try {  
        if (str.equals(new String(str.getBytes(encode), encode))) {  
       String s2 = encode;  
       return s2;  
        }  
      } catch (Exception exception2) {  
      }  
      encode = "GBK";  
      try {  
        if (str.equals(new String(str.getBytes(encode), encode))) {  
       String s3 = encode;  
       return s3;  
        }  
      } catch (Exception exception3) {  
      }  
      return "";  
    }

    
}
