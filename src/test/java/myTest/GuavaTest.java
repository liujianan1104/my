package myTest;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * User: Liu Jianan
 * Date: 2018/5/2 10:08
 * Desc:
 */
public class GuavaTest {



    public static void main(String[] args) throws ExecutionException, InterruptedException {


        Person.person = CacheBuilder.newBuilder().
                maximumSize(1000).
                expireAfterWrite(4, TimeUnit.SECONDS).
                build(new CacheLoader<String, Person>() {
                    @Override
                    public Person load(String key) throws Exception {
                        Person p = new Person();


                        if (StringUtils.isEmpty(key)) {
                            System.out.println("key is null");


                        } else {
                            p.setAge(1);
                            p.setName("haha");
                            Person.person.put(key, p);
                            System.out.println("key is not null");
                        }


                        return p;
                    }
                });
        System.out.println(Person.person.get("11").getAge());
        System.out.println(Person.person.size());
        Thread.sleep(3*1000);

        System.out.println(Person.person.get("11").getAge());

//        for (int i=0; i<10; i++) {
//            System.out.println(Person.person.get("11").getAge()+Person.person.get("11").getName());
//            Thread.sleep(61*1000);
//            System.out.println(Person.person.get("11").getAge()+Person.person.get("11").getName());
//        }
//        System.out.println(Person.person.get("11").getAge()+Person.person.get("11").getName());
//
//        System.out.println(Person.person.get("222").getAge()+Person.person.get("222").getName());
//        Person.person.refresh("");
//        System.out.println(Person.person.get("222").getAge()+Person.person.get("222").getName());
    }

}
