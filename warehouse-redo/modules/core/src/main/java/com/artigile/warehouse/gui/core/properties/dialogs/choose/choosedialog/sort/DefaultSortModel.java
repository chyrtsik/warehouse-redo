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

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * @author Borisok V.V., 17.01.2009
 */
public class DefaultSortModel extends AbstractTableModel {

    public static int SORTORDERCOLUMN = 0;

    private List<SortItem> dataList;

    public DefaultSortModel(List<SortItem> dataList) {
        this.dataList = dataList;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == SORTORDERCOLUMN) {
            return SortOrder.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public int getRowCount() {
        return dataList.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dataList.get(rowIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == SORTORDERCOLUMN) {
            return true;
        }
        return super.isCellEditable(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == SORTORDERCOLUMN) {
            SortItem sortItem = dataList.get(rowIndex);
            sortItem.setSortOrder((SortOrder) aValue);
            return;
        }

        super.setValueAt(aValue, rowIndex, columnIndex);
    }
}
