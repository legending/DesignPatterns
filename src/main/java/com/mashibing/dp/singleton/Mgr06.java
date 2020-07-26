package com.mashibing.dp.singleton;

/**
 * lazy loading
 * 也称懒汉式
 * 虽然达到了按需初始化的目的，但却带来线程不安全的问题
 * 可以通过synchronized解决，但也带来效率下降
 */
public class Mgr06 {
    private static volatile Mgr06 INSTANCE; //JIT 必须加volatile,否则会出现指令重排序的问题，这里是禁止对这个对象的指令重排序

    private Mgr06() {
    }

    public static Mgr06 getInstance() {
        if (INSTANCE == null) {//之所以外层也要判断是为了节省时间，提高效率，因为只要单例已经生成了，后面直接通过当前这一层判断拿到实例，而不用加锁（加锁是一个耗时耗力的操作）
            //双重检查
            synchronized (Mgr06.class) {
                if(INSTANCE == null) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    INSTANCE = new Mgr06(); //在产生instance的时候如果没有用volatile，可能会获取到半初始化的instance
                }
            }
        }
        return INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        for(int i=0; i<100; i++) {
            new Thread(()->{
                System.out.println(Mgr06.getInstance().hashCode());
            }).start();
        }
    }
}
