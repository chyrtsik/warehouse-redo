/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.chargeoff;

/**
 * @author Shyrik, 10.03.2010
 */

/**
 * Class for holding temporary data of shipping from warehouse operation.
 */
public class ShippingFromWarehouseContext {
    /**
     * Next number, that should be used for new charge off document.
     */
    private long nextChargeOffNumber;

    /**
     * Next number, that should be used for new delivery note document.
     */
    private long nextDeliveryNoteNumber;

    //================================= Getters and setters =======================================
    public long getNextChargeOffNumber() {
        return nextChargeOffNumber;
    }

    public void setNextChargeOffNumber(long nextChargeOffNumber) {
        this.nextChargeOffNumber = nextChargeOffNumber;
    }

    public long getNextDeliveryNoteNumber() {
        return nextDeliveryNoteNumber;
    }

    public void setNextDeliveryNoteNumber(long nextDeliveryNoteNumber) {
        this.nextDeliveryNoteNumber = nextDeliveryNoteNumber;
    }
}
