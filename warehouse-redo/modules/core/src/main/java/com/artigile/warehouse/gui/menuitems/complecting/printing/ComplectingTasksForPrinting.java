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

import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

import java.util.List;

/**
 * @author Shyrik, 23.06.2009
 */
public interface ComplectingTasksForPrinting {
    WarehouseTOForReport getWarehouse();

    List<ComplectingTaskTO> getItems();
}
