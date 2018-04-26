package mywork.my.myUtil;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarOutputStream;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * User: Liu Jianan
 * Date: 2018/4/26 13:45
 * Desc:
 */
public class TarUtilsbak {

    private static int BUFFER = 1024 * 4;  //缓冲大小
    private static byte[] B_ARRAY = new byte[BUFFER];

    public void execute(String inputFileName, String targetFilePath) {
        File inputFile = new File(inputFileName);

        String targetFileName = "";
        if (inputFile.isDirectory()) { //获得打包文件夹名
            targetFileName = inputFileName.substring(inputFileName.lastIndexOf(File.separator) + 1) + ".tar";

        } else { //获得打包文件名
            targetFileName = inputFileName.substring((inputFileName.lastIndexOf(File.separator) + 1), (inputFileName.lastIndexOf("."))) + ".tar";

        }
        TarOutputStream out = getTarOutputStream(targetFilePath + File.separator + targetFileName);


        packFile(out, inputFile, inputFileName.substring((inputFileName.lastIndexOf(File.separator) + 1)));
        try {
            if (null != out) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        compress(new File(targetFilePath + File.separator + targetFileName));

        System.out.println(1);
    }

    /**
     * 获得打包后文件的流
     *
     * @param targetFileName 打包后文件名
     * @return
     */
    private TarOutputStream getTarOutputStream(String targetFileName) {

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(targetFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        TarOutputStream out = new TarOutputStream(bufferedOutputStream);

        //如果不加下面这段，当压缩包中的路径字节数超过100 byte时，就会报错
        out.setLongFileMode(TarOutputStream.LONGFILE_GNU);
        return out;
    }


    private void packFile(TarOutputStream out, File inputFile, String base) {
        TarEntry tarEntry = new TarEntry(base);

        //设置打包文件的大小，如果不设置，打包有内容的文件时，会报错
        tarEntry.setSize(inputFile.length());
        try {
            out.putNextEntry(tarEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileInputStream in = null;
        try {
            in = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int b = 0;

        try {
            while ((b = in.read(B_ARRAY, 0, BUFFER)) != -1) {
                out.write(B_ARRAY, 0, b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("NullPointerException info ======= [FileInputStream is null]");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.closeEntry();
                }
            } catch (IOException e) {

            }
        }
    }


    private void packFolder(TarOutputStream out, File inputFile, String base) {
        File[] fileList = inputFile.listFiles();
        try {
            //在打包文件中添加路径
            out.putNextEntry(new TarEntry(base + "/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        base = base.length() == 0 ? "" : base + "/";
//        for (File file : fileList) {
//            tarPack(out, file, base + file.getName());
//        }
    }

    private void compress(File srcFile) {
        File target = new File(srcFile.getAbsolutePath() + ".gz");
        FileInputStream in = null;
        GZIPOutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new GZIPOutputStream(new FileOutputStream(target));
            int number = 0;
            while ((number = in.read(B_ARRAY, 0, BUFFER)) != -1) {
                out.write(B_ARRAY, 0, number);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
