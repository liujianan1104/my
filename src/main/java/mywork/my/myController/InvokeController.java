package mywork.my.myController;

import mywork.my.myService.InvokeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User: Liu Jianan
 * Date: 2018/5/18 17:19
 * Desc:
 */
@RestController
@Component
public class InvokeController {

    @Autowired
    private InvokeService invokeService;

    @RequestMapping("/excute")
    public void excute() throws Exception {
        invokeService.excute();
    }

}
