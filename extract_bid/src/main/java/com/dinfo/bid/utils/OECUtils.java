package com.dinfo.bid.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.dinfo.bid.bean.OECResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * User: Adonis wu
 * Date: 2018/4/20
 * Desc:    OEC操作主类
 * 主要流程：
 *      1.  从实体识别接口返回标注好的实体文本
 *      2.  获取token
 *      3.  发送请求获取信息抽取模型结果，进行分析
 *      4.  发送请求获取C分类模型结果，并进行分析
 *      5.  返回类型OECResult
 */
public class OECUtils {

    private static final Log logger = LogFactory.get();

    //  存储token的map
    private static Map<String, OECResult> tokenMap = new HashMap<String, OECResult>();

    /**
     * @param token_url   获取token url
     * @param ner_url     实体识别url
     * @param appKey      应用id
     * @param e_model_url 信息抽取模型路径url
     * @param c_model_url C分类模型路径url
     * @param e_input     信息抽取输入参数名
     * @param c_input     C分类输入参数名
     * @param data        输出参数值
     * @return
     */
    public static OECResult getResult(String token_url, String ner_url, String appKey, String e_model_url, String c_model_url, String e_input, String c_input, String data) {
        //  存储最后的返回结果
        OECResult result = new OECResult();

        //  获取命名实体识别之后的结果
        OECResult nerResult = getNerResult(ner_url, data);
        String nerContent = null;
        if (nerResult.getStatus()) {
            nerContent = nerResult.getNerContent();
        } else {
            result.setStatus(Boolean.FALSE);
            result.setMessage("get ner result failure");
            return result;
        }
        //  获取token
        OECResult tokenResult = getToken(token_url, appKey);
        String token = null;
        if (tokenResult.getStatus()) {
            token = tokenResult.getToken();
        } else {
            result.setStatus(Boolean.FALSE);
            result.setMessage("get token result failure");
            return result;
        }
        //  拿到NER结果 和 token 之后，封装参数,请求信息抽取模型
        OECResult e_response = sendPost(e_model_url, token, e_input, nerContent, 5000);
        if (e_response.getStatus()) {
            Map<String, Object> extractMap = result.getExtractMap();
            JSONObject e_json = JSONUtil.parseObj(e_response.getOecResult());
            //  解析C分类结果
            if (200 == e_json.getInt("status")) {
                if (e_json.getJSONObject("data") != null
                        && e_json.getJSONObject("data").getJSONObject("result") != null
                        && e_json.getJSONObject("data").getJSONObject("result").getJSONArray("extraResult") != null) {
                    JSONArray extraResult = e_json.getJSONObject("data").getJSONObject("result").getJSONArray("extraResult");
                    if (extraResult != null && extraResult.size() != 0) {
                        for (int i = 0; i < extraResult.size(); i++) {
                            String key = extraResult.getJSONObject(i).getStr("namePath");
                            if (extractMap == null) {
                                extractMap = new HashMap<String, Object>();
                            }
                            Set<String> set = new HashSet<String>();
                            extractMap.put(key, set);
                            JSONArray extractInfo = extraResult.getJSONObject(i).getJSONArray("extractInfo");
                            if (extractInfo != null && extractInfo.size() != 0) {
                                for (int j = 0; j < extractInfo.size(); j++) {
                                    JSONArray matchedInfo = extractInfo.getJSONObject(j).getJSONArray("matchedInfo");
                                    if (matchedInfo != null && matchedInfo.size() != 0) {
                                        for (int k = 0; k < matchedInfo.size(); k++) {
                                            JSONArray matchedValues = matchedInfo.getJSONObject(k).getJSONArray("matchedValues");
                                            if (matchedValues != null && matchedValues.size() != 0) {
                                                for (int l = 0; l < matchedValues.size(); l++) {
                                                    String matchedContent = matchedValues.getJSONObject(l).getStr("matchedContent");
                                                    set.add(matchedContent);
                                                }
                                            }

                                        }
                                    }
                                }
                            }


                        }
                    }
                }
            } else {
                logger.error("get oec E result failure: " + e_json);
                result.setStatus(Boolean.FALSE);
                result.setMessage("get oec E result failure");
                return result;
            }
        } else {
            result.setMessage("get extract result failure");
            result.setStatus(Boolean.FALSE);
            return result;
        }


        //  拿到NER结果 和 token 之后，封装参数,请求C分类模型
        OECResult c_response = sendPost(c_model_url, token, c_input, nerContent, 5000);
        if (c_response.getStatus()) {
            Map<String, Object> classMap = result.getcClassMap();
            JSONObject c_json = JSONUtil.parseObj(c_response.getOecResult());
            if (200 == c_json.getInt("status")) {
                //  解析C分类结果
                JSONObject jsonData = c_json.getJSONObject("data");
                JSONObject outJson = jsonData.getJSONObject("result");
                JSONArray cClasResults = outJson.getJSONArray("cClasResult");
                if (cClasResults != null && cClasResults.size() > 0) {
                    for (int i = 0; i < cClasResults.size(); i++) {
                        JSONObject cClasResult = cClasResults.getJSONObject(i);
                        JSONArray clas_exp_lists = cClasResult.getJSONArray("clas_exp_list");
                        for (int j = 0; j < clas_exp_lists.size(); j++) {
                            JSONObject clas_exp_list = clas_exp_lists.getJSONObject(j);
                            JSONArray matchConinfos = clas_exp_list.getJSONArray("matchConinfo");
                            for (int k = 0; k < matchConinfos.size(); k++) {
                                JSONObject matchConinfo = matchConinfos.getJSONObject(k);
                                String matchName = matchConinfo.getStr("name");
                                JSONArray valueLists = matchConinfo.getJSONArray("valueList");
                                for (int m = 0; m < valueLists.size(); m++) {
                                    JSONObject valueList = valueLists.getJSONObject(m);
                                    JSONArray matchInfos = valueList.getJSONArray("matchInfo");
                                    String matchedContent = "";
                                    for (int n = 0; n < matchInfos.size(); n++) {
                                        JSONObject matchInfo = matchInfos.getJSONObject(n);
                                        String matchContents = matchInfo.getStr("content");
                                        String matchContent = matchContents.substring(matchContents.indexOf(":"));
                                        if (classMap.containsKey(matchName)) {
                                            if(matchConinfos.size()==1){
                                                matchedContent += matchContent;
                                            }else{
                                                matchedContent += matchContent + "|";
                                            }

                                        } else {
                                            if(matchConinfos.size()==1){
                                                matchedContent += matchContent;
                                            }else{
                                                matchedContent += matchContent + "|";
                                            }
                                        }
                                        classMap.put(matchName, matchedContent);
                                    }
                                }
                            }
                        }
                    }
                }

            } else {
                logger.error("get oec C result failure: " + c_json);
                result.setStatus(Boolean.FALSE);
                result.setMessage("get oec C result failure");
                return result;
            }
        } else {
            result.setMessage("get c result failure");
            result.setStatus(Boolean.FALSE);
            return result;
        }

        return result;

    }

    /**
     * @param token_url 请求路径
     * @param appKey    应用id
     * @return 返回token
     */
    public static OECResult getToken(String token_url, String appKey) {
        OECResult oecResult = new OECResult();
        if (isValid(appKey)) {
            return tokenMap.get(appKey);
        } else {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("appKey", appKey);
            String response = HttpUtil.get(token_url, params, 3000);

            if (StrUtil.isNotBlank(response) && 200 == JSONUtil.parseObj(response).getInt("status")) {

                String token = JSONUtil.parseObj(response).getStr("data");
                oecResult.setToken(token);
                oecResult.setTime(System.currentTimeMillis());
                oecResult.setStatus(Boolean.TRUE);
                tokenMap.put(appKey, oecResult);
            } else {
                logger.error("get token error appKey={},response={}", appKey, response);
                oecResult.setStatus(Boolean.FALSE);
            }
        }

        return oecResult;
    }

    /**
     * @param appKey 通过应用key判断token是否过期
     * @return boolean 是否失效
     */
    public static boolean isValid(String appKey) {
        OECResult oecToken = tokenMap.get(appKey);

        if (oecToken == null || (System.currentTimeMillis() - oecToken.getTime()) > oecToken.getTokenSession()) {
            return false;
        }

        return true;

    }

    /**
     * @param ner_url 访问NER服务地址获取结果
     * @param text    输入的语料
     * @return
     */
    public static OECResult getNerResult(String ner_url, String text) {
        //  去除语料中原有的【】符号
        text = text.replace("【", "").replace("】", "");

        JSONObject json = new JSONObject();
        json.put("text", text);

        String response = null;
        OECResult oecResult = new OECResult();
        try {
            response = HttpRequest.post(ner_url)
                    .header(Header.CONTENT_TYPE, "application/json")
                    .body(json)
                    .execute().body();
        } catch (Exception e) {
            logger.error("get ner result error exception={}", e);
            oecResult.setStatus(Boolean.FALSE);
        }
        oecResult.setNerContent(response);
        oecResult.setStatus(Boolean.TRUE);
        return oecResult;

    }


    public static OECResult sendPost(String model_url, String token, String input, String data, int timeout) {
        //返回post请求结果 指定返回结果为json格式，便于解析
        String body = "token=" + token + "&"+input +"="+ data;
        String response = HttpRequest.post(model_url)
                .header(Header.ACCEPT, "application/json")
                .body(body)
                .execute().body();
        OECResult result = new OECResult();
        result.setStatus(Boolean.TRUE);
        result.setOecResult(response);
        return result;
    }

    public static void main(String[] args) {
        String token_url = "http://60.247.77.152:8990/getToken";
        String ner_url = "http://192.168.1.104:5000/p/";
        String appKey = "1524031869518224";
        String e_model_url = "http://60.247.77.152:8990/run/322/863";
        String c_model_url = "http://60.247.77.152:8990/run/322/865";
        String e_input = "input";
        String c_input = "input";
        String data = "一、采购项目名称：潍坊市体育局笼式足球示范工程建设二、采购项目编号：ZFCG-2017-073 预算金额：12万元三、采购公告发布日期：2017年4月12日四、开标日期：2017年5月8日五、采购方式：公开招标六、中标情况：标段采购内容拟中标单位单位地址投标报价（元）合同履行时间1笼式足球示范工程建设青岛英派斯健康科技股份有限公司山东省青岛市即墨市华山二路369号724200签订合同后90日内完成笼式足球场地的建设，具备验收条件。2监理没有投标单位递交投标文件，废标七、评标委员会成员名单：许丽华、马秀丽、王祥、孙玛臣、张启宾八、评标委员会成员评审结果：青岛英派斯健康科技股份有限公司（29.5、31.0、33.0、34.0、34.0）、济南力生体育用品有限公司（26.0、26.0、29.0、29.0、29.0）、浙江康明特体育用品有限公司（28.0、29.0、29.0、30.0、30.0）、山东飞尔康体育设施有限公司（25.0、25.5、26.0、28.0、28.0）九、联系方式：1.采购人：潍坊市体育局         地   址：潍坊市高新区阳光大厦24层  联系人：陈金成              联系方式：0536-80909812.采购代理机构：潍坊华成招标有限公司   地   址：健康东街9969号  联系人：徐静                     联系方式：0536-2258068                                           发布人：潍坊华成招标有限公司                                               发布时间：2017年5月8日";
        //  招标人为【林周县发展和改革委员会】。
        getResult(token_url, ner_url, appKey, e_model_url, c_model_url, e_input, c_input, data);
//        OECResult result = getNerResult(ner_url, data);
//        System.out.println(result.getNerContent());
    }
}
