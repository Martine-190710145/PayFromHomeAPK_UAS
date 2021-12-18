package com.example.payfromhome.view;

import static org.junit.Assert.*;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.payfromhome.R;
import com.github.javafaker.Faker;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import java.util.UUID;

@RunWith(AndroidJUnit4ClassRunner.class)
public class RegisterActivityTest {
    public String name, email, password;

    @Before
    public void setup(){
        Faker faker = new Faker();
        name = faker.name().fullName();
        email = faker.name().firstName()+"@gmail.com";
        password = UUID.randomUUID().toString();
        ActivityScenario.launch(RegisterActivity.class);
    }

    @Test
    public void register() throws InterruptedException {
        onView(withId(R.id.edit_name)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.edit_username)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.edit_password)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.button_register)).check(matches(isDisplayed()));
        onView(withId(R.id.button_register)).perform(click());
        Intents.init();
        Thread.sleep(5000);
        intended(hasComponent(LoginActivity.class.getName()));
        Intents.release();
    }
}