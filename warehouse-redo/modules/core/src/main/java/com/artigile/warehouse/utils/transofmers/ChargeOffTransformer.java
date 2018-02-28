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

import com.artigile.warehouse.domain.chargeoff.ChargeOff;
import com.artigile.warehouse.domain.chargeoff.ChargeOffItem;
import com.artigile.warehouse.utils.dto.chargeoff.ChargeOffItemTO;
import com.artigile.warehouse.utils.dto.chargeoff.ChargeOffTO;
import com.artigile.warehouse.utils.dto.chargeoff.ChargeOffTOForReport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 09.10.2009
 */
public final class ChargeOffTransformer {
    private ChargeOffTransformer(){
    }

    public static ChargeOffTO transform(ChargeOff chargeOff) {
        ChargeOffTO chargeOffTO = new ChargeOffTO();
        update(chargeOffTO, chargeOff);
        chargeOffTO.setItems(transformItemsList(chargeOff.getItems()));
        return chargeOffTO;
    }

    public static ChargeOffTOForReport transformForReport(ChargeOff chargeOff) {
        if (chargeOff == null){
            return null;
        }
        ChargeOffTOForReport chargeOffTO = new ChargeOffTOForReport();
        update(chargeOffTO, chargeOff);
        return chargeOffTO;
    }

    public static List<ChargeOffTOForReport> transformListForReport(List<ChargeOff> chargeOffs) {
        List<ChargeOffTOForReport> chargeOffTOs = new ArrayList<ChargeOffTOForReport>();
        for (ChargeOff chargeOff : chargeOffs){
            chargeOffTOs.add(transformForReport(chargeOff));
        }
        return chargeOffTOs;
    }

    private static List<ChargeOffItemTO> transformItemsList(List<ChargeOffItem> items) {
        List<ChargeOffItemTO> itemTOs = new ArrayList<ChargeOffItemTO>();
        for (ChargeOffItem item : items){
            itemTOs.add(transformItem(item));
        }
        return itemTOs;
    }

    public static ChargeOffItemTO transformItem(ChargeOffItem item) {
        ChargeOffItemTO itemTO = new ChargeOffItemTO();
        updateItem(itemTO, item);
        return itemTO;
    }

    /**
     * @param itemTO (in, out) -- DTO to be synchonized with entity.
     * @param item (in) -- entity with fresh data.
     */
    private static void updateItem(ChargeOffItemTO itemTO, ChargeOffItem item) {
        itemTO.setId(item.getId());
        itemTO.setNumber(item.getNumber());
        itemTO.setDetailBatch(DetailBatchTransformer.batchTO(item.getDetailBatch()));
        itemTO.setStoragePlace(StoragePlaceTransformer.transformForReport(item.getStoragePlace()));
        itemTO.setCount(item.getAmount());
        itemTO.setNotice(item.getNotice());
    }

    /**
     * @param chargeOffTO (in, out) -- DTO to be updated.
     * @param chargeOff (in) -- Entity with fresh data.
     */
    private static void update(ChargeOffTOForReport chargeOffTO, ChargeOff chargeOff) {
        chargeOffTO.setId(chargeOff.getId());
        chargeOffTO.setNumber(chargeOff.getNumber());
        chargeOffTO.setWarehouse(WarehouseTransformer.transformForReport(chargeOff.getWarehouse()));
        chargeOffTO.setPerformer(UserTransformer.transformUser(chargeOff.getPerformer()));
        chargeOffTO.setPerformDate(chargeOff.getPerformDate());
        chargeOffTO.setNotice(chargeOff.getNotice());
        chargeOffTO.setReason(chargeOff.getReason());
    }
}
