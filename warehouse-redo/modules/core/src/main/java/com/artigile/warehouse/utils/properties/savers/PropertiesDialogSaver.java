/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.properties.savers;

import com.artigile.warehouse.utils.properties.Properties;

import javax.swing.*;
import java.awt.*;

/**
 * @author Valery Barysok, 30.12.2009
 */
public class PropertiesDialogSaver extends Saver {
    private static String LEFT = "left";
    private static String TOP = "top";

    public static void store(JDialog propertiesDialog, String frameId) {
        Point point = propertiesDialog.getLocation();
        Properties.setProperty(getId(frameId, LEFT), "" + point.x);
        Properties.setProperty(getId(frameId, TOP), "" + point.y);
    }

    public static void restore(JDialog propertiesDialog, String frameId) {
        Integer left = Properties.getPropertyAsInteger(getId(frameId, LEFT));
        Integer top = Properties.getPropertyAsInteger(getId(frameId, TOP));
        if (left != null && top != null) {
            propertiesDialog.setLocation(adjustPosition(propertiesDialog, left, top));
        }

        /** /
        Integer dividerLocation = Properties.getPropertyAsInteger(getId(frameId, DIVIDER_LOCATION));
        if (dividerLocation != null) {
            splitPane.setDividerLocation(dividerLocation);
        }
        /**/
    }

    private static Point adjustPosition(JDialog propertiesDialog, Integer left, Integer top) {
        int width = propertiesDialog.getWidth();
        int height = propertiesDialog.getHeight();
        int right = left + width;
        int bottom = top + height;
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        if (right > dimension.width) {
            left = dimension.width-width;
        }
        if (bottom > dimension.height) {
            top = dimension.height-height;
        }
        /* Disabled to support multiple monitors.
        if (left < 0) {
            left = 0;
        }*/
        if (top < 0) {
            top = 0;
        }
        
        return new Point(left, top);
    }
}
