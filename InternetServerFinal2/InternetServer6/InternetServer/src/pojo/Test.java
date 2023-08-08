package pojo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    Singleton s1;
    Singleton s2;
    public static void main(String[] args) throws InterruptedException {
//        Test t = new Test();
//        Runnable task1 =()->{
//            t.s1= Singleton.getInstance();
//        };
//        Runnable task2 =()->{
//            t.s2= Singleton.getInstance();
//        };
//
//        Runnable[] runnables = new Runnable[2];
//        runnables[0] = task1;
//        runnables[1] = task2;
//
//        Thread[] threads = new Thread[2];
//        for (int i = 0; i < threads.length; i++) {
//            threads[i] = new Thread(runnables[i]);
//        }
//
//        for (int i = 0; i < threads.length; i++) {
//            threads[i].start();
//        }
//
//        for (int i = 0; i < threads.length; i++) {
//            threads[i].join();
//        }

//        System.out.println(Integer.toHexString(t.s1.hashCode()));
//        System.out.println(Integer.toHexString(t.s2.hashCode()));


        System.out.println(Test.class.getSimpleName()); // reflection
        Singleton singleton = Singleton.getInstance();
        System.out.println(Integer.toHexString(singleton.hashCode()));
        Singleton breakingSingleton = null;

        Class<Singleton> clsObj = Singleton.class;
        Constructor[] classCtors = clsObj.getDeclaredConstructors();
        for (int i = 0; i <classCtors.length ; i++) {
            classCtors[i].setAccessible(true);
            try {
                breakingSingleton = (Singleton) classCtors[i].newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            break;
        }
        System.out.println("We broke the Singleton pattern");
        System.out.println(Integer.toHexString(breakingSingleton.hashCode()));

    }
}
