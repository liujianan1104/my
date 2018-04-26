package mywork.my.myUtil;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;

import java.io.File;

/**
 * User: Liu Jianan
 * Date: 2018/4/26 16:36
 * Desc: 压缩文件工具类
 */
public class ZipUtils {

    public void getGZFile(String inputFileName, String outFilePath) {
        File inputFile = new File(inputFileName);
        String targetFileName = "";

        if (inputFile.isDirectory()) { //获得打包文件夹名
            targetFileName = inputFileName.substring(inputFileName.lastIndexOf(File.separator) + 1) + ".tar.gz";
            zipFile(inputFile,targetFileName);
        } else { //获得打包文件名
            targetFileName = inputFileName.substring((inputFileName.lastIndexOf(File.separator) + 1), (inputFileName.lastIndexOf("."))) + ".tar.gz";
        }

    }

    private void zipFile(File inputFile, String outFileName) {
        ZipUtil.zip(inputFile.getAbsolutePath(), outFileName);
    }

    private void zipFiles(File inputFile, String outFileName) {
        File[] fileList = inputFile.listFiles();

        if (null != fileList && fileList.length > 0) {
            for (File f : fileList) {
                getGZFile(fileList[1].getAbsolutePath(), outFileName);
            }
        }
    }

}
