/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.column.relationship.gui;

import com.artigile.warehouse.adapter.spi.impl.column.relationship.TableRelationshipEditor;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Valery Barysok, 6/20/11
 */

public class RelationshipColumnPanel extends JPanel implements PropertyChangeListener {

    private TableColumn column;
    private TableRelationshipEditor relationshipEditor;

    public RelationshipColumnPanel(TableColumn column, TableRelationshipEditor editor) {
        super(new BorderLayout());
        this.column = column;
        setRelationshipEditor(editor);
        initialization();
    }

    private void initialization() {
        updatePreferredSize();
        column.addPropertyChangeListener(this);
    }

    private void removeRelationshipEditor() {
        if (this.relationshipEditor != null) {
            remove(this.relationshipEditor.getComponent());
        }
    }

    public void finalization() {
        removeRelationshipEditor();
        column.removePropertyChangeListener(this);
    }

    public void setRelationshipEditor(TableRelationshipEditor relationshipEditor) {
        removeRelationshipEditor();
        this.relationshipEditor = relationshipEditor;
        add(this.relationshipEditor.getComponent(), BorderLayout.CENTER);
        repaint();
    }

    public TableRelationshipEditor getRelationshipEditor() {
        return relationshipEditor;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.relationshipEditor.getComponent().setEnabled(enabled);
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
