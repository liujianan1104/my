package mywork.my.myService;

import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import mywork.my.myBean.OECResult;
import mywork.my.myUtil.OECUtils;

import java.io.*;
import java.util.Date;

/**
 * User: Liu Jianan
 * Date: 2018/5/3 17:30
 * Desc:
 */
public class EnterpriseRiskControlTestMain {

    private static final Log logger = LogFactory.get();

    public static void main(String[] args) throws IOException {
        String tokenUrl = "http://60.247.77.152:8990/getToken";
        String appKey = "1511246120191054";
        String modekUrl = "http://60.247.77.152:8990/run/121/382";
        String sentenceRegex = "。|；|？|！|;|\\?|!";
        String token = "";
        String data = "";

        File inputFile = new File("D:\\testFile\\语料\\2.txt");
        InputStreamReader stream = new InputStreamReader (new FileInputStream(inputFile));
        BufferedReader bufferedReader = new BufferedReader(stream);

        while((data = bufferedReader.readLine()) != null){

            OECResult tokenResult = OECUtils.getToken(tokenUrl,appKey);

            String[] list = data.split(sentenceRegex);

            int count = 1;
            int max = 0;
            Date date1 = new Date();
            for (String s : list) {
                logger.info("第"+count+"条分句长度为："+s.length());
                if (s.length()>max) {
                    max = s.length();
                }
                Date begin = new Date();
                if (tokenResult.getStatus()) {
                    token = tokenResult.getToken();
                } else {
                    tokenResult = OECUtils.getToken(tokenUrl,appKey);
                    token = tokenResult.getToken();
                }

                OECUtils.sendPost(modekUrl,token,"content",data,300);
                Date end = new Date();
                logger.info("第"+count+"次耗时:"+ DateUtil.formatBetween(begin,end));
                count++;
            }
            Date date2 = new Date();
            logger.info("分句最大长度为："+max);
            logger.info("总耗时:"+ DateUtil.formatBetween(date1,date2));
            logger.info("平均耗时:"+ ((date2.getTime() - date1.getTime())/count) + "毫秒");

        }
        bufferedReader.close();

    }

}
