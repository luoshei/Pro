package com.asdf.luo2.util;

import android.app.Activity;

import java.lang.reflect.Field;

/**
 * Created by asdf on 2017/4/19.
 */

public class BindIdUtil {

    public static void register(Activity activity){
            Field[] fields = activity.getClass().getDeclaredFields();
            for (Field field : fields) {
                try {
                    if(field.isAnnotationPresent(BindView.class)){
                        field.setAccessible(true);
                        BindView bv = field.getAnnotation(BindView.class);
                        int id = bv.value();
                        if(id > 0) field.set(activity,activity.findViewById(id));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
    }
}
