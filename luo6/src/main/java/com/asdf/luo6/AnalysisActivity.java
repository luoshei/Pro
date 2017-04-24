package com.asdf.luo6;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.asdf.luo6.dao.EnvirDao;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.ArrayList;
import java.util.Date;

public class AnalysisActivity extends AppCompatActivity {

    private FrameLayout flBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        flBar = (FrameLayout) findViewById(R.id.fl_bar);
        int sensorType = getIntent().getIntExtra("sensor",0);

        XYMultipleSeriesDataset xsd = EnvirCharts.values()[sensorType].buildMainDataSet();
        XYMultipleSeriesRenderer xsr = EnvirCharts.values()[sensorType].buildMainRenderer();
        XYSeries series = xsd.getSeriesAt(0);
        //往数据库中查找
        Cursor cursor = EnvirDao.selectEnvir((new Date().getTime() - 60 * 1000) + "");

        int [] num = new int[5];
        ArrayList<Integer> al = new ArrayList();
        while(cursor.moveToNext()){
            String vals = cursor.getString(1);
            String[] val = vals.split(",");
            al.add(Integer.parseInt(val[sensorType]));
        }
        int[] data = getZuju(al);
        for(Integer intt:al){
            if(intt == data[1]) num[4]++;
            else
                num[(intt - data[0]) / data[2]]++;
        }

        for (int i = 0;i<num.length;i++){
            series.add(i,num[i]);
        }

        GraphicalView gv = ChartFactory.getBarChartView(this,xsd,xsr, BarChart.Type.DEFAULT);
        flBar.addView(gv);

    }
    public int[] getZuju(ArrayList<Integer> al){
        int min;
        int max;
        min = max = al.get(0);
        for(int i = 1;i<al.size();i++){
            int cur = al.get(i);
            if(cur < min){
                min = cur;
            }
            if(cur > max){
                max = cur;
            }

        }
        return new int[]{min,max,(max - min) % 5 == 0? (max - min)/5:(max - min)/5 +1};
    }



}
