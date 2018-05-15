package mywork.my.myUtil;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Liu Jianan
 * Date: 2018/5/14 11:27
 * Desc:
 */
public class MatchUtils {
    private static final Log logger = LogFactory.get();

    public static boolean match(String pattern, String param) {
        logger.info("正则表达式为：" + pattern);
        logger.info("参数为："+ param);

        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(param);
        return matcher.matches();
    }
}
