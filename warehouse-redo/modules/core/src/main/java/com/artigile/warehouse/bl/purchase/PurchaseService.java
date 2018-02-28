    /*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.purchase;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.bl.common.verifications.Verifications;
import com.artigile.warehouse.bl.finance.CurrencyExchangeService;
import com.artigile.warehouse.dao.PurchaseDAO;
import com.artigile.warehouse.dao.PurchaseItemDAO;
import com.artigile.warehouse.domain.purchase.Purchase;
import com.artigile.warehouse.domain.purchase.PurchaseItem;
import com.artigile.warehouse.domain.purchase.PurchaseState;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.purchase.PurchaseItemTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTOForReport;
import com.artigile.warehouse.utils.transofmers.PurchaseTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 01.03.2009
 */
@Transactional(rollbackFor = BusinessException.class)
public class PurchaseService {
    
    private PurchaseDAO purchaseDAO;

    private PurchaseItemDAO purchaseItemDAO;

    //============================== Construction and initialization =========================
    public PurchaseService() { /* Default constructor */ }

    //=============================== Listeners support =================================
    /**
     * List of listeners on action of changing purchase state. 
     */
    private ArrayList<PurchaseChangeListener> listeners = new ArrayList<PurchaseChangeListener>();

    /**
     * Add new listener of purchase state changing to list.
     * @param listener New listener of purchase state changing.
     */
    public void addListener(PurchaseChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Activate listeners.
     * Notify that purchase changed state.
     * @param purchase Purchase which changed state.
     */
    public void firePurchaseStateChanged(Purchase purchase) {
        for (PurchaseChangeListener listener: listeners) {
            listener.onPurchaseStateChanged(purchase);
        }
    }

    //==================================== Operations ==================================
    public List<PurchaseTOForReport> getAllPurchases() {
        return PurchaseTransformer.trasformList(purchaseDAO.getAll());
    }

    public List<PurchaseTOForReport> getAllPurchasesByStates(PurchaseState[] statesFilter) {
        return PurchaseTransformer.trasformList(purchaseDAO.getPurchasesByStates(statesFilter));
    }

    public PurchaseTO getPurchaseFullData(long purchaseId) {
        return PurchaseTransformer.transformPurchaseFull(purchaseDAO.get(purchaseId));
    }

    public PurchaseTOForReport getPurchaseForReport(long purchaseId) {
        return PurchaseTransformer.transform(purchaseDAO.get(purchaseId));
    }

    public void deletePurchase(long purchaseId) {
        Purchase purchase = purchaseDAO.get(purchaseId);

        //1. At first we must explicitly delete all purchase items, because they may be linked with
        //other domain objects.
        for (PurchaseItem item : purchase.getItems()){
            deleteItemFromPurchase(item);
        }

        //2. Delete the purchase itself.
        purchaseDAO.remove(purchase);
    }

    public void savePurchase(Purchase purchase) {
        purchaseDAO.save(purchase);
    }

    public void savePurchase(PurchaseTOForReport purchase) {
        Purchase persistentPurchase = purchaseDAO.get(purchase.getId());
        if (persistentPurchase == null){
            persistentPurchase = new Purchase();
        }
        else if (persistentPurchase.getCurrency().getId() != purchase.getCurrency().getId()){
            //We needs to recalculate prices of the items in the purchase in the new currency.
            CurrencyExchangeService exchange = SpringServiceContext.getInstance().getCurencyExchangeService();
            long oldCurrencyId = persistentPurchase.getCurrency().getId();
            long newCurrencyId = purchase.getCurrency().getId();
            for (PurchaseItem item : persistentPurchase.getItems()){
                item.setPrice(exchange.convert(newCurrencyId, oldCurrencyId, item.getPrice()));
            }
        }

        PurchaseTransformer.updatePurchase(persistentPurchase, purchase);
        purchaseDAO.save(persistentPurchase);
        purchaseDAO.flush();
        purchaseDAO.refresh(persistentPurchase);
        PurchaseTransformer.updatePurchase(purchase, persistentPurchase);
    }

    public long getNextAvailablePurchaseNumber() {
        return purchaseDAO.getNextAvailablePurchaseNumber();
    }

    public boolean isUniquePurchaseNumber(long number, long purchaseId) {
        Purchase samePurchase = purchaseDAO.getPurchaseByNumber(number);
        return samePurchase == null || samePurchase.getId() == purchaseId;
    }

    public void addItemToPurchase(PurchaseItemTO newPurchaseItemTO) throws BusinessException {
        //1. Adding item to the purchase
        PurchaseItem newPurchaseItem = PurchaseTransformer.transformItem(newPurchaseItemTO);
        PurchaseTransformer.updateItem(newPurchaseItem, newPurchaseItemTO);

        Purchase purchase = newPurchaseItem.getPurchase();
        if (purchase != null) {
            if (newPurchaseItem.getNumber() == null) {
                newPurchaseItem.setNumber(purchaseItemDAO.getNextAvailableNumber(purchase.getId()));
            }

            if (!newPurchaseItemTO.isText()) {
                //2. Changing state of the ware need item to reflect, that it has been added to a purchase.
                SpringServiceContext.getInstance().getWareNeedsService().onWareNeedItemAddedToPurchase(newPurchaseItem);
            }

            //3. Saving changes
            purchaseItemDAO.save(newPurchaseItem);
            purchaseItemDAO.flush();
            purchaseItemDAO.refresh(purchase);
            PurchaseTransformer.updateItem(newPurchaseItemTO, newPurchaseItem);
        }
    }

    public void deleteItemFromPurchase(PurchaseItemTO purchaseItem) throws BusinessException {
        PurchaseItem persistentPurchaseItem = purchaseItemDAO.get(purchaseItem.getId());
        deleteItemFromPurchase(persistentPurchaseItem);
    }

    private void deleteItemFromPurchase(PurchaseItem purchaseItem) {
        if (!purchaseItem.isText()){
            //2. Changing state of the ware need item to reflect, that it has beed deleted from the purchase.
            SpringServiceContext.getInstance().getWareNeedsService().onWareNeedItemDeletedFromPurchase(purchaseItem);
        }

        //2. Deleting item from the purchase.
        purchaseItemDAO.remove(purchaseItem);

        //3. Refresh total price of purchase
        purchaseItemDAO.flush();
        purchaseItemDAO.refresh(purchaseItem.getPurchase());
    }

    public void savePurchaseItem(PurchaseItemTO purchaseItemTO) {
        PurchaseItem purchaseItem = PurchaseTransformer.transformItem(purchaseItemTO);

        if (!purchaseItemTO.isText()){
            //1. Saving changes to the ware need item, related to the purchase item.
            SpringServiceContext.getInstance().getWareNeedsService().onWareNeedItemInPurchaseChanged(purchaseItem);
        }

        //2. Saving purchase item changes item to the purchase
        purchaseItemDAO.save(purchaseItem);
        purchaseItemDAO.flush();
        purchaseItemDAO.refresh(purchaseItem.getPurchase());
        PurchaseTransformer.updateItem(purchaseItemTO, purchaseItem);
    }

    public void waitPurchase(PurchaseTOForReport purchase) throws BusinessException {
        Purchase persistentPurchase = purchaseDAO.get(purchase.getId());

        //1. Verify, is purchase is ready to became wating for it.
        VerificationResult verificationResult = Verifications.performVerification(persistentPurchase, new BeforeWaitPurchaseVerification());
        if (verificationResult.isFailed()){
            throw new BusinessException(verificationResult.getFailReason());
        }

        //2. Make purchase waiting.
        //2.1. New state of the purchase.
        persistentPurchase.setState(PurchaseState.WAITING);
        purchaseDAO.save(persistentPurchase);

        //2.2. Notify about changing state of the purchase.
        firePurchaseStateChanged(persistentPurchase);

        //3. Refresh purchase representation to conform new state.
        PurchaseTransformer.updatePurchase(purchase, persistentPurchase);
    }

    public void shipPurchase(PurchaseTOForReport purchase) throws BusinessException {
        Purchase persistentPurchase = purchaseDAO.get(purchase.getId());

        //1. Make purchase shipped.
        persistentPurchase.setState(PurchaseState.SHIPPED);
        purchaseDAO.save(persistentPurchase);

        //2. Notify about changing state of the purchase.
        firePurchaseStateChanged(persistentPurchase);

        //3. Refresh purchase representation to conform new state.
        PurchaseTransformer.updatePurchase(purchase, persistentPurchase);
    }

    public PurchaseItem getPurchaseItemById(long itemId) {
        return purchaseItemDAO.get(itemId);
    }

    public Purchase getPurchaseById(long purchaseId) {
        return purchaseDAO.get(purchaseId);
    }

    //========================= String setters ======================================
    public void setPurchaseDAO(PurchaseDAO purchaseDAO) {
        this.purchaseDAO = purchaseDAO;
    }

    public void setPurchaseItemDAO(PurchaseItemDAO purchaseItemDAO) {
        this.purchaseItemDAO = purchaseItemDAO;
    }
}
