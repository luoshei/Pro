package com.asdf.luo5;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by asdf on 2017/4/22.
 */

public class BusStationBean {
    public int bsId;

    public List<Object> busAll = new ArrayList<>();

    public int[] busAll1;
    public int[] busAll2;
    public int bus1Id;
    public int bus2Id;
    public int distance1;
    public int distance2;

    public BusStationBean(int bsId,String result) throws Exception{
        this.bsId = bsId;
        JSONObject serverinfo = new JSONObject(result);
        String content = serverinfo.getString("serverinfo");
        JSONArray entity = new JSONArray(content);
        JSONObject bus1 = entity.getJSONObject(0);
        JSONObject bus2 = entity.getJSONObject(1);
        this.bus1Id = bus1.getInt("BusId");
        this.bus2Id = bus2.getInt("BusId");
        this.distance1 = bus1.getInt("Distance");
        this.distance2 = bus2.getInt("Distance");
        busAll1 = new int[]{this.bus1Id,this.distance1};
        busAll2 = new int[]{this.bus2Id,this.distance2};
        busAll.add(busAll1);
        busAll.add(busAll2);
        Object[] object = busAll.toArray();
        Arrays.sort(object, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                int[] bsb1 = (int[])o1;
                int[] bsb2 = (int[])o2;
                if(bsb1[1] > bsb2[1]){
                    return 1;
                }else if(bsb1[1] < bsb2[1]){
                    return -1;
                }
                return 0;
            }
        });
        busAll = Arrays.asList(object);
    }

}
