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
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Shyrik, 24.12.2008
 */
public class BooleanFieldEditor extends FieldEditorBase {
    private JPanel radioPanel;
    private JRadioButton buttonYes;
    private JRadioButton buttonNo;
    private JRadioButton buttonNull;

    private DetailFieldTO field;

    public BooleanFieldEditor(DetailFieldTO field) {
        this.field = field;
        initRadioButtons();
    }

    private void initRadioButtons() {
        radioPanel = new JPanel(new GridLayoutManager(1, 3));

        buttonYes = new JRadioButton(DetailTypesUtils.getBoooleanAsString(true));
        buttonYes.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                selectValue(true);
                onFieldValueChanged();
            }
        });
        addRadio(buttonYes, 0, 0);

        buttonNo = new JRadioButton(DetailTypesUtils.getBoooleanAsString(false));
        buttonNo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                selectValue(false);
                onFieldValueChanged();
            }
        });
        addRadio(buttonNo, 0, 1);

        buttonNull = new JRadioButton(DetailTypesUtils.getNullAsString());
        if (field.getMandatory()){
            //Don't show mull value radion button, because used nust choose one of the values.
        }
        else{
            buttonNull.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectValue(null);
                    onFieldValueChanged();
                }
            });
            addRadio(buttonNull, 0, 2);
        }
    }

    private void onFieldValueChanged() {
        fireFieldValueChanged(field);
    }

    @Override
    public JComponent getEditorComponent() {
        return radioPanel;
    }

    @Override
    protected void doLoadValue(DetailFieldValueTO fieldValue) {
        Boolean valueBool = DetailTypesUtils.getStringAsBoolean(fieldValue.getValue());
        selectValue(valueBool);
    }

    @Override
    public void validateData(DetailFieldValueTO fieldValue) throws DataValidationException {
        if (field.getMandatory()){
            if (!buttonYes.isSelected() && !buttonNo.isSelected()){
                DataValidation.failRes(radioPanel, "validation.select.value");
            }
        }
    }

    @Override
    public void saveValue(DetailFieldValueTO fieldValue) {
        if (buttonYes.isSelected()){
            fieldValue.setValue(DetailTypesUtils.getBoooleanAsString(true));
        }
        else if (buttonNo.isSelected()){
            fieldValue.setValue(DetailTypesUtils.getBoooleanAsString(false));
        }
        else{
            fieldValue.setValue("");
        }
    }

    //===================== Helpers ============================================
    private void addRadio(JRadioButton button, int row, int col) {
        GridConstraints constraints = new GridConstraints();
        constraints.setRow(row);
        constraints.setColumn(col);
        radioPanel.add(button, constraints);
    }

    private void selectValue(Boolean valueBool) {
        if (valueBool == null){
            buttonYes.setSelected(false);
            buttonNo.setSelected(false);
            buttonNull.setSelected(true);
        }
        else if (valueBool){
            buttonYes.setSelected(true);
            buttonNo.setSelected(false);
            buttonNull.setSelected(false);
        }
        else{
            buttonYes.setSelected(false);
            buttonNo.setSelected(true);
            buttonNull.setSelected(false);
        }
    }
}
