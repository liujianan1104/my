package mywork.my.myService;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

/**
 * User: Liu Jianan
 * Date: 2018/5/18 16:16
 * Desc:
 */
public class InvokeService {
    private static ApplicationContext applicationContext;

    @Test
    public static void main(String[] args) throws Exception {
        Method method;


        Class classObject = Class.forName("mywork.my.myService.MethodTest");
        method=classObject.getMethod("getA", String.class);
        Object o = applicationContext.getBean(classObject);
        method.invoke(o,"1");
    }

}
