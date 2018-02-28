/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.view.datatransfer;

import javax.swing.*;

/**
 * @author Shyrik, 07.07.2009
 */
public interface DropLocationTranslator {
    /**
     * Translates drop location to the item's index.
     * @param dropLocation - drop location to be translated.
     * @return
     */
    public int translateLocationToItemIndex(TransferHandler.DropLocation dropLocation);
}
