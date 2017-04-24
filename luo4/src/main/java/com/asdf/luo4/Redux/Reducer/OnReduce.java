package com.asdf.luo4.Redux.Reducer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by asdf on 2017/4/22.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnReduce {
    enum Mode {
        MAIN,
        ASYNC,
        BACKGROUND,
        POST
    }
    String value();
    Mode mode()default Mode.POST;
}
