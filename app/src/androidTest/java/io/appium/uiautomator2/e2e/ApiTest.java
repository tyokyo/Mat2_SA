package io.appium.uiautomator2.e2e;

import android.content.Context;

import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.io.IOException;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.Configurator;
import io.appium.uiautomator2.model.By;
import io.appium.uiautomator2.server.ServerInstrumentation;
import io.appium.uiautomator2.unittest.test.Config;
import io.appium.uiautomator2.unittest.test.internal.Client;
import io.appium.uiautomator2.unittest.test.internal.Logger;
import io.appium.uiautomator2.unittest.test.internal.NettyStatus;
import io.appium.uiautomator2.unittest.test.internal.TestUtils;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static io.appium.uiautomator2.unittest.test.internal.TestUtils.waitForElement;
import static io.appium.uiautomator2.unittest.test.internal.TestUtils.waitForElementInvisibility;
import static io.appium.uiautomator2.unittest.test.internal.commands.DeviceCommands.createSession;
import static io.appium.uiautomator2.unittest.test.internal.commands.DeviceCommands.deleteSession;
import static io.appium.uiautomator2.unittest.test.internal.commands.ElementCommands.click;
import static io.appium.uiautomator2.utils.Device.getUiDevice;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public abstract class ApiTest {
    protected static ServerInstrumentation serverInstrumentation;
    private static Context ctx;

    /**
     * start io.appium.uiautomator2.server and launch the application main activity
     */
    @BeforeClass
    public static void startServer() throws JSONException, IOException {
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

    @Before
    public void launchAUT() throws JSONException {
        startActivity(Config.APP_NAME);
        waitForElement(By.accessibilityId("Accessibility"));
    }

    public void startActivity(String activity) throws JSONException {
        TestUtils.startActivity(ctx, activity);
    }

    protected void clickAndWaitForStaleness(String elementId) throws JSONException {
        click(elementId);
        waitForElementInvisibility(elementId);
    }
}
