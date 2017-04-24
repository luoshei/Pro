package com.asdf.luo4;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.asdf.luo4.Actions.CreateDownloadAction;
import com.asdf.luo4.Actions.PauseDownloadAction;
import com.asdf.luo4.Actions.RemoveDownloadAction;
import com.asdf.luo4.Actions.StartServiceAction;
import com.asdf.luo4.Actions.UpdateProgressAction;
import com.asdf.luo4.Redux.Reducer.OnReduce;
import com.asdf.luo4.Redux.Store.Store;
import com.asdf.luo4.model.NetFileInfo;
import com.asdf.luo4.service.DownloadService;

public class DuandianActivity extends Activity implements View.OnClickListener{
    private TextView tvFileName;
    private Button btnPause;
    private Button btnStop;
    private Button btnDownload;
    private ProgressBar pb;
    private static final String testUrl = "http://sw.bos.baidu.com/sw-search-sp/software/d2622df9c559c/WeChat_2.4.1.67_setup.exe";

//    private MyReciever mr = new MyReciever();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duandian);
        tvFileName = (TextView) findViewById(R.id.fileName);
        btnDownload = (Button) findViewById(R.id.btn_download);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnStop = (Button) findViewById(R.id.btn_stop);
        pb = (ProgressBar) findViewById(R.id.pb);

        Store.register(this);
//        Store.debug();
        //手动注册广播接收器
//        IntentFilter ift = new IntentFilter(DownloadService.ACTION_UPDATE);
//        registerReceiver(mr,ift);
        Button[] btns = new Button[]{btnPause,btnStop,btnDownload};
        for(int i = 0;i<btns.length;i++){
            btns[i].setOnClickListener(this);
        }
        Store.dispatch(new StartServiceAction(DownloadService.class, DownloadService.ACTION_START));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Store.unregister(this);
//        unregisterReceiver(mr);
    }

    @Override
    public void onClick(View v) {
        NetFileInfo  netFileInfo = new NetFileInfo("Q22Q",this.testUrl,0);
//        Toast.makeText(this, "被点击", Toast.LENGTH_SHORT).show();
        switch(v.getId()){
            case R.id.btn_download:
                Store.dispatch(new CreateDownloadAction(netFileInfo));
                break;
            case R.id.btn_pause:
                Store.dispatch(new PauseDownloadAction(netFileInfo.id));
                break;
            case R.id.btn_stop:
                Store.dispatch(new RemoveDownloadAction(netFileInfo.id, RemoveDownloadAction.TYPE_STOP));
                break;
        }
    }

    @OnReduce(StartServiceAction.TYPE)
    public void startService(StartServiceAction action) {
        Class<? extends Service> sClass = action.getService();
        Intent intent = new Intent(this.getApplicationContext(), action.getService());
        intent.setAction(action.getAction());
        startService(intent);
    }

    @OnReduce(value = UpdateProgressAction.TYPE, mode = OnReduce.Mode.MAIN)
    private void updateProgress(UpdateProgressAction action) {
        pb.setProgress(action.getProgress());
    }

//    class MyReciever extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int pro = intent.getIntExtra("pro",0);
//            pb.setProgress(pro);
//
//        }
//    }
}
