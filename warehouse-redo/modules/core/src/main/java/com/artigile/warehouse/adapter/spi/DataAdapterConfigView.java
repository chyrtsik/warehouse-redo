/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi;

import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;

import javax.swing.*;

/**
 * @author Valery Barysok, 6/7/11
 */

public interface DataAdapterConfigView {
    
    public JComponent getView();
    
    public void setConfigurationString(String conf);

    public String getConfigurationString();

    void validateData() throws DataValidationException;
}
