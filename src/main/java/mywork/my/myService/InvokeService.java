package mywork.my.myService;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * User: Liu Jianan
 * Date: 2018/5/18 16:16
 * Desc:
 */
@Component
public class InvokeService {
    private static final Log logger = LogFactory.get();

    @Autowired
    private ApplicationContext applicationContext;

    public void excute() throws Exception {

        Method method;
        Class classObject = Class.forName("mywork.my.myService.MethodTest");
        method = classObject.getMethod("getA", String.class);
        Object o = applicationContext.getBean(classObject);
        method.invoke(o, "1");

    }

}
