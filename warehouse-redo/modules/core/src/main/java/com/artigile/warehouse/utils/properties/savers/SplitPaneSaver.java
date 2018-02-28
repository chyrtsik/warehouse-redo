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
 * @author Valery Barysok, 30.12.2009
 */
public class SplitPaneSaver extends Saver {
    private static String DIVIDER_LOCATION = "dividerLocation";

    public static void store(JSplitPane splitPane, String frameId) {
        Properties.setProperty(getId(frameId, DIVIDER_LOCATION), "" + splitPane.getDividerLocation());
    }

    public static void restore(JSplitPane splitPane, String frameId) {
        Integer dividerLocation = Properties.getPropertyAsInteger(getId(frameId, DIVIDER_LOCATION));
        if (dividerLocation != null) {
            splitPane.setDividerLocation(dividerLocation);
        }
    }
}
