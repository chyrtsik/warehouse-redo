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
 * @author Borisok V.V., 10.10.2009
 */

/**
 * All possible states of inventorization item.
 */
public enum InventorizationItemState {
    /**
     * In this state inventorization item is being not processed by workers.
     */
    NOT_PROCESSED(I18nSupport.message("inventorization.item.state.notProcessed")),

    /**
     * In this state inventorization item is being processed by workers.
     */
    IN_PROCESS(I18nSupport.message("inventorization.item.state.inProcess")),

    /**
     * Task has been processed by warehouse workers and is ready to be closed.
     */
    PROCESSED(I18nSupport.message("inventorization.item.state.processed")),;

    //===================== Naming impementation =================================
    private String name;

    InventorizationItemState(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    /**
     * Use this method to get initial state (at the creation of the inventorization item).
     * @return
     */
    public static InventorizationItemState getInitialState() {
        return NOT_PROCESSED;
    }
}
