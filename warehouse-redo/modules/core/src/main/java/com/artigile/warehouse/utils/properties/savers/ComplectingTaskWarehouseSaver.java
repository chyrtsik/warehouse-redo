/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.properties.savers;

import com.artigile.warehouse.bl.warehouse.WarehouseService;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.properties.Properties;

/**
 * @author Valery Barysok, 24.08.2010
 */
public class ComplectingTaskWarehouseSaver extends Saver {
    private static String WAREHOUSE = "warehouse";

    public static void store(WarehouseTOForReport warehouse, String frameId) {
        Properties.setProperty(getId(frameId, WAREHOUSE), String.valueOf(warehouse.getId()));
    }

    public static WarehouseTOForReport restore(WarehouseTOForReport warehouse, String frameId) {
        Long wId = Properties.getPropertyAsLong(getId(frameId, WAREHOUSE));
        if (wId != null && warehouse != null && !wId.equals(warehouse.getId())) {
            WarehouseService warehouseService = SpringServiceContext.getInstance().getWarehouseService();
            return warehouseService.getWarehouseForReport(wId);
        }
        return warehouse;
    }

}
