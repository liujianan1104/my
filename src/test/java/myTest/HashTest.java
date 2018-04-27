package myTest;

import java.util.HashMap;

public class HashTest implements Runnable {

    private HashMap<String, Integer> h = new HashMap<String, Integer>();

    public HashTest() {
        h.put("1", 0);
    }

    @Override
    public void run() {

        h.put("1", h.get("1") + 1);

    }
    public static void main(String[] args) {
        HashTest h = new HashTest();

        for(int i=1; i<=1000; i++) {
            Thread t = new Thread(h);
            t.start();
        }

        System.out.println(h.getH().get("1"));
    }


    public void setH(HashMap<String, Integer> h) {
        this.h = h;
    }

    public HashMap<String, Integer> getH() {
        return h;
    }
}
