import java.util.StringTokenizer;

/**
 * User: Liu Jianan
 * Date: 2018/6/25 9:51
 * Desc:
 */
public class Test {
    public static void main(String[] args) {
        String line = "hello world! hello";
        StringTokenizer tokenizer = new StringTokenizer(line);

        System.out.println(tokenizer);
        while (tokenizer.hasMoreTokens()) {
            System.out.println(tokenizer.nextToken());

        }
    }
}
