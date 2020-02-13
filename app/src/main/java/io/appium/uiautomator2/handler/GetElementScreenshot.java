package io.appium.uiautomator2.handler;

import android.graphics.Rect;

import androidx.test.uiautomator.UiObjectNotFoundException;
import io.appium.uiautomator2.handler.request.SafeRequestHandler;
import io.appium.uiautomator2.http.AppiumResponse;
import io.appium.uiautomator2.http.IHttpRequest;
import io.appium.uiautomator2.model.AndroidElement;
import io.appium.uiautomator2.model.AppiumUIA2Driver;
import io.appium.uiautomator2.model.Session;
import io.appium.uiautomator2.server.WDStatus;
import io.appium.uiautomator2.utils.Logger;
import io.appium.uiautomator2.utils.ScreenshotHelper;

public class GetElementScreenshot extends SafeRequestHandler {

    public GetElementScreenshot(String mappedUri) {
        super(mappedUri);
    }

    @Override
    protected AppiumResponse safeHandle(IHttpRequest request) throws UiObjectNotFoundException {
        Logger.info("Capture screenshot of an element command");
        String id = getElementId(request);
        Session session = AppiumUIA2Driver.getInstance().getSessionOrThrow();
        AndroidElement element = session.getKnownElements().getElementFromCache(id);
        if (element == null) {
            return new AppiumResponse(getSessionId(request), WDStatus.NO_SUCH_ELEMENT);
        }
        final Rect elementRect = element.getBounds();
        final String result = ScreenshotHelper.takeScreenshot(elementRect);
        return new AppiumResponse(getSessionId(request), WDStatus.SUCCESS, result);
    }
}
