/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.inventorization.task.printing;

/**
 * @author Borisok V.V., 03.10.2009
 */

/**
 * This class holds options for printing inventorization tasks.
 */
public class InventorizationTasksPrintOptions {

    /**
     * Defines a rule for choosing items to be printed.
     */
    private InventorizationTasksFilterType whatToPrint = InventorizationTasksFilterType.ALL;

    public InventorizationTasksFilterType getWhatToPrint() {
        return whatToPrint;
    }

    public void setWhatToPrint(InventorizationTasksFilterType whatToPrint) {
        this.whatToPrint = whatToPrint;
    }
}
