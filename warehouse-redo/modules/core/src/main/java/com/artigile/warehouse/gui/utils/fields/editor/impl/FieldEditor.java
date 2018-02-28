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

import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;

import javax.swing.*;

/**
 * Interface of the editor of a single detail model field.
 * @author Shyrik, 24.12.2008
 */
public interface FieldEditor {
    /**
     * Must return editor control.
     * @return
     */
    JComponent getEditorComponent();

    /**
     * Called, when field editor must load value from the given field to the editor control.
     */
    void loadValue(DetailFieldValueTO fieldValue);

    /**
     * Called when its time to validate value, entered by user.
     */
    void validateData(DetailFieldValueTO fieldValue) throws DataValidationException;

    /**
     * Called, when field editor must save values, entered in the control, to the field
     */
    void saveValue(DetailFieldValueTO fieldValue);

    /**
     * Called to register new listener of changing the field value.
     * @param fieldChangeListener
     */
    void addFieldChangeListener(FieldChangeListener fieldChangeListener);
}
