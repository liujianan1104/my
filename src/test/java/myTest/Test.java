package myTest;

import mywork.my.myUtil.ZipUtils;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {

        ZipUtils m = new ZipUtils();

        m.getGZFile("D:\\testFile\\tar","D:\\testFile\\tar");
//        TarUtilsbak tarUtils = new TarUtilsbak();
//        tarUtils.execute("D:\\testFile\\tar\\bid_detail_single_src_ggzy.weifang.gov.cn.json","D:\\testFile\\tar");
//        System.out.println(1);

//        TarUtilsTest t = new TarUtilsTest();
//        t.execute("D:\\testFile\\tar\\bid_detail_single_src_ggzy.weifang.gov.cn.json","D:\\testFile\\tar");
//        System.out.println(1);
//        String s = "D:\\testFile\\1.txt";
//        System.out.println(s.substring((s.lastIndexOf("\\") + 1),(s.lastIndexOf("."))));

//        String in = "住房租赁和【销售管理条例】POS公开征求意见  解读_要闻_【中标国网】ORG";
//
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
