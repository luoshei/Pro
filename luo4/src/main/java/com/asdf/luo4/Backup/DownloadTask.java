package com.asdf.luo4.service;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.asdf.luo4.dao.TaskDao;
import com.asdf.luo4.model.NetFileInfo;
import com.asdf.luo4.model.NetTaskInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by asdf on 2017/4/21.
 */

public class DownloadTask extends Thread {

    private static final String path = Environment.getExternalStorageDirectory().toString();
    private NetTaskInfo netTaskInfo;
    private Context context;

    public  DownloadTask(Context c, NetFileInfo nfi) {
        this.context = c;
        ArrayList<NetTaskInfo> netTasks = TaskDao.select(nfi.url);
        if (netTasks.size() == 0) {
            this.netTaskInfo = new NetTaskInfo(nfi, 0, 0);
            TaskDao.insert(this.netTaskInfo);
        } else {
            this.netTaskInfo = netTasks.get(0);
        }
        prepareTask();//任务开始前的准备
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    private boolean pause;

  /*  public void scheduleTask(Context c, NetFileInfo nfi) {
        this.context = c;
        ArrayList<NetTaskInfo> netTasks = TaskDao.select(nfi.url);
        if (netTasks.size() == 0) {
            this.netTaskInfo = new NetTaskInfo(nfi, 0, 0);
        } else {
            this.netTaskInfo = netTasks.get(0);
        }
        prepareTask();//任务开始前的准备
        start();//任务真正开始
    }*/

    @Override
    public void run() {
        InputStream is = null;
        RandomAccessFile raf = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(netTaskInfo.url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setDoInput(true);
            long curStart = netTaskInfo.start + netTaskInfo.finished;
            //设置下载起点
            connection.setRequestProperty("Range", "bytes=" + curStart + "-" + netTaskInfo.length);
            //设置写入起点
            raf = new RandomAccessFile(new File(Environment.getExternalStorageDirectory() , netTaskInfo.fileName),"rwd");
            raf.seek(curStart);
            Log.e(TAG, "run: head");
            //开始下载
            connection.connect();
            int code = 0;
            try{
                code = connection.getResponseCode();
            }catch(Exception e){
                TaskService.remove(100);
                Log.e(TAG, "run: 异常" + e.getClass().getSimpleName() );
            }
            if (code == 206) {//在服务器成功完成一个带有Range域的请求返回的状态码
                is = connection.getInputStream();
                int len = -1;
                byte[] by = new byte[1024];
                long pro = netTaskInfo.start + netTaskInfo.finished;
                Intent progress = new Intent(DownloadService.ACTION_UPDATE);
                while ((len = is.read(by)) != -1) {
                    raf.write(by, 0, len);
                    //进度更新，发送广播
                    pro += len;
                    Log.e(TAG, "run: " + pro);
                    progress.putExtra("pro", (int)(pro * 100 / netTaskInfo.length));
                    this.context.sendBroadcast(progress);
                    if(isPause()){
                        TaskDao.update(netTaskInfo.url,pro);
                        Log.e(TAG, "run: 成功暂停");
                        TaskService.remove(netTaskInfo.id);
                        return;
                    }
                }
                TaskDao.delete(netTaskInfo.url);
                TaskService.remove(100);
            }else{
                Log.e(TAG, "run: 请求失败");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(Exception e){
            TaskService.remove(100);
            Log.e(TAG, "run: 异常" + e.getClass().getSimpleName() );
        }finally {
            try {
                if(raf!=null)  raf.close();
                if(is!=null)  is.close();
                if(connection!=null)  connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void prepareTask() {
        //在内存卡写一个文件，大小和下载的文件一致
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file = new File(Environment.getExternalStorageDirectory(), netTaskInfo.fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                RandomAccessFile raf = new RandomAccessFile(file,"rwd");
                raf.setLength(netTaskInfo.length);
                Log.e(TAG, "prepareTask: 文件已经存在");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
