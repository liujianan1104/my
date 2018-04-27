package myTest;

import groovy.util.CharsetToolkit;
import mywork.my.myUtil.ZipUtils;

import java.io.File;
import java.nio.charset.Charset;

public class Test {
    public static void main(String[] args) throws Exception {
//        Charset c = Charset;

        ZipUtils m = new ZipUtils();

        m.getGZFile("D:\\testFile\\1.csv");

//        File file = new File("D:\\testFile\\1.csv");
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
