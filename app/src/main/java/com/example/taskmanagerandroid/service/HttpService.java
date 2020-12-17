package com.example.taskmanagerandroid.service;

import com.example.taskmanagerandroid.data.model.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import java.net.URL;
import java.net.HttpURLConnection;

public class HttpService {
    private final static String PROTOCOL = "http://";
    private final static String HOST = "34.70.221.174";
    private final static String PORT = ":80";
    private final static String TASK_API = "/task-api";
    private final static String SYNCHRONIZE = "/synchronize";
    private final static String TASK = "/task";
    private final static String ALL = "/all";
    private final static String ADD = "/add";
    private final static String EDIT = "/edit";
    private final static String DELETE = "/deleteV2";
    private final static String PATH_TO_SERVER = PROTOCOL + HOST + PORT;
    private final static String PATH_TO_TASK_API = PATH_TO_SERVER + TASK_API;
    private final static String TEST = "/test";


    private String userId;

    public HttpService() {
        //Fixme complete for dynamic userId
        this.userId = "user1";
    }

    public List<Task> getAllTaskUser() {
        ArrayList<Task> tasks = null;
        String response = sendGetRequest(PATH_TO_TASK_API + TASK + "/" + userId + ALL);
        tasks = convertJsonToTasks(response);
        return tasks;
    }

    public boolean addTask(Task task) {
        boolean flagSuccess = false;
        String code = null;
        if (task != null && task.getId() != null) {
            code = sendPostRequest(PATH_TO_TASK_API + TASK + "/" + userId + ADD, addUserToTask(task.toString()));
            if (code != null && code.equals("200")) {
                flagSuccess = true;
            }
        }
        return flagSuccess;
    }

    public int addTasks(List<Task> tasks) {
        int countAddTasks = 0;
        if (tasks != null && tasks.size() > 0) {
            tasks = denormalizeTask((ArrayList<Task>) tasks);
            for (Task task : tasks) {
                countAddTasks = addTask(task) ? countAddTasks + 1 : countAddTasks;
            }
        }
        return countAddTasks;
    }

    public boolean editTask(Task task) {
        boolean flagSuccess = false;
        String code = null;
        if (task != null && task.getId() != null) {
            code = sendPostRequest(PATH_TO_TASK_API + TASK + "/" + userId + EDIT, task.toString());
            if (code != null && code.equals("200")) {
                flagSuccess = true;
            }
        }
        return flagSuccess;
    }

    public int editTasks(List<Task> tasks) {
        int countEditTask = 0;
        if (tasks != null && tasks.size() > 0) {
            tasks = denormalizeTask((ArrayList<Task>) tasks);
            for (Task task : tasks) {
                countEditTask = editTask(task) ? countEditTask + 1 : countEditTask;
            }
        }
        return countEditTask;
    }

    public boolean synchronizeTasks(List<String> taskIds) {
        boolean flagSuccess = false;
        if (taskIds != null && taskIds.size() > 0) {
            String idDelete = null;
            String parameters = "?idTask=";
            for (String taskId :
                    taskIds) {

                idDelete = sendGetRequest(PATH_TO_TASK_API + TASK + "/" + userId + DELETE + parameters + denormalizeTaskId(taskId));
                if (idDelete != null) {
                    flagSuccess = true;
                    System.out.println("[INFO] Deleted task with id " + idDelete);
                } else {
                    flagSuccess = false;
                    System.out.println("[ERROR] Not DELETED");
                }
            }
        }
        return flagSuccess;
    }

    private String sendGetRequest(String path) {
        String result = null;
        if (path != null) {
            System.out.println("[Debug] send to get request on: " + path);
            BufferedReader reader = null;
            try {
                URL url = new URL(path);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(10000);
                c.connect();
                reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder buf = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buf.append(line + "\n");
                }
                System.out.println("[DEBUG] out: " + buf.toString());
                result = buf.toString();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        return result;
    }

    private String sendPostRequest(String path, String body) {
        String result = null;
        if (path != null && body != null) {
            System.out.println("[Debug] send to get request on: " + path);
            try {
                URL url = new URL(path);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("POST");
                c.setReadTimeout(15000);
                c.setConnectTimeout(15000);
                c.setRequestProperty("Content-Type", "application/json; utf-8");
                c.setRequestProperty("Accept", "application/json");
                c.setDoOutput(true);

                try (OutputStream os = c.getOutputStream()) {
                    byte[] input = body.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                int code = c.getResponseCode();
                result = Integer.toString(code);

            } catch (MalformedURLException ex) {
                System.out.println("[ERROR] URL is not correct");
                System.out.println(ex.getMessage());
            } catch (IOException ioException) {
                System.out.println("[ERROR] Not connect");
                System.out.println(ioException.getMessage());
            }
        }

        return null;
    }

    private ArrayList<Task> convertJsonToTasks(String json) {
        ArrayList<Task> result = null;
        if (json != null) {
            try {
                JSONArray jsonArrayTasks = new JSONArray(json);
                if (jsonArrayTasks.length() > 0) {
                    result = new ArrayList<>();
                    JSONObject jsonTask;
                    Task task = null;
                    String id;
                    String name;
                    String time = null;
                    String date = null;
                    String description;
                    boolean complete;
                    for (int i = 0; i < jsonArrayTasks.length(); i++) {
                        jsonTask = jsonArrayTasks.getJSONObject(i);
                        id = normalizeTaskId(jsonTask.getString("id"));
                        name = jsonTask.getString("name");
                        description = jsonTask.getString("description");
                        String[] convertDate = convertDate(jsonTask.getString("date"));
                        date = convertDate != null ? convertDate[0] : null;
                        time = convertDate != null ? convertDate[1] : null;
                        complete = jsonTask.getBoolean("complete");
                        task = new Task(name, time, date, id, description, complete);
                        result.add(task);
                    }
                }
            } catch (JSONException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return result;
    }

    private String normalizeTaskId(String idTask) {
        String result = null;
        if (idTask != null) {
            String[] partsIds = idTask.split("_");
            if (partsIds != null && partsIds.length > 1) {
                result = partsIds[partsIds.length - 1];
                System.out.println("[Debug] convert idbd " + idTask + " to " + result);
            }
        }
        return result;
    }

    private String[] convertDate(String dateConvert) {
        String[] result = null;
        if (dateConvert != null) {
            String[] date = dateConvert.split(" ");
            if (date != null && date.length > 1) {
                result = date;
            }
        }
        return result;
    }

    private ArrayList<Task> denormalizeTask(ArrayList<Task> tasks) {
        for (Task task : tasks) {
            task.setId(denormalizeTaskId(task.getId()));
        }
        return tasks;
    }

    private String denormalizeTaskId(String taskId) {
        return "task" + this.userId + "_" + taskId;

    }

    private String addUserToTask(String task) {
        StringBuilder builder = new StringBuilder(task);
        String userInfo = ",\"user\":{ \"id\":\"" + this.userId + "\"}";
        builder.insert(task.length() - 1, userInfo);
        return builder.toString();
    }

    public boolean testRequest() {
        System.out.println("testRequest");
        boolean flagSuccess = false;
        BufferedReader reader = null;
        try {
            String path = "http://34.70.221.174:80/task-api/task/user1/all";
            URL url = new URL(path);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setReadTimeout(10000);
            c.connect();
            reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buf.append(line + "\n");
            }
            System.out.println("[DEBUG] out: " + buf.toString());
            if (line != null) {
                flagSuccess = true;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return flagSuccess;
    }


    //
//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
   //             / _||||| -:- |||||- \
   //            |   | \\\  -  /// |   |
   //            | \_|  ''\---/''  |_/ |
   //            \  .-\__  '-'  ___/-. /
   //          ___'. .'  /--.--\  `. .'___
   //       ."" '<  `.___\_<|>_/___.' >' "".
   //      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
   //      \  \ `_.   \_ __\ /__ _/   .-` /  /
   //  =====`-.____`.___ \_____/___.-`___.-'=====
   //                       `=---='
   //
   //
   //  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  //
  //            God Bless         No Bugs
  //
}
