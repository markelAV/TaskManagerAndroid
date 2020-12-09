package com.example.taskmanagerandroid.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanagerandroid.data.model.Task;
import com.example.taskmanagerandroid.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    private Cursor getAllEntries(){
        String[] columns = new String[] {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_NAME_TASK,
                DatabaseHelper.COLUMN_DATE_TASK, DatabaseHelper.COLUMN_TIME_TASK, DatabaseHelper.COLUMN_DESCRIPTION_TASK, DatabaseHelper.COLUMN_COMPLETE_TASK};
        return  database.query(DatabaseHelper.TABLE, columns, null, null, null, null, null);
    }

    public List<Task> getAllTasks(){
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String nameTask = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_TASK));
                String dateTask = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE_TASK));
                String timeTask = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME_TASK));
                String descriptionTask = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION_TASK));
                boolean complete = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COMPLETE_TASK)) == 1;
                tasks.add(new Task(nameTask, timeTask, dateTask, Integer.toString(id), descriptionTask, complete));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  tasks;
    }

    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE);
    }

    public List<Task> getTaskByDate(String date){
        List<Task> tasks = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE, DatabaseHelper.COLUMN_DATE_TASK);
        Cursor cursor = database.rawQuery(query, new String[]{ date});
        if(cursor.moveToFirst()) {
            tasks = new ArrayList<>();
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String nameTask = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_TASK));
                String dateTask = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE_TASK));
                String timeTask = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME_TASK));
                String descriptionTask = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION_TASK));
                boolean complete = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COMPLETE_TASK)) == 1;
                tasks.add(new Task(nameTask, timeTask, dateTask, Integer.toString(id), descriptionTask, complete));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  tasks;
    }

    public Task getTask(String id){
        Task task = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE, DatabaseHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String nameTask = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME_TASK));
            String dateTask = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE_TASK));
            String timeTask = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME_TASK));
            String descriptionTask = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION_TASK));
            boolean complete = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_COMPLETE_TASK)) == 1;
            task = new Task(nameTask, timeTask, dateTask, id, descriptionTask, complete);
        }
        cursor.close();
        return  task;
    }



    public long insert(Task task){

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME_TASK, task.getName());
        cv.put(DatabaseHelper.COLUMN_DATE_TASK, task.getDate());
        cv.put(DatabaseHelper.COLUMN_TIME_TASK, task.getTime());
        cv.put(DatabaseHelper.COLUMN_DESCRIPTION_TASK, task.getDescription());
        cv.put(DatabaseHelper.COLUMN_COMPLETE_TASK, task.isCompleteV2());

        return  database.insert(DatabaseHelper.TABLE, null, cv);
    }

    public long delete(String taskId){

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{taskId};
        return database.delete(DatabaseHelper.TABLE, whereClause, whereArgs);
    }

    public long update(Task task){

        String whereClause = DatabaseHelper.COLUMN_ID + "=" + task.getId();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_NAME_TASK, task.getName());
        cv.put(DatabaseHelper.COLUMN_DATE_TASK, task.getDate());
        cv.put(DatabaseHelper.COLUMN_TIME_TASK, task.getTime());
        cv.put(DatabaseHelper.COLUMN_DESCRIPTION_TASK, task.getDescription());
        cv.put(DatabaseHelper.COLUMN_COMPLETE_TASK, task.isCompleteV2());
        return database.update(DatabaseHelper.TABLE, cv, whereClause, null);
    }

}
