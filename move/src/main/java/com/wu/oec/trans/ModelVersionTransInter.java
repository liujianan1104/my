package com.wu.oec.trans;

import java.io.IOException;

public interface ModelVersionTransInter {

	/**
     * 从模型文件中加载模型
	 * @param path
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	Object load(String path) throws IOException, ClassNotFoundException;
	
	Object transe(Object obj);
	
	void save(String inPath,String outPath) throws IOException, ClassNotFoundException;
}