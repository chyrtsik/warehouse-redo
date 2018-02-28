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

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Borisok V.V., 03.10.2009
 */

/**
 * Enumeration, which elements specifies different print targets.
 */
public enum InventorizationTasksFilterType {
    /**
     * Print all inventorization tasks for warehouse worker.
     */
    ALL(I18nSupport.message("inventorization.task.print.form.all")),

    /**
     * Not printed inventorization tasks.
     */
    NOT_PRINTED(I18nSupport.message("inventorization.task.print.form.not.printed")),

    /**
     * Pring currently selected inventorization tasks.
     */
    SELECTED(I18nSupport.message("inventorization.task.print.form.selected"));

    //===================== Naming impementation =================================
    private String name;

    InventorizationTasksFilterType(String name) {
        this.name = name;
    }

    /**
     * Use this method to retrive names of each print target.
     * @return
     */
    public String getName() {
        return name;
    }
}
