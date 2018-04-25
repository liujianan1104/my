//package mywork.my.myService;
//
//
//import cn.hutool.json.JSONArray;
//import cn.hutool.json.JSONObject;
//
//import java.io.*;
//import java.util.HashMap;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class Content {
//
//    public static void main(String[] args) {
////        System.out.println(1);
////        getContent("D:\\testFile\\bid_detail_single_src_ggzy.weifang.gov.cn.json");
//
////        String content = "{\"status\":200,\"err\":null,\"data\":{\"segWords\":null,\"analysisType\":\"E\",\"matchElementInfo\":null,\"matchConceptInfo\":null,\"cClasResult\":null,\"sClasResult\":null,\"extraResult\":[{\"id\":\"6326233773755252157\",\"name\":\"否定前缀\",\"idPath\":\"6326235043954085746->6326233773734280635->6326233773755252157\",\"namePath\":\"裁判文书->辅助树->否定前缀\",\"extractInfo\":[{\"idExp\":\"@无|不|没|未@\",\"nameExp\":\"@无|不|没|未@\",\"validFlg\":null,\"languageId\":null,\"splitFlg\":null,\"splitSymbol\":null,\"factor\":null,\"matchedInfo\":[{\"matchedIdExp\":\"@无|不|没|未@\",\"matchedNameExp\":\"@无|不|没|未@\",\"splitFlg\":0,\"splitSymbol\":\"\",\"ruleList\":[{\"ruleContent\":\"无|不|没|未\",\"ruleType\":0}],\"synRes\":null,\"matchedValues\":[{\"matchedSentence\":\"裁判结果：无罪释放\",\"sentenceIndex\":0,\"matchIndex\":5,\"matchedName\":\"否定前缀\",\"matchedExp\":\"无|不|没|未\",\"matchedContent\":\"无\"}]}]}]}],\"multiTupleResult\":null,\"relationResult\":null},\"success\":true}";
//        String content = "{\"status\":200,\"err\":null,\"data\":{\"result\":{\"analysisType\":\"C\",\"cClasResult\":[{\"clas_exp_list\":[{\"clasId\":\"6386399949218746915\",\"factor\":5,\"id\":\"34283\",\"idExp\":\"c_6386399949281661572\",\"identFlg\":1,\"matchConinfo\":[{\"id\":\"6386399949281661572\",\"id_path\":\"6386399949281661571->6386399949281661572\",\"langueId\":\"1\",\"level\":1,\"name\":\"预算金额\",\"name_path\":\"招投标C分类->预算金额\",\"note\":\"预算金额\",\"parentId\":\"6386399949281661571\",\"sort\":2,\"topTreeId\":\"6386399949281661571\",\"valueList\":[{\"conceptId\":\"6386399949281661572\",\"id\":\"279298\",\"matchInfo\":[{\"content\":\"采购预算 12万元\",\"position\":0}],\"type\":1,\"validFlg\":1,\"value\":\"(?<!(招标))(本?项\\\\s*目\\\\s*|合同|总财政)?((工程)?概算|总?估算价|招标控制价|限价|总?投资|预\\\\s*算\\\\s*(金额)?(采\\\\s*购)?|采购(项目)?)[^(元|公司|/|售|保证金|要求|代理费)]{0,12}[:： 约为]?[ ]*[\\\\d.,壹贰叁肆伍陆柒捌玖拾万佰仟亿元角零分圆]+[ ]*(圆|仟?元|万元|\\\\(万?元/.\\\\)|\\\\(万?元\\\\)|亿元)(/.)?\\\\)?\"}],\"weight\":0}],\"matchEleinfo\":[],\"matchKeyword\":[],\"nameExp\":\"c_预算金额\",\"splitFlg\":0,\"splitSymbol\":\"\",\"srcIdExp\":\"c_6386399949281661572\",\"srcNameExp\":\"c_预算金额\",\"synAnalyExp\":\"TP:NO=1;OBJ:预算金额概念;POS:0;DIS:5;COMP:1\",\"validFlg\":1}],\"id\":\"6386399949218746915\",\"id_path\":\"6386399949231329828->6386399949218746915\",\"level\":1,\"name\":\"预算金额\",\"name_path\":\"招投标C分类->预算金额\",\"root\":\"6386399949231329828\",\"srcNameExpList\":[\"c_预算金额\"],\"synFlg\":0,\"weight\":0.5}],\"matchConceptInfo\":[{\"id\":\"6386399949281661572\",\"id_path\":\"6386399949281661571->6386399949281661572\",\"langueId\":\"1\",\"level\":1,\"name\":\"预算金额\",\"name_path\":\"招投标C分类->预算金额\",\"note\":\"预算金额\",\"parentId\":\"6386399949281661571\",\"sort\":2,\"topTreeId\":\"6386399949281661571\",\"valueList\":[{\"conceptId\":\"6386399949281661572\",\"id\":\"279298\",\"matchInfo\":[{\"content\":\"采购预算 12万元\",\"position\":0}],\"type\":1,\"validFlg\":1,\"value\":\"(?<!(招标))(本?项\\\\s*目\\\\s*|合同|总财政)?((工程)?概算|总?估算价|招标控制价|限价|总?投资|预\\\\s*算\\\\s*(金额)?(采\\\\s*购)?|采购(项目)?)[^(元|公司|/|售|保证金|要求|代理费)]{0,12}[:： 约为]?[ ]*[\\\\d.,壹贰叁肆伍陆柒捌玖拾万佰仟亿元角零分圆]+[ ]*(圆|仟?元|万元|\\\\(万?元/.\\\\)|\\\\(万?元\\\\)|亿元)(/.)?\\\\)?\"}],\"weight\":0}],\"matchElementInfo\":[]},\"meta\":[{\"analysisResultType\":\"c\",\"dataSourceList\":[1],\"defaultValue\":\"\",\"desc\":\"result\",\"fields\":[],\"name\":\"result\",\"sort\":1,\"type\":\"com.dinfo.model.onto.bean.AnalysisResult\"}]},\"success\":true}";
//
//        HashMap<String,String> h = getResult(content);
//        System.out.println(h);
//
//
//    }
//
//    public static void getContent(String filePath) {
//        String line = null ;
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
//
//            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filePath+".out")));
//            while( (line = br.readLine())!= null ){
//
////                System.out.println(line);
//                JSONObject jsonObject = JSONObject.fromObject(line);
////                String Content = jsonObject.getString("Content");
//                String content = jsonObject.get("Content").toString();
//                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//                Matcher m = p.matcher(content);
//                content = m.replaceAll("");
//                bw.write(content);
//                bw.newLine();
//            }
//            br.close();
//            bw.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static HashMap getResult(String content) {
//        HashMap<String,String> resultMap=new HashMap<String,String>();
//
//        JSONObject json=JSONObject.fromObject(content);
//        JSONObject data=json.getJSONObject("data");
//        JSONObject result=json.getJSONObject("result");
//        String analysisType = result.getString("analysisType");
//
//
//        if ("E".equals(analysisType)) {
//            resultMap = getExtraResult(content);
//        } else if ("C".equals(analysisType)) {
//            resultMap = getCClasResult(content);
//        }
//    return resultMap;
//    }
//
//    /**
//     * 解析语料中matchedName与matchedContent
//     * @param content 语料
//     * @return
//     */
//    public static HashMap<String,String> getExtraResult(String content) {
//
//        HashMap<String,String> resultMap=new HashMap<String,String>();
//
//        JSONObject json=JSONObject.fromObject(content);
//        JSONObject data=json.getJSONObject("data");
//        JSONArray extraResult=data.getJSONArray("extraResult");
//
//        if(extraResult.size()>0){
//            for(int i=0;i<extraResult.size();i++){
//                JSONObject cClasResult = extraResult.getJSONObject(i);
//                JSONArray extractInfos=cClasResult.getJSONArray("extractInfo");
//                for(int j=0;j<extractInfos.size();j++){
//                    JSONObject extractInfo = extractInfos.getJSONObject(j);
//                    JSONArray matchedInfos=extractInfo.getJSONArray("matchedInfo");
//                    for(int k=0;k<matchedInfos.size();k++){
//                        JSONObject matchConinfo=matchedInfos.getJSONObject(k);
//                        JSONArray matchedValues=matchConinfo.getJSONArray("matchedValues");
//                        for(int m=0;m<matchedValues.size();m++){
//                            JSONObject matchedValue=matchedValues.getJSONObject(m);
//                            String matchedName=matchedValue.getString("matchedName");
//                            String matchedContent=matchedValue.getString("matchedContent");
//                            if(resultMap.containsKey(matchedName)){
//                                String value=resultMap.get(matchedName)+"|"+matchedContent;
//                                resultMap.put(matchedName, value);
//                            }else{
//                                resultMap.put(matchedName, matchedContent);
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
//    /**
//     * 解析语料中name与Content
//     * @param content 语料
//     * @return
//     */
//    public static HashMap<String,String> getCClasResult(String content) {
//
//        HashMap<String,String> resultMap=new HashMap<String,String>();
//
//        JSONObject json=JSONObject.fromObject(content);
//        JSONObject data=json.getJSONObject("data");
//        JSONObject result=json.getJSONObject("result");
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
//                                String matchContent=matchInfo.getString("Content");
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
//}
