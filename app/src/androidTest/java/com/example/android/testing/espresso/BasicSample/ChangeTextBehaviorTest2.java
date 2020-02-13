/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.testing.espresso.BasicSample;

import android.app.Activity;
import android.content.Context;

import org.json.JSONException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.Configurator;
import io.appium.uiautomator2.server.ServerInstrumentation;
import io.appium.uiautomator2.unittest.test.internal.Client;
import io.appium.uiautomator2.unittest.test.internal.Logger;
import io.appium.uiautomator2.unittest.test.internal.NettyStatus;
import io.appium.uiautomator2.unittest.test.internal.TestUtils;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static io.appium.uiautomator2.unittest.test.internal.commands.DeviceCommands.createSession;
import static io.appium.uiautomator2.unittest.test.internal.commands.DeviceCommands.deleteSession;
import static io.appium.uiautomator2.utils.Device.getUiDevice;
import static org.junit.Assert.assertNotNull;


/**
 * Basic tests showcasing simple view matchers and actions like {@link ViewMatchers#withId},
 * {@link ViewActions#click} and {@link ViewActions#typeText}.
 * <p>
 * Note that there is no need to tell Espresso that a view is in a different {@link Activity}.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChangeTextBehaviorTest2 {

    protected static ServerInstrumentation serverInstrumentation;
    private static Context ctx;


    public static final String STRING_TO_BE_TYPED = "Espresso";


    @AfterClass
    public static void stopSever() {
        deleteSession();
        if (serverInstrumentation == null) {
            return;
        }
        serverInstrumentation.stopServer();
        Client.waitForNettyStatus(NettyStatus.OFFLINE);
        serverInstrumentation = null;
    }
    @BeforeClass
    public static void startServer() throws JSONException, IOException {
        ActivityScenario.launch(MainActivity.class);

        if (serverInstrumentation != null) {
            return;
        }
        assertNotNull(getUiDevice());
        ctx = InstrumentationRegistry.getInstrumentation().getContext();
        serverInstrumentation = ServerInstrumentation.getInstance();
        Logger.info("Starting Server");
        serverInstrumentation.startServer();
        Client.waitForNettyStatus(NettyStatus.ONLINE);
        createSession();
        Configurator.getInstance().setWaitForSelectorTimeout(0);
        Configurator.getInstance().setWaitForIdleTimeout(50000);
        TestUtils.grantPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        TestUtils.grantPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
    }
    public void launchActivity() throws JSONException, IOException {
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void changeText_sameActivity() {

        // Type text and then press the button.
        onView(withId(R.id.editTextUserInput))
                .perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
        onView(withId(R.id.changeTextBt)).perform(click());

        // Check that the text was changed.
        //onView(withId(R.id.textToBeChanged)).check(matches(withText(STRING_TO_BE_TYPED)));
    }

    @Test
    public void changeText_newActivity() {
        // Type text and then press the button.
        onView(withId(R.id.editTextUserInput)).perform(typeText(STRING_TO_BE_TYPED),
                closeSoftKeyboard());
        onView(withId(R.id.activityChangeTextBtn)).perform(click());

        // This view is in a different Activity, no need to tell Espresso.
        onView(withId(R.id.show_text_view)).check(matches(withText(STRING_TO_BE_TYPED)));
    }
}