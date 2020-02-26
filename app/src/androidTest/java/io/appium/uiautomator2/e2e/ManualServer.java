package io.appium.uiautomator2.e2e;

import android.content.Context;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.Configurator;
import io.appium.uiautomator2.server.ServerInstrumentation;
import io.appium.uiautomator2.unittest.test.internal.Client;
import io.appium.uiautomator2.unittest.test.internal.Logger;
import io.appium.uiautomator2.unittest.test.internal.NettyStatus;
import io.appium.uiautomator2.unittest.test.internal.TestUtils;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static io.appium.uiautomator2.unittest.test.internal.commands.DeviceCommands.createSession;
import static io.appium.uiautomator2.utils.Device.getUiDevice;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class ManualServer {
    protected static ServerInstrumentation serverInstrumentation;
    private static Context ctx;
    //@Test
    public void startServer() throws JSONException, IOException, InterruptedException {
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
        TestUtils.grantPermission(getApplicationContext(), READ_PHONE_STATE);

        TimeUnit.MINUTES.sleep(9000);
    }
}
