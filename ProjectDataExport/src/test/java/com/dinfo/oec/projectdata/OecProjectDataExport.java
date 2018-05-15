package com.dinfo.oec.projectdata;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import com.main.ProjectDataExportMain431;
import com.main.ProjectDataExportMain431.ResourceType;

public class OecProjectDataExport {
	public static void main(String[] args) throws ClassNotFoundException,
			UnsupportedEncodingException, FileNotFoundException, SQLException,
			IOException {
		String mysqlHost = "127.0.0.1:3306";// 数据库地址
		//String dbName = "dinfooec_poc";// 数据库名称
		String dbName = "oec4.3";// 数据库名称
		String user = "root";
		String password = "root";
		String projectCode = "PR10024";
		int options = ResourceType.oec_all;
		new ProjectDataExportMain431().exportDataToSQL(mysqlHost, dbName, user,
				password, projectCode, projectCode, options,
				new OutputStreamWriter(new FileOutputStream(
						"export/classify.sql"), "UTF-8"));
	}
}
