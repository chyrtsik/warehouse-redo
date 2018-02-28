/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.browser;

import javax.swing.*;
import javax.swing.tree.TreeCellEditor;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.EventObject;

/**
 * @author Borisok V.V., 23.09.2009
 */

/**
 * Tree table cell editor, that decorates all tree elements by check boxes.
 */
public class CheckBoxTreeCellEditor extends AbstractCellEditor implements TreeCellEditor {
    private CheckBoxTreeCellRenderer renderer = new CheckBoxTreeCellRenderer();

    /**
     * Model of check box in tree cell 
     */
    private CheckBoxTreeCellModel model;

    /**
     * Node of tree
     */
    private Object value = null;

    public CheckBoxTreeCellEditor(CheckBoxTreeCellModel model) {
        this.model = model;
        renderer.setModel(model);
        renderer.getCheck().addItemListener(new CheckBoxItemListener());
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        return true;
    }

    @Override
    public Object getCellEditorValue() {
        return value;
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        this.value = value;
        return renderer.getTreeCellRendererComponent(tree, value, true, expanded, leaf, row, true);
    }

    /**
     * Listener that reacts on changes of the cell's checkbox.
     */
    private class CheckBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent itemEvent) {
            if (stopCellEditing()) {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED && !model.isItemChecked(value)) {
                    model.checkItem(value);
                }
                else if (itemEvent.getStateChange() == ItemEvent.DESELECTED && model.isItemChecked(value)) {
                    model.unCheckItem(value);
                }
            }
        }
    }
   
}
