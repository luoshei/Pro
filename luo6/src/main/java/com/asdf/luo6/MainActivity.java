package com.asdf.luo6;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.asdf.luo6.bean.SensorValues;
import com.asdf.luo6.dao.EnvirDao;
import com.asdf.luo6.http.HttpExecutor;
import com.asdf.luo6.http.HttpRequest;
import com.asdf.luo6.utils.SpUtil;

public class MainActivity extends AppCompatActivity {
    private ViewPager vp;
    private ChartPagers cp;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 300:
                    try{
                        SensorValues sv = new SensorValues(msg.obj.toString());
                        int[] sen = {
                          sv.pm25,sv.co2,sv.light,sv.humi,sv.temp
                        };
                        for (EnvirCharts ec:EnvirCharts.values()){
                            ec.addData(sen[ec.ordinal()]);
                            //插入数据库，做历史查询功能
                            EnvirDao.insertEnvir(sen[0] + ","+sen[1] + ","+sen[2] + ","+sen[3] + ","+sen[4]);
                        }
                        vp.invalidate();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化图表
        for (EnvirCharts ec:EnvirCharts.values()){
            ec.initView(this);
            ec.addSlaveSeries(SpUtil.getInstance().getFz(ec.key));
        }

        vp = (ViewPager) findViewById(R.id.chart_vp);
        cp = new ChartPagers();
        vp.setAdapter(cp);

        HttpExecutor he = new HttpExecutor();
        HttpRequest hr = new HttpRequest(300,handler).setMethod("POST").open(HttpRequest.ACTION_SENSE).param("{}");
        he.add(hr);
        he.exec();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        for (EnvirCharts ec:EnvirCharts.values()){
            ec.addSlaveSeries(SpUtil.getInstance().getFz(ec.key));
        }
    }

    public void doClick(View v){
        switch(v.getId()){
            case R.id.btn_setfz:
                    Intent intent = new Intent(this,SettingActivity.class);
                    startActivity(intent);
                break;
            case R.id.btn_other:
                Intent intentHistory = new Intent(this,HistoryActivity.class);
                startActivity(intentHistory);
                break;
        }
    }

}
