/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.detail;

/**
 * Filter for loading detail batches.
 * @author Aliaksandr Chyrtsik
 * @since 14.06.13
 */
public class DetailBatchFilter {
    /**
     * Load only detail batches without bar code specified.
     */
    private boolean withoutBarCode;

    public void setWithoutBarCode(boolean withoutBarCode) {
        this.withoutBarCode = withoutBarCode;
    }

    public boolean isWithoutBarCode() {
        return withoutBarCode;
    }
}
