package com.sequence.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.db.base.BaseDBDao;

public class SequenceUpdate extends BaseDBDao {

	private static Pattern numPattern=Pattern.compile("\\d+");
	
	/**
	 * 
	 * @param className
	 *            MYSQL,ORACLE
	 * @param driverUrl
	 *            mysql :jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&
	 *            characterEncoding=utf8
	 *            oracle:jdbc:oracle:thin:@103.41.52.3:1521:orcl
	 * @param userName
	 * @param password
	 */
	public SequenceUpdate(ClassName className, String driverUrl,
			String userName, String password) {
		super(className, driverUrl, userName, password);
	}

	/**
	 * 根据传入参数更新序列
	 * @param conn
	 * @param tableName
	 * @param fieldName
	 * @param seqName
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void updateSequence(String tableName, String fieldName,
			String seqName) throws ClassNotFoundException, SQLException {
		//参数格式检查
		{
			if(tableName==null|| tableName.length()==0||fieldName==null||fieldName.length()==0||seqName==null||seqName.length()==0){
				System.out.println("参数错误");
				return;
			}
		}
		Connection conn=getConnection();
		//1.查询当前最大值
		Map<String,Object> maxMap=this.queryForMap(conn,"select max(" + fieldName + ") as MAX from " + tableName);
		String maxStr=maxMap.get("MAX")!=null?maxMap.get("MAX").toString():"0";
		int max=-1;
		Matcher matcher=numPattern.matcher(maxStr);
		while(matcher.find()){
			max=Integer.parseInt(matcher.group());
		}
		if(max==-1){
			return;
		}
		System.out.println(tableName.toUpperCase()+"\t\tmax="+max);
		//2.查询当前Sequence最大值
		Map<String,Object> nextValueMap=this.queryForMap(conn, "select "+seqName+".nextval as NEXTVALUE from dual");
		int nextValue=Integer.parseInt(nextValueMap.get("NEXTVALUE").toString());
		System.out.println(seqName.toUpperCase()+"\t\tnextval="+nextValue);
		//3.计算差值
		int length=max-nextValue;
		//4.更新序列
		if(length>0){
			this.update(conn, "alter sequence "+seqName+" increment by "+length);
			this.update(conn, "select "+seqName+".nextval as NEXTVALUE from dual");
			this.update(conn, "alter sequence "+seqName+" increment by 1");
			System.out.println("更新完毕");
		}else{
			System.out.println("符合要求");
		}
		conn.close();
	}

	/**
	 * 读取文件更新全部序列
	 */
	public void upateOecOracleSequences(){
		BufferedReader reader =new BufferedReader(new InputStreamReader(SequenceUpdate.class.getClassLoader().getResourceAsStream("tableSeqRelation.config")));
		try{
			String line=null;
			while((line=reader.readLine())!=null){
				if(line.startsWith("#")){
					continue;
				}
				String []items=line.trim().split("\t");
				if(items.length==3){
					updateSequence(items[0], items[1], items[2]);
				}else{
					System.out.println("配置格式不正确："+line);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		SequenceUpdate sequenceUpdate=null;
		sequenceUpdate=new SequenceUpdate(ClassName.ORACLE, "jdbc:oracle:thin:@192.168.75.128:1521:orcl2", "oec_v421", "oec_v421123");
		sequenceUpdate.upateOecOracleSequences();
	}
}
