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
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;

/**
 * Editor for date field.
 * @author Aliaksandr Chyrtsik
 * @since 04.07.13
 */
public class DateFieldEditor extends FieldEditorBase {
    private DetailFieldTO field;
    private JXDatePicker dateEditor;

    public DateFieldEditor(DetailFieldTO field) {
        this.field = field;

        dateEditor = new JXDatePicker();
        dateEditor.setFormats(StringUtils.getDateFormat());
    }

    @Override
    protected void doLoadValue(DetailFieldValueTO fieldValue) {
        dateEditor.setDate(StringUtils.parseDate(fieldValue.getValue()));
    }

    @Override
    public JComponent getEditorComponent() {
        return dateEditor;
    }

    @Override
    public void validateData(DetailFieldValueTO fieldValue) throws DataValidationException {
        if (field.getMandatory()){
            DataValidation.checkNotNull(dateEditor.getDate(), dateEditor);
        }
    }

    @Override
    public void saveValue(DetailFieldValueTO fieldValue) {
        fieldValue.setValue(StringUtils.formatDate(dateEditor.getDate()));
    }
}
