package com.example.taskmanagerandroid;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.taskmanagerandroid.adapter.DatabaseAdapter;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @BeforeClass
    public static void init () {
        System.out.println("sl");
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(appContext);
        databaseAdapter.open();
        databaseAdapter.close();
        assertEquals("com.example.taskmanagerandroid", appContext.getPackageName());
    }
}