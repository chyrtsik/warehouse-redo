/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.purchase;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.utils.SpringServiceContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 02.03.2009
 */

/**
 * Full data of the purchase (with list of purchase items).
 */
public class PurchaseTO extends PurchaseTOForReport {
    private List<PurchaseItemTO> items = new ArrayList<PurchaseItemTO>();

    public PurchaseTO(boolean init) {
        super(init);
    }

    public List<PurchaseItemTO> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItemTO> items) {
        this.items = items;
    }

    //===================== Operations ================================
    public void addNewItem(PurchaseItemTO newPurchaseItem) throws BusinessException {
        newPurchaseItem.setPurchase(this);
        SpringServiceContext.getInstance().getPurchaseService().addItemToPurchase(newPurchaseItem);

        //Framework should place new item here automatically. So, to should replace data, placed to list
        //by the framework to repair nofification, that will be broken, because object, placed to the list by
        //framework references to the other instance of different OrderTO.
        assert(getItems().contains(newPurchaseItem));
        getItems().set(getItems().indexOf(newPurchaseItem), newPurchaseItem);

        fireItemAdded(newPurchaseItem);
    }

    public void deleteItem(PurchaseItemTO purchaseItem) throws BusinessException {
        SpringServiceContext.getInstance().getPurchaseService().deleteItemFromPurchase(purchaseItem);
        items.remove(purchaseItem);
        fireItemRemoved(purchaseItem);
    }

    //======================= Listeners support =======================
    private List<PurchaseItemsChangeListener> listeners = new ArrayList<PurchaseItemsChangeListener>();

    public void addItemsChangeListener(PurchaseItemsChangeListener newListener) {
        if (!listeners.contains(newListener)) {
            listeners.add(newListener);
        }
    }

    public void removeItemsChangeListener(PurchaseItemsChangeListener removingListener) {
        listeners.remove(removingListener);
    }

    private void fireItemAdded(PurchaseItemTO newItem) {
        for (PurchaseItemsChangeListener listener : listeners) {
            listener.itemAdded(newItem);
        }
    }

    private void fireItemRemoved(PurchaseItemTO removedItem) {
        for (PurchaseItemsChangeListener listener : listeners) {
            listener.itemRemoved(removedItem);
        }
    }

    public void fireItemChanged(PurchaseItemTO changedItem) {
        for (PurchaseItemsChangeListener listener : listeners) {
            listener.itemChanged(changedItem);
        }
    }
}
