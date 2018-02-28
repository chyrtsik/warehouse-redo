/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.bl.directory.MeasureUnitService;
import com.artigile.warehouse.bl.needs.WareNeedService;
import com.artigile.warehouse.domain.purchase.Purchase;
import com.artigile.warehouse.domain.purchase.PurchaseItem;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.purchase.PurchaseItemTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTOForReport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 01.03.2009
 */
public final class PurchaseTransformer {
    private PurchaseTransformer(){
    }

    public static List<PurchaseTOForReport> trasformList(List<Purchase> purchases) {
        List<PurchaseTOForReport> purchasesTO = new ArrayList<PurchaseTOForReport>();
        for (Purchase purchase : purchases){
            purchasesTO.add(transform(purchase));
        }
        return purchasesTO;
    }

    public static PurchaseTOForReport transform(Purchase purchase) {
        if (purchase == null){
            return null;
        }
        PurchaseTOForReport purchaseTO = new PurchaseTOForReport(false);
        updatePurchase(purchaseTO, purchase);
        return purchaseTO;
    }

    /**
     * @param purchaseTO - (in, out) item to be updates.
     * @param purchase - (in) entity with fresh data.
     */
    public static void updatePurchase(PurchaseTO purchaseTO, Purchase purchase) {
        updatePurchase((PurchaseTOForReport)purchaseTO, purchase);
        for (int i=0; i<purchase.getItems().size(); i++){
            updateItem(purchaseTO.getItems().get(i), purchase.getItems().get(i));
        }
    }

    /**
     * @param purchaseTO - (in, out) item to be updates.
     * @param purchase - (in) entity with fresh data.
     */
    public static void updatePurchase(PurchaseTOForReport purchaseTO, Purchase purchase) {
        purchaseTO.setId(purchase.getId());
        purchaseTO.setNumber(purchase.getNumber());
        purchaseTO.setState(purchase.getState());
        purchaseTO.setCreateDate(purchase.getCreateDate());
        purchaseTO.setCreatedUser(UserTransformer.transformUser(purchase.getCreatedUser()));
        purchaseTO.setContractor(ContractorTransformer.transformContractor(purchase.getContractor()));
        purchaseTO.setLoadPlace(ContractorTransformer.transformLoadPlace(purchase.getLoadPlace()));
        purchaseTO.setCurrency(CurrencyTransformer.transformCurrency(purchase.getCurrency()));
        purchaseTO.setTotalPrice(purchase.getTotalPrice());
        purchaseTO.setNotice(purchase.getNotice());
    }

    /**
     * Update entity from DTO (all fields except total price).
     * @param purchase - (in, out) entity to be updated.
     * @param purchaseTO - (in) DTO with fresh data.
     */
    public static void updatePurchase(Purchase purchase, PurchaseTOForReport purchaseTO) {
        purchase.setId(purchaseTO.getId());
        purchase.setNumber(purchaseTO.getNumber());
        purchase.setState(purchaseTO.getState());
        purchase.setCreateDate(purchaseTO.getCreateDate());
        purchase.setCreatedUser(UserTransformer.transformUser(purchaseTO.getCreatedUser()));
        purchase.setContractor(ContractorTransformer.transformContractor(purchaseTO.getContractor()));
        purchase.setLoadPlace(ContractorTransformer.transformLoadPlace(purchaseTO.getLoadPlace()));
        purchase.setCurrency(CurrencyTransformer.transformCurrency(purchaseTO.getCurrency()));
        purchase.setNotice(purchaseTO.getNotice());
    }

    public static Purchase transform(PurchaseTOForReport purchaseTO) {
        if (purchaseTO == null){
            return null;
        }
        Purchase purchase = SpringServiceContext.getInstance().getPurchaseService().getPurchaseById(purchaseTO.getId());
        if (purchase == null){
            purchase = new Purchase();
        }
        return purchase;
    }

    public static PurchaseTO transformPurchaseFull(Purchase purchase) {
        if (purchase == null){
            return null;
        }
        PurchaseTO purchaseTO = new PurchaseTO(false);
        updatePurchase((PurchaseTOForReport)purchaseTO, purchase);
        purchaseTO.setItems(transformItems(purchaseTO, purchase.getItems()));
        return purchaseTO;
    }

    private static List<PurchaseItemTO> transformItems(PurchaseTO purchaseTO, List<PurchaseItem> items) {
        List<PurchaseItemTO> itemsTO = new ArrayList<PurchaseItemTO>();
        for (PurchaseItem item : items){
            itemsTO.add(transformItem(purchaseTO, item));
        }
        return itemsTO;
    }

    private static PurchaseItemTO transformItem(PurchaseTOForReport purchaseTO, PurchaseItem item) {
        if (item == null){
            return null;
        }
        PurchaseItemTO itemTO = new PurchaseItemTO(purchaseTO);
        updateItem(itemTO, item);
        return itemTO;
    }

    public static void updateItem(PurchaseItemTO itemTO, PurchaseItem item) {
        itemTO.setId(item.getId());
        itemTO.setNumber(item.getNumber());
        itemTO.setPrice(item.getPrice());
        itemTO.setCount(item.getAmount());
        itemTO.setCountMeas(MeasureUnitTransformer.transform(item.getCountMeas()));
        itemTO.setWareNeedItem(WareNeedTransformer.transformItem(item.getWareNeedItem()));
        itemTO.setName(item.getName());
        itemTO.setMisc(item.getMisc());
        itemTO.setNotice(item.getNotice());
    }

    public static void updateItem(PurchaseItem item, PurchaseItemTO itemTO) {
        item.setNumber(itemTO.getNumber());
        item.setPrice(itemTO.getPrice());
        item.setAmount(itemTO.getCount());
        MeasureUnitService measureUnitService = SpringServiceContext.getInstance().getMeasureUnitService();
        item.setCountMeas(measureUnitService.getMeasureById(itemTO.getCountMeas().getId()));
        WareNeedService needService = SpringServiceContext.getInstance().getWareNeedsService();
        item.setWareNeedItem(needService.getWareNeedItem(itemTO.getWareNeedItem().getId()));
        item.setName(itemTO.getName());
        item.setMisc(itemTO.getMisc());
        item.setNotice(itemTO.getNotice());
    }

    public static PurchaseItem transformItem(PurchaseItemTO itemTO) {
        if (itemTO == null){
            return null;
        }
        PurchaseItem item = SpringServiceContext.getInstance().getPurchaseService().getPurchaseItemById(itemTO.getId());
        if (item == null){
            item = new PurchaseItem();
            item.setPurchase(SpringServiceContext.getInstance().getPurchaseService().getPurchaseById(itemTO.getPurchase().getId()));
        }
        updateItem(item, itemTO);
        return item;
    }

    public static PurchaseItemTO transformItem(PurchaseItem purchaseItem) {
        if (purchaseItem == null){
            return null;
        }
        return transformItem(transform(purchaseItem.getPurchase()), purchaseItem);
    }
}
