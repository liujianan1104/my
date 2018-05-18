package mywork.my;

import java.lang.reflect.Method;
import java.nio.charset.Charset;

/**
 * User: Liu Jianan
 * Date: 2018/5/18 18:33
 * Desc:
 */
public class InvokeTest {

    public static void main(String[] args) throws Exception {
        Class cla=Class.forName("cn.hutool.core.util.CharsetUtil");
        Method method;
        method=cla.getMethod("charset", String.class);
        Object o = cla.newInstance();
        Charset c = (Charset)method.invoke(o,"UTF-8");
        System.out.print(c.name());
    }
}
