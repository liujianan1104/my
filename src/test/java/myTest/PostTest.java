package myTest;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Adonis wu
 * Date: 2018/4/24
 * Desc:
 * headers.put("Accept", "application/json");//表示接受json类型的响应
 * headers.put("Content-Type", "application/json; charset=UTF-8");//表示传递给服务器的参数是json类型
 */
public class PostTest {

    public static void main(String[] args) {
        Map<String, String> classMap = new HashMap<String, String>();
        String c_model_url = "http://60.247.77.152:8990/run/322/865";
        String token = "0a44a905e2a2423a8af8a6c4acb9ae62";
        String c_input = "input";
        String nerContent = "预算金额：126万元";
        String c_response = sendPost(c_model_url, token, c_input, nerContent);
        JSONObject json = JSONUtil.parseObj(c_response);
        int status = json.getInt("status");
        if (status == 200) {
            json = json.getJSONObject("data");
            json = json.getJSONObject("result");
            JSONArray cClasResults = json.getJSONArray("cClasResult");
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
                                        matchedContent += matchContent + "|";
                                    } else {
                                        matchedContent = matchContent + "|";
                                    }
                                    classMap.put(matchName, matchedContent);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println();

    }

    public static String sendPost(String model_url, String token, String input, String data) {
        String body = "token=" + token + "&" + input + "=" + data;
        String response = HttpRequest.post(model_url)
                .header(Header.ACCEPT, "application/json")
                .body(body)
                .execute().body();
        return response;
    }
}
