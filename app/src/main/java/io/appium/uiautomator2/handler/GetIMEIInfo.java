/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.appium.uiautomator2.handler;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

import io.appium.uiautomator2.handler.request.SafeRequestHandler;
import io.appium.uiautomator2.http.AppiumResponse;
import io.appium.uiautomator2.http.IHttpRequest;
import io.appium.uiautomator2.server.WDStatus;
import io.appium.uiautomator2.utils.BatteryHelper;
import io.appium.uiautomator2.utils.Logger;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class GetIMEIInfo extends SafeRequestHandler {
    private final Instrumentation mInstrumentation = getInstrumentation();

    public GetIMEIInfo(String mappedUri) {
        super(mappedUri);
    }

    @Override
    protected AppiumResponse safeHandle(IHttpRequest request) throws JSONException {
        Context context=mInstrumentation.getTargetContext();
        //实例化TelephonyManager对象
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //获取IMEI号
        @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
        if(null==imei){
            imei="";
        }
        Logger.info("Get IMEI Info command");

        @SuppressLint("MissingPermission") String imsi=telephonyManager.getSubscriberId();
        if(null==imsi){
            imsi="";
        }

        final JSONObject response = new JSONObject();
        response.put("imei", imei);
        response.put("imsi", imsi);
        return new AppiumResponse(getSessionId(request), WDStatus.SUCCESS, response);
    }
}
