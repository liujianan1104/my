package com.wu.oec.utils;

import com.wu.oec.trans.ModelVersionTransInter;
import com.wu.oec.trans.TransFrom510To610;

import java.io.File;
import java.io.IOException;

/**
 * User: Adonis wu
 * Date: 2018/5/7
 * Desc:
 */
public class PleaseTagMe {

    public static void main(String[] args) throws ClassNotFoundException, IOException {
//		Scanner  scanner=new Scanner(System.in);
//		System.out.println("请输入OEC5.x版本模型文件路径:");
        String inFile="D:/data/PR10022_20180511171338.oec";
        File fileOne=new File(inFile);
        if(!fileOne.exists()){
            System.err.println("ERROR:找不到文件");
            return;
        }
        System.out.println("开始转换。。。。。。");
        ModelVersionTransInter tools = new TransFrom510To610();
        tools.save(fileOne.getAbsolutePath(), "D:/data/PR10022_20170323160132_final_last.xml");
        System.out.println("转换结束");
    }
}
