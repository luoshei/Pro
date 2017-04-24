package com.asdf.luo4.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.version;

/**
 * Created by asdf on 2017/4/21.
 */

public class MySqlitDb extends SQLiteOpenHelper{
    public static final String DB_NAME = "download.db";
    public static final int DB_VERSION = 1;

    private static final String createSql = "create table threadTask(_id integer primary key autoincrement," +
            "fileName text,url text, length integer, start integer, finished integer)";
    private static final String dropSql = "drop table if exists threadTask";

    private static MySqlitDb mySqlitDb;
    private MySqlitDb(Context context) {
        super(context, DB_NAME, null, version);
    }

    public static MySqlitDb getInstance(){
        return mySqlitDb;
    }
    public static void register(Context c){
        if(mySqlitDb == null)
            mySqlitDb = new MySqlitDb(c);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropSql);
        db.execSQL(createSql);
    }

}
