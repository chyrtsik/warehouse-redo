/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.inventorization.task;

import com.artigile.warehouse.gui.core.report.controller.CellEditingStrategy;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.inventorization.task.InventorizationTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

/**
 * @author Borisok V.V., 03.10.2009
 */

/**
 * Saver for found amounts of ware, entered by warehouse worker after inventorization.
 */
public class InventorizationTaskFoundCountSaver implements CellEditingStrategy {
    /**
     * Rule for checking of availabity to edit a inventorization task.
     */
    private InventorizationTaskEditAvailability editAvailability;

    public InventorizationTaskFoundCountSaver(UserTO worker, WarehouseTOForReport warehouse){
        editAvailability = new InventorizationTaskEditAvailability(worker, warehouse);
    }

    public void setWarehouse(WarehouseTOForReport warehouse) {
        editAvailability.setWarehouse(warehouse);
    }

    @Override
    public boolean isEditable(Object reportItem) {
        //User is needed to be permitted to edit inventorization tasks and
        // inventorization task should be in process of counting.
        InventorizationTaskTO task = (InventorizationTaskTO)reportItem;
        return editAvailability.isAvailable(null) && task.isInProcess();
    }

    @Override
    public void saveValue(Object reportItem, Object newValue) {
        InventorizationTaskTO task = (InventorizationTaskTO)reportItem;
        String value = (String)newValue;
        Long count = null;
        if (!value.isEmpty()){
            if (!StringUtils.isNumberLong(value)){
                return;
            }
            count = Long.valueOf(value);
            if (count < 0) {
                return;
            }
        }
        //Saving new found count value.
        SpringServiceContext.getInstance().getInventorizationTaskService().saveNewTaskFoundCount(task, count);
    }
}
