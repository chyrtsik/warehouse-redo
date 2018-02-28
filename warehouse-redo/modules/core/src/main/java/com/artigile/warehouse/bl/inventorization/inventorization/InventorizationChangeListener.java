/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.inventorization.inventorization;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.domain.inventorization.Inventorization;
import com.artigile.warehouse.domain.inventorization.InventorizationState;

/**
 * @author Shyrik, 24.10.2009
 */

/**
 * Listener, that may be implemented for synchonization with inventorization
 * document states.
 */
public interface InventorizationChangeListener {
    
    /**
     * This method is called after inventorization was moved into processing state.
     * @param inventorization entity being changed.
     * @param oldState
     * @param newState @throws CannotPerformOperationException
     */
    public void onInventorizationStateChanged(Inventorization inventorization, InventorizationState oldState, InventorizationState newState) throws BusinessException;
}
