package com.asdf.luo2.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.asdf.luo2.R;

/**
 * Created by asdf on 2017/4/19.
 */

public class MyAdapter extends BaseAdapter{
    private Context context;

    private String[] strings = {"asd","aasdsd","asasdd","asddd","asdsagg"};

    private Bitmap[] bitmaps = new Bitmap[5];;
    public MyAdapter(Context c){
        this.context = c;

        bitmaps[0] = BitmapFactory.decodeResource(context.getResources(),R.drawable.p1);
        bitmaps[1] = BitmapFactory.decodeResource(context.getResources(),R.drawable.p2);
        bitmaps[2] = BitmapFactory.decodeResource(context.getResources(),R.drawable.p3);
        bitmaps[3] = BitmapFactory.decodeResource(context.getResources(),R.drawable.p4);
        bitmaps[4] = BitmapFactory.decodeResource(context.getResources(),R.drawable.p5);
    }

    @Override
    public int getCount() {
        return strings.length;
    }

    @Override
    public Object getItem(int position) {
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart,null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.textView);
        tv.setText(strings[position]);

        ImageView iv = (ImageView)convertView.findViewById(R.id.src);
        iv.setImageBitmap(bitmaps[position]);
        return convertView;
    }
}
