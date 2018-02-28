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

import com.artigile.warehouse.domain.needs.WareNeed;
import com.artigile.warehouse.domain.needs.WareNeedItem;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.needs.WareNeedItemTO;
import com.artigile.warehouse.utils.dto.needs.WareNeedTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 28.02.2009
 */
public final class WareNeedTransformer {

    private WareNeedTransformer() {
    }

    public static WareNeedTO transformWareNeed(WareNeed wareNeed) {
        if (wareNeed == null){
            return null;
        }
        WareNeedTO wareNeedTO = new WareNeedTO();
        wareNeedTO.setId(wareNeed.getId());
        wareNeedTO.setItems(transformItems(wareNeedTO, wareNeed.getItems()));
        return wareNeedTO;
    }

    private static WareNeed transformWareNeed(WareNeedTO wareNeedTO) {
        if (wareNeedTO == null){
            return null;
        }
        WareNeed wareNeed = SpringServiceContext.getInstance().getWareNeedsService().getWareNeed(wareNeedTO.getId());
        if (wareNeed == null){
            wareNeed = new WareNeed();
        }
        return wareNeed;
    }

    private static List<WareNeedItemTO> transformItems(WareNeedTO wareNeedTO, List<WareNeedItem> items) {
        List<WareNeedItemTO> itemsTO = new ArrayList<WareNeedItemTO>();
        for (WareNeedItem item : items){
            itemsTO.add(transformItem(wareNeedTO, item));
        }
        return itemsTO;
    }

    private static WareNeedItemTO transformItem(WareNeedTO wareNeedTO, WareNeedItem item) {
        if (item == null){
            return null;
        }
        WareNeedItemTO itemTO = new WareNeedItemTO(wareNeedTO, false);
        updateItem(itemTO, item);
        return itemTO;
    }

    /**
     * @param itemTO - DTO representation to be updated (in, out).
     * @param item - domain model object with fresh data (in).
     */
    public static void updateItem(WareNeedItemTO itemTO, WareNeedItem item) {
        itemTO.setId(item.getId());
        itemTO.setState(item.getState());
        itemTO.setNumber(item.getNumber());
        itemTO.setSubNumber(item.getSubNumber());
        itemTO.setCreateDateTime(item.getCreateDateTime());
        itemTO.setCreatedUser(UserTransformer.transformUser(item.getCreatedUser()));
        itemTO.setDetailBatch(DetailBatchTransformer.batchTO(item.getDetailBatch()));
        itemTO.setName(item.getName());
        itemTO.setMisc(item.getMisc());
        itemTO.setCount(item.getAmount());
        itemTO.setMinYear(item.getMinYear());
        itemTO.setMaxPrice(item.getMaxPrice());
        itemTO.setCurrency(CurrencyTransformer.transformCurrency(item.getCurrency()));
        itemTO.setNotice(item.getNotice());
        itemTO.setAutoCreated(item.getAutoCreated());
        itemTO.setCustomer(ContractorTransformer.transformContractor(item.getCustomer()));
        itemTO.setSupplier(item.getPurchaseItem() == null ? null : ContractorTransformer.transformContractor(item.getPurchaseItem().getPurchase().getContractor()));
        itemTO.setBuyPrice(item.getPurchaseItem() == null ? null : item.getPurchaseItem().getPrice());
        itemTO.setBuyCurrency(item.getPurchaseItem() == null ? null : CurrencyTransformer.transformCurrency(item.getPurchaseItem().getPurchase().getCurrency()));
    }

    /**
     * @param item - domaim object to be updated (in, out).
     * @param itemTO - object with data to be stpred in the domain (in).
     */
    public static void updateItem(WareNeedItem item, WareNeedItemTO itemTO) {
        if (itemTO == null){
            return;
        }
        item.setState(itemTO.getState());
        item.setNumber(itemTO.getNumber());
        item.setSubNumber(itemTO.getSubNumber());
        item.setCreateDateTime(itemTO.getCreateDateTime());
        item.setCreatedUser(UserTransformer.transformUser(itemTO.getCreatedUser()));
        item.setDetailBatch(DetailBatchTransformer.batch(itemTO.getDetailBatch()));
        item.setName(itemTO.getName());
        item.setMisc(itemTO.getMisc());
        item.setAmount(itemTO.getCount());
        item.setMinYear(itemTO.getMinYear());
        item.setMaxPrice(itemTO.getMaxPrice());
        item.setCurrency(CurrencyTransformer.transformCurrency(itemTO.getCurrency()));
        item.setNotice(itemTO.getNotice());
        item.setAutoCreated(itemTO.getAutoCreated());
        item.setCustomer(ContractorTransformer.transformContractor(itemTO.getCustomer()));
    }

    public static WareNeedItem transformItem(WareNeedItemTO itemTO) {
        if (itemTO == null){
            return null;
        }

        WareNeedItem item = SpringServiceContext.getInstance().getWareNeedsService().getWareNeedItem(itemTO.getId());
        if (item == null){
            item = new WareNeedItem(WareNeedTransformer.transformWareNeed(itemTO.getWareNeed()));
        }
        return item;
    }

    public static WareNeedItemTO transformItem(WareNeedItem wareNeedItem) {
        if (wareNeedItem == null){
            return null;
        }
        return transformItem(transformWareNeed(wareNeedItem.getWareNeed()), wareNeedItem); 
    }
}
