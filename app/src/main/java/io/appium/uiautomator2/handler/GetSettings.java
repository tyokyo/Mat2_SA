package io.appium.uiautomator2.handler;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.VisibleForTesting;
import io.appium.uiautomator2.handler.request.SafeRequestHandler;
import io.appium.uiautomator2.http.AppiumResponse;
import io.appium.uiautomator2.http.IHttpRequest;
import io.appium.uiautomator2.model.settings.ISetting;
import io.appium.uiautomator2.model.settings.Settings;
import io.appium.uiautomator2.server.WDStatus;
import io.appium.uiautomator2.utils.Logger;

/**
 * This method return settings
 */
public class GetSettings extends SafeRequestHandler {

    public GetSettings(String mappedUri) {
        super(mappedUri);
    }

    @Override
    protected AppiumResponse safeHandle(IHttpRequest request) throws JSONException {
        Logger.debug("Get settings:");
        return new AppiumResponse(getSessionId(request), WDStatus.SUCCESS, getPayload());
    }

    @VisibleForTesting
    public JSONObject getPayload() throws JSONException {
        final JSONObject result = new JSONObject();
        for (Settings value : Settings.values()) {
            try {
                ISetting setting = value.getSetting();
                result.put(setting.getName(), setting.getValue());
            } catch (IllegalArgumentException e) {
                Logger.error("No Setting: " + value.toString() + " : " + e);
            }
        }
        return result;
    }
}
