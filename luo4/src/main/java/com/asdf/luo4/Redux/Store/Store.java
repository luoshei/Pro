package com.asdf.luo4.Redux.Store;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.asdf.luo4.Redux.Action.Action;
import com.asdf.luo4.Redux.Reducer.OnReduce;
import com.asdf.luo4.Redux.Reducer.Reducer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by asdf on 2017/4/22.
 */

public class Store {
    private static final String TAG = "Store";
    private Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Store.dispatch((Action) msg.obj);
        }
    };
    private ExecutorService asyncThread = Executors.newCachedThreadPool();
    private ExecutorService backgroundThread = Executors.newSingleThreadExecutor();

    private static Store instance = null;

    private HashMap<Class<?>, Object> useInstances = new HashMap<>();
    private HashMap<String, List<Reducer>> rootReducer = new HashMap<>();
    private HashMap<String, List<String>> actionTable = new HashMap<>();

    private Store() {}

    public static void use(Class<?> clazz) {
        Store store = Store.getInstance();
        if (clazz == null) return;
        store.useInstances.put(clazz, null);
    }

    public static <T> T get(Class<T> tClass) {
        T t = null;
        Store store = Store.getInstance();
        t = (T) store.useInstances.get(tClass);
        if (t == null) {
            synchronized (tClass) {
                t = (T) store.useInstances.get(tClass);
                if (t == null) {
                    try {
                        t = tClass.newInstance();
                        store.useInstances.put(tClass, t);
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return t;
    }

    public static ExecutorService getAsyncThread() {
        return Store.getInstance().asyncThread;
    }

    public static ExecutorService getBackgroundThread() {
        return Store.getInstance().backgroundThread;
    }

    public static Handler getMainHandler() {
        return Store.getInstance().uiHandler;
    }

    private static Store getInstance() {
        if (Store.instance == null) {
            synchronized (Store.class) {
                if (Store.instance == null) {
                    Store.instance = new Store();
                }
            }
        }
        return Store.instance;
    }

    private void updateStateTable(Action action) {
        List<String> actionHistory = this.actionTable.get(action.getType());
        if (actionHistory == null) this.actionTable.put(action.getType(), actionHistory = new ArrayList<>());
        StringBuilder filedContent = new StringBuilder();
        for (Field field : action.getClass().getDeclaredFields()) {
            if (field.getName().toLowerCase().equals("type")) continue;
            field.setAccessible(true);
            try {
                filedContent.append(String.format(" action::%s:= %s |", field.getName(), String.valueOf(field.get(action))));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        filedContent.deleteCharAt(filedContent.length() - 1);
        actionHistory.add(String.format("Action::@:=%s@%d { %s }\n",
                action.getType(),
                action.hashCode(),
                filedContent.toString()));
    }

    public static void register(Object context) {
        Store store = Store.getInstance();
        for (Method method : context.getClass().getDeclaredMethods()) {
            OnReduce onReduce = method.getAnnotation(OnReduce.class);
            if (onReduce == null) continue;
            Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes.length != 1) throw new RuntimeException("Reducer only can have one parameter and the parameter must be an action.");
//            if (!paramTypes[0].isAssignableFrom(Action.class)) throw new RuntimeException("Reducer only can have one parameter and the parameter must be an action.");
//            if (!method.getReturnType().isAssignableFrom(Action.class)) throw new RuntimeException("Reducer must return a new action or null.");
            String action = onReduce.value();
            Log.e(TAG, String.format("Match Reducer: %s { Reducer::context:= %s@%d | Reducer::handleAction:= %s }",
                    method.getName(),
                    context.getClass().getSimpleName(),
                    context.hashCode(),
                    action));
            List<Reducer> reducers = store.rootReducer.get(action);
            if (reducers == null) {
                reducers = new ArrayList<>();
                store.rootReducer.put(action, reducers);
                Log.e(TAG, "Create reducer list for " + action);
            }
            reducers.add(new Reducer(context, method));
        }
    }

    public static void unregister(Object context) {
        Store store = Store.getInstance();
        for (String key : store.rootReducer.keySet()) {
            for (Reducer reducer : store.rootReducer.get(key)) {
                if (reducer.getContext().equals(context)) {
                    Log.e(TAG, String.format("Unregister %s", reducer.toString()));
                    store.rootReducer.remove(reducer);
                }
            }
        }
    }

    public static void dispatch(Action action) {
        if (action == null) return;
        if (action.getType() == null) throw new RuntimeException("Action " + action.getClass().getSimpleName() + ".getType() returns null");
        try {
            action.getClass().getDeclaredField("TYPE");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Action must have const field 'TYPE'!");
        }
        Store store = Store.getInstance();
        for (Reducer reducer : store.rootReducer.get(action.getType())) {
            Action nextAction = null;
            try {
                Log.d(TAG, String.format("dispatch action::%s@%d with %s", action.getType(), action.hashCode(), reducer.toString()));
                nextAction = reducer.reduce(action);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            store.updateStateTable(action);
            if (nextAction == null) continue;
            if (nextAction.equals(action)) continue;
            dispatch(nextAction);
        }
    }

    public static void debug() {
        Store store = Store.getInstance();
        StringBuilder builder = new StringBuilder();
        builder.append("Store Debug INFO @ ").append(store.hashCode());
        builder.append("\n\n <=== Reducers ===>\n");
        for (String key : store.rootReducer.keySet()) {
            builder.append(" >... Action::type:=").append(key).append(": [\n");
            List<Reducer> reducers = store.rootReducer.get(key);
            if (reducers == null) {
                builder.append(" >...  NULL\n");
            } else {
                for (int i = 0; i < reducers.size(); i++) {
                    Reducer reducer = reducers.get(i);
                    builder.append(String.format(" >...  #%d Reducer::name:=%s { action::type:= %s | invoke::target:= %s } ,\n",
                            i,
                            reducer.getMethod().getName(),
                            reducer.getMethod().getAnnotation(OnReduce.class).value(),
                            reducer.getContext().getClass().getSimpleName()));
                }
            }
            builder.deleteCharAt(builder.length() - 2).append(" >... ]\n");
        }
        builder.append("\n\n <=== Action Table ===>\n");
        for (String key : store.actionTable.keySet()) {
            builder.append(" >... ").append(key).append("::history:= [\n");
            List<String> actions = store.actionTable.get(key);
            if (actions == null) {
                builder.append(" >...  NULL\n");
            } else {
                for (int i = 0; i < actions.size(); i++) {
                    builder.append(String.format(" >...  #%d %s\n",
                            i,actions.get(i)));
                }
            }
            builder.append(" >... ]\n");
        }
        for (String line : builder.toString().split("\n")) {
            Log.e(TAG, line);
        }
    }
}
