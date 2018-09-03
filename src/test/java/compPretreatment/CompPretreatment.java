package compPretreatment;

/**
 * User: Liu Jianan
 * Date: 2018/8/15 9:33
 * Desc:
 */
public class CompPretreatment {

    public static void main(String[] args) {
        String in = "test００１！你好";
        System.out.println(in);
        CompPretreatment tmp = new CompPretreatment();
        String out = tmp.fullToHalf(in);
        System.out.println(out);
    }

    private String fullToHalf(String input) {
        if (input.isEmpty()) {
            return null;
        }
        char[] array = input.toCharArray();
        for (int i = 0; i < array.length; i++) {
            // 全角空格转换为半角空格
            if (array[i] == '\u3000') {
                array[i] = '\u0020';
                // 除空格外的其他字符转换
            } else if (array[i] > '\uff00' && array[i] < '\uff5f') {
                //65248是全角和半角的Unicode值相差
                array[i] = (char) (array[i] - 65248);
            }
        }
        String output = new String(array);
        return output;
    }
}
