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
import com.artigile.warehouse.gui.menuitems.details.types.DetailTypesUtils;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Editor for the enum field.
 * @author Shyrik, 24.12.2008
 */
public class EnumFieldEditor extends FieldEditorBase {
    private JComboBox enumEditor;
    private DetailFieldTO field;

    public EnumFieldEditor(DetailFieldTO field) {
        this.field = field;

        enumEditor = new JComboBox();
        if (!field.getMandatory()){
            //"Not selected" value for non mandatory fields.
            enumEditor.addItem(DetailTypesUtils.getNullAsString());
        }
        for (String value : field.getEnumValues()){
            enumEditor.addItem(value);
        }
        if (field.getMandatory()){
            //Mandatory fields should have no selection by default,
            //because user has to manually choose this fields' value.
            enumEditor.setSelectedIndex(-1);    
        }
        
        enumEditor.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                onFieldValueChanged();
            }
        });
    }

    private void onFieldValueChanged() {
        fireFieldValueChanged(field);
    }

    @Override
    public JComponent getEditorComponent() {
        return enumEditor;
    }

    @Override
    protected void doLoadValue(DetailFieldValueTO fieldValue) {
        if (fieldValue.getValue().isEmpty()){
            selectNull();   
        }
        else{
            enumEditor.setSelectedItem(fieldValue.getValue());
        }
    }

    @Override
    public void validateData(DetailFieldValueTO fieldValue) throws DataValidationException {
        if (field.getMandatory()){
            DataValidation.checkSelected(enumEditor);
        }        
    }

    @Override
    public void saveValue(DetailFieldValueTO fieldValue) {
        if (field.getMandatory()){
            if (enumEditor.getSelectedItem() == null){
                //For mandatory value we should warn to select value.
                fieldValue.setValue(DetailTypesUtils.getSelectValueWarning());                                            
            }
            else{
                fieldValue.setValue((String)enumEditor.getSelectedItem());
            }
        }
        else{
            String selectedValue = (String)enumEditor.getSelectedItem();
            if (selectedValue.equals(DetailTypesUtils.getNullAsString())){
                //"Not set" should be represented as empty string.
                fieldValue.setValue("");
            }
            else{
                fieldValue.setValue(selectedValue);
            }
        }
    }

    private void selectNull(){
        if (!field.getMandatory()){
            //The first item means null.
            enumEditor.setSelectedIndex(0);
        }
    }
}
