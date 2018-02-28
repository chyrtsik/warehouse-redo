/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.utils.fields.editor;

import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.gui.utils.fields.editor.impl.FieldChangeListener;
import com.artigile.warehouse.gui.utils.fields.editor.impl.FieldEditorHolder;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Editor class providing feature to view and fill tempValues of dymamic fields.
 * For example: detail model or serial number fields.
 * @author Aliaksandr Chyrtsik
 * @since 30.06.13
 */
public class FieldsEditor {
    private JPanel contentPanel;
    private List<FieldEditorHolder> fieldEditors;
    private List<DetailFieldValueTO> tempValues;

    /**
     * Create fields editor for defined list of fields.
     * @param fieldTypes types of fields to be edited in current editor.
     */
    public FieldsEditor(List<DetailFieldTO> fieldTypes) {
        if (!fieldTypes.isEmpty()){
            //Fields editor has fields to fill out.
            contentPanel = new JPanel(new GridLayoutManager(fieldTypes.size(), 2));
            fieldEditors = new ArrayList<FieldEditorHolder>(fieldTypes.size());

            for (int i = 0; i < fieldTypes.size(); i++) {
                FieldEditorHolder editor = new FieldEditorHolder(fieldTypes.get(i));
                fieldEditors.add(editor);

                addFieldTitle(i, editor.getTitleComponent());
                addFieldEditor(i, editor.getEditorComponent());

                editor.addFieldChangeListener(new FieldChangeListener() {
                    @Override
                    public void fieldValueChanged(DetailFieldTO field) {
                        refreshDependentFieldsValues(field);
                    }
                });
            }
        }
        else{
            //No customer fields defines. Just let user know about this.
            contentPanel = new JPanel(new GridLayoutManager(1, 1));
            contentPanel.add(new JLabel(I18nSupport.message("detail.field.editor.no.fields")), GridLayoutUtils.getCenteredCellConstraints());
        }
        contentPanel.revalidate();
    }

    /**
     *@return panel with fields editor content.
     */
    public JPanel getContentPanel(){
        return contentPanel;
    }

    /**
     * Validate data entered into editors by user.
     * @throws DataValidationException
     */
    public void validateFields(List<DetailFieldValueTO> values) throws DataValidationException {
        if (fieldEditors != null){
            for (int i = 0; i < values.size(); i++) {
                fieldEditors.get(i).validateData(values.get(i));
            }
        }
    }

    /**
     * Load fields editors with values specified.
     * @param values field values (should be ordered in the same way as field types specified in constructor).
     */
    public void loadFields(List<DetailFieldValueTO> values) {
        if (fieldEditors != null){
            this.tempValues = values;
            for (int i = 0; i < values.size(); i++) {
                DetailFieldValueTO value = values.get(i);
                fieldEditors.get(i).loadValue(value);
            }
        }
    }

    /**
     * Save values specified in editor to the specified values containers.
     * @param values containers to save values to (should be ordered in the same way as field types specified in constructor).
     */
    public void saveFields(List<DetailFieldValueTO> values) {
        if (fieldEditors != null){
            for (int i = 0; i < values.size(); i++) {
                fieldEditors.get(i).saveValue(values.get(i));
            }
        }
    }

    private void addFieldTitle(int row, JLabel titleComponent) {
        GridConstraints constraints = new GridConstraints();
        constraints.setRow(row);
        constraints.setColumn(0);
        constraints.setAnchor(GridConstraints.ANCHOR_WEST);
        constraints.setFill(GridConstraints.FILL_NONE);
        constraints.setVSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        contentPanel.add(titleComponent, constraints);
    }

    private void addFieldEditor(int row, JComponent editorComponent) {
        GridConstraints constraints = new GridConstraints();
        constraints.setRow(row);
        constraints.setColumn(1);
        constraints.setAnchor(GridConstraints.ANCHOR_WEST);
        constraints.setFill(GridConstraints.FILL_HORIZONTAL);
        constraints.setVSizePolicy(GridConstraints.SIZEPOLICY_FIXED);
        constraints.setHSizePolicy(GridConstraints.SIZEPOLICY_CAN_GROW);
        contentPanel.add(editorComponent, constraints);
    }

    private void refreshDependentFieldsValues(DetailFieldTO field) {
        //Refresh other fields values. Is there are some calculated fields, depends
        //on the given field, their values are updated.
        //1. Save fields to the temporary object.
        saveFields(tempValues);

        //2. Load new values of the fields into the editors.
        for (int i = 0; i < tempValues.size(); i++) {
            if (tempValues.get(i).getType().getId() == field.getId()) {
                //Field, that's editor was triggered call of this method, needs not to be updated,
                //because now user may be editing field value, and updating this field editor may
                //break normal editing process.
            } else {
                fieldEditors.get(i).loadValue(tempValues.get(i));
            }
        }
    }
}
