package com.asdf.luo4.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asdf.luo4.db.MySqlitDb;
import com.asdf.luo4.model.NetTaskInfo;

import java.util.ArrayList;

/**
 * Created by asdf on 2017/4/21.
 */

public class TaskDao {

    public TaskDao dbOperate;

    public static ArrayList<NetTaskInfo> select(String url){
        SQLiteDatabase sqb = MySqlitDb.getInstance().getReadableDatabase();
        Cursor cursor = sqb.rawQuery("select * from threadTask where url = ?",new String[]{url});
        ArrayList<NetTaskInfo> netTaskInfos = new ArrayList<>();
        while(cursor.moveToNext()) {
            NetTaskInfo nti = new NetTaskInfo();
            nti.fileName = cursor.getString(cursor.getColumnIndex("fileName"));
            nti.start = cursor.getLong(cursor.getColumnIndex("start"));
            nti.length = cursor.getLong(cursor.getColumnIndex("length"));
            nti.url = cursor.getString(cursor.getColumnIndex("url"));
            nti.finished = cursor.getLong(cursor.getColumnIndex("finished"));
            nti.id = nti.url.hashCode();
            netTaskInfos.add(nti);
        }
        sqb.close();
        return netTaskInfos;
    }

    public static void update(String url, long finished){
        SQLiteDatabase sqb = MySqlitDb.getInstance().getWritableDatabase();
        sqb.execSQL("update threadTask set finished = ? where url = ?", new Object[]{finished,url});
        sqb.close();
    }

    public static void insert(NetTaskInfo nti){
        SQLiteDatabase sqb = MySqlitDb.getInstance().getWritableDatabase();
        sqb.execSQL("insert into threadTask(fileName,url,length,start,finished) values(?,?,?,?,?)", new Object[]{nti.fileName,
                nti.url,
                nti.length,
                nti.start,
                nti.finished
        });
        sqb.close();
    }

    public static void delete(String url){
        SQLiteDatabase sqb = MySqlitDb.getInstance().getWritableDatabase();
        sqb.execSQL("delete from threadTask where url = ?", new Object[]{url});
        sqb.close();
    }

}
