package com.asdf.luo4.Redux.Reducer;

import android.os.Looper;
import android.util.Log;

import com.asdf.luo4.Redux.Action.Action;
import com.asdf.luo4.Redux.Store.Store;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by asdf on 2017/4/22.
 */

public class Reducer {

    private static final String TAG = "Reducer";

    private Object context;
    private Method method;

    public Reducer(Object context, Method method) {
        this.context = context;
        this.method = method;
    }

    public Object getContext() {
        return this.context;
    }

    public Method getMethod() {
        return this.method;
    }

    public Action reduce(final Action action) throws InvocationTargetException, IllegalAccessException {
        boolean isMainThread = Looper.getMainLooper() == Looper.myLooper();
        OnReduce.Mode mode = method.getAnnotation(OnReduce.class).mode();
        method.setAccessible(true);
        if (OnReduce.Mode.MAIN.equals(mode)) {
            if (isMainThread) {
                Log.e(TAG, String.format("{ %s } running in Thread.Main", this.toString()));
                return (Action) method.invoke(context, action);
            } else {
                Store.getMainHandler().obtainMessage(100, action).sendToTarget();
            }
        } else if (OnReduce.Mode.BACKGROUND.equals(mode)) {
            Store.getBackgroundThread().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e(TAG, String.format("{ %s } running in Thread.Background", Reducer.this.toString()));
                        Store.dispatch((Action) method.invoke(context, action));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (OnReduce.Mode.ASYNC.equals(mode)) {
            Store.getAsyncThread().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.e(TAG, String.format("{ %s } running in Thread.Async", Reducer.this.toString()));
                        Store.dispatch((Action) method.invoke(context, action));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (OnReduce.Mode.POST.equals(mode)) {
            Log.e(TAG, String.format("{ %s } running in Thread." + (isMainThread ? "MainThread" : "Current"), Reducer.this.toString()));
            return (Action) method.invoke(context, action);
        }
        return null;
    }

    public String toString() {
        return String.format("Reducer: %s { Reducer::context:= %s@%d | Reducer::handleAction:= %s }",
                this.method.getName(),
                this.context.getClass().getName(),
                this.context.hashCode(),
                this.method.getAnnotation(OnReduce.class).value());
    }
}
