package com.asdf.jxone;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.asdf.jxone.bean.CarInfoBean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            showNotification((CarInfoBean) msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new MyThread().start();

    }

    public void showNotification(CarInfoBean carInfoBean){
        if(carInfoBean.lasttype == carInfoBean.type && new Date().getTime() - carInfoBean.times <= 10 * 1000 )return;
        NotificationCompat.Builder nb = new NotificationCompat.Builder(this);
        nb.setContentTitle("小车余额警告");
        nb.setContentText("小车ID："+ carInfoBean.id +"余额："+carInfoBean.balance+"类型："+new String[]{"正常","过高","过低"}[carInfoBean.type] + "time" + sdf.format(carInfoBean.times));
        nb.setAutoCancel(true);
        nb.setSmallIcon(R.drawable.circle);
        nb.setPriority(NotificationCompat.PRIORITY_MAX);
        carInfoBean.times = new Date().getTime();
        NotificationManager nm = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        nm.notify(carInfoBean.id,nb.build());

    }


    class MyThread extends Thread {

        public boolean stop;

        @Override
        public void run() {
            while(!stop){

                for (int i = 1;i<=1;i++){
                    CarInfoBean cib = CarInfoBean.getInstance(i);
                    cib.id = i;
                    cib.balance = 3000;
                    cib.setType();
                    if(cib.times == 0) cib.times = new Date().getTime();
                    myHandler.obtainMessage(100,cib).sendToTarget();
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
