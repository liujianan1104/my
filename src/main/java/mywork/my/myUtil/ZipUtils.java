package mywork.my.myUtil;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import groovy.util.CharsetToolkit;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * User: Liu Jianan
 * Date: 2018/4/26 16:36
 * Desc: 压缩文件工具类
 */
public class ZipUtils {

    private static final Log logger = LogFactory.get();

    /**
     * 压缩文件或文件夹
     *
     * @param inputFileName 源文件或文件夹路径（绝对路径）
     * @throws Exception
     */
    public void getGZFile(String inputFileName) throws Exception {
        logger.info("Compression start...");

        try {
            logger.info("inputFileName:" + inputFileName);

            File inputFile = new File(inputFileName);
            String targetFileName = ""; //压缩文件名

            if (inputFile.isDirectory()) { //获得打包文件夹名
                targetFileName = inputFileName.substring(inputFileName.lastIndexOf(File.separator) + 1) + ".tar.gz";
                logger.info("targetFileName:" + inputFile.getParent() + File.separator + targetFileName);
                String charset = "";
                File[] files = inputFile.listFiles();
                for (File f : files) {
                    charset = getCharSet(f); //获得文件编码格式
                }
                zipFile(inputFileName, inputFile.getParent() + File.separator + targetFileName, charset); //压缩文件件
            } else { //获得打包文件名
                targetFileName = inputFileName.substring(0, inputFileName.lastIndexOf(".")) + ".tar.gz";
                logger.info("targetFileName:" + targetFileName);
                String charset = getCharSet(inputFile); //获得文件编码格式
                zipFile(inputFileName, targetFileName, charset); // 压缩文件
            }

            logger.info("Compression success end...");
        } catch (Exception e) {
            logger.error("Compression failed ! " + e.getMessage());
            throw e;
        }
    }

    /**
     * 压缩文件或文件夹
     *
     * @param inputFile   源文件或文件夹路径
     * @param outFileName
     * @param charset
     */
    private void zipFile(String inputFile, String outFileName, String charset) {
        if (charset.isEmpty()) {
            charset = CharsetUtil.UTF_8;
        }
        ZipUtil.zip(inputFile, outFileName, Charset.forName(charset), true);
    }

    /**
     * 获得文件编码
     *
     * @param inputFile 源文件
     * @return 文件编码
     * @throws IOException
     */
    public String getCharSet(File inputFile) throws IOException {
        CharsetToolkit detector = new CharsetToolkit(inputFile);
        detector.setDefaultCharset(Charset.forName(CharsetUtil.GBK));//对无法解析的都使用期默认的编码,故要设置默认编码
        Charset charset = detector.getCharset();
        logger.info("inputFile: " + inputFile.getName() + ". Charset: " + charset.name());
        return charset.name();
    }

}
