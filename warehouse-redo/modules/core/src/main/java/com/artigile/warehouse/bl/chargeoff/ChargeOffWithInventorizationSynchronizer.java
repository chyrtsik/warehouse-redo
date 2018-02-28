/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.chargeoff;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.inventorization.inventorization.InventorizationChangeAdapter;
import com.artigile.warehouse.bl.inventorization.inventorization.InventorizationService;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchChangeDocument;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.domain.chargeoff.ChargeOff;
import com.artigile.warehouse.domain.chargeoff.ChargeOffItem;
import com.artigile.warehouse.domain.chargeoff.ChargeOffReason;
import com.artigile.warehouse.domain.inventorization.Inventorization;
import com.artigile.warehouse.domain.inventorization.InventorizationItem;
import com.artigile.warehouse.domain.inventorization.InventorizationItemProcessingResult;
import com.artigile.warehouse.domain.inventorization.InventorizationState;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.UserTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Shyrik, 25.10.2009
 */

/**
 * This class reacts on changes in inventorization documents to perform appropriate
 * actions on charge off documents.
 */
@Transactional(rollbackFor = BusinessException.class)
public class ChargeOffWithInventorizationSynchronizer extends InventorizationChangeAdapter {
    
    private InventorizationService inventorizationService;

    private ChargeOffService chargeOffService;

    private WarehouseBatchService warehouseBatchService;

    public ChargeOffWithInventorizationSynchronizer() { /* Default constructor */ }

    public void initialize(){
        //This class listens for changes made in inventorizations.
        inventorizationService.addListener(this);
    }

    //======================= Synchronization with inventrizations ==========================
    @Override
    public void onInventorizationStateChanged(Inventorization inventorization, InventorizationState oldState, InventorizationState newState) throws BusinessException {
        if (newState.equals(InventorizationState.CLOSED)){
            //When inventorization has been closed, we should create change off for all positions, that
            //are known as been lost.
            onCloseInventorization(inventorization);
        }
    }

    private void onCloseInventorization(Inventorization inventorization) throws BusinessException {
        //1. Searching for ability to create charge off items.
        List<ChargeOffItem> chargeOffItems = new ArrayList<ChargeOffItem>();
        long currentNumber = 1;
        for (InventorizationItem inventorizationItem : inventorization.getItems()){
            if (inventorizationItem.getProcessingResult().equals(InventorizationItemProcessingResult.LACK_COUNT)){
                //Creating charge off item.
                ChargeOffItem chargeOffItem = new ChargeOffItem();
                chargeOffItem.setNumber(currentNumber++);
                chargeOffItem.setStoragePlace(inventorizationItem.getStoragePlace());
                chargeOffItem.setDetailBatch(inventorizationItem.getDetailBatch());
                chargeOffItem.setAmount(inventorizationItem.getCountDifference());
                chargeOffItem.setCountMeas(inventorizationItem.getCountMeas());
                chargeOffItems.add(chargeOffItem);
                inventorizationItem.setChargeOffItem(chargeOffItem);

                //Performing charge off for warehouse batch. We should detach inventorization items
                //from warehouse batches, because batch may be delete during charge charge off operation.
                long warehouseBatchId = inventorizationItem.getWarehouseBatch().getId();
                inventorizationItem.setWarehouseBatch(null);
                WarehouseBatchChangeDocument document = WarehouseBatchChangeDocument.createChargeOffDocument(chargeOffItem);
                warehouseBatchService.performWareChargeOff(warehouseBatchId, chargeOffItem.getAmount(), document);
            }
        }

        //2. Creating charge off.
        if ( chargeOffItems.size() > 0 ){
            ChargeOff chargeOff = new ChargeOff();
            chargeOff.setNumber(chargeOffService.getNextAvailableNumber());
            chargeOff.setPerformDate(Calendar.getInstance().getTime());
            chargeOff.setPerformer(UserTransformer.transformUser(WareHouse.getUserSession().getUser())); //TODO: eliminate this reference to presentation tier.
            chargeOff.setInventorization(inventorization);
            chargeOff.setReason(ChargeOffReason.INVENTORIZATION);
            chargeOff.setWarehouse(inventorization.getWarehouse());
            chargeOff.setItems(chargeOffItems);
            chargeOff.setNotice(I18nSupport.message("chargeOff.notice.forInventorizationWithNumber", inventorization.getNumber()));
            chargeOffService.saveChargeOff(chargeOff);

            inventorization.setChargeOff(chargeOff);
        }
    }

    //================================ Spring setters ================================
    public void setInventorizationService(InventorizationService inventorizationService) {
        this.inventorizationService = inventorizationService;
    }

    public void setChargeOffService(ChargeOffService chargeOffService) {
        this.chargeOffService = chargeOffService;
    }

    public void setWarehouseBatchService(WarehouseBatchService warehouseBatchService) {
        this.warehouseBatchService = warehouseBatchService;
    }
}