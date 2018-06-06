package mywork.my.myUtil;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * User: Liu Jianan
 * Date: 2018/6/6 10:00
 * Desc:
 */
public class JsonToExclUtils {

    public static void main(String[] args) throws Exception {
        System.out.println(1);
        getExcl("D:\\testFile\\result.json");
        System.out.println(2);
    }

    public static void getExcl(String path) throws Exception {
        String json = reader(path);
        json = json.replace("\r","").replace("\n","").replace("\t","");

        System.out.println(json);
        JSONArray list = JSONUtil.parseArray(json);

        Workbook wb = new XSSFWorkbook();
        String excelPath = "D:\\testFile\\123.xlsx";
        Sheet sheet = (Sheet) wb.createSheet("sheet");

        Row row = null;
        Cell cell = null;
        int rowNo = 0;
        int cellNo = 0;

        //表头
        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("人工摘要");
        cell = row.createCell(1);
        cell.setCellValue("机器摘要");
        cell = row.createCell(2);
        cell.setCellValue("人工摘要分词去停用词");
        cell = row.createCell(3);
        cell.setCellValue("机器摘要分词去停用词");
        cell = row.createCell(4);
        cell.setCellValue("intersect");
        cell = row.createCell(5);
        cell.setCellValue("result");

        //循环插入数据
        if(null != list && list.size() > 0) {
            for(int i=0; i<list.size(); i++) {
                JSONObject jsonObject = (JSONObject)list.get(i);
                row = sheet.createRow(i+1);
                cell = row.createCell(0);
                cell.setCellValue(jsonObject.get("人工摘要") != null ? jsonObject.get("人工摘要").toString() : " ");
                cell = row.createCell(1);
                cell.setCellValue(jsonObject.get("机器摘要") != null ? jsonObject.get("机器摘要").toString() : " ");
                cell = row.createCell(2);
                cell.setCellValue(jsonObject.get("人工摘要分词去停用词") != null ? jsonObject.get("人工摘要分词去停用词").toString() : " ");
                cell = row.createCell(3);
                cell.setCellValue(jsonObject.get("机器摘要分词去停用词") != null ? jsonObject.get("机器摘要分词去停用词").toString() : " ");
                cell = row.createCell(4);
                cell.setCellValue(jsonObject.getJSONObject("result") != null ? jsonObject.getJSONObject("result").get("intersect").toString() : " ");
                cell = row.createCell(5);
                cell.setCellValue(jsonObject.get("result") != null ? jsonObject.get("result").toString() : " ");
            }
        }
        //创建文件流
        OutputStream stream = new FileOutputStream(excelPath);
        //写入数据
        wb.write(stream);
        //关闭文件流
        stream.close();
    }

    public static String reader(String filePath) {
        try {
            File file = new File(filePath);
            StringBuffer sb = new StringBuffer();
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    sb.append(lineTxt);
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            System.out.println("Cannot find the file specified!");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.out.println("Error reading file content!");
            e.printStackTrace();
            return null;
        }
    }
}
