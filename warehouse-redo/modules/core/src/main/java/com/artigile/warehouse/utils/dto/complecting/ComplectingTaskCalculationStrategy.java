/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.complecting;

import com.artigile.warehouse.utils.custom.types.CompositeNumber;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

/**
 * @author Shyrik, 10.12.2009
 */

/**
 * Interface of strategy, used for calculation of complecting task's calculated fields.
 */
interface ComplectingTaskCalculationStrategy {
    String getTargetLocation(ComplectingTaskTO complectingTask);

    Long getParcelNo(ComplectingTaskTO complectingTask);

    CompositeNumber getItemNo(ComplectingTaskTO complectingTask);

    String getItemName(ComplectingTaskTO complectingTask);

    String getItemMisc(ComplectingTaskTO complectingTask);

    String getItemType(ComplectingTaskTO complectingTask);

    String getItemMeas(ComplectingTaskTO complectingTask);

    Long getRemainder(ComplectingTaskTO complectingTask);

    Integer getYear(ComplectingTaskTO complectingTask);

    WarehouseTOForReport getWarehouse(ComplectingTaskTO complectingTask);

    String getStoragePlace(ComplectingTaskTO complectingTask);

    Long getFillRate(ComplectingTaskTO complectingTask);

    String getNotice(ComplectingTaskTO complectingTask);

    String getArticle(ComplectingTaskTO complectingTask);

    String getBarCode(ComplectingTaskTO complectingTask);

    String getWarehouseNotice(ComplectingTaskTO complectingTask);
}
