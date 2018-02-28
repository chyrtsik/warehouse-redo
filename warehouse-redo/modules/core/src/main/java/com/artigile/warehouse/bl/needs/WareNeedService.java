/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.needs;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.dao.WareNeedDAO;
import com.artigile.warehouse.dao.WareNeedItemDAO;
import com.artigile.warehouse.domain.needs.WareNeed;
import com.artigile.warehouse.domain.needs.WareNeedItem;
import com.artigile.warehouse.domain.needs.WareNeedItemState;
import com.artigile.warehouse.domain.purchase.PurchaseItem;
import com.artigile.warehouse.utils.dto.needs.WareNeedItemTO;
import com.artigile.warehouse.utils.dto.needs.WareNeedTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.WareNeedTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 28.02.2009
 */
@Transactional(rollbackFor = BusinessException.class)
public class WareNeedService {
    
    private WareNeedDAO wareNeedDAO;

    private WareNeedItemDAO wareNeedItemDAO;

    public WareNeedService() { /* Default constructor */ }

    /**
     * Loads ware needs version, available for editing. Inthis version need items, that have been ordered or
     * shipped are not shown.
     * @param wareNeedId
     * @return
     */
    public WareNeedTO getWareNeedForEditing(long wareNeedId) {
        return WareNeedTransformer.transformWareNeed(wareNeedDAO.getWareNeedForEditing(wareNeedId));
    }

    public void addItemToWareNeed(WareNeedItemTO newWareNeedItemTO) {
        WareNeedItem newWareNeedItem = WareNeedTransformer.transformItem(newWareNeedItemTO);
        WareNeedTransformer.updateItem(newWareNeedItem, newWareNeedItemTO);
        if (newWareNeedItem.getNumber() == null){
            newWareNeedItem.setNumber(wareNeedItemDAO.getNextAvailableNumber(newWareNeedItem.getWareNeed().getId()));
        }
        wareNeedItemDAO.save(newWareNeedItem);
        WareNeedTransformer.updateItem(newWareNeedItemTO, newWareNeedItem);
    }

    public void deleteItemFromWareNeed(WareNeedItemTO wareNeedItem) {
        WareNeedItem persistentWareNeedItem = wareNeedItemDAO.get(wareNeedItem.getId());
        //1. Deletes item from the ware need.
        if (persistentWareNeedItem != null){
            wareNeedItemDAO.remove(persistentWareNeedItem);
        }
        //2. Updating numbers of the items, next to the deteting item.
        //TODO: think, should it be implemented of not
    }

    public void saveWareNeedItem(WareNeedItemTO wareNeedItemTO) {
        WareNeedItem wareNeedItem = WareNeedTransformer.transformItem(wareNeedItemTO);
        WareNeedTransformer.updateItem(wareNeedItem, wareNeedItemTO);
        wareNeedItemDAO.save(wareNeedItem);
    }

    public void saveWareNeedItem(WareNeedItem wareNeedItem) {
        wareNeedDAO.save(wareNeedItem);
    }

    /**
     * Now there is only one ware need in the system is supported. User this method to
     * obtain identifier of this "main" ware need.
     * @return
     */
    public long getMainWareNeedId(){
        return 1;
    }

    public WareNeedItem getWareNeedItem(long wareNeedItemId) {
        return wareNeedItemDAO.get(wareNeedItemId);
    }

    public WareNeed getWareNeed(long wareNeedId) {
        return wareNeedDAO.get(wareNeedId);
    }

    private void deleteItemFromWareNeed(WareNeedItem itemToDelete) {
        itemToDelete.getWareNeed().getItems().remove(itemToDelete);
        wareNeedItemDAO.remove(itemToDelete);
    }

    /**
     * Processes adding ware need item to the purchase.
     * @param purchaseItem
     */
    public void onWareNeedItemAddedToPurchase(PurchaseItem purchaseItem) throws BusinessException {
        WareNeedItem addedNeedItem = purchaseItem.getWareNeedItem();
        if (addedNeedItem.getState() != WareNeedItemState.NOT_FOUND) {
            throw new BusinessException(I18nSupport.message("wareNeed.operation.error.wareNeedItem.already.processed"));
        }

        if (addedNeedItem.getAutoCreated()) {
            addedNeedItem.setAmount(purchaseItem.getAmount());
        }
        else if (purchaseItem.getAmount() < addedNeedItem.getAmount()) {
            //Not all ware need will be satisfied be adding it to the purchase item. So,
            //we must split ware need item into two elements with first, added to the purchase.
            WareNeedItem unsatisfiedNeedItem = new WareNeedItem(addedNeedItem);
            unsatisfiedNeedItem.setAmount(addedNeedItem.getAmount() - purchaseItem.getAmount());
            addedNeedItem.setAmount(purchaseItem.getAmount());

            if (addedNeedItem.getSubNumber() == null) {
                //Ware need items is being splitter for the first time.
                addedNeedItem.setSubNumber(1L);
                unsatisfiedNeedItem.setSubNumber(2L);
            }
            else{
                //Ware need item was splitter earlier.
                unsatisfiedNeedItem.setSubNumber(wareNeedItemDAO.getNextAvailableSubNumber(unsatisfiedNeedItem.getWareNeed().getId(), unsatisfiedNeedItem.getNumber()));
            }
            wareNeedItemDAO.save(unsatisfiedNeedItem);
        }
        addedNeedItem.setState(WareNeedItemState.FOUND);
        addedNeedItem.setPurchaseItem(purchaseItem);
    }

    /**
     * Processes deleting ware need item from the purchase.
     * @param purchaseItem
     */
    public void onWareNeedItemDeletedFromPurchase(PurchaseItem purchaseItem) {
        WareNeedItem deletedNeedItem = purchaseItem.getWareNeedItem();
        deletedNeedItem.setState(WareNeedItemState.NOT_FOUND);
        deletedNeedItem.setPurchaseItem(null);

        //Now we try to find same ware need item and unite it with deleted ware need item.
        List<WareNeedItem> sameWareNeedItems = wareNeedItemDAO.findSameWareNeedItems(deletedNeedItem);
        if (sameWareNeedItems.size() > 0) {
            for (WareNeedItem sameItem : sameWareNeedItems) {
                if (sameItem.getState() == deletedNeedItem.getState()) {
                    //We have found exactly the same ware need item.
                    sameItem.setAmount(sameItem.getAmount() + deletedNeedItem.getAmount());
                    if (sameWareNeedItems.size() == 1) {
                        //Remaining ware need item is the last with such number. So, we don't need sub nembers any more.
                        sameItem.setSubNumber(null);
                    }
                    deleteItemFromWareNeed(deletedNeedItem);
                    wareNeedItemDAO.save(sameItem);
                    break;
                }
            }
        }
        else {
            //No same items found. So, we should left this ware need.
            wareNeedItemDAO.save(deletedNeedItem);
        }
    }

    /**
     * Process changing of a purchase item, related to the ware need item.
     * @param purchaseItem
     */
    public void onWareNeedItemInPurchaseChanged(PurchaseItem purchaseItem) {
        WareNeedItem changedNeedItem = purchaseItem.getWareNeedItem();
        if (changedNeedItem.getAmount().equals(purchaseItem.getAmount())) {
            //Count of items not changes. Nothing to update.
            return;
        }

        if (!changedNeedItem.getAutoCreated()) {
            //Now we try to find same ware need item and unite it with deleted ware need item.
            List<WareNeedItem> sameWareNeedItems = wareNeedItemDAO.findSameWareNeedItems(changedNeedItem);
            long countDiff = purchaseItem.getAmount() - changedNeedItem.getAmount();
            if (countDiff > 0) {
                //Count, specified in the purchase was encreased, and we can to satisfy more same ware needs.
                List<WareNeedItem> removedItems = new ArrayList<WareNeedItem>();
                for (WareNeedItem sameItem : sameWareNeedItems){
                    if (sameItem.getState() == WareNeedItemState.NOT_FOUND) {
                        if (countDiff >= sameItem.getAmount()) {
                            //Additional count of the same item will be satisfied by changed ware need item.
                            deleteItemFromWareNeed(sameItem);
                            removedItems.add(sameItem);

                            countDiff -= sameItem.getAmount();
                            if (countDiff == 0) {
                                break;
                            }
                        }
                        else{
                            //Additional count can satisfy only a part of other same ware need.
                            sameItem.setAmount(sameItem.getAmount() - countDiff);
                            wareNeedItemDAO.save(sameItem);
                            break;
                        }
                    }
                }
                if (removedItems.size() == sameWareNeedItems.size()) {
                    //Ware need item is individual and it needn't subnumber.
                    changedNeedItem.setSubNumber(null);
                }
            }
            else {
                //Count, specified in the purchase was decreased, and we are to increase count in one of
                //other same ware needs or create unsatisfied need with the difference of ware quantity.
                for (WareNeedItem sameItem : sameWareNeedItems) {
                    if (sameItem.getState() == WareNeedItemState.NOT_FOUND) {
                        sameItem.setAmount(sameItem.getAmount() - countDiff); //Remember, that countDiff < 0!!!
                        wareNeedItemDAO.save(sameItem);
                        countDiff = 0;
                        break;
                    }
                }
                if (countDiff != 0) {
                    //We couldn't found the same item to put difference of quantity into it, so we are the create
                    //new ware need utem for it.
                    WareNeedItem newItem = new WareNeedItem(changedNeedItem);
                    newItem.setState(WareNeedItemState.NOT_FOUND);
                    newItem.setAmount(-countDiff);
                    newItem.setSubNumber(wareNeedItemDAO.getNextAvailableSubNumber(newItem.getWareNeed().getId(), newItem.getNumber()));
                    wareNeedItemDAO.save(newItem);

                    if (changedNeedItem.getSubNumber() == null) {
                        changedNeedItem.setSubNumber(wareNeedItemDAO.getNextAvailableSubNumber(changedNeedItem.getWareNeed().getId(), changedNeedItem.getNumber()));
                    }
                }
            }
        }
        
        changedNeedItem.setAmount(purchaseItem.getAmount());
        wareNeedItemDAO.save(changedNeedItem);
    }

    //============================= Spring setters ==========================================
    public void setWareNeedDAO(WareNeedDAO wareNeedsDAO) {
        this.wareNeedDAO = wareNeedsDAO;
    }

    public void setWareNeedItemDAO(WareNeedItemDAO wareNeedItemDAO) {
        this.wareNeedItemDAO = wareNeedItemDAO;
    }
}
