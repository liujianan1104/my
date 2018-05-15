package com.dinfo.oec.main;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.dinfo.oec.trans.impl.TransFrom510To610;
import com.dinfo.oec.trans.inter.ModelVersionTransInter;

public class MainWork {
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Scanner  scanner=new Scanner(System.in);
		System.out.println("请输入OEC5.x版本模型文件路径:");
		String inFile=scanner.nextLine();
		File fileOne=new File(inFile);
		if(!fileOne.exists()){
			System.err.println("ERROR:找不到文件");
			return;
		}
		System.out.println("开始转换。。。。。。");
		ModelVersionTransInter tools = new TransFrom510To610();
		tools.save(fileOne.getAbsolutePath(), "result.xml");
		System.out.println("转换结束");
	}
}
