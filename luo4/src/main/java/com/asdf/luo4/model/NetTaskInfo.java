package com.asdf.luo4.model;

/**
 * Created by asdf on 2017/4/21.
 */

public class NetTaskInfo {
    public int id;
    public String fileName;
    public String url;
    public long length;
    public long start;
    public long finished;

    public NetTaskInfo(){

    }

    public NetTaskInfo(NetFileInfo nfi,long start, long finished) {
        this.id = nfi.url.hashCode();
        this.fileName = nfi.fileName;
        this.url = nfi.url;
        this.length = nfi.length;
        this.start = start;
        this.finished = finished;
    }
}
