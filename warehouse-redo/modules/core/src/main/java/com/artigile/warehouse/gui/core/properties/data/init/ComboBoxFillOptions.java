/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.data.init;

/**
 * @author Shyrik, 01.03.2010
 */

/**
 * Options, that are applied when combo box is filled with InitUtils functions.
 */
public class ComboBoxFillOptions {
    /**
     * If set, than "not selected" item should be added to the list.
     */
    private boolean addNotSelectedItem;

    /**
     * If set then "Not selected" item should be selected by default.
     */
    private boolean selectNotSelectedByDefault;

    public ComboBoxFillOptions setAddNotSelectedItem(boolean addNotSelectedItem){
        this.addNotSelectedItem = addNotSelectedItem;
        return this;
    }

    public boolean getAddNotSelectedItem(){
        return addNotSelectedItem;
    }

    public boolean isSelectNotSelectedByDefault() {
        return selectNotSelectedByDefault;
    }

    public ComboBoxFillOptions setSelectNotSelectedByDefault(boolean selectNotSelectedByDefault) {
        this.selectNotSelectedByDefault = selectNotSelectedByDefault;
        return this;
    }
}
