package myTest;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * User: Liu Jianan
 * Date: 2018/5/9 17:08
 * Desc:
 */
public class Test1 {
    private static final Log logger = LogFactory.get();

    public static void main(String[] args) {
        System.out.print(1);
        logger.info("info");
        logger.error("error");
    }
}
