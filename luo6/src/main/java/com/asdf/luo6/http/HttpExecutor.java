package com.asdf.luo6.http;

import java.util.LinkedList;

/**
 * Created by asdf on 2017/4/22.
 */

public class HttpExecutor extends Thread{

    public LinkedList<Runnable> ll = new LinkedList<>();

    public int sleepTime = 1000;

    public HttpExecutor add(Runnable runnable){
        ll.add(runnable);
        return this;
    }

    public void exec(){
        start();
    }

    @Override
    public void run() {
        while(true){
            for(Runnable runnable : ll){
                runnable.run();
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
