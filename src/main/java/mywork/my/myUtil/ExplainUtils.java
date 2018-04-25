package mywork.my.myUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Liu Jianan
 * Date: 2018/4/25 18:37
 * Desc: 解析语句工具类
 */
public class ExplainUtils {

    /**
     * @param in 要解析语句
     * @param param 正则表达式参数
     * @return
     * @Desc: 语义解析为单列+标签形式，如“住房租赁和【销售管理条例】POS公开征求意见  解读_要闻_【中标国网】ORG”处理结果为
     *  * 住 O
     * ...
     *  * 销 POS
     *  ...
     *  * 公 O
     *  *...
     *  * 中 ORG
     */
    public static String getExplain(String in, HashMap<String, String> param) {

        in = in.replace(" ","");
        String result = "";
        char x = '【';
        char y = '】';
        StringBuffer sb = new StringBuffer();

        HashMap<String, List<String>> dic = new HashMap<String, List<String>>();
        if (null != param && param.size()>0) {
            for (String key : param.keySet()) {
                Pattern p=Pattern.compile(key);
                Matcher m=p.matcher(in);
                m.find();

                List<String> list = new ArrayList<String>();
                for (char c : m.group(1).toCharArray()) {
                    list.add(c + " " + param.get(key));
                }
                dic.put(m.group(1),list);
                in = in.replace(param.get(key),"");

            }
        }

        int i = 0;
        while (i < in.length()) {
            if ((x == in.charAt(i))) {
                i++;
                StringBuffer tmp = new StringBuffer();
                while (y != in.charAt(i)) {

                    tmp.append(in.charAt(i));
                    i++;

                    if (dic.containsKey(tmp.toString())) {
                        for (String s : dic.get(tmp.toString())) {
                            sb.append(s+"\n");
                        }
                    }
                }
                i++;
            } else {
                sb.append(in.charAt(i) + " O\n");
                i++;
            }
        }

        return sb.toString();
    }
}
