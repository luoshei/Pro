package com.asdf.jxone.bean;

/**
 * Created by asdf on 2017/4/16.
 */

public class CarInfoBean {
    public int id;
    public int lasttype;
    public int type;//0正常 1过高 2过低
    public int balance;
    public long times;

    public static CarInfoBean[] carInfoBeans = new CarInfoBean[4];
    static {
        for (int i = 0;i<carInfoBeans.length;i++){
            CarInfoBean cif = new CarInfoBean();
            cif.id = i+1;
            carInfoBeans[i] = cif;
        }
    }
    public static CarInfoBean getInstance(int id){
        return carInfoBeans[id-1];
    }
    public void setType(){
        this.lasttype = this.type;
        int temp = 0;
        if(this.balance > 600)
            temp = 1;
        if(this.balance < 300)
            temp = 2;
        this.type = temp;
    }
}
