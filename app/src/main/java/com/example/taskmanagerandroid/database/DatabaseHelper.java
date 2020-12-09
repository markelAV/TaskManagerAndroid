package com.example.taskmanagerandroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks.db";
    private static final int SCHEMA = 1;
    public static final String TABLE = "tasks";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME_TASK = "name";
    public static final String COLUMN_DATE_TASK = "date";
    public static final String COLUMN_TIME_TASK = "time";
    public static final String COLUMN_DESCRIPTION_TASK = "description";
    public static final String COLUMN_COMPLETE_TASK = "complete";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME_TASK+ " TEXT, "
                + COLUMN_DATE_TASK+ " TEXT, "
                + COLUMN_TIME_TASK+ " TEXT, "
                + COLUMN_DESCRIPTION_TASK+ " TEXT, "
                + COLUMN_COMPLETE_TASK + " INTEGER);");

        //Todo delete here -> this for testing
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME_TASK
                + ", " + COLUMN_DATE_TASK
                + ", " + COLUMN_TIME_TASK
                + ", " + COLUMN_DESCRIPTION_TASK
                + ", " + COLUMN_COMPLETE_TASK+ ") VALUES ('Тествое имя', '09.12.2020','11:30','Тествое описание',0);");

        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_NAME_TASK
                + ", " + COLUMN_DATE_TASK
                + ", " + COLUMN_TIME_TASK
                + ", " + COLUMN_DESCRIPTION_TASK
                + ", " + COLUMN_COMPLETE_TASK+ ") VALUES ('Тествое имя2', '15.12.2020','11:40','Тествое описание',0);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}
