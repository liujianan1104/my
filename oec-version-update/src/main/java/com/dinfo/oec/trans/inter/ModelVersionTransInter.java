package com.dinfo.oec.trans.inter;

import java.io.IOException;

/**
 * 模型版本转换接口
 * <p>Date:2017年5月5日 上午9:59:58</p>
 * <p>Module:</p>
 * <p>Description: </p>
 * <p>Remark: </p>
 * @author gaofei
 * @version 1.0
 * <p>------------------------------------------------------------</p>
 * <p> Change history</p>
 * <p> Serial number: date:modified person: modification reason:</p>
 * <p> 1 </p>
 */
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
