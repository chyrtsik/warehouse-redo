/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.choose.choosedialog.sort;

import org.jdesktop.swingx.JXButton;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Borisok V.V., 17.01.2009
 */
public class SortEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    private SortOrder sortOrder;
    private JXButton dummyButton;

    public SortEditor() {
        dummyButton = new JXButton();
        dummyButton.addActionListener(this);
    }

    @Override
    public Object getCellEditorValue() {
        return sortOrder;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof SortItem) {
            SortItem sortItem = (SortItem) value;
            sortOrder = sortItem.getSortOrder();
            if (sortOrder == SortOrder.ASCENDING) {
                dummyButton.setIcon(new ImageIcon(getClass().getResource("/images/ascending.png")));
            } else if (sortOrder ==SortOrder.DESCENDING) {
                dummyButton.setIcon(new ImageIcon(getClass().getResource("/images/descending.png")));
            }
        }
        return dummyButton;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sortOrder = sortOrder == SortOrder.ASCENDING ? SortOrder.DESCENDING : SortOrder.ASCENDING;
        fireEditingStopped();
    }
}
