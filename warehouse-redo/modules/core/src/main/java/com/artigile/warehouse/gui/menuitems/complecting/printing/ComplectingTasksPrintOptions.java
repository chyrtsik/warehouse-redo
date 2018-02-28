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

/**
 * @author Shyrik, 16.05.2009
 */

/**
 * This class holds options for printing complecting tasks.
 */
public class ComplectingTasksPrintOptions {
    /**
     * Is true, a tasks list for warehouse worker will be printed.
     */
    private boolean printPositions;

    /**
     * If true, stickers for each order item will be printed.
     */
    private boolean printStickers;

    /**
     * Defines a rule for choosing items to be printed.
     */
    private ComplectingTasksToPrintFilterType whatToPrint = ComplectingTasksToPrintFilterType.ALL;

    //============================= Getters and setters ===============================
    public boolean isPrintPositions() {
        return printPositions;
    }

    public void setPrintPositions(boolean printTasksList) {
        this.printPositions = printTasksList;
    }

    public boolean isPrintStickers() {
        return printStickers;
    }

    public void setPrintStickers(boolean printStickers) {
        this.printStickers = printStickers;
    }

    public ComplectingTasksToPrintFilterType getWhatToPrint() {
        return whatToPrint;
    }

    public void setWhatToPrint(ComplectingTasksToPrintFilterType whatToPrint) {
        this.whatToPrint = whatToPrint;
    }
}
