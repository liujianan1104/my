package com.wu.nlp;

import com.dinfo.ml.model.analysis.CClassficationAnalysisVT;
import com.dinfo.ml.model.analysis.ExtractAnalysisVT;
import com.dinfo.ml.model.core.model.OecCompleteModel;
import com.dinfo.ml.model.core.onto.bean.AnalysisResult;
import com.dinfo.ml.model.schema.SchemaModelTransfer;
import org.junit.Test;

/**
 * User: Adonis wu
 * Date: 2018/5/7
 * Desc:
 */
public class SimpleWay {


    @Test
    public void testEAnalysisLocal(){

        String schema = SchemaModelTransfer.checkSchema("/Users/wushaojun/Downloads/tmp/PR10022_20170323160132_final.xml");
        OecCompleteModel oecCompleteModel = SchemaModelTransfer.getOecCompleteModel(schema);


        AnalysisResult analysisResult = null;
        try {
            String content = "分析内容";
            analysisResult = new ExtractAnalysisVT().analyTextByExModel(oecCompleteModel, -1L, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(analysisResult);


    }

    @Test
    public void testCSingle(){

        String s = SchemaTest.readFile("/Users/wushaojun/Downloads/tmp/PR10022_20170323160132_final.xml", "utf-8");
        OecCompleteModel oecCompleteModel = SchemaModelTransfer.getOecCompleteModel(s);
        try {
            AnalysisResult result = new CClassficationAnalysisVT().analyTextByCclasModel(oecCompleteModel, -1L,-1,"吴相博服务很好");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
