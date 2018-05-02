package myTest;

import com.google.common.cache.LoadingCache;

/**
 * User: Liu Jianan
 * Date: 2018/5/2 10:13
 * Desc:
 */
public class Person {

    public static LoadingCache<String, Person> person;

    private String name;

    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
