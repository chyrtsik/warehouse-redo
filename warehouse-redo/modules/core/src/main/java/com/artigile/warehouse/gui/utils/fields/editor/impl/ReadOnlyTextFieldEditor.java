/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.utils.fields.editor.impl;

import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;

/**
 * Editor for the field, calculated with the help of string template or other automatic mechanism.
 * This "editor" just displays values of a field.
 * @author Shyrik, 24.12.2008
 */
public class ReadOnlyTextFieldEditor extends TextFieldEditor {
    public ReadOnlyTextFieldEditor(DetailFieldTO field) {
        super(field);
        textEditor.setEditable(false);
    }

    @Override
    protected void doLoadValue(DetailFieldValueTO fieldValue) {
        textEditor.setText(fieldValue.getDisplayValue());
    }

    @Override
    public void saveValue(DetailFieldValueTO fieldValue) {
        //Suppress parents implementation of value storing.
    }
}
