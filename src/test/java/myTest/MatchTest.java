package myTest;

import mywork.my.myUtil.MatchUtils;

/**
 * User: Liu Jianan
 * Date: 2018/5/14 12:13
 * Desc:
 */
public class MatchTest {

    public static void main(String[] args) {
        boolean b = MatchUtils.match("(Risk|risk|security|Security)\\s+(governance|management|Governance)","risk       management");
        System.out.print(b);
    }
}
