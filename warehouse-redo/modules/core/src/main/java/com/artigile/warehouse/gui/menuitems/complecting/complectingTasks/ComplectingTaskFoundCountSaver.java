/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting.complectingTasks;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.controller.CellEditingStrategy;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

/**
 * @author Shyrik, 17.05.2009
 */

/**
 * Saver for found amounts of ware, entered by warehouse worker after complecting.
 */
public class ComplectingTaskFoundCountSaver implements CellEditingStrategy {
    /**
     * Rule for checking of availabity to edit a comlecting task.
     */
    private ComplectingTaskEditAvailability editAvailability;

    public ComplectingTaskFoundCountSaver(UserTO worker, WarehouseTOForReport warehouse){
        editAvailability = new ComplectingTaskEditAvailability(worker, warehouse);
    }

    public void setWarehouse(WarehouseTOForReport warehouse) {
        editAvailability.setWarehouse(warehouse);
    }

    @Override
    public boolean isEditable(Object reportItem) {
        //User is needed to be permitted to edit complecting tasks and complecting task should be
        //in process of complecting.
        ComplectingTaskTO task = (ComplectingTaskTO)reportItem;
        return editAvailability.isAvailable(null) &&
               (task.getState() == ComplectingTaskState.PROCESSING ||
                task.getState() == ComplectingTaskState.PROCESSED);
    }

    @Override
    public void saveValue(Object reportItem, Object newValue) {
        ComplectingTaskTO task = (ComplectingTaskTO)reportItem;
        String value = (String)newValue;
        Long count = null;
        if (!value.isEmpty()){
            if (!StringUtils.isNumberLong(value)){
                return;
            }
            count = Long.valueOf(value);
            if (count < 0 || count > task.getNeededCount()){
                return;
            }
        }
        //Saving new found count value.
        try {
            SpringServiceContext.getInstance().getComplectingTaskService().saveNewTaskFoundCount(task, count);
        } catch (BusinessException e) {
            MessageDialogs.showWarning(e.getMessage());
        }
    }
}
