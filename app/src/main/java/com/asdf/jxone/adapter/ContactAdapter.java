package com.asdf.jxone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asdf.jxone.R;
import com.asdf.jxone.bean.User;

import java.util.ArrayList;

/**
 * Created by asdf on 2017/4/16.
 */

public class ContactAdapter extends BaseAdapter{

    private Context c;
    private ArrayList<User> al;

    public ContactAdapter(Context c,ArrayList<User> al) {
        this.c = c;
        this.al = al;
    }

    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public Object getItem(int position) {
        return al.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(null == convertView)
            convertView = LayoutInflater.from(c).inflate(R.layout.item_contact,null);
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView pass = (TextView) convertView.findViewById(R.id.pass);
        name.setText(al.get(position).name);
        pass.setText(al.get(position).pass);
        return convertView;
    }
}
