/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.filter.gui.editor;

import com.artigile.swingx.filter.FilterObservable;
import com.artigile.swingx.filter.TableFilterEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Table filter editor represented by a JTextField
 *
 * @author Borisok V.V., 24.01.2009
 */
public class TextFilterEditor extends JTextField implements TableFilterEditor {

    /**
     * The TextField instance that handles all the work
     */
    private TextField editor = null;

    /**
     * Filter position represent model index
     */
    //private int filterPosition;
    public TextFilterEditor(int filterPosition) {
        editor = new TextField(this, filterPosition);
        //this.filterPosition = filterPosition;
    }

    public Component getComponent() {
        return this;
    }

    public FilterObservable getFilterObservable() {
        return editor;
    }

    public void updateFilter() {
        editor.updateFilter(editor.getEditor().getText());
    }

    public void resetFilter() {
        editor.resetFilter();
    }
}
