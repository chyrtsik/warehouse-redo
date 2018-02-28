/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.workarounds;

import com.artigile.warehouse.utils.i18n.I18nSupport;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Valery Barysok, 7/14/11
 */

public class JRowNumberTable implements ChangeListener, PropertyChangeListener {
    private JTable main;
    private JTable fixed;
    private JScrollPane scrollPane;

    public JRowNumberTable(JTable mainTable, final JTable rowNumberTable, JScrollPane scrollPane) {
        this.main = mainTable;
        this.fixed = rowNumberTable;
        this.scrollPane = scrollPane;

        mainTable.addPropertyChangeListener(this);
        rowNumberTable.setSelectionModel(mainTable.getSelectionModel());
        rowNumberTable.setModel(new RowNumberModel(mainTable));
        rowNumberTable.setFocusable(false);
        rowNumberTable.setPreferredScrollableViewportSize(rowNumberTable.getPreferredSize());

        scrollPane.setRowHeaderView(rowNumberTable);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowNumberTable.getTableHeader());

        scrollPane.getRowHeader().addChangeListener(this);
        mainTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                rowNumberTable.revalidate();
                rowNumberTable.repaint();
            }
        });
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JViewport viewport = (JViewport) e.getSource();
        scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if ("selectionModel".equals(e.getPropertyName())) {
            fixed.setSelectionModel(main.getSelectionModel());
        }
    }

    private static class RowNumberModel extends AbstractTableModel {

        private JTable table;

        private RowNumberModel(JTable table) {
            this.table = table;
        }

        @Override
        public int getRowCount() {
            return table.getRowCount();
        }

        @Override
        public int getColumnCount() {
            return 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return rowIndex+1;
        }

        @Override
        public String getColumnName(int column) {
            return I18nSupport.message("table.column.rownumber");
        }
    }
}
