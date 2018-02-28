/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.postings;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.utils.SpringServiceContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 06.02.2009
 */

/**
 * Full posting data (with posting items).
 * Supports listeners mechanism for detecting changed in the order.
 */
public class PostingTO extends PostingTOForReport{
    private List<PostingItemTO> items = new ArrayList<PostingItemTO>();

    //===================== Operations ================================
    public void addNewItem(PostingItemTO postingItem) throws BusinessException {
        postingItem.setPosting(this);
        SpringServiceContext.getInstance().getPostingsService().addItemToPosting(postingItem);

        //Framework should place new item here automatically. So, to should replace data, placed to list
        //by the framework to repair nofification, that will be broken, because object, placed to the list by
        //framework references to the other instance of different OrderTO.
        //assert(getItems().contains(postingItem));
        //getItems().set(getItems().indexOf(postingItem), postingItem);
        List<PostingItemTO> items = getItems();
        if (items.contains(postingItem)) {
            items.set(items.indexOf(postingItem), postingItem);
        } else {
            items.add(postingItem);
        }

        fireItemAdded(postingItem);
    }

    public void deleteItem(PostingItemTO postingItem) throws BusinessException {
        SpringServiceContext.getInstance().getPostingsService().deleteItemFromPosting(postingItem.getId());
        items.remove(postingItem);
        fireItemRemoved(postingItem);
    }

    public boolean hasSameItem(PostingItemTO postingItemToSearch) {
        return getSameItem(postingItemToSearch) != null;
    }

    public PostingItemTO getSameItem(PostingItemTO postingItemToSearch) {
        for (PostingItemTO item : items){
            if (item.isSameItem(postingItemToSearch)){
                return item;
            }
        }
        return null;
    }

    //================ Getters and setters ============================
    public List<PostingItemTO> getItems() {
        return items;
    }

    public void setItems(List<PostingItemTO> items) {
        this.items = items;
    }

    //======================= Listeners support =======================
    private List<PostingItemsChangeListener> listeners = new ArrayList<PostingItemsChangeListener>();

    public void addItemsChangeListener(PostingItemsChangeListener newListener){
        if (!listeners.contains(newListener)) {
            listeners.add(newListener);
        }
    }

    public void removeItemsChangeListener(PostingItemsChangeListener removingListener) {
        listeners.remove(removingListener);
    }

    private void fireItemAdded(PostingItemTO newItem){
        for (PostingItemsChangeListener listener : listeners){
            listener.itemAdded(newItem);
        }
    }

    private void fireItemRemoved(PostingItemTO removedItem){
        for (PostingItemsChangeListener listener : listeners){
            listener.itemRemoved(removedItem);
        }
    }

    public void fireItemChanged(PostingItemTO changedItem){
        for (PostingItemsChangeListener listener : listeners){
            listener.itemChanged(changedItem);
        }
    }

}
