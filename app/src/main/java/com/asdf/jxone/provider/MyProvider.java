package com.asdf.jxone.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.asdf.jxone.MyDbHelper;

/**
 * Created by asdf on 2017/4/16.
 */

public class MyProvider extends ContentProvider {
    private static final int TABLE1_DIR = 0;
    private static final int TABLE1_ITEM = 1;
    private static UriMatcher uriMatcher;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.asdf.jxone.provider","table1",TABLE1_DIR);
        uriMatcher.addURI("com.asdf.jxone.provider","table1/#",TABLE1_ITEM);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch(uriMatcher.match(uri)){
            case TABLE1_DIR:
                SQLiteDatabase sqb = new MyDbHelper(getContext(),"MYDB.db",null,2).getWritableDatabase();
                return sqb.query("table1",projection,selection,selectionArgs,sortOrder,null,null);
            case TABLE1_ITEM:

                break;
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)){
            case TABLE1_DIR:
                return "vnd.android.cursor.dir/com.asdf.jxone.provider.table1";
            case TABLE1_ITEM:
                return "vnd.android.cursor.item/com.asdf.jxone.provider.table1";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sqb = new MyDbHelper(getContext(),"MYDB.db",null,2).getWritableDatabase();
        sqb.insert("table1",null,values);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
