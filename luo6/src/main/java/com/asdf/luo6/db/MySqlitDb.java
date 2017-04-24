package com.asdf.luo6.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asdf on 2017/4/24.
 */

public class MySqlitDb extends SQLiteOpenHelper{
    private static final String DB_NAME = "envir.db";
    private static final int DB_VERSION = 2;

    private static final String createEnvir = "create table envir(id integer primary key autoincrement, " +
                                                    "val text," +
                                                   "times integer);";
    private static final String dropEnvir = "drop table if exists envir";

    private static MySqlitDb mySqlitDb;

    public static void register(Context c){
        mySqlitDb = new MySqlitDb(c);
    }
    public static MySqlitDb getInstance(){
        return mySqlitDb;
    }
    public MySqlitDb(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createEnvir);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropEnvir);
        db.execSQL(createEnvir);
    }
}
