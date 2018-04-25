//package mywork.my.myService;
//
//import com.dinfo.db.HttpPostReq;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class CClasResult {
//
//    public static void main(String[] args) throws IOException {
//
//
//        Map<String,String> params=new HashMap<String,String>();
//        String simple = "采购预算 12万元";
////		String rootExtId = "6iuxJesx";
//        params.put("input", simple);
//        params.put("token","36f83fa6cf3c4380bd206b8791feda06");
////		params.put("rootExtId", rootExtId);
//        String str=new HttpPostReq().post("http://60.247.77.152:8990/run/322/865",params);
//        System.out.println(str);
//
//
//        CClasResult cClasResult = new CClasResult();
////        str = "{\"status\":200,\"err\":null,\"data\":{\"result\":{\"analysisType\":\"C\",\"cClasResult\":[{\"clas_exp_list\":[{\"clasId\":\"6386399949218746915\",\"factor\":5,\"id\":\"34283\",\"idExp\":\"c_6386399949281661572\",\"identFlg\":1,\"matchConinfo\":[{\"id\":\"6386399949281661572\",\"id_path\":\"6386399949281661571->6386399949281661572\",\"langueId\":\"1\",\"level\":1,\"name\":\"预算金额\",\"name_path\":\"招投标C分类->预算金额\",\"note\":\"预算金额\",\"parentId\":\"6386399949281661571\",\"sort\":2,\"topTreeId\":\"6386399949281661571\",\"valueList\":[{\"conceptId\":\"6386399949281661572\",\"id\":\"279298\",\"matchInfo\":[{\"content\":\"采购预算 12万元\",\"position\":0}],\"type\":1,\"validFlg\":1,\"value\":\"(?<!(招标))(本?项\\\\s*目\\\\s*|合同|总财政)?((工程)?概算|总?估算价|招标控制价|限价|总?投资|预\\\\s*算\\\\s*(金额)?(采\\\\s*购)?|采购(项目)?)[^(元|公司|/|售|保证金|要求|代理费)]{0,12}[:： 约为]?[ ]*[\\\\d.,壹贰叁肆伍陆柒捌玖拾万佰仟亿元角零分圆]+[ ]*(圆|仟?元|万元|\\\\(万?元/.\\\\)|\\\\(万?元\\\\)|亿元)(/.)?\\\\)?\"}],\"weight\":0}],\"matchEleinfo\":[],\"matchKeyword\":[],\"nameExp\":\"c_预算金额\",\"splitFlg\":0,\"splitSymbol\":\"\",\"srcIdExp\":\"c_6386399949281661572\",\"srcNameExp\":\"c_预算金额\",\"synAnalyExp\":\"TP:NO=1;OBJ:预算金额概念;POS:0;DIS:5;COMP:1\",\"validFlg\":1}],\"id\":\"6386399949218746915\",\"id_path\":\"6386399949231329828->6386399949218746915\",\"level\":1,\"name\":\"预算金额\",\"name_path\":\"招投标C分类->预算金额\",\"root\":\"6386399949231329828\",\"srcNameExpList\":[\"c_预算金额\"],\"synFlg\":0,\"weight\":0.5}],\"matchConceptInfo\":[{\"id\":\"6386399949281661572\",\"id_path\":\"6386399949281661571->6386399949281661572\",\"langueId\":\"1\",\"level\":1,\"name\":\"预算金额\",\"name_path\":\"招投标C分类->预算金额\",\"note\":\"预算金额\",\"parentId\":\"6386399949281661571\",\"sort\":2,\"topTreeId\":\"6386399949281661571\",\"valueList\":[{\"conceptId\":\"6386399949281661572\",\"id\":\"279298\",\"matchInfo\":[{\"content\":\"采购预算 12万元\",\"position\":0}],\"type\":1,\"validFlg\":1,\"value\":\"(?<!(招标))(本?项\\\\s*目\\\\s*|合同|总财政)?((工程)?概算|总?估算价|招标控制价|限价|总?投资|预\\\\s*算\\\\s*(金额)?(采\\\\s*购)?|采购(项目)?)[^(元|公司|/|售|保证金|要求|代理费)]{0,12}[:： 约为]?[ ]*[\\\\d.,壹贰叁肆伍陆柒捌玖拾万佰仟亿元角零分圆]+[ ]*(圆|仟?元|万元|\\\\(万?元/.\\\\)|\\\\(万?元\\\\)|亿元)(/.)?\\\\)?\"}],\"weight\":0}],\"matchElementInfo\":[]},\"meta\":[{\"analysisResultType\":\"c\",\"dataSourceList\":[1],\"defaultValue\":\"\",\"desc\":\"result\",\"fields\":[],\"name\":\"result\",\"sort\":1,\"type\":\"com.dinfo.model.onto.bean.AnalysisResult\"}]},\"success\":true}";
//        HashMap<String,String> h = cClasResult.getCClasResult(str);
//
//        System.out.println(h);
//
//
//
//    }
//
//
//    /**
//     * 解析语料中name与Content
//     * @param content 语料
//     * @return
//     */
//    public HashMap<String,String> getCClasResult(String content) {
//
//        HashMap<String,String> resultMap=new HashMap<String,String>();
//
//        JSONObject json=JSONObject.fromObject(content);
//        JSONObject data=json.getJSONObject("data");
//        JSONObject result=data.getJSONObject("result");
//        JSONArray cClasResults=result.getJSONArray("cClasResult");
//
//        if(cClasResults.size()>0){
//            for(int i=0;i<cClasResults.size();i++){
//                JSONObject cClasResult = cClasResults.getJSONObject(i);
//                JSONArray clas_exp_lists=cClasResult.getJSONArray("clas_exp_list");
//                for(int j=0;j<clas_exp_lists.size();j++){
//                    JSONObject clas_exp_list = clas_exp_lists.getJSONObject(j);
//                    JSONArray matchConinfos=clas_exp_list.getJSONArray("matchConinfo");
//                    for(int k=0;k<matchConinfos.size();k++){
//                        JSONObject matchConinfo=matchConinfos.getJSONObject(k);
//                        String matchName=matchConinfo.getString("name");
//                        JSONArray valueLists=matchConinfo.getJSONArray("valueList");
//                        for(int m=0;m<valueLists.size();m++){
//                            JSONObject valueList=valueLists.getJSONObject(m);
//                            JSONArray matchInfos=valueList.getJSONArray("matchInfo");
//                            String matchedContent="";
//                            for(int n=0;n<matchInfos.size();n++){
//                                JSONObject matchInfo=matchInfos.getJSONObject(n);
//                                String matchContent=matchInfo.getString("content");
//                                if(resultMap.containsKey(matchName)){
//                                    matchedContent+=matchContent+"|";
//                                }else{
//                                    matchedContent=matchContent+"|";
//                                }
//                                resultMap.put(matchName, matchedContent);
//                            }
//
//                        }
//                    }
//                }
//
//            }
//
//        }
//
//        return resultMap;
//    }
//
//}
