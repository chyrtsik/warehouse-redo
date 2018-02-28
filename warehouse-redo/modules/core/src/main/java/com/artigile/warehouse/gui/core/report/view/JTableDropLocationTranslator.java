/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.view;

import com.artigile.warehouse.gui.core.report.view.datatransfer.DropLocationTranslator;

import javax.swing.*;

/**
 * @author Shyrik, 07.07.2009
 */

/**
 * <strong>Singleton</strong><br>
 * Translator of the drop location for JTable and derived classes.
 */
public class JTableDropLocationTranslator implements DropLocationTranslator {
    private static JTableDropLocationTranslator instance = null;

    public static DropLocationTranslator getInstance() {
        if (instance == null){
            instance = new JTableDropLocationTranslator();
        }
        return instance;
    }

    @Override
    public int translateLocationToItemIndex(TransferHandler.DropLocation dropLocation) {
        JTable.DropLocation tableDropLocation = (JTable.DropLocation)dropLocation;
        return tableDropLocation.getRow();
    }
}
