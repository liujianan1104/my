//package mywork.my.myService;
//
//import cn.hutool.json.JSONArray;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.dinfo.db.HttpPostReq;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ExtraResult {
//
//    public static void main(String[] args) throws IOException {
//
//        Map<String,String> params=new HashMap<String,String>();
//        String simple = "开心";
////		String rootExtId = "6iuxJesx";
//        params.put("input", simple);
//        params.put("token","36f83fa6cf3c4380bd206b8791feda06");
////		params.put("rootExtId", rootExtId);
//        String str=new HttpPostReq().post("http://60.247.77.152:8990/run/322/865",params);
//        System.out.println(str);
//
//
//        ExtraResult extraResult = new ExtraResult();
//        str = "{\"status\": 200,\"err\": null,\"data\": {\"segWords\": null, \"analysisType\": \"E\", \"matchElementInfo\": null, \"matchConceptInfo\": null, \"cClasResult\": null, \"sClasResult\": null, \"extraResult\": [    {        \"id\": \"6326233773755252157\",         \"name\": \"否定前缀\",         \"idPath\": \"6326235043954085746->6326233773734280635->6326233773755252157\",         \"namePath\": \"裁判文书->辅助树->否定前缀\",         \"extractInfo\": [            {                \"idExp\": \"@无|不|没|未@\",                 \"nameExp\": \"@无|不|没|未@\",                 \"validFlg\": null,                 \"languageId\": null,                 \"splitFlg\": null,                 \"splitSymbol\": null,                 \"factor\": null,                 \"matchedInfo\": [                    {                        \"matchedIdExp\": \"@无|不|没|未@\",                         \"matchedNameExp\": \"@无|不|没|未@\",                         \"splitFlg\": 0,                         \"splitSymbol\": \"\",                         \"ruleList\": [                            {                                \"ruleContent\": \"无|不|没|未\",                                 \"ruleType\": 0                            }                        ],                         \"synRes\": null,                         \"matchedValues\": [                            {                                \"matchedSentence\": \"裁判结果：无罪释放\",                                 \"sentenceIndex\": 0,                                 \"matchIndex\": 5,                                 \"matchedName\": \"否定前缀\",                                 \"matchedExp\": \"无|不|没|未\",                                 \"matchedContent\": \"无\"                            }                        ]                    }                ]            }        ]    }], \"multiTupleResult\": null, \"relationResult\": null},\"success\": true}";
//        HashMap<String,String> h = extraResult.getExtraResult(str);
//
//        System.out.println(h);
//
//
//
//
//    }
//
//
//    /**
//     * 解析语料中matchedName与matchedContent
//     * @param content 语料
//     * @return
//     */
//    public HashMap<String,String> getExtraResult(String content) {
//
//        HashMap<String,String> resultMap=new HashMap<String,String>();
//
//        JSONObject json=JSONUtil.parseObj(content);
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
//                            String matchedName=matchedValue.getStr("matchedName");
//                            String matchedContent=matchedValue.getStr("matchedContent");
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
//}
