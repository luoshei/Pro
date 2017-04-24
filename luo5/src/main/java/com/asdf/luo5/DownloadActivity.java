package com.asdf.luo5;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

public class DownloadActivity extends AppCompatActivity {
    private static final String TAG = "DownloadActivity";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String result = msg.obj.toString();
            switch (msg.what){
                case 100:
                    if(null != result){
                        try {
                            BusStationBean bsb = new BusStationBean(1,result);
                            ea.update(bsb);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 200:
                    if(null != result){
                        try {
                            BusStationBean bsb = new BusStationBean(2,result);
                            ea.update(bsb);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }

        }
    };

    private ExpandableListView elv;
    private ExpandableAdapter ea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        elv = (ExpandableListView) findViewById(R.id.elv);
        ea = new ExpandableAdapter(this,new BusStationBean[2]);
        elv.setAdapter(ea);
        HttpRequest hr1 = new HttpRequest(100,handler).open(HttpRequest.ACTION_BUS).setMethod("POST").param("{'BusStationId':1}");//站台1
        HttpRequest hr2 = new HttpRequest(200,handler).open(HttpRequest.ACTION_BUS).setMethod("POST").param("{'BusStationId':2}");//站台2
        HttpExecutor he = new HttpExecutor();
        he.add(hr1).add(hr2).exec();
        elv.expandGroup(0);elv.expandGroup(1);
    }

}
