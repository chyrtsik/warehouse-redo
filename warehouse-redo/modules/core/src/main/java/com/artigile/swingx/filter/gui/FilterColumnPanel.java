/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.filter.gui;

import com.artigile.swingx.filter.TableFilterEditor;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Class controlling the filter applied to one specific column It resizes itself
 * automatically as the associated table column is resized
 *
 * @author Borisok V.V., 24.01.2009
 */
public class FilterColumnPanel extends JPanel implements PropertyChangeListener {

    /**
     * The TableColumn object, to which is registered to get property changes,
     * in order to keep the same width.
     */
    private TableColumn column;
    /**
     * The associated filterEditor
     */
    private TableFilterEditor filterEditor;

    public FilterColumnPanel(TableColumn column, TableFilterEditor editor) {
        super(new BorderLayout());
        this.column = column;
        setFilterEditor(editor);
        initialization();
    }

    private void initialization() {
        updatePreferredSize();
        column.addPropertyChangeListener(this);
    }

    private void removeFilterEditor() {
        if (this.filterEditor != null) {
            remove(this.filterEditor.getComponent());
        }
    }

    public void finalization() {
        removeFilterEditor();
        column.removePropertyChangeListener(this);
    }

    public void setFilterEditor(TableFilterEditor filterEditor) {
        removeFilterEditor();
        this.filterEditor = filterEditor;
        add(this.filterEditor.getComponent(), BorderLayout.CENTER);
        repaint();
    }

    public TableFilterEditor getFilterEditor() {
        return filterEditor;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.filterEditor.getComponent().setEnabled(enabled);
    }

    /**
     * PropertyChangeListener, listening for changes
     * on the width of the table's column
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("width")) {
            // that property is updated with the table column width
            updatePreferredSize();
            // as we're on a flow layout, the controller will remain
            // in sync with the table header as long as we maintain
            // the same preferred sizes
            revalidate();
        }
    }

    public String getColumnHeader() {
        return column.getHeaderValue().toString();
    }

    private void updatePreferredSize() {
        int w = column.getWidth();
        Dimension dimension = getPreferredSize();
        setPreferredSize(new Dimension(w, dimension.height));
    }
}
