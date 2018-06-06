//package mywork.my.myUtil;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.Writer;
//
//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//
//
//public class Demo {
//
//	 public static void main(String[] args) throws IOException {
//		 InputStream in = new FileInputStream("D:\\testFile\\11.xlsx");
//
//		 BufferedReader  bf= null;
//		 BufferedWriter  bw=new BufferedWriter(new FileWriter("D:\\新建文件夹\\33\\1.xlsx"));
//		   File file=new File("D:\\新建文件夹\\33");
//		 // 读取模板，03版本的对象
//			// Workbook wb = new HSSFWorkbook(in);
//
//			// 读取的是07版本
//			Workbook wb = new XSSFWorkbook(in);
//
//			// 获取工作表对象
//			Sheet sheet = wb.getSheetAt(0);
//
//			// 定义变量，从第一行开始的
//			int rowNo = 0;
//			// 从第二列开始的
//			int cellNo = 1;
//			// 定义行对象
//			Row row = null;
//			// 定义单元格对象
//			Cell cell = null;
//
//			if(file.isDirectory()){
//				File[] listFiles = file.listFiles();
//				String  len=null;
//				String content=null;
//				for (File file2 : listFiles) {
//					bf=new BufferedReader(new FileReader(file2.getAbsolutePath()));
//				 while((len=bf.readLine())!=null){
//					   len.replaceAll("\\s+", "");
//					   bw.write(len);
//					   content+=len;
//					   System.out.println(len);
//				 }
//				      cell.setCellValue(content);
//				}
//			}
//
//			DownloadUtil downloadUtil = new DownloadUtil();
//			// 下载
//			/**
//			 * byteArrayOutputStream	缓冲流
//			 * response					response对象
//			 * returnName				下载文件的名称
//			 */
//			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//			wb.write(byteArrayOutputStream);
//			HttpServletResponse response = ServletContext.getResponse();
//			String returnName = content+".xls";
//			// 下载
//			downloadUtil.download(byteArrayOutputStream, response, returnName);
//	}
//
//}
