package com.example.taskmanagerandroid.data.model;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Comparable<Task> {
    private String name;
    private String time;
    private String date;
    private String id;
    private String description;
    private boolean complete;

    public boolean isComplete() {
        return complete;
    }

    public int isCompleteV2() {
        return complete ? 1 : 0;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setComplete(int complete) {
        this.complete = complete == 1;
    }

    public Task(String name, String time, String date, String id, String description, boolean complete) {
        this.name = name;
        this.time = time;
        this.date = date;
        this.id = id;
        this.description = description;
        this.complete = complete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Task(String name, String time, String date, String id, String description) {
        this.name = name;
        this.time = time;
        this.date = date;
        this.id = id;
        this.description = description;
        this.complete = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return complete == task.complete &&
                name.equals(task.name) &&
                time.equals(task.time) &&
                date.equals(task.date) &&
                id.equals(task.id) &&
                description.equals(task.description);
    }

    @Override
    public int compareTo(Task o) {
        String[] time = o.getTime().split(":");
        String[] timeTh = this.getTime().split(":");
        if(time.length == timeTh.length) {
            int time1  = Integer.parseInt(time[0]+time[1]);
            int time2  = Integer.parseInt(timeTh[0]+timeTh[1]);
            return  time2 - time1;
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        DateFormat formatter1 = new SimpleDateFormat("dd.MM.yyyy");
        String date = this.date;
        if (date != null) {
            try {
                Date convertDate = formatter1.parse(date);
                date = formatter1.format(convertDate);
            } catch (ParseException ex) {
                System.out.println("[ERROR] Date not normalized");
                System.out.println(ex.getMessage());
            }
        }
         date = date + " " + time;

        builder.append('{').append("\"id\":").append("\"").append(id).append("\",")
                .append("\"name\":").append("\"").append(name).append("\",")
                .append("\"date\":").append("\"").append(date).append("\",")
                .append("\"complete\":").append(complete).append(",")
                .append("\"description\":").append("\"").append(description).append("\"")
                .append('}');

        return builder.toString();
    }
}
