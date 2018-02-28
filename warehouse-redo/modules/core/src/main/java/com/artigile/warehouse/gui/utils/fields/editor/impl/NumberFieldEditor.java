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

import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;

/**
 * Editor of the fields with numeric value.
 * @author Shyrik, 24.12.2008
 */
public class NumberFieldEditor extends TextFieldEditor {
    private final boolean isInteger;

    public NumberFieldEditor(DetailFieldTO field, boolean isInteger) {
        super(field);
        this.isInteger = isInteger;
    }

    @Override
    public void validateData(DetailFieldValueTO fieldValue) throws DataValidationException {
        super.validateData(fieldValue);
        if (isInteger){
            if (StringUtils.hasValue(textEditor.getText())){
                DataValidation.checkIsNumberLong(textEditor.getText(), textEditor);
            }
        }
        else{
            DataValidation.checkIsNumberOrIsEmpty(textEditor.getText(), textEditor);
        }
    }
}
