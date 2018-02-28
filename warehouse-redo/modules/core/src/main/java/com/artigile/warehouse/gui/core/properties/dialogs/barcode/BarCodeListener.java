/*
 * Copyright (c) 2007-2012 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.barcode;

/**
 * Listener receiving scanned bar codes.
 *
 * @author Aliaksandr.Chyrtsik, 06.11.12
 */
public interface BarCodeListener {
    /**
     * This method is when new bar code is received from the scanner.
     * @param barCode barcode received.
     */
    void onBarCodeReceived(String barCode);
}
