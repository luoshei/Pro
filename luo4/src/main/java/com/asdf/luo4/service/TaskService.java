package com.asdf.luo4.service;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by asdf on 2017/4/21.
 */

public class TaskService  {
    private ExecutorService service = Executors.newCachedThreadPool();
    private HashMap<Integer, Runnable> tasks = new HashMap<>();

    private static TaskService instance;

    private TaskService() {}

    private static TaskService getInstance() {
        if (TaskService.instance == null) {
            synchronized (TaskService.class) {
                if (TaskService.instance == null) {
                    TaskService.instance = new TaskService();
                }
            }
        }
        return TaskService.instance;
    }

    public static TaskService init() {
        return TaskService.getInstance();
    }

    public static void exec(Runnable task) {
        TaskService taskService = TaskService.getInstance();
        taskService.service.execute(task);
    }

    public static void exec(int id, Runnable task) {
        TaskService taskService = TaskService.getInstance();
        taskService.tasks.put(id, task);
        taskService.service.execute(task);
    }

    public static Runnable getTask(int id) {
        TaskService taskService = TaskService.getInstance();
        return taskService.tasks.get(id);
    }

    public static void remove(int id){
        TaskService taskService = TaskService.getInstance();
        if(taskService.tasks.containsKey(id)) {
            taskService.tasks.remove(id);
        }
    }

    public static <T> void getTask(int id, T obj) {
        try {
            obj = (T) TaskService.getTask(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
