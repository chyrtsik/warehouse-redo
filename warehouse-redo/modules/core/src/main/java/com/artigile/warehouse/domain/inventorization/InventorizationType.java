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
 * Possible types of the inventorization.
 */
public enum InventorizationType {
    /**
     * Inventorization is created for checking given warehouse storage places.
     */
    BY_WAREHOUSE_PLACES(I18nSupport.message("inventorization.type.by.warehouse.places"));

    //================= Enum values naming implementation ============================
    private String name;

    InventorizationType(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
