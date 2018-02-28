/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.warehouseBatch;

import com.artigile.warehouse.domain.warehouse.WarehouseBatch;

/**
 * Holds information about warehouse batch count changing.
 *
 * @author Aliaksandr.Chyrtsik, 02.10.11
 */
public class WarehouseBatchCountChangeEvent {
    //Information about warehouse batch and it's count.
    private WarehouseBatch finalWarehouseBatch;
    private long initialCount;
    private long countDelta;

    //Information about reason of warehouse batch count changing.
    private WarehouseBatchChangeDocument document;

    public WarehouseBatchCountChangeEvent(WarehouseBatch finalWarehouseBatch, long initialCount, long countDelta, WarehouseBatchChangeDocument document) {
        this.finalWarehouseBatch = finalWarehouseBatch;
        this.initialCount = initialCount;
        this.countDelta = countDelta;
        this.document = document;
    }

    public WarehouseBatch getFinalWarehouseBatch() {
        return finalWarehouseBatch;
    }

    public long getInitialCount() {
        return initialCount;
    }

    public long getCountDelta() {
        return countDelta;
    }

    public WarehouseBatchChangeDocument getDocument() {
        return document;
    }
}
