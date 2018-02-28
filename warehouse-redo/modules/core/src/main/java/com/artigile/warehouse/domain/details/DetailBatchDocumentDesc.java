/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.details;

import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.movement.Movement;
import com.artigile.warehouse.domain.orders.Order;
import com.artigile.warehouse.domain.postings.Posting;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.util.Date;

/**
 * Helper class to describe document of detail batch operation.
 * @author Aliaksandr Chyrtsik
 * @since 13.05.13
 */
public abstract class DetailBatchDocumentDesc {
    public abstract Long getNumber(DetailBatchOperation operation);
    public abstract String getName(DetailBatchOperation operation);
    public abstract Date getDate(DetailBatchOperation operation);
    public abstract String getContractorName(DetailBatchOperation operation);

    private final static OrderDocumentDesc ORDER_DOCUMENT_DESC = new OrderDocumentDesc();
    private final static MovementDocumentDesc MOVEMENT_DOCUMENT_DESC = new MovementDocumentDesc();
    private final static InventorizationDocumentDesc INVENTORIZATION_DOCUMENT_DESC = new InventorizationDocumentDesc();
    private final static PostingDocumentDesc POSTING_DOCUMENT_DESC = new PostingDocumentDesc();
    private final static InternalMovementDocumentDesc INTERNAL_MOVEMENT_DOCUMENT_DESC = new InternalMovementDocumentDesc();
    private final static ManualCorrectionDocumentDesc MANUAL_CORRECTION_DOCUMENT_DESC = new ManualCorrectionDocumentDesc();

    /**
     * Factory method returning implementation suitable for given detail batch operation.
     * @param operation operation to find document implementation.
     * @return document implementation for this operation.
     */
    public static DetailBatchDocumentDesc getInstance(DetailBatchOperation operation) {
        DetailBatchOperationType operationType = operation.getOperationType();
        if (DetailBatchOperationType.CHARGE_OFF.equals(operationType)){
            ComplectingTask complectingTask = operation.getChargeOffItem().getComplectingTask();
            if (complectingTask != null && complectingTask.getOrderSubItem() != null){
                //Order shipment to customer.
                return ORDER_DOCUMENT_DESC;
            }
            else if (complectingTask != null && complectingTask.getMovementItem() != null){
                //Movement between warehouses.
                return MOVEMENT_DOCUMENT_DESC;
            }
            else{
                //Inventorization.
                return INVENTORIZATION_DOCUMENT_DESC;
            }
        }
        else if (DetailBatchOperationType.POSTING.equals(operationType)){
            if (operation.getPostingItem().getPosting().getDeliveryNote() != null){
                //Movement between warehouses.
                return MOVEMENT_DOCUMENT_DESC;
            }
            else if (operation.getPostingItem().getPosting().getInventorization() != null){
                //Inventorizazation resulted in addition of new intems in stock.
                return INVENTORIZATION_DOCUMENT_DESC;
            }
            else{
                //Goods receipt from supplier.
                return POSTING_DOCUMENT_DESC;
            }
        }
        else if (DetailBatchOperationType.MOVEMENT.equals(operationType)){
            //Internal warehouse movement.
            return INTERNAL_MOVEMENT_DOCUMENT_DESC;
        }
        else if (DetailBatchOperationType.MANUAL_CORRECTION.equals(operationType)){
            //Manual count correction.
            return MANUAL_CORRECTION_DOCUMENT_DESC;
        }
        else{
            LoggingFacade.logError("DetailBatchDocumentDesc.getInstance: Not supported detail history operation type. Id = " + operation.getId());
            throw new AssertionError("Not supported detail history operation type. Id = " + operation.getId());
        }
    }

    private static class OrderDocumentDesc extends DetailBatchDocumentDesc{
        @Override
        public Long getNumber(DetailBatchOperation operation) {
            return getOrder(operation).getNumber();
        }

        @Override
        public String getName(DetailBatchOperation operation) {
            return I18nSupport.message("detail.batch.history.document.order");
        }

        @Override
        public Date getDate(DetailBatchOperation operation) {
            return getOrder(operation).getCreateDate();
        }

        @Override
        public String getContractorName(DetailBatchOperation operation) {
            return getOrder(operation).getContractor().getName();
        }

        private Order getOrder(DetailBatchOperation operation) {
            return operation.getChargeOffItem().getComplectingTask().getOrderSubItem().getOrderItem().getOrder();
        }
    }

    private static class MovementDocumentDesc extends DetailBatchDocumentDesc{
        @Override
        public Long getNumber(DetailBatchOperation operation) {
            if (operation.getChargeOffItem() != null){
                //This is a shipment from warehouse.
                return operation.getChargeOffItem().getComplectingTask().getMovementItem().getMovement().getNumber();
            }
            else if (operation.getPostingItem() != null){
                //This is a receipt of wares from another warehouse.
                return getPostingMovement(operation).getNumber();
            }
            else{
                LoggingFacade.logError(this, "MovementDocumentDesc.getNumber: Not supported type of movement document. Id =" + operation.getId());
                return null;
            }
        }

        @Override
        public String getName(DetailBatchOperation operation) {
            return I18nSupport.message("detail.batch.history.document.movement");
        }

        @Override
        public Date getDate(DetailBatchOperation operation) {
            if (operation.getChargeOffItem() != null){
                //This is a shipment from warehouse.
                return operation.getChargeOffItem().getComplectingTask().getMovementItem().getMovement().getCreateDate();
            }
            else if (operation.getPostingItem() != null){
                //This is a receipt of wares from another warehouse.
                return getPostingMovement(operation).getCreateDate();
            }
            else{
                LoggingFacade.logError(this, "MovementDocumentDesc.getNumber: Not supported type of movement document. Id =" + operation.getId());
                return null;
            }
        }

        private Movement getPostingMovement(DetailBatchOperation operation) {
            return operation.getPostingItem().getPosting().getDeliveryNote().getChargeOff().getMovement();
        }

        @Override
        public String getContractorName(DetailBatchOperation operation) {
            if (operation.getChargeOffItem() != null){
                //This is a shipment from warehouse.
                return operation.getChargeOffItem().getComplectingTask().getMovementItem().getMovement().getToWarehouse().getName();
            }
            else if (operation.getPostingItem() != null){
                //This is a receipt of wares from another warehouse.
                return operation.getPostingItem().getPosting().getDeliveryNote().getChargeOff().getWarehouse().getName();
            }
            else{
                LoggingFacade.logError(this, "MovementDocumentDesc.getNumber: Not supported type of movement document. Id =" + operation.getId());
                return null;
            }
        }
    }

    private static class InventorizationDocumentDesc extends DetailBatchDocumentDesc{
        @Override
        public Long getNumber(DetailBatchOperation operation) {
            if (operation.getChargeOffItem() != null){
                return operation.getChargeOffItem().getChargeOff().getInventorization().getNumber();
            }
            else if (operation.getPostingItem() != null){
                return operation.getPostingItem().getPosting().getInventorization().getNumber();
            }
            else{
                LoggingFacade.logError(this, "InventorizationDocumentDesc.getNumber: Not supported type of movement document. Id =" + operation.getId());
                return null;
            }
        }

        @Override
        public String getName(DetailBatchOperation operation) {
            return I18nSupport.message("detail.batch.history.document.inventorization");
        }

        @Override
        public Date getDate(DetailBatchOperation operation) {
            if (operation.getChargeOffItem() != null){
                return operation.getChargeOffItem().getChargeOff().getInventorization().getCreateDate();
            }
            else if (operation.getPostingItem() != null){
                return operation.getPostingItem().getPosting().getInventorization().getCreateDate();
            }
            else{
                LoggingFacade.logError(this, "InventorizationDocumentDesc.getDate: Not supported type of movement document. Id =" + operation.getId());
                return null;
            }
        }

        @Override
        public String getContractorName(DetailBatchOperation operation) {
            return null;
        }
    }

    private static class PostingDocumentDesc extends DetailBatchDocumentDesc{
        @Override
        public Long getNumber(DetailBatchOperation operation) {
            return getPosting(operation).getNumber();
        }

        @Override
        public String getName(DetailBatchOperation operation) {
            return I18nSupport.message("detail.batch.history.document.posting");
        }

        @Override
        public Date getDate(DetailBatchOperation operation) {
            return getPosting(operation).getCreateDate();
        }

        @Override
        public String getContractorName(DetailBatchOperation operation) {
            Contractor contractor = getPosting(operation).getContractor();
            return contractor == null ? null : contractor.getName();
        }

        private Posting getPosting(DetailBatchOperation operation) {
            return operation.getPostingItem().getPosting();
        }
    }

    private static class InternalMovementDocumentDesc extends DetailBatchDocumentDesc{
        @Override
        public Long getNumber(DetailBatchOperation operation) {
            return null;
        }

        @Override
        public String getName(DetailBatchOperation operation) {
            return I18nSupport.message("detail.batch.history.document.movementInsideWarehouse");
        }

        @Override
        public Date getDate(DetailBatchOperation operation) {
            return operation.getOperationDateTime();
        }

        @Override
        public String getContractorName(DetailBatchOperation operation) {
            return null;
        }
    }

    private static class ManualCorrectionDocumentDesc extends DetailBatchDocumentDesc{
        @Override
        public Long getNumber(DetailBatchOperation operation) {
            return null;
        }

        @Override
        public String getName(DetailBatchOperation operation) {
            return I18nSupport.message("detail.batch.history.document.manualCorrection");
        }

        @Override
        public Date getDate(DetailBatchOperation operation) {
            return operation.getOperationDateTime();
        }

        @Override
        public String getContractorName(DetailBatchOperation operation) {
            return null;
        }
    }
}
