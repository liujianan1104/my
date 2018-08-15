package com.wu.oec.controller;


import com.wu.oec.trans.ModelVersionTransInter;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping
public class PleaseTagMeController {

    private static final Logger logger = Logger.getLogger(PleaseTagMeController.class);

    @Autowired
    private ModelVersionTransInter modelVersionTrans;


    /**
     * 文档转换格式方法
     * @param filePath
     * @param toFilePath
     * @return
     */
    @RequestMapping("/trans")
    public String transFile(@RequestParam String filePath, @RequestParam String toFilePath) {

        try {
            logger.info("开始转换文件");
            File fileOne = new File(filePath);
            if (!fileOne.exists()) {
                logger.error("文件找不到");
                return null;
            }
            modelVersionTrans.save(fileOne.getAbsolutePath(), toFilePath);

            logger.info("文件转换完成");
        } catch (Exception e) {

            logger.error("文件转换出现错误");
        }
        return null;
    }
}
