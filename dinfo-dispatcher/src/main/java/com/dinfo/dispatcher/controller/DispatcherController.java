package com.dinfo.dispatcher.controller;

import com.dinfo.dispatcher.net.ModuleInvocation;
import com.dinfo.dispatcher.net.Response;
import com.dinfo.dispatcher.net.SimpleResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.dinfo.dispatcher.util.ObjectUtil.isNull;

/**
 * @author yangxf
 */
@RestController
@RequestMapping("/dispatcher")
public class DispatcherController {
    private static final Logger logger = LoggerFactory.getLogger(DispatcherController.class);

    @PostMapping("/invocation")
    public Response dispatch(@RequestBody ModuleInvocation invocation) {
        long t1 = System.currentTimeMillis();
        if (isNull(invocation))
            return SimpleResponse.buildFailure("param invocation is null");
        Response result = invocation.invoke();
        long t2 = System.currentTimeMillis();
        logger.info("duration {}", t2 - t1);
        return result;
    }

}
