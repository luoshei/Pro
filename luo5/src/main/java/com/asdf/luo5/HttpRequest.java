package com.asdf.luo5;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by asdf on 2017/4/22.
 */

public class HttpRequest extends Thread{
    public static final String ACTION_BUS = "http://192.168.1.101:8080/transportservice/type/jason/action/GetBusStationInfo.do";

    public int what;
    public Handler handler;

    public String url;
    public String method;
    public String param;

    public HttpRequest(int what, Handler handler){
        this.what = what;
        this.handler = handler;
    }

    public HttpRequest open(String url){
        this.url = url;
        return this;
    }

    public HttpRequest setMethod(String method){
        this.method = method;
        return this;
    }

    public HttpRequest param(String param){
        this.param = param;
        return this;
    }
    @Override
    public void run() {
        http();
    }

    public void http(){
        OutputStream os = null;
        InputStream is = null;
        BufferedReader br = null;
        String line = null;
        try {
            URL r = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) r.openConnection();
            connection.setRequestProperty("Content-Type","text/plain;charset=utf-8");
            connection.setRequestMethod(this.method);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            os = connection.getOutputStream();
            os.write(this.param.getBytes());
            if(connection.getResponseCode() == 200){
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is));
                line = br.readLine();
                handler.obtainMessage(this.what,line).sendToTarget();
            }else{
                Log.e(TAG, "run: 请求未成功");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){

        }
    }
}
