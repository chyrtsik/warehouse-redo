/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting.printing;

import com.artigile.warehouse.gui.menuitems.complecting.complectingTasks.ComplectingTaskList;
import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 16.05.2009
 */

/**
 * Object, holding informationg about complecting tasks to be printed.
 */
public class TasksForPrinting implements ComplectingTasksForPrinting {
    private WarehouseTOForReport warehouse;

    /**
     * Filtered list of complecting tasks to be printed.
     */
    private List<ComplectingTaskTO> complectingTasks = new ArrayList<ComplectingTaskTO>();

    public TasksForPrinting(ComplectingTaskList tasksList, ComplectingTasksToPrintFilterType whatToPrint) {
        this.warehouse = tasksList.getWarehouse();
        this.complectingTasks = tasksList.getFilteredTasksList(whatToPrint, false);
    }

    @Override
    public WarehouseTOForReport getWarehouse() {
        return warehouse;
    }

    @Override
    public List<ComplectingTaskTO> getItems(){
        return complectingTasks;
    }
}
