//package myTest;
//
//import java.util.HashMap;
//import java.util.Hashtable;
//
//public class HashTest {
//    static Hashtable<Integer, Integer> table = new Hashtable<>();
//    //分别在两个子线程内对hashtable进行put,get操作
////    public static void main(String[] args) {
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                System.out.println("线程1执行");
////                for (int i = 0; i < 1000; i++) {
////                    table.put(i,i);
////                }
////                System.out.println("线程1---------" + table.get(500));
////            }
////        }).start();
////        new Thread(new Runnable() {
////
////            @Override
////            public void run() {
////                System.out.println("线程2执行");
////                for (int i = 1000; i < 2000; i++) {
////                    table.put(i, i);
////                }
////                System.out.println("线程2---------" + table.get(1500));
////            }
////        }).start();
////    }
//
//
//    static HashMap<Integer, Integer> map = new HashMap<>();
//    //分别在两个子线程内对hashmap进行put,get操作
//    public static void main(String[] args) {
//
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                System.out.println("线程1执行");
//                for (int i = 0; i < 1000; i++) {
//                    map.put(i,i);
//                }
//                System.out.println("线程1---------500：" + map.get(500));
//                System.out.println("线程1---------1500：" + map.get(1500));
//            }
//        }).start();
//
//
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                System.out.println("线程2执行");
//                for (int i = 1000; i < 2000; i++) {
//                    map.put(i, i);
//                }
//                System.out.println("线程2---------1500：" + map.get(1500));
//                System.out.println("线程2---------500：" + map.get(500));
//            }
//        }).start();
//    }
//}
