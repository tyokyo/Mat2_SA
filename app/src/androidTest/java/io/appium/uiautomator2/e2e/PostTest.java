package io.appium.uiautomator2.e2e;

import org.apache.commons.lang.SystemUtils;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.appium.uiautomator2.unittest.test.internal.BaseTest;

public class PostTest extends BaseTest {

    //@Test
    public void testA(){
        try {
            TimeUnit.MINUTES.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
