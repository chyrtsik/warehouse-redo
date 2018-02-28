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
 * All possible states of inventorizations.
 */
public enum InventorizationState {
    /**
     * Initial state of the inventorization. Inventorization has been created, by not processed yet.
     */
    NOT_PROCESSED(I18nSupport.message("inventorization.state.not.processed")),

    /**
     * In this state inventorization is being processed by workers.
     */
    IN_PROCESS(I18nSupport.message("inventorization.state.in.process")),

    /**
     * Inventorization has been processed by warehouse workers and is ready to be closed.
     */
    PROCESSED(I18nSupport.message("inventorization.state.processed")),

    /**
     * Inventorization has been closed.
     */
    CLOSED(I18nSupport.message("inventorization.state.closed")),

    /**
     * Surrogate inventorization state.
     * Inventorization is marked deleted before being deleted. 
     */
    DELETED("Deleted");

    //===================== Naming impementation =================================
    private String name;

    InventorizationState(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
