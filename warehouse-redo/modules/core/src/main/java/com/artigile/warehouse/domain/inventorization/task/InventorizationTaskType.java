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
 * All possible warehouse task types
 */
public enum InventorizationTaskType {
    /**
     * Inventorization type. task for Warehouse to perform inventarization
     */
    INVENTORIZATION(I18nSupport.message("inventorization.task.type.name"));

    //===================== Naming impementation =================================
    private String name;

    InventorizationTaskType(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
