/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.warehouseBatch;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;

/**
 * Listener interface to be implemented by any code depending on changes in warehouse batch.
 * <strong>IMPORTANT:</strong> If listener throws exception when warehouse batch chang is rolled back.
 *
 * @author Aliaksandr.Chyrtsik, 02.10.11
 */
public interface WarehouseBatchChangeListener {
    /**
     * Called when warehouse batch count is changes.
     * @param event full information about count change.
     * @throws BusinessException if listener cannot perform related operations and change should be rolled back.
     */
    public void onWarehouseBatchCountChanged(WarehouseBatchCountChangeEvent event) throws BusinessException;
}
