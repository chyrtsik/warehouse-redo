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

import com.artigile.warehouse.domain.details.DetailFieldType;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;

import javax.swing.*;

/**
 * Helper class to work with various fields through the one interface.
 */
public class FieldEditorHolder {
    /**
     * Label with the name of the field.
     */
    private JLabel title;

    /**
     * Component for editing value of the field.
     */
    private FieldEditor editor;

    public FieldEditorHolder(DetailFieldTO field) {
        this.title = new JLabel(field.getName() + " :");
        this.editor = createFieldEditor(field);
    }

    public JLabel getTitleComponent(){
        return title;
    }

    public JComponent getEditorComponent(){
        return editor.getEditorComponent();
    }

    //================== Methods, simular to the methods of properties strategy ==================
    public void loadValue(DetailFieldValueTO fieldValue){
        editor.loadValue(fieldValue);
    }

    public void validateData(DetailFieldValueTO fieldValue) throws DataValidationException {
        editor.validateData(fieldValue);
    }

    public void saveValue(DetailFieldValueTO fieldValue){
        editor.saveValue(fieldValue);
    }

    //============================ Listeners management =========================================
    public void addFieldChangeListener(FieldChangeListener fieldChangeListener) {
        editor.addFieldChangeListener(fieldChangeListener);
    }

    //=========================== Helpers =======================================================
    private FieldEditor createFieldEditor(DetailFieldTO field) {
        DetailFieldType fieldType = field.getType();
        if (fieldType == DetailFieldType.TEXT){
            return new TextFieldEditor(field);
        }
        else if (fieldType == DetailFieldType.NUMBER){
            return new NumberFieldEditor(field, false);
        }
        else if (fieldType == DetailFieldType.INTEGER_NUMBER || fieldType == DetailFieldType.COUNT_IN_PACKAGING){
            return new NumberFieldEditor(field, true);
        }
        else if (fieldType == DetailFieldType.BOOLEAN){
            return new BooleanFieldEditor(field);
        }
        else if (fieldType == DetailFieldType.ENUM){
            return new EnumFieldEditor(field);
        }
        else if (fieldType == DetailFieldType.TEMPLATE_TEXT || fieldType == DetailFieldType.CURRENT_DATE ||
                 fieldType == DetailFieldType.CURRENT_TIME || fieldType == DetailFieldType.CURRENT_USER)
        {
            return new ReadOnlyTextFieldEditor(field);
        }
        else if (fieldType == DetailFieldType.DATE || fieldType == DetailFieldType.SHELF_LIFE){
            return new DateFieldEditor(field);
        }
        else{
            throw new RuntimeException("Cannot find appropriate editor for the field '" + field.getName() + "'");
        }
    }
}
