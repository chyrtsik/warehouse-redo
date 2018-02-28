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

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 08.11.2009
 */

/**
 * Enumerates possible posting types (only actual for GUI).
 */
public enum PostingType {
    SIMPLE(I18nSupport.message("posting.postingType.simple")),

    FROM_PURCHASE(I18nSupport.message("posting.postingType.fromPurchase")),

    FROM_DELIVERY_NOTE(I18nSupport.message("posting.postingType.fromDeliveryNote"));

    //===================== Enum values naming =========================
    private String name;

    private PostingType(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
