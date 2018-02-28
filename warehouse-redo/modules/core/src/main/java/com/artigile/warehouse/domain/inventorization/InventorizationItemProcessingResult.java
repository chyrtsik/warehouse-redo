/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.inventorization;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Borisok V.V., 07.10.2009
 */

/**
 * All possible item processing result of counting.
 */
public enum InventorizationItemProcessingResult {
    /**
     * In this state item processing result of counting we have lack.
     */
    LACK_COUNT(I18nSupport.message("inventorization.item.counting.processingResult.lack")),

    /**
     * In this state item processing result of counting we have surplus.
     */
    SURPLUS_COUNT(I18nSupport.message("inventorization.item.counting.processingResult.surplus")),

    /**
     * In this state item processing result of counting we have true count.
     */
    TRUE_COUNT(I18nSupport.message("inventorization.item.counting.processingResult.true")),;

    //===================== Naming impementation =================================
    private String name;

    InventorizationItemProcessingResult(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
