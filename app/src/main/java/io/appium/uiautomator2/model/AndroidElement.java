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

package io.appium.uiautomator2.model;

import android.graphics.Rect;

import java.util.List;

import androidx.test.uiautomator.UiObjectNotFoundException;
import io.appium.uiautomator2.common.exceptions.InvalidCoordinatesException;
import io.appium.uiautomator2.common.exceptions.InvalidSelectorException;
import io.appium.uiautomator2.common.exceptions.NoAttributeFoundException;
import io.appium.uiautomator2.utils.Point;

public interface AndroidElement {

    By getBy();

    void clear() throws UiObjectNotFoundException;

    void click() throws UiObjectNotFoundException;

    boolean longClick() throws UiObjectNotFoundException;

    String getText() throws UiObjectNotFoundException;

    String getName() throws UiObjectNotFoundException;

    String getAttribute(String attr) throws UiObjectNotFoundException, NoAttributeFoundException;

    boolean setText(final String text);

    String getId();

    Rect getBounds() throws UiObjectNotFoundException;

    Object getChild(final Object sel) throws UiObjectNotFoundException,
            InvalidSelectorException, ClassNotFoundException;

    List<Object> getChildren(final Object selector, final By by)
            throws UiObjectNotFoundException, InvalidSelectorException, ClassNotFoundException;

    String getContentDesc() throws UiObjectNotFoundException;

    Object getUiObject();

    Point getAbsolutePosition(final Point point)
            throws UiObjectNotFoundException, InvalidCoordinatesException;

    boolean dragTo(final int destX, final int destY, final int steps)
            throws UiObjectNotFoundException, InvalidCoordinatesException;

    boolean dragTo(final Object destObj, final int steps)
            throws UiObjectNotFoundException, InvalidCoordinatesException;
}
