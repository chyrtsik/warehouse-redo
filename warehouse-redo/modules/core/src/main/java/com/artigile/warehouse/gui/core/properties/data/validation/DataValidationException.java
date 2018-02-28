/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.data.validation;

import javax.swing.*;

/**
 * @author Shyrik, 07.12.2008
 */

/**
 * This exception is used by validation to indicate a component with
 * error in data, that are entered by user.
 */
public class DataValidationException extends Exception {
    /**
     * Component, where intered data is invalid.
     */
    private JComponent component;

    public DataValidationException(String message){
        super(message);
    }

    public DataValidationException(JComponent component, String message){
        super(message);
        this.component = component;
    }

    //========================= Getters =========================
    public JComponent getComponent(){
        return component;
    }
}
