package com.example.taskmanagerandroid.service;

import android.content.Context;

import com.example.taskmanagerandroid.adapter.DatabaseAdapter;
import com.example.taskmanagerandroid.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskSynchronizeService {

    private HttpService httpService;
    private DatabaseAdapter databaseAdapter;

    public TaskSynchronizeService(Context context) {
        this.httpService = new HttpService();
        this.databaseAdapter = new DatabaseAdapter(context);
    }

    public int updateTasksFromServer() {
        int tasks = 0;
        List<Task> tasksFromServer = httpService.getAllTaskUser();
        databaseAdapter.open();
        List<Task> tasksFromDB = databaseAdapter.getAllTasks();
        ArrayList<String> exitIds = getIdsFromListTasks(tasksFromDB);
        if (exitIds != null && exitIds.size() > 0) {
            tasks = tasksFromServer.size();
            for (Task task : tasksFromServer) {
                if (task.getId() != null) {
                    if (!exitIds.contains(task.getId())) {
                        databaseAdapter.insert(task);
                    } else {
                        databaseAdapter.update(task);
                    }
                }
            }
        }

        databaseAdapter.close();

        return tasks;
    }

    public boolean synchronizeTasks() {
        List<Task> tasksFromServer = httpService.getAllTaskUser();
        databaseAdapter.open();
        List<Task> tasksFromDB = databaseAdapter.getAllTasks();
        if (tasksFromServer != null && tasksFromDB != null) {
            ArrayList<Task> taskForAdd = new ArrayList<>();
            ArrayList<Task> taskForUpdate = new ArrayList<>();
            ArrayList<String> idsTaskForDelete = getIdsFromListTasks(tasksFromServer);
            for (Task task : tasksFromDB) {
                if (idsTaskForDelete.contains(task.getId())) {
                    taskForUpdate.add(task);
                    idsTaskForDelete.remove(task.getId());
                } else {
                    taskForAdd.add(task);
                    idsTaskForDelete.remove(task.getId());
                }
            }
            int add = httpService.addTasks(taskForAdd);
            int edit = httpService.editTasks(taskForUpdate);
            boolean sync = httpService.synchronizeTasks(idsTaskForDelete);
            if (add > 0 || edit > 0 || sync) {
                databaseAdapter.close();
                return true;
            }
        }

        databaseAdapter.close();
        return false;
    }

    public boolean testConnection() {
        return httpService.testRequest();
    }

    private ArrayList<String> getIdsFromListTasks(List<Task> tasks) {
        ArrayList<String> ids = new ArrayList<>();
        if (tasks != null && tasks.size() > 0) {
            for (Task task : tasks) {
                if (task.getId() != null) {
                    ids.add(task.getId());
                }
            }
        }
        return ids;
    }
}
