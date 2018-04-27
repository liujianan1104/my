package mywork.my.myUtil;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
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
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset; //文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; //文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; //文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; //文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return charset;
    }

}
