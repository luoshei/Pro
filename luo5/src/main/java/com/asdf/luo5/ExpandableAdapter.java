package com.asdf.luo5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Created by asdf on 2017/4/22.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter {

    public BusStationBean[] busStationBeans = new BusStationBean[2];

    public Context context;
    public ExpandableAdapter(Context c, BusStationBean[] busStationBeans){
        this.context = c;
        this.busStationBeans = busStationBeans;
    }

    public void update(BusStationBean bsb){
        busStationBeans[bsb.bsId-1] = bsb;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 2;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return busStationBeans[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return busStationBeans[groupPosition].busAll.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_busstation_group,null,false);
        if(busStationBeans.length>0 && busStationBeans[groupPosition]!=null) {
            TextView station = (TextView) convertView.findViewById(R.id.station);
            station.setText(this.busStationBeans[groupPosition].bsId + "号公交站台");
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_busstation_child,null);
        TextView busid = (TextView) convertView.findViewById(R.id.busid);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        if(busStationBeans.length>0 && busStationBeans[groupPosition]!=null) {
            busid.setText(((int[])this.busStationBeans[groupPosition].busAll.get(childPosition))[0] + "号公交车");
            distance.setText("距离站台" + ((int[])this.busStationBeans[groupPosition].busAll.get(childPosition))[1]);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
