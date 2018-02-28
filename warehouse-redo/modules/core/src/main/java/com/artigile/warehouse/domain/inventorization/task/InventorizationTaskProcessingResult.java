/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.inventorization.task;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Borisok V.V., 30.09.2009
 */

/**
 * All possible processing result of counting.
 */
public enum InventorizationTaskProcessingResult {
    /**
     * In this state processing result of counting we have lack.
     */
    LACK_COUNT(I18nSupport.message("inventorization.task.counting.processing.result.lack")),

    /**
     * In this state processing result of counting we have surplus.
     */
    SURPLUS_COUNT(I18nSupport.message("inventorization.task.counting.processing.result.surplus")),

    /**
     * In this state processing result of counting we have true count.
     */
    TRUE_COUNT(I18nSupport.message("inventorization.task.counting.processing.result.true")),;

    //===================== Naming impementation =================================
    private String name;

    InventorizationTaskProcessingResult(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
