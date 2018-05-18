package mywork.my.myService;


import org.springframework.stereotype.Component;

/**
 * User: Liu Jianan
 * Date: 2018/5/18 16:13
 * Desc:
 */

@Component
public class MethodTest {

    public void getA(String s) {
        System.out.print("AAA" + s);
    }
}
