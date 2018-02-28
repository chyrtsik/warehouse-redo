/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.properties.savers;

import com.artigile.warehouse.utils.properties.Properties;

import javax.swing.*;

/**
 * @author Valery Barysok, 10.04.2010
 */
public class SplitPaneOrietationSaver extends Saver {
    private static String SPLITPANE_ORIENTATION = "splitPaneOrientation";
    public static int DEFAULT_ORIENTATION = -1;

    public static void store(int splitPaneOrientation, Class clazz) {
        Properties.setProperty(getId(clazz, SPLITPANE_ORIENTATION), "" + splitPaneOrientation);
    }

    public static int getOrientation(Class clazz) {
        Integer splitPaneOrientation = Properties.getPropertyAsInteger(getId(clazz, SPLITPANE_ORIENTATION));
        if (splitPaneOrientation != null) {
            return splitPaneOrientation;
        }

        return DEFAULT_ORIENTATION;
    }

    public static void restore(JSplitPane splitPane, Class clazz) {
        int orientation = getOrientation(clazz);
        if (orientation != DEFAULT_ORIENTATION) {
            splitPane.setOrientation(orientation);
        }
    }
}
