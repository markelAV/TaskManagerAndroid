package com.example.taskmanagerandroid;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITests {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
   public void checkLabelDateToday() {
       onView(withId(R.id.id_label_today)).check(matches(withText(R.string.text_today)));
   }

    @Test
    public void checkDateToday() {
        String date = new Date().toString();
        String[] datesForView = date.split(" ");
        String dateForView = datesForView[0] + " " + datesForView[1] + " " + datesForView[2];
        onView(withId(R.id.text_date_today)).check(matches(withText(dateForView)));
    }


}
