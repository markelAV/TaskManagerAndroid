package com.example.taskmanagerandroid;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.taskmanagerandroid.adapter.DatabaseAdapter;
import com.example.taskmanagerandroid.data.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DataModelTests {
    private DatabaseAdapter databaseAdapter;
    private Task testTask;

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.taskmanagerandroid", appContext.getPackageName());

        databaseAdapter = new DatabaseAdapter(appContext);
        databaseAdapter.open();

        testTask = new Task("__NameTask2534", "11:30", "15.12.20", null, "TestDescription", false);
        addTask();
    }

    @After
    public void finalize() {
        databaseAdapter.delete(testTask.getId());
        Task search = databaseAdapter.getTask(testTask.getId());
        databaseAdapter.close();
    }

    @Test
    public void testEditTask() {
        testTask.setTime("20:30");
        databaseAdapter.update(testTask);
        Task search = databaseAdapter.getTask(testTask.getId());
        assertEquals(search, testTask);
    }


    private void addTask() {
        databaseAdapter.insert(testTask);
        ArrayList<Task> searchTasks = new ArrayList<>();
        ArrayList<Task> tasks = (ArrayList<Task>) databaseAdapter.getTaskByDate(testTask.getDate());
        Task searchTask;
        for (int i = 0; i < tasks.size(); i++) {
            searchTask = tasks.get(i);
            if (searchTask.getName().equals(testTask.getName()) &&
                    searchTask.getTime().equals(testTask.getTime()) &&
                    searchTask.getDescription().equals(testTask.getDescription()) &&
                    searchTask.isComplete() == testTask.isComplete()) {
                searchTasks.add(tasks.get(i));
            }
        }

        if (searchTasks.size() == 1) {
            testTask.setId(searchTasks.get(0).getId());
        } else {
            System.out.println("size tasks is " + searchTasks.size());

        }
    }
}
