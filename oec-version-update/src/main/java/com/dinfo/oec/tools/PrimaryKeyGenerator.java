package com.dinfo.oec.tools;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

/**
 * 主键生成器
 * <p>Date:2016年11月19日 下午5:39:14</p>
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
public class PrimaryKeyGenerator {

	
	private static Long preMillisTime;
	
	private static Integer num=0;
	
	public synchronized final static Long getKeya(){
		Long key=System.currentTimeMillis();
		if(preMillisTime==null){
			preMillisTime=key;
		}else if(preMillisTime.equals(key)&&num<100){
			key=key*100+num++;
		}else if(preMillisTime.equals(key)&&num==100){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else{
			preMillisTime=key;
			num=0;
			key=key*100+num++;
		}
		
		return key;
	}
	
	public static void main(String[] args) {
		Map<String,Integer> map=new HashMap<String, Integer>();
		while(true){
			Long id=getKeya();
			if(map.containsKey(id.toString())){
				break;
			}else{
				map.put(id.toString(),1);
			}
			System.out.println(map.size());
		}
	}

}
