package com.asdf.luo4.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.asdf.luo4.Actions.CreateDownloadAction;
import com.asdf.luo4.Actions.DownloadAction;
import com.asdf.luo4.Actions.DownloadDoneActino;
import com.asdf.luo4.Actions.GetFileLenAction;
import com.asdf.luo4.Actions.PauseDownloadAction;
import com.asdf.luo4.Actions.RemoveDownloadAction;
import com.asdf.luo4.Actions.StartDownloadAction;
import com.asdf.luo4.Actions.ToastAction;
import com.asdf.luo4.Actions.UpdateProgressAction;
import com.asdf.luo4.Redux.Action.Action;
import com.asdf.luo4.Redux.Reducer.OnReduce;
import com.asdf.luo4.Redux.Store.Store;
import com.asdf.luo4.dao.TaskDao;
import com.asdf.luo4.model.NetFileInfo;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by asdf on 2017/4/21.
 */

public class DownloadService extends Service {

    public static final String ACTION_START = "0";
    public static final String ACTION_PAUSE = "1";
    public static final String ACTION_STOP = "2";
    public static final String ACTION_UPDATE = "3";

    public static final int MSG_F = 100;

    private HashMap<Integer, Runnable> tasks = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Store.register(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @OnReduce(CreateDownloadAction.TYPE)
    private Action createDownlaod(CreateDownloadAction action) {
        NetFileInfo nfi = action.getNetFileInfo();
        DownloadAction dt = (DownloadAction) this.tasks.get(nfi.id);
        if (dt == null) {
            return new GetFileLenAction(nfi);
        } else {
            if (dt.isPause()) {
                return new GetFileLenAction(nfi);
            } else {
                return new ToastAction("任务正在运行");
            }
        }
    }

    @OnReduce(DownloadAction.TYPE)
    private StartDownloadAction execDownload(DownloadAction action) {
        this.tasks.put(action.getId(), action);
        return new StartDownloadAction(action);
    }

    @OnReduce(value = StartDownloadAction.TYPE, mode = OnReduce.Mode.ASYNC)
    private void startDownload(StartDownloadAction action) {
        Store.dispatch(new ToastAction("开始下载"));
        action.getAction().run();
    }

    @OnReduce(RemoveDownloadAction.TYPE)
    private Action removeDownlaod(RemoveDownloadAction action) {
        DownloadAction downloadAction = (DownloadAction) this.tasks.get(action.getId());
        if (downloadAction == null) return null; //new ToastAction("找不到任务: " + action.getId())
        downloadAction.setPause(true);
        if (action.getRemoveType() == RemoveDownloadAction.TYPE_STOP) {
            this.tasks.remove(action.getId());
            TaskDao.delete(downloadAction.getUrl());
            Store.dispatch(new UpdateProgressAction(0));
            return new ToastAction("任务已停止");
        }
        return new ToastAction("任务已暂停");
    }

    @OnReduce(DownloadDoneActino.TYPE)
    private Action onDownloadDone(DownloadDoneActino actino) {
        Store.dispatch(new ToastAction("下载完成!"));
        return new RemoveDownloadAction(actino.getId(), RemoveDownloadAction.TYPE_STOP);
    }

    @OnReduce(value = PauseDownloadAction.TYPE, mode = OnReduce.Mode.BACKGROUND)
    private void pauseDownload(PauseDownloadAction action) {
        DownloadAction downloadAction = (DownloadAction) this.tasks.get(action.getId());
        if(downloadAction == null) return;
        downloadAction.setPause(true);
    }

    @OnReduce(value = ToastAction.TYPE, mode = OnReduce.Mode.MAIN)
    private void toast(ToastAction action) {
        Toast.makeText(getApplicationContext(), action.getMsg(), Toast.LENGTH_SHORT).show();
    }

    @OnReduce(value = GetFileLenAction.TYPE, mode = OnReduce.Mode.ASYNC)
    private Action getFileLen(GetFileLenAction action) {
        NetFileInfo nfi = action.getNetFileInfo();
        Log.e(TAG, "length run: ");
//        DownloadTask dt = (DownloadTask) getTask(nfi.id);
        HttpURLConnection connection = null;
        try {
            URL url = new URL(nfi.url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.connect();
            int code = 0;
            try {
                code = connection.getResponseCode();
            } catch (Exception e) {
//                TaskService.remove(100);
                Store.dispatch(new ToastAction("获取长度异常" + e.getClass().getSimpleName()));
                return new RemoveDownloadAction(nfi.id, RemoveDownloadAction.TYPE_PAUSE);
            }
            if (code == 200) {
                nfi.length = connection.getContentLength();
                return new DownloadAction(getApplicationContext(), nfi);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return  new ToastAction("获取zong异常" + e.getClass().getSimpleName());
        }
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Store.unregister(this);
    }
}
