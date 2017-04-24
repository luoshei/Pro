package com.asdf.luo6;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by asdf on 2017/4/23.
 */

public class ChartPagers extends PagerAdapter{
    @Override
    public int getCount() {
        return EnvirCharts.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(EnvirCharts.values()[position].gv);
        return EnvirCharts.values()[position].gv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(EnvirCharts.values()[position].gv);
    }
}
