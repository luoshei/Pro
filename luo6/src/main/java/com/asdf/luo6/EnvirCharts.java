package com.asdf.luo6;

import android.content.Context;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.ColoursXYSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.util.MathHelper;

import java.util.Date;

/**
 * Created by asdf on 2017/4/23.
 */

public enum  EnvirCharts {

    PM25("pm25",500,"Pm2.5传感器"),
    CO2("co2",10000,"Co2传感器"),
    LIGHT("light",5000,"光照强度传感器"),
    HUMI("humi",100,"湿度传感器"),
    TEMP("temp",100,"温度传感器");


    public int ymax;

    public int fzdown;
    public int fzup;

    public String key;

    public String title;

    public XYMultipleSeriesDataset dataset;
    public XYMultipleSeriesRenderer renderer;
    public GraphicalView gv;

    EnvirCharts(String key, int ymax,String title){
        this.key = key;
        this.ymax = ymax;
        this.title = title;
        this.dataset = buildMainDataSet();
        this.renderer = buildMainRenderer();
    }

    public XYMultipleSeriesRenderer buildMainRenderer() {
        Date date = new Date();
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setChartTitle(this.title);
        renderer.setChartTitleTextSize(30);
        renderer.setLabelsColor(Color.RED);

        renderer.setMargins(new int[]{0,20,0,20});
        renderer.setApplyBackgroundColor(true);
        renderer.setMarginsColor(Color.argb(0,1,0,0));
        renderer.setBackgroundColor(Color.TRANSPARENT);
        renderer.setXAxisMax(date.getTime() + 60 * 1000);
        renderer.setXAxisMin(date.getTime());
        renderer.setYAxisMax(ymax);
        renderer.setYAxisMin(0);
        renderer.setPointSize(10);
        renderer.setAxisTitleTextSize(30);
        //renderer.setShowGrid(true);
        renderer.setAxisTitleTextSize(30);
        renderer.setLabelsTextSize(15);
        renderer.setZoomButtonsVisible(true);
        renderer.setGridColor(Color.WHITE);
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0,Color.BLACK);

        ColoursXYSeriesRenderer mRenderer = new ColoursXYSeriesRenderer();
        mRenderer.setDisplayChartValues(true);
        mRenderer.setDisplayChartValuesDistance(200);
        mRenderer.setPointColor(Color.BLUE);
        mRenderer.setPointStyle(PointStyle.CIRCLE);
        mRenderer.setFillPoints(true);
        mRenderer.setWarningColor(Color.RED);


        renderer.addSeriesRenderer(mRenderer);
        return renderer;
    }

    public XYMultipleSeriesDataset buildMainDataSet() {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        TimeSeries ts = new TimeSeries(this.title);
        ts.add(new Date().getTime(), MathHelper.NULL_VALUE);

        dataset.addSeries(ts);
        return dataset;
    }

    public void addData(int y){
        Date date = new Date();
        int[] alldata = {y,fzdown,fzup};
        for(int i = 0;i<this.dataset.getSeriesCount();i++){
            TimeSeries series = (TimeSeries) this.dataset.getSeriesAt(i);
            if(i>0){
                series.add(this.renderer.getXAxisMin(),alldata[i]);
            }
            series.add(i>=1?this.renderer.getXAxisMax():date.getTime(),alldata[i]);
        }
        if(date.getTime() + 2 * 1000 > this.renderer.getXAxisMax()){
            this.renderer.setXAxisMin(date.getTime() - 58 * 1000);
            this.renderer.setXAxisMax(date.getTime() + 2 * 1000);
        }
        this.gv.repaint();
    }

    public GraphicalView initView(Context c) {
        GraphicalView gv = ChartFactory.getTimeChartView(c,dataset,renderer,"HH:mm:ss");
        this.gv = gv;
        return gv;
    }

    public GraphicalView initView(Context c,XYMultipleSeriesDataset dataset,XYMultipleSeriesRenderer renderer) {
        GraphicalView gv = ChartFactory.getTimeChartView(c,dataset,renderer,"HH:mm:ss");
        return gv;
    }

    public void addSlaveSeries(int[] fz){
        if(this.renderer==null || fz == null)return;
        if(this.renderer.getSeriesRendererCount() == 3){
            this.renderer.removeSeriesRenderer(this.renderer.getSeriesRendererAt(1));
            this.renderer.removeSeriesRenderer(this.renderer.getSeriesRendererAt(1));
            this.dataset.removeSeries(1);
            this.dataset.removeSeries(1);
        }
        String[] fzTitle = {"下限","上限"};
        this.fzdown = fz[0];
        this.fzup = fz[1];
        ColoursXYSeriesRenderer mRenderer = (ColoursXYSeriesRenderer) this.renderer.getSeriesRendererAt(0);
        mRenderer.setUseColor(true);
        mRenderer.setWarningMinValue(this.fzdown);
        mRenderer.setWarningMaxValue(this.fzup);
        for (int i = 0;i<fz.length;i++){
            TimeSeries ts = new TimeSeries(fzTitle[i]);
            ts.add(new Date(),fz[i]);
            this.dataset.addSeries(ts);
        }
        for (int i = 0;i<fz.length;i++){
            XYSeriesRenderer ts = new XYSeriesRenderer();
            ts.setColor(new int[]{Color.RED,Color.CYAN}[i]);
            this.renderer.addSeriesRenderer(ts);
        }
    }


}
