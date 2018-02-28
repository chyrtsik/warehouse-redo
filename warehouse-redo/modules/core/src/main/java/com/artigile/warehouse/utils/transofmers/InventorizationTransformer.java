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

import com.artigile.warehouse.domain.inventorization.Inventorization;
import com.artigile.warehouse.domain.inventorization.InventorizationItem;
import com.artigile.warehouse.domain.inventorization.InventorizationItemState;
import com.artigile.warehouse.domain.inventorization.InventorizationState;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationTO;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationTOForReport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 29.09.2009
 */
public final class InventorizationTransformer {
    private InventorizationTransformer() {
    }

    public static List<InventorizationTOForReport> transformListForReport(List<Inventorization> inventorizations) {
        List<InventorizationTOForReport> inventorizationTOs = new ArrayList<InventorizationTOForReport>();
        for (Inventorization inventorization : inventorizations){
          inventorizationTOs.add(transformForReport(inventorization));
        }
        return inventorizationTOs;
    }

    public static InventorizationTOForReport transformForReport(Inventorization inventorization) {
        InventorizationTOForReport inventorizationTO = new InventorizationTOForReport();
        update(inventorizationTO, inventorization);
        return inventorizationTO;
    }

    public static Inventorization transformFromReport(InventorizationTOForReport inventorizationTO) {
        if (inventorizationTO == null){
            return null;
        }
        Inventorization inventorization = SpringServiceContext.getInstance().getInventorizationService().getInventorizationById(inventorizationTO.getId());
        if (inventorization == null){
            inventorization = new Inventorization();
        }
        return inventorization;
    }

    public static InventorizationTO transform(Inventorization inventorization) {
        InventorizationTO inventorizationTO = new InventorizationTO();
        update(inventorizationTO, inventorization);
        inventorizationTO.setItems(InventorizationItemTransformer.transformList(inventorization.getItems(), inventorizationTO));
        return inventorizationTO;
    }

    /**
     *
     * @param items which need to check for completed
     * @return count of completed processings of inventorization items
     */
    private static int getCompleted(List<InventorizationItem> items) {
        int completed = 0;
        for (InventorizationItem item : items) {
            if (item.getState().equals(InventorizationItemState.PROCESSED)) {
                ++completed;
            }
        }

        return completed;
    }

    /**
     * Synchonizes DTO with entity.
     * @param inventorizationTO (in, out) -- DTO to be updated.
     * @param inventorization (in) -- entity with fresh data.
     */
    public static void update(InventorizationTOForReport inventorizationTO, Inventorization inventorization) {
        inventorizationTO.setId(inventorization.getId());
        inventorizationTO.setNumber(inventorization.getNumber());
        inventorizationTO.setType(inventorization.getType());
        inventorizationTO.setState(inventorization.getState());
        inventorizationTO.setResult(inventorization.getResult());
        inventorizationTO.setCreateDate(inventorization.getCreateDate());
        inventorizationTO.setCreateUser(UserTransformer.transformUser(inventorization.getCreateUser()));
        inventorizationTO.setCloseDate(inventorization.getCloseDate());
        inventorizationTO.setCloseUser(UserTransformer.transformUser(inventorization.getCloseUser()));
        inventorizationTO.setWarehouse(WarehouseTransformer.transform(inventorization.getWarehouse()));
        inventorizationTO.setNotice(inventorization.getNotice());

        if (!inventorization.getState().equals(InventorizationState.DELETED)){
            inventorizationTO.setStoragePlacesToCheck(StoragePlaceTransformer.transformListForReport(inventorization.getStoragePlacesToCheck()));

            // how many positions are processed have sense only for state in process and processed
            InventorizationState state = inventorization.getState();
            if(state.equals(InventorizationState.IN_PROCESS) || state.equals(InventorizationState.PROCESSED)) {
                List<InventorizationItem> items = inventorization.getItems();
                int completed = getCompleted(items);
                inventorizationTO.setCompleted(completed);
                inventorizationTO.setTotal(items.size());
            }
        }
    }

    /**
     * Synchronizes entity with DTO.
     * @param inventorization (in, out) -- entity to be updated.
     * @param inventorizationTO (in) -- DTO with fresh data.
     */
    public static void update(Inventorization inventorization, InventorizationTOForReport inventorizationTO) {
        inventorization.setNumber(inventorizationTO.getNumber());
        inventorization.setType(inventorizationTO.getType());
        inventorization.setState(inventorizationTO.getState());
        inventorization.setResult(inventorizationTO.getResult());
        inventorization.setCreateDate(inventorizationTO.getCreateDate());
        inventorization.setCreateUser(UserTransformer.transformUser(inventorizationTO.getCreateUser()));
        inventorization.setCloseDate(inventorizationTO.getCloseDate());
        inventorization.setCloseUser(UserTransformer.transformUser(inventorizationTO.getCloseUser()));
        inventorization.setWarehouse(WarehouseTransformer.transform(inventorizationTO.getWarehouse()));
        inventorization.setStoragePlacesToCheck(StoragePlaceTransformer.transformListFromReport(inventorizationTO.getStoragePlacesToCheck()));
        inventorization.setNotice(inventorizationTO.getNotice());
    }
}
