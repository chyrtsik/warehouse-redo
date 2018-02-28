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

import java.util.ArrayList;
import java.util.List;

/**
 * This is the base class for all field editors. It provides an implementation of the
 * listeners management methods.
 * @author Shyrik, 25.12.2008
 */
public abstract class FieldEditorBase implements FieldEditor {
    private List<FieldChangeListener> listeners = new ArrayList<FieldChangeListener>();
    private boolean listenersEnabled = true;

    @Override
    public void addFieldChangeListener(FieldChangeListener fieldChangeListener) {
        if (!listeners.contains(fieldChangeListener)){
            listeners.add(fieldChangeListener);
        }
    }

    /**
     * Called, when field editor must load value from the given field to the editor control.
     */
    @Override
    public void loadValue(DetailFieldValueTO fieldValue){
        //Disabling listeners is used to prevent fireing change event during save field process.  
        disableChangeListeners();
        doLoadValue(fieldValue);
        enableChangeListeners();
    }

    protected abstract void doLoadValue(DetailFieldValueTO fieldValue);

    /**
     * Fires field change event.
     * @param field
     */
    protected void fireFieldValueChanged(DetailFieldTO field){
        if (listenersEnabled){
            for (FieldChangeListener listener : listeners){
                listener.fieldValueChanged(field);
            }
        }
    }

    private void enableChangeListeners(){
        listenersEnabled = true;
    }

    private void disableChangeListeners(){
        listenersEnabled = false;
    }
}
