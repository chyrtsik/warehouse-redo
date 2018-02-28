/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.deliveryNote.DeliveryNote;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteItem;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteItemTO;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTO;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTOForReport;
import com.artigile.warehouse.utils.dto.postings.PostingTOForReport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 01.11.2009
 */
public final class DeliveryNoteTransformer {
    private DeliveryNoteTransformer(){
    }

    public static DeliveryNoteTOForReport transformForReport(DeliveryNote deliveryNote) {
        return transformForReport(deliveryNote, null);
    }

    public static DeliveryNoteTOForReport transformForReport(DeliveryNote deliveryNote, PostingTOForReport postingTO) {
        if ( deliveryNote == null ){
            return null;
        }
        DeliveryNoteTOForReport deliveryNoteTO = new DeliveryNoteTOForReport();
        update(deliveryNoteTO, deliveryNote, postingTO);
        return deliveryNoteTO;
    }

    public static List<DeliveryNoteTOForReport> listForReport(List<DeliveryNote> deliveryNoteList) {
        List<DeliveryNoteTOForReport> listForReport = new ArrayList<DeliveryNoteTOForReport>();
        for (DeliveryNote deliveryNote : deliveryNoteList){
            listForReport.add(transformForReport(deliveryNote));
        }
        return listForReport;
    }

    public static DeliveryNoteTO transform(DeliveryNote deliveryNote) {
        if (deliveryNote == null){
            return null;
        }
        DeliveryNoteTO deliveryNoteTO = new DeliveryNoteTO();
        update(deliveryNoteTO, deliveryNote);
        deliveryNoteTO.setItems(transformItemsList(deliveryNote.getItems()));
        return deliveryNoteTO;
    }

    public static DeliveryNote transform(DeliveryNoteTOForReport deliveryNoteTO) {
        if (deliveryNoteTO == null){
            return null;
        }
        return SpringServiceContext.getInstance().getDeliveryNoteService().getDeliveryNoteById(deliveryNoteTO.getId());
    }

    private static void update(DeliveryNoteTOForReport deliveryNoteTO, DeliveryNote deliveryNote, PostingTOForReport postingTO) {
        deliveryNoteTO.setId(deliveryNote.getId());
        deliveryNoteTO.setState(deliveryNote.getState());
        deliveryNoteTO.setNumber(deliveryNote.getNumber());
        deliveryNoteTO.setDocumentDate(deliveryNote.getDocumentDate());
        deliveryNoteTO.setDocumentName(deliveryNote.getDocumentName());
        deliveryNoteTO.setDocumentNumber(deliveryNote.getDocumentNumber());
        deliveryNoteTO.setChargeOff(ChargeOffTransformer.transformForReport(deliveryNote.getChargeOff()));
        deliveryNoteTO.setDestinationWarehouse(WarehouseTransformer.transformForReport(deliveryNote.getDestinationWarehouse()));
        deliveryNoteTO.setContractor(ContractorTransformer.transformContractor(deliveryNote.getContractor()));
        deliveryNoteTO.setPosting(postingTO == null ? PostingsTransformer.transformPostingForReport(deliveryNote.getPosting()) : postingTO);
        deliveryNoteTO.setCurrency(CurrencyTransformer.transformCurrency(deliveryNote.getCurrency()));
        deliveryNoteTO.setTotalPrice(deliveryNote.getTotalPrice());
        deliveryNoteTO.setVatRate(deliveryNote.getVatRate());
        deliveryNoteTO.setNotice(deliveryNote.getNotice());
        deliveryNoteTO.setBlankNumber(deliveryNote.getBlankNumber());
        deliveryNoteTO.setCarBrand(deliveryNote.getBrand());
        deliveryNoteTO.setCarStateNumber(deliveryNote.getStateNumber());
        deliveryNoteTO.setDriverFullName(deliveryNote.getFullName());
        deliveryNoteTO.setCarOwner(deliveryNote.getOwner());
        deliveryNoteTO.setCarTrailer(deliveryNote.getTrailer());
    }

    /**
     * Synchronizes DTO with entity.
     * @param deliveryNoteTO DTO to be updated.
     * @param deliveryNote entity with fresh data.
     */
    public static void update(DeliveryNoteTOForReport deliveryNoteTO, DeliveryNote deliveryNote) {
        update(deliveryNoteTO, deliveryNote,  null);
    }

    public static void update(DeliveryNote deliveryNote, DeliveryNoteTOForReport deliveryNoteTO) {
        deliveryNote.setBlankNumber(deliveryNoteTO.getBlankNumber());
        deliveryNote.setBrand(deliveryNoteTO.getCarBrand());
        deliveryNote.setStateNumber(deliveryNoteTO.getCarStateNumber());
        deliveryNote.setFullName(deliveryNoteTO.getDriverFullName());
        deliveryNote.setOwner(deliveryNoteTO.getCarOwner());
        deliveryNote.setTrailer(deliveryNoteTO.getCarTrailer());
    }

    private static List<DeliveryNoteItemTO> transformItemsList(List<DeliveryNoteItem> items) {
        List<DeliveryNoteItemTO> itemsTO = new ArrayList<DeliveryNoteItemTO>();
        for (DeliveryNoteItem item : items){
            itemsTO.add(transformItem(item));
        }
        return itemsTO;
    }

    public static DeliveryNoteItemTO transformItem(DeliveryNoteItem item) {
        if (item == null){
            return null;
        }
        DeliveryNoteItemTO itemTO = new DeliveryNoteItemTO();
        updateItem(itemTO, item);
        return itemTO;
    }

    /**
     * Synchronizes DTO with entity.
     * @param itemTO (in, out) - DTO to be updated.
     * @param item (in) - entity with fresh data.
     */
    private static void updateItem(DeliveryNoteItemTO itemTO, DeliveryNoteItem item) {
        itemTO.setId(item.getId());
        itemTO.setNumber(item.getNumber());
        itemTO.setDeliveryNote(transformForReport(item.getDeliveryNote()));
        itemTO.setDetailBatch(DetailBatchTransformer.batchTO(item.getDetailBatch()));
        itemTO.setShelfLifeDate(item.getShelfLifeDate());
        itemTO.setWarehouseBatchNotice(item.getWarehouseBatchNotice());
        itemTO.setCount(item.getAmount());
        itemTO.setCountMeas(MeasureUnitTransformer.transform(item.getCountMeas()));
        itemTO.setPrice(item.getPrice());
    }
}
