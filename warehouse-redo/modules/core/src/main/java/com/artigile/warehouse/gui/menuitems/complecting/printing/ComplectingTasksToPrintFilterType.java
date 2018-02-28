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

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 16.05.2009
 */

/**
 * Enumeration, which elements specifies different print targets.
 */
public enum ComplectingTasksToPrintFilterType {
    /**
     * Print all complecting tasks for warehouse worker.
     */
    ALL,

    /**
     * Not printed complecting tasks.
     */
    NOT_PRINTED,

    /**
     * Problem complecting tasks (for example, when not found wares).
     */
    PROBLEM,

    /**
     * Pring currently selected tasks.
     */
    SELECTED;

    /**
     * User this method to retrieve names of each print target.
     * @return
     */
    public String getName(){
        if (this.equals(ALL)){
            return I18nSupport.message("complectingTask.printForm.all");
        }
        else if (this.equals(NOT_PRINTED)){
            return I18nSupport.message("complectingTask.printForm.notPrinted");
        }
        else if (this.equals(PROBLEM)){
            return I18nSupport.message("complectingTask.printForm.problem");
        }
        else if (this.equals(SELECTED)){
            return I18nSupport.message("complectingTask.printForm.selected");
        }
        else {
            throw new RuntimeException("Invalid ComplectingTasksToPrintFilterType value.");
        }
    }
}
