/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties;

import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;

import javax.swing.*;

/**
 * @author Shyrik, 07.12.2008
 */

/**
 * Interface of the properties strategy.
 */
public interface PropertiesForm {
    /**
     * Must return title of the properties dialog.
     */
    public String getTitle();
    
    /**
     * Must return panel, that is containing all components to be shown in the dialog.
     */
    public JPanel getContentPanel();

    /**
     * Must return true, if user can press OK button, and false, if can't.
     */
    public boolean canSaveData();

    /**
     * Called when it's time to load datas into controls.
     */
    public void loadData();

    /**
     * Called, when it's time to check data, that have been entered by user.
     * @throws DataValidationException - if there are any validation errors.
     */
    public void validateData() throws DataValidationException;

    /**
     * Called, when it's time to save data, entered (and validated) by user into the controls.
     */
    public void saveData();
}
