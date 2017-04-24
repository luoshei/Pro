package com.asdf.luo6;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.asdf.luo6.dao.EnvirDao;

import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.Date;

import static com.asdf.luo6.EnvirCharts.CO2;

public class HistoryActivity extends AppCompatActivity {

    private Spinner spiSensor;
    private Spinner spiPeriod;

    private Button btnHistory;
    private Button btnAnalysis;

    private FrameLayout frameLayout;

    private int[] periods = {60 * 1000, 5 * 60 * 1000, 10 * 60 * 1000 };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        spiSensor = (Spinner) findViewById(R.id.sensor_spi);
        spiPeriod = (Spinner) findViewById(R.id.period_spi);
        btnHistory = (Button) findViewById(R.id.btn_query);
        btnAnalysis = (Button) findViewById(R.id.btn_analy);

        frameLayout = (FrameLayout) findViewById(R.id.gv_container);

        MyOnClick myOnClick = new MyOnClick();
        btnHistory.setOnClickListener(myOnClick);
        btnAnalysis.setOnClickListener(myOnClick);
    }

    class MyOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //获取传感器类型
            int type = spiSensor.getSelectedItemPosition();

            switch(v.getId()){
                case R.id.btn_query:
                    //获取查询周期
                    int period = spiPeriod.getSelectedItemPosition();
                    //往数据库中查找
                    Cursor cursor = EnvirDao.selectEnvir((new Date().getTime() - periods[period]) + "");

                    XYMultipleSeriesDataset dataset = EnvirCharts.values()[type].buildMainDataSet();
                    XYMultipleSeriesRenderer renderer = EnvirCharts.values()[type].buildMainRenderer();
                    renderer.setXAxisMin(new Date().getTime() - periods[period]);
                    renderer.setXAxisMax(new Date().getTime());

                    XYSeries series = dataset.getSeriesAt(0);
                    while(cursor.moveToNext()){
                        String vals = cursor.getString(1);
                        String[] val = vals.split(",");
                        series.add(cursor.getLong(2),Double.parseDouble(val[type]));
                    }
                    GraphicalView gv = CO2.initView(HistoryActivity.this,dataset,renderer);
                    frameLayout.removeAllViews();
                    frameLayout.addView(gv);
                    break;
                case R.id.btn_analy:
                    Intent analyIntent = new Intent(HistoryActivity.this,AnalysisActivity.class);
                    analyIntent.putExtra("sensor",type);
                    startActivity(analyIntent);
                    break;
            }
        }
    }
}
