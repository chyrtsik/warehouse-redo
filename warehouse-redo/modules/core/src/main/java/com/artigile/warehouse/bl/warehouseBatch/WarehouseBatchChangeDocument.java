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

import com.artigile.warehouse.domain.chargeoff.ChargeOffItem;
import com.artigile.warehouse.domain.postings.PostingItem;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;

/**
 * Information about document that cuases change of warehouse batch count.
 *
 * @author Aliaksandr.Chyrtsik, 02.10.11
 */
public class WarehouseBatchChangeDocument {
    //Warehouse batch that was changed, created or deleted by this operation (used in movemens and manual count correction).
    private WarehouseBatch changedWarehouseBatch;

    //If set then count changes is caused by posting operation.
    private boolean posting;
    private PostingItem postingItem;

    //If set then count changes is caused by charge off operation.
    private boolean chargeOff;
    private ChargeOffItem chargeOffItem;

    //If set then count change is caused by in warehouse movement (internal ware movement).
    private boolean inWarehouseMovement;

    //If set them count change is caused by manual warehouse batch item count correction.
    private boolean manualCountCorrection;

    private WarehouseBatchChangeDocument(){
    }

    public static WarehouseBatchChangeDocument createPostingDocument(PostingItem postingItem){
        WarehouseBatchChangeDocument document = new WarehouseBatchChangeDocument();
        document.posting = true;
        document.postingItem = postingItem;
        return document;
    }

    public static WarehouseBatchChangeDocument createChargeOffDocument(ChargeOffItem chargeOffItem){
        WarehouseBatchChangeDocument document = new WarehouseBatchChangeDocument();
        document.chargeOff = true;
        document.chargeOffItem = chargeOffItem;
        return document;
    }

    public static WarehouseBatchChangeDocument createInWarehouseMovementDocument(WarehouseBatch changedWarehouseBatch){
        WarehouseBatchChangeDocument document = new WarehouseBatchChangeDocument();
        document.inWarehouseMovement = true;
        document.changedWarehouseBatch = changedWarehouseBatch;
        return document;
    }

    public static WarehouseBatchChangeDocument createManualCountCorrectionDocument(WarehouseBatch changedWarehouseBatch){
        WarehouseBatchChangeDocument document = new WarehouseBatchChangeDocument();
        document.manualCountCorrection = true;
        document.changedWarehouseBatch = changedWarehouseBatch;
        return document;
    }

    public boolean isPosting() {
        return posting;
    }

    public PostingItem getPostingItem() {
        return postingItem;
    }

    public boolean isChargeOff() {
        return chargeOff;
    }

    public ChargeOffItem getChargeOffItem() {
        return chargeOffItem;
    }

    public boolean isInWarehouseMovement() {
        return inWarehouseMovement;
    }

    public WarehouseBatch getChangedWarehouseBatch() {
        return changedWarehouseBatch;
    }

    public boolean isManualCountCorrection() {
        return manualCountCorrection;
    }
}
