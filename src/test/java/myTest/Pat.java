package myTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pat {
    public static void main(String[] args) {
//        Pattern p=Pattern.compile("【([^【】]*)】ORG");

        String param = "住房租赁和【销售管理条例】POS公开征求意见  解读_要闻_【中标国网】ORG".replace(" ", "");
        Pattern p1=Pattern.compile("【([^【】]*)】ORG");
        Pattern p2=Pattern.compile("【(.+?)】POS");

        StringBuffer sb = new StringBuffer();
        HashMap<String, List<String>> dic = new HashMap<String, List<String>>();


        Matcher m=p1.matcher(param);
        m.find();
        String pStr1 = m.group(1);


        List<String> list = new ArrayList<String>();
        for (char c : pStr1.toCharArray()) {
            list.add(c+" ORG");
        }
        dic.put(pStr1,list);


        m=p2.matcher(param);
        m.find();
        String pStr2 = m.group(1);



        System.out.println(sb.toString());
        List<String> list2 = new ArrayList<String>();
        for (char c : pStr2.toCharArray()) {
            list2.add(c+" POS");
        }
        dic.put(pStr2,list2);

        param = param.replace("ORG","").replace("POS","");
        int i = 0;
        while (i < param.length()) {
            if (("【".equals(param.substring(i,i+1)))) {
                i++;
                StringBuffer tmp = new StringBuffer();
                while (!"】".equals(param.substring(i,i+1))) {

                    tmp.append(param.charAt(i));
                    i++;

                    if (dic.containsKey(tmp.toString())) {
                        for (String s : dic.get(tmp.toString())) {
                            sb.append(s+"\n");
                        }
                    }
                }
                i++;
            } else {
                sb.append(param.charAt(i) + " O\n");
                i++;
            }
        }
        System.out.println(sb.toString());






//        Matcher m=p1.matcher(param);
//        boolean flag1 = m.find();
//        String pStr1 = m.group(1);
//        System.out.println("pStr1:"+m.group(1));
//
//        param = m.replaceAll(pStr1);
//        System.out.println(param);
//
//        m = p2.matcher(param);
//        boolean flag2 = m.find();
//        String pStr2 = m.group(1);
//        System.out.println("pStr2:"+pStr2);
//
//        param = m.replaceAll(pStr2);
//        System.out.println(param);
//
//
//        for (char c : param.toCharArray()) {
//            sb.append(c + " O" + "\n");
//        }
//        String result = sb.toString();
//
//        if (flag1) {
//            sb.delete(0,sb.length());
//            StringBuffer tmp = new StringBuffer();
//            for (char c : pStr1.toCharArray()) {
//                sb.append(c + " ORG"+"\n");
//                tmp.append(c + " O"+"\n");
//
//            }
//            System.out.println("替换："+sb.toString());
//            System.out.println("替换："+tmp.toString());
//            System.out.println("-----："+tmp.toString());
//
//
//            System.out.println("替换前："+result);
//            result.replace(sb.toString(), tmp.toString());
//            System.out.println("替换后："+result);
//        }
//
//
//        if (flag2) {
//            sb.delete(0,sb.length());
//            StringBuffer tmp = new StringBuffer();
//            for (char c : pStr2.toCharArray()) {
//                sb.append(c + " POS");
//                tmp.append(c + " O" );
//
//            }
//            result.replace(sb.toString(), tmp.toString());
////            System.out.println("替换："+tmp.toString() + sb.toString());
//        }
//
//        System.out.println(result);

    }
}
