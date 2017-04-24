package com.asdf.luo4.model;

import java.io.Serializable;

/**
 * Created by asdf on 2017/4/21.
 */

public class NetFileInfo implements Serializable{
    public int id;
    public String fileName;
    public String url;
    public long length;

    public NetFileInfo(String fileName, String url, long length) {
        this.fileName = fileName;
        this.url = url;
        this.length = length;
        this.id = this.url.hashCode();
    }
}
