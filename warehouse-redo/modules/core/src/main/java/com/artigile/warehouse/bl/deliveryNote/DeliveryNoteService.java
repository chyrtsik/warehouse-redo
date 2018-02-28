/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.deliveryNote;

import com.artigile.warehouse.bl.chargeoff.ShippingFromWarehouseContext;
import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.verifications.Verification;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.bl.common.verifications.Verifications;
import com.artigile.warehouse.dao.DeliveryNoteDAO;
import com.artigile.warehouse.dao.DeliveryNoteItemsDAO;
import com.artigile.warehouse.domain.chargeoff.ChargeOff;
import com.artigile.warehouse.domain.chargeoff.ChargeOffItem;
import com.artigile.warehouse.domain.contractors.Contractor;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNote;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteItem;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.domain.warehouse.Warehouse;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteItemTO;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTO;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.DeliveryNoteTransformer;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shyrik, 01.11.2009
 */
@Transactional(rollbackFor = BusinessException.class)
public class DeliveryNoteService {
    private DeliveryNoteDAO deliveryNoteDAO;
    private DeliveryNoteItemsDAO deliveryNoteItemsDAO;
    //======================= Listeners support ====================================
    private ArrayList<DeliveryNoteChangeListener> listeners = new ArrayList<DeliveryNoteChangeListener>();

    //==================== Construction and initialization =====================
    public DeliveryNoteService() {
    }

    public void addListener(DeliveryNoteChangeListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    public void removeListener(DeliveryNoteChangeListener listener){
        listeners.remove(listener);
    }

    private void fireDeliveryNoteStateChanged(DeliveryNote deliveryNote, DeliveryNoteState oldState, DeliveryNoteState newState) throws BusinessException {
        for (DeliveryNoteChangeListener listener : listeners){
            listener.onDeliveryNoteStateChanged(deliveryNote, oldState, newState);
        }
    }

    private void fireDeliveryNoteCreated(DeliveryNote deliveryNote) throws BusinessException {
        for (DeliveryNoteChangeListener listener : listeners){
            listener.onDeliveryNoteCreated(deliveryNote);
        }
    }

    //================== Operations ============================================

    public List<DeliveryNoteTOForReport> getAllDeliveryNotesForReport() {
        return DeliveryNoteTransformer.listForReport(deliveryNoteDAO.getAll());
    }

    public List<DeliveryNoteTOForReport> getDeliveryNotesForReportByFilter(DeliveryNoteFilter filter) {
        return DeliveryNoteTransformer.listForReport(deliveryNoteDAO.getDeliveryNotesByFilter(filter));
    }

    public DeliveryNoteTOForReport getDeliveryNoteForReport(long deliveryNoteId) {
        return DeliveryNoteTransformer.transformForReport(deliveryNoteDAO.get(deliveryNoteId));
    }

    public DeliveryNoteTO getDeliveryNoteFullData(long deliveryNoteId) {
        DeliveryNoteTO deliveryNoteTO= DeliveryNoteTransformer.transform(deliveryNoteDAO.get(deliveryNoteId));
        int totalQuantity=0;
        for (DeliveryNoteItemTO deliveryNoteItemTO : deliveryNoteTO.getItems()) {
            totalQuantity+=deliveryNoteItemTO.getCount();
        }
        deliveryNoteTO.setTotalQuantity(totalQuantity);
        return deliveryNoteTO;
    }

    public DeliveryNote getDeliveryNoteById(long deliveryNoteId) {
        return deliveryNoteDAO.get(deliveryNoteId);
    }

    /**
     * Marks given list of delivery notes as have been shipped.
     * @param deliveryNoteIds
     */
    public void markDeliveryNotesListAsShipped(List<Long> deliveryNoteIds) throws BusinessException {
        for (Long deliveryNoteId : deliveryNoteIds){
            markDeliveryNoteAsShipped(deliveryNoteId);
        }
    }

    /**
     * Marks given delivery note as have been shipped.
     * @param deliveryNoteId
     */
    public void markDeliveryNoteAsShipped(long deliveryNoteId) throws BusinessException {
        changeDeliveryNoteState(deliveryNoteId, DeliveryNoteState.SHIPPING_TO_WAREHOUSE, DeliveryNoteState.SHIPPED);
    }

    /**
     * Changes state of th delivery note.
     * @param deliveryNoteId delivery note identifier.
     * @param oldState old state.
     * @param newState new state.
     * @throws BusinessException
     */
    public void changeDeliveryNoteState(long deliveryNoteId, DeliveryNoteState oldState, DeliveryNoteState newState) throws BusinessException {
        changeDeliveryNoteState(deliveryNoteId, oldState, newState, null);
    }

    /**
     * Changes state of th delivery note.
     * @param deliveryNoteId delivery note identifier.
     * @param oldState old state.
     * @param newState new state.
     * @param notice note to be placed into delivery note.
     * @throws BusinessException
     */
    public void changeDeliveryNoteState(long deliveryNoteId, DeliveryNoteState oldState, DeliveryNoteState newState, String notice) throws BusinessException {
        DeliveryNote deliveryNote = deliveryNoteDAO.get(deliveryNoteId);

        //1. Checkes, that delivery note state can be changed.
        Verification stateChangeVerification = new BeforeDeliveryNoteStateChangeVerification(oldState);
        VerificationResult verificationResult = Verifications.performVerification(deliveryNote, stateChangeVerification);
        if (verificationResult.isFailed()){
            throw new BusinessException(
                I18nSupport.message(
                    "deliveryNote.verification.error.template", 
                    deliveryNote.getNumber(),
                    verificationResult.getFailReason()
                )
            );
        }

        //2. Changes delivery note.
        deliveryNote.setState(newState);
        if (!StringUtils.isStringNullOrEmpty(notice)){
            deliveryNote.setNotice(notice);
        }
        deliveryNoteDAO.save(deliveryNote);

        //3. Notifies listeners about changes have been made.
        fireDeliveryNoteStateChanged(deliveryNote, oldState, newState);
    }

    /**
     * Creates delivery note for wares, what ar charged off from the warehouse.
     * @param context context of current operation.
     * @param chargeOff charge off, from which to take wares for delivery note.
     * @param destinationWarehouse warehouse, to which delivery note will be shipped.
     */
    public void createDeliveryNoteForChargeOff(ShippingFromWarehouseContext context, ChargeOff chargeOff, Warehouse destinationWarehouse) throws BusinessException {
        //1. Group together charge off items with the same currency.
        Map<Currency, List<ChargeOffItem>> chargeOffItemsByCurrency = new HashMap<Currency, List<ChargeOffItem>>();
        for (ChargeOffItem item : chargeOff.getItems()){
            Currency currency = item.getDetailBatch().getCurrency();
            if (!chargeOffItemsByCurrency.containsKey(currency)){
                chargeOffItemsByCurrency.put(currency, new ArrayList<ChargeOffItem>());
            }
            chargeOffItemsByCurrency.get(currency).add(item);
        }

        //2. Create separate delivery note for each difference currency.
        for (Currency currency : chargeOffItemsByCurrency.keySet()){
            List<ChargeOffItem> chargeOffItems  = chargeOffItemsByCurrency.get(currency);

            //1. Constructing delivery note.
            long deliveryNoteNumber = context.getNextDeliveryNoteNumber();
            context.setNextDeliveryNoteNumber(deliveryNoteNumber + 1);

            DeliveryNote deliveryNote = new DeliveryNote();
            deliveryNote.setNumber(deliveryNoteNumber);
            deliveryNote.setState(DeliveryNoteState.SHIPPING_TO_WAREHOUSE);
            deliveryNote.setChargeOff(chargeOff);
            deliveryNote.setDestinationWarehouse(destinationWarehouse);
            deliveryNote.setCurrency(currency);
            deliveryNoteDAO.save(deliveryNote);

            //2. Constructing delivery note items.
            for (int i=0; i<chargeOffItems.size(); i++){
                ChargeOffItem chargeOffItem = chargeOffItems.get(i);

                DeliveryNoteItem deliveryNoteItem = new DeliveryNoteItem();
                deliveryNoteItem.setDeliveryNote(deliveryNote);
                deliveryNoteItem.setNumber(i+1);
                deliveryNoteItem.setDetailBatch(chargeOffItem.getDetailBatch());
                deliveryNoteItem.setShelfLifeDate(chargeOffItem.getShelfLifeDate());
                deliveryNoteItem.setWarehouseBatchNotice(chargeOffItem.getWarehouseNotice());
                deliveryNoteItem.setAmount(chargeOffItem.getAmount());
                deliveryNoteItem.setCountMeas(chargeOffItem.getCountMeas());
                deliveryNoteItem.setPrice(chargeOffItem.getChargeOffOperation().getItemCost());
                deliveryNote.getItems().add(deliveryNoteItem);

                deliveryNoteItemsDAO.save(deliveryNoteItem);
                chargeOffItem.setDeliveryNoteItem(deliveryNoteItem);
            }

            //3. Notify all listeners that delivery note has been created.
            fireDeliveryNoteCreated(deliveryNote);
        }
    }

    /**
     * Creates delivery note for wares, that are changed off for specified contractor.
     * @param context context of current operation.
     * @param chargeOff charge off, from which to take wares for delivery note.
     * @param contractor contractor, who will receive wares.
     * @param currency currency of the delivery note.
     * @param vatRate vat rate used in order (for accounting).
     * @param itemPrices prices of every item (may differ from original price of wares).
     */
    public void createDeliveryNoteToContractor(ShippingFromWarehouseContext context, ChargeOff chargeOff,
                                               Contractor contractor, Currency currency, BigDecimal vatRate,
                                               Map<ChargeOffItem, BigDecimal> itemPrices) throws BusinessException
    {
        //1. Constructing delivery note.
        long deliveryNoteNumber = context.getNextDeliveryNoteNumber();
        context.setNextDeliveryNoteNumber(deliveryNoteNumber + 1);

        DeliveryNote deliveryNote = new DeliveryNote();
        deliveryNote.setNumber(deliveryNoteNumber);
        deliveryNote.setState(DeliveryNoteState.SHIPPING_TO_CONTRACTOR);
        deliveryNote.setChargeOff(chargeOff);
        deliveryNote.setContractor(contractor);
        deliveryNote.setCurrency(currency);
        deliveryNote.setVatRate(vatRate);
        deliveryNoteDAO.save(deliveryNote);

        //2. Constructing delivery note items.
        BigDecimal totalPrice = null;
        for (int i=0; i<chargeOff.getItems().size(); i++){
            ChargeOffItem chargeOffItem = chargeOff.getItems().get(i);

            DeliveryNoteItem deliveryNoteItem = new DeliveryNoteItem();
            deliveryNoteItem.setDeliveryNote(deliveryNote);
            deliveryNoteItem.setNumber(i+1);
            deliveryNoteItem.setDetailBatch(chargeOffItem.getDetailBatch());
            deliveryNoteItem.setShelfLifeDate(chargeOffItem.getShelfLifeDate());
            deliveryNoteItem.setWarehouseBatchNotice(chargeOffItem.getWarehouseNotice());
            deliveryNoteItem.setAmount(chargeOffItem.getAmount());
            deliveryNoteItem.setCountMeas(chargeOffItem.getCountMeas());
            deliveryNoteItem.setPrice(itemPrices.get(chargeOffItem));

            deliveryNote.getItems().add(deliveryNoteItem);
            totalPrice = (totalPrice == null) ? deliveryNoteItem.getPrice() : totalPrice.add(deliveryNoteItem.getPrice());

            deliveryNoteItemsDAO.save(deliveryNoteItem);
            chargeOffItem.setDeliveryNoteItem(deliveryNoteItem);
        }
        deliveryNote.setTotalPrice(totalPrice);

        //3. Notify all listeners that delivery note has been created.
        fireDeliveryNoteCreated(deliveryNote);
    }

    /**
     * saves blank number into the Delivery note object wit id {@code delivetyNoteId}. Used on deliveyNoteList page
     * to edit the values right inside cells.
     * @param deliveryNoteObject delivery object
     * @param blankNumber blank number value.
     */
    public void saveBlankNumber(DeliveryNoteTOForReport deliveryNoteObject, String blankNumber) {
        if (blankNumber != null) {
            DeliveryNote deliveryNote = deliveryNoteDAO.get(deliveryNoteObject.getId());
            deliveryNote.setBlankNumber(blankNumber);
            deliveryNoteDAO.save(deliveryNote);
            DeliveryNoteTransformer.update(deliveryNoteObject, deliveryNote);
        }
    }

    public void save(DeliveryNoteTOForReport deliveryNoteObject) {
        DeliveryNote deliveryNote = deliveryNoteDAO.get(deliveryNoteObject.getId());
        if (deliveryNote == null) {
            deliveryNote = new DeliveryNote();
        }
        DeliveryNoteTransformer.update(deliveryNote, deliveryNoteObject);
        deliveryNoteDAO.save(deliveryNote);
        DeliveryNoteTransformer.update(deliveryNoteObject, deliveryNote);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long getNextDeliveryNoteNumber() {
        return deliveryNoteDAO.getNextDeliveryNoteNumber();
    }

    //========================== Spring setters ================================
    public void setDeliveryNoteDAO(DeliveryNoteDAO deliveryNoteDAO) {
        this.deliveryNoteDAO = deliveryNoteDAO;
    }

    public void setDeliveryNoteItemsDAO(DeliveryNoteItemsDAO deliveryNoteItemsDAO) {
        this.deliveryNoteItemsDAO = deliveryNoteItemsDAO;
    }
}
