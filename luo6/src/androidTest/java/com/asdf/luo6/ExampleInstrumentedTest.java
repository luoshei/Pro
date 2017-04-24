package com.asdf.luo6;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.asdf.luo6.dao.EnvirDao;
import com.asdf.luo6.db.MySqlitDb;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.ContentValues.TAG;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        SQLiteDatabase sqb = MySqlitDb.getInstance().getWritableDatabase();
        //sqb.execSQL("insert into envir(val,times) values(?,?)",new Object[]{"asd",123});
        EnvirDao.insertEnvir("123sdadsa");
        Cursor c = EnvirDao.selectEnvir("123sdadsa");
        while(c.moveToNext()){
            Log.e(TAG, "useAppContext: " + c.getString(1) + "asd" +c.getLong(2));
        }
    }
}
