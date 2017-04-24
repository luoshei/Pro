package com.asdf.luo1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.asdf.luo1.diyview.WuziqiPanel;

public class MainActivity extends AppCompatActivity {
    TextView tv1;
    WuziqiPanel wqp;

    HorizontalScrollView hsv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.tv1);
        wqp = (WuziqiPanel) findViewById(R.id.qp);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wqp.reBegin();
            }
        });


    }

}
