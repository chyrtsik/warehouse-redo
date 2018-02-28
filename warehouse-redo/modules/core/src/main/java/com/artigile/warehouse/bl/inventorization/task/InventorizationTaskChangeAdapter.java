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
 * @author Shyrik, 25.10.2009
 */
public class InventorizationTaskChangeAdapter implements InventorizationTaskChangeListener {
    @Override
    public void onTasksStateChaged(List<InventorizationTask> tasks) throws BusinessException {
    }
}
