package com.asdf.luo6.bean;

import org.json.JSONObject;

/**
 * Created by asdf on 2017/4/23.
 */

public class SensorValues {
    public int pm25;
    public int co2;
    public int light;
    public int humi;
    public int temp;

    public SensorValues(){

    }
    public SensorValues(String json) throws Exception {
        parseJson(json);

    }

    public void parseJson(String json) throws Exception {
        JSONObject serverinfo = new JSONObject(json);
        JSONObject sensor = new JSONObject(serverinfo.getString("serverinfo"));
        this.pm25 = sensor.getInt("pm2.5");
        this.co2 = sensor.getInt("co2");
        this.light = sensor.getInt("LightIntensity");
        this.humi = sensor.getInt("humidity");
        this.temp = sensor.getInt("temperature");
    }

}
