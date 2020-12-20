package com.example.taskmanagerandroid;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.taskmanagerandroid.adapter.DatabaseAdapter;
import com.example.taskmanagerandroid.data.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntegrationTests {

    private DatabaseAdapter databaseAdapter;
    private Task testTask;

    @Rule
    public ActivityScenarioRule<CreateEditTask> activityRule
            = new ActivityScenarioRule<>(CreateEditTask.class);

    @Before
    public void setUp() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.taskmanagerandroid", appContext.getPackageName());

        databaseAdapter = new DatabaseAdapter(appContext);
        databaseAdapter.open();
        testTask = new Task("Task AutoTest E2E", "04:20", "20.12.2020", null, "TestDescription", false);

    }

    @After
    public void finalize() {
        findIdTask();
        databaseAdapter.delete(testTask.getId());
        databaseAdapter.close();
    }

    @Test
    public void checkCreateTask() {
        List<Task> tasksBeforeAdd = databaseAdapter.getTaskByDate(testTask.getDate()); // get all tasks on 20.12.20

        onView(withId(R.id.edit_text_task_name)).perform(typeText(testTask.getName()));
        onView(withId(R.id.edit_text_date)).perform(typeText(testTask.getDate()));
        onView(withId(R.id.edit_text_time)).perform(typeText(testTask.getTime()));
        onView(withId(R.id.editTextTextMultiLine)).perform(typeText(testTask.getDescription()));
        onView(isRoot()).perform(closeSoftKeyboard());
        onView(withId(R.id.id_button_save)).perform(click());

        List<Task> tasksAfterAdd  = databaseAdapter.getTaskByDate(testTask.getDate());

        assertEquals(tasksAfterAdd.size() - tasksBeforeAdd.size(), 1);
    }

    private void findIdTask() {
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
