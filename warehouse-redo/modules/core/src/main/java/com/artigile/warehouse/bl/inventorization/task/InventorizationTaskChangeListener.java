/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.inventorization.task;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.domain.inventorization.task.InventorizationTask;

import java.util.List;

/**
 * @author Shyrik, 18.10.2009
 */

/**
 * Interface, that should be implemented for synchronization with changes in
 * inventorization tasks.
 */
public interface InventorizationTaskChangeListener {
    
    /**
     * Called, when inventorization tasks state has been changed.
     * @param tasks changed tasks list.
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    void onTasksStateChaged(List<InventorizationTask> tasks) throws BusinessException;
}
