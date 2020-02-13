package io.appium.uiautomator2.handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import io.appium.uiautomator2.common.exceptions.UiAutomator2Exception;
import io.appium.uiautomator2.core.AccessibilityNodeInfoGetter;
import io.appium.uiautomator2.handler.request.SafeRequestHandler;
import io.appium.uiautomator2.http.AppiumResponse;
import io.appium.uiautomator2.http.IHttpRequest;
import io.appium.uiautomator2.model.AndroidElement;
import io.appium.uiautomator2.model.AppiumUIA2Driver;
import io.appium.uiautomator2.model.Session;
import io.appium.uiautomator2.server.WDStatus;
import io.appium.uiautomator2.utils.Logger;

import static io.appium.uiautomator2.utils.Device.getAndroidElement;

/**
 * This method return first visible element inside provided element
 */
public class FirstVisibleView extends SafeRequestHandler {

    public FirstVisibleView(String mappedUri) {
        super(mappedUri);
    }

    @Override
    protected AppiumResponse safeHandle(IHttpRequest request) throws UiObjectNotFoundException,
            JSONException {
        Logger.info("Get first visible element inside provided element");
        String elementId = getElementId(request);
        Session session = AppiumUIA2Driver.getInstance().getSessionOrThrow();

        AndroidElement element = session.getKnownElements().getElementFromCache(elementId);
        if (element == null) {
            return new AppiumResponse(getSessionId(request), WDStatus.NO_SUCH_ELEMENT);
        }
        Object firstObject = null;
        if (element.getUiObject() instanceof UiObject) {
            UiObject uiObject = (UiObject) element.getUiObject();
            Logger.debug("Container for first visible is a uiobject; looping through children");
            for (int i = 0; i < uiObject.getChildCount(); i++) {
                UiObject object = uiObject.getChild(new UiSelector().index(i));
                if (object.exists()) {
                    firstObject = object;
                    break;
                }
            }
        } else {
            UiObject2 uiObject = (UiObject2) element.getUiObject();
            Logger.debug("Container for first visible is a uiobject2; looping through children");
            List<UiObject2> childObjects = uiObject.getChildren();
            if (childObjects.isEmpty()) {
                throw new UiObjectNotFoundException("Could not get children for container object");
            }
            for (UiObject2 childObject : childObjects) {
                try {
                    if (AccessibilityNodeInfoGetter.fromUiObject(childObject) != null) {
                        firstObject = childObject;
                        break;
                    }
                } catch (UiAutomator2Exception ignored) {
                }
            }
        }

        if (firstObject == null) {
            Logger.error("No visible child was found for element");
            return new AppiumResponse(getSessionId(request), WDStatus.NO_SUCH_ELEMENT);
        }

        String id = UUID.randomUUID().toString();
        AndroidElement androidElement = getAndroidElement(id, firstObject, null);
        session.getKnownElements().add(androidElement);
        JSONObject result = new JSONObject();
        result.put("ELEMENT", id);
        return new AppiumResponse(getSessionId(request), WDStatus.SUCCESS, result);
    }
}
