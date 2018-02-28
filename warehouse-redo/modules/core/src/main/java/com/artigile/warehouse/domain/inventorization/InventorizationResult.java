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
 * @author Shyrik, 29.09.2009
 */

/**
 * Possible results of inventorizations.
 */
public enum InventorizationResult {
    /**
     * Resision has been ended with no errors.
     */
    SUCCESS(I18nSupport.message("inventorization.result.success")),

    /**
     * Resision has been ended with some errors.
     */
    PROBLEM(I18nSupport.message("inventorization.result.problem"));
    
    //===================== Naming impementation =================================
    private String name;

    InventorizationResult(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
