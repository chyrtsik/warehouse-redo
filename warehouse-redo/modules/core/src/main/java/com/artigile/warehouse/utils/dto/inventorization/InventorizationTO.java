/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.inventorization;

import com.artigile.warehouse.bl.inventorization.inventorization.InventorizationService;
import com.artigile.warehouse.utils.SpringServiceContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 07.10.2009
 */

/**
 * Full inventorization data (with inventorization items).
 * Supports listeners mechanism for detecting changed in the inventorization.
 */
public class InventorizationTO extends InventorizationTOForReport {
    private List<InventorizationItemTO> items = new ArrayList<InventorizationItemTO>();

    public List<InventorizationItemTO> getItems() {
        return items;
    }

    public void setItems(List<InventorizationItemTO> items) {
        this.items = items;
    }

    //===================== Operations ================================

    private InventorizationService getInventorizationService() {
        return SpringServiceContext.getInstance().getInventorizationService();
    }

    public void addNewItem(InventorizationItemTO newInventorizationItem) {
        newInventorizationItem.setInventorization(this);
        getInventorizationService().addItemToInventorization(newInventorizationItem);

        //Framework should place new item here automatically. So, to should replace data, placed to list
        //by the framework to repair nofification, that will be broken, because object, placed to the list by
        //framework references to the other instance of different InventorizationTO.
        assert(getItems().contains(newInventorizationItem));
        getItems().set(getItems().indexOf(newInventorizationItem), newInventorizationItem);

        fireItemAdded(newInventorizationItem);
    }

    public void deleteItem(InventorizationItemTO inventorizationItemTO) {
        items.remove(inventorizationItemTO);
        getInventorizationService().deleteItemFromInventorization(inventorizationItemTO);
        fireItemRemoved(inventorizationItemTO);
    }

    //======================= Listeners support =======================
    private List<InventorizationItemsChangeListener> listeners = new ArrayList<InventorizationItemsChangeListener>();

    public void addItemsChangeListener(InventorizationItemsChangeListener newListener){
        if (!listeners.contains(newListener)) {
            listeners.add(newListener);
        }
    }

    public void removeItemsChangeListener(InventorizationItemsChangeListener removingListener) {
        listeners.remove(removingListener);
    }

    private void fireItemAdded(InventorizationItemTO newItem){
        for (InventorizationItemsChangeListener listener : listeners){
            listener.itemAdded(newItem);
        }
    }

    private void fireItemRemoved(InventorizationItemTO removedItem){
        for (InventorizationItemsChangeListener listener : listeners){
            listener.itemRemoved(removedItem);
        }
    }

    public void fireItemChanged(InventorizationItemTO changedItem){
        for (InventorizationItemsChangeListener listener : listeners){
            listener.itemChanged(changedItem);
        }
    }
}
