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
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Editor for the text fields.
 * @author Shyrik, 24.12.2008
 */
public class TextFieldEditor extends FieldEditorBase {
    protected JTextField textEditor;
    protected DetailFieldTO field;

    public TextFieldEditor(DetailFieldTO field){
        this.field = field;

        textEditor = new JTextField();
        textEditor.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e){
                onFieldValueChanged();
            }
        });
    }

    private void onFieldValueChanged() {
        fireFieldValueChanged(field);
    }

    @Override
    public JComponent getEditorComponent() {
        return textEditor;
    }

    @Override
    protected void doLoadValue(DetailFieldValueTO fieldValue) {
        textEditor.setText(fieldValue.getValue());
    }

    @Override
    public void validateData(DetailFieldValueTO fieldValue) throws DataValidationException {
        if (field.getMandatory()){
            DataValidation.checkNotEmpty(textEditor);
        }
        if (field.isUnique()){
            if (!fieldValue.isValueUnique(textEditor.getText())){
                DataValidation.failRes(textEditor, "detail.models.error.field.value.is.not.unique", field.getName());
            }
        }
    }

    @Override
    public void saveValue(DetailFieldValueTO fieldValue) {
        fieldValue.setValue(textEditor.getText());
    }
}
