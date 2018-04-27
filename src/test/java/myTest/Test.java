package myTest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

public class Test {
    private static  String getFilecharset(File sourceFile) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
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
            e.printStackTrace();
        }
        return charset;
    }
    public static void main(String[] args) throws Exception {
//        Charset c = Charset;

//        ZipUtils m = new ZipUtils();
//
//        m.getGZFile("D:\\testFile\\1.csv");
        File file = new File("D:\\testFile\\tar\\新建文本文档.txt");
        System.out.println(getFilecharset(file));


//            CharsetToolkit detector = new CharsetToolkit(file);
//            detector.setDefaultCharset(Charset.forName("GBK"));
//            Charset charset = detector.getCharset();
//            System.out.println(charset.name());
//        String fileContent=FileUtils.readFileToString(new File(filePath),fileEncode);

//        InputStream inputStream  = new FileInputStream("D:\\testFile\\tar\\1.txt");
//        byte[] head = new byte[3];
//
//        inputStream.read(head);
//        inputStream.close();
//        System.out.println(head);
//
//        String code = "";
//
//        code = "gb2312";
//
//        if (head[0] == -1 && head[1] == -2 )
//
//            code = "UTF-16";
//
//        if (head[0] == -2 && head[1] == -1 )
//
//            code = "Unicode";
//
//        if(head[0]==-17 && head[1]==-69 && head[2] ==-65)
//
//            code = "UTF-8";
//
//        System.out.println(code);

//        CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
//        detector.add(new ParsingDetector(false));
//        detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar
//        // ASCIIDetector用于ASCII编码测定
//        detector.add(ASCIIDetector.getInstance());
//        // UnicodeDetector用于Unicode家族编码的测定
//        detector.add(UnicodeDetector.getInstance());
//        java.nio.charset.Charset charset = null;
//        File f = new File("D:\testFile\\tar\\bid_detail_single_src_ggzy.weifang.gov.cn.json");
//        try {
//            charset = detector.detectCodepage(f.toURI().toURL());
//            System.out.println(charset);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }



//        ZipUtil.zip("D:\\testFile\\tar", "D:\\testFile\\a.tar.gz", Charset.forName("utf-8"), true);
//        TarUtilsbak tarUtils = new TarUtilsbak();
//        tarUtils.execute("D:\\testFile\\tar\\bid_detail_single_src_ggzy.weifang.gov.cn.json","D:\\testFile\\tar");
//        System.out.println(1);

//        TarUtilsTest t = new TarUtilsTest();
//        t.execute("D:\\testFile\\tar\\bid_detail_single_src_ggzy.weifang.gov.cn.json","D:\\testFile\\tar");
//        System.out.println(1);
//        String s = "D:\\testFile\\1.txt";
//        System.out.println(s.substring((s.lastIndexOf("\\") + 1),(s.lastIndexOf("."))));

//        String in = "住房租赁和【销售管理条例】POS公开征求意见  解读_要闻_【中标国网】ORG";
//w
//        HashMap<String, String> param = new HashMap<String, String>();
//        param.put("【([^【】]*)】ORG","ORG");
//        param.put("【(.+?)】POS","POS");
//        System.out.println(ExplainUtils.getExplain(in, param));
//        ZipUtil.zip("D:\\testFile\\tar\\bid_detail_single_src_ggzy.weifang.gov.cn.json","D:\\testFile\\tar\\1.tar");
//        ZipUtil.zip("D:\\testFile\\tar\\新建文本文档.txt","D:\\testFile\\tar\\1.tar.gz");


//        File f = new File("D:\\testFile\\tar\\bid_detail_single_src_ggzy.weifang.gov.cn.json");
//
//        File f1 = new File("D:\\testFile\\tar\\1.csv");
//
//        FileReader in = new FileReader(f);
//        BufferedReader br = new BufferedReader(in);
//
//        FileWriter ou = new FileWriter(f1);
//        BufferedWriter bw = new BufferedWriter(ou);
//
//        int i =1;
//        while (null != br.readLine()) {
//            String tmp = br.readLine();
//            for(int j=0; j<=100; j++) {
//                bw.write(tmp);
//                bw.newLine();
//            }
//            bw.flush();
//            i++;
//
//        }
//        bw.flush();
//        System.out.println(i);
//        in.close();
//        br.close();
//        ou.close();
//        bw.close();

//        File f = new File("D:\\testFile\\tar\\1.csv");
//        FileReader in = new FileReader(f);
//        int i =1;
//        while ( in.read() != -1) {
//
//            i++;
//        }
//        System.out.println(i);

    }
}
