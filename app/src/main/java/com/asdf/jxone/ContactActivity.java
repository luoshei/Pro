package com.asdf.jxone;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.asdf.jxone.adapter.ContactAdapter;
import com.asdf.jxone.bean.User;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ListView lv = (ListView) findViewById(R.id.lv_contacts);

        Uri uri = Uri.parse("content://com.asdf.jxone.provider/table1");
        ContentValues cv = new ContentValues();
        cv.put("name","zhangsan");
        cv.put("pass","111");
        getContentResolver().insert(uri,cv);


        Cursor cursor = getContentResolver().query(uri,null,null,null,null,null);
        /*while(cursor.moveToNext()){
            Toast.makeText(this, cursor.getString(0) + "**" + cursor.getString(1), Toast.LENGTH_SHORT).show();
        }*/
        ArrayList<User> al = new ArrayList<>();
        while(cursor.moveToNext()){
            User user = new User();
            user.id = cursor.getInt(cursor.getColumnIndex("id"));
            user.name = cursor.getString(cursor.getColumnIndex("name"));
            user.pass = cursor.getString(cursor.getColumnIndex("pass"));
            al.add(user);
        }
        ContactAdapter ca = new ContactAdapter(this,al);
        lv.setAdapter(ca);
    }
    public void doClick(View v){
        int i = 0;
        for (;i<9;i++);
    }
}

