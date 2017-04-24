package com.asdf.luo6.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asdf.luo6.db.MySqlitDb;

import java.util.Date;

/**
 * Created by asdf on 2017/4/24.
 */

public class EnvirDao {

    private static MySqlitDb mdb = MySqlitDb.getInstance();

    public static Cursor selectEnvir(String times){
        SQLiteDatabase sqb = mdb.getReadableDatabase();
        Cursor c = sqb.rawQuery("select * from envir where times > ?",new String[]{times});
        return c;
    }

    public static void insertEnvir(String sv){
        SQLiteDatabase sqb = mdb.getReadableDatabase();
        sqb.execSQL("insert into envir(val,times) values(?,?)",new Object[]{sv,new Date().getTime()});
        sqb.close();
    }

}
