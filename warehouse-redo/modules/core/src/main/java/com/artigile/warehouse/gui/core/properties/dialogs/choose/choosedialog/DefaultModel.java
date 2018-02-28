/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.choose.choosedialog;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * @author Borisok V.V., 17.01.2009
 */
public class DefaultModel extends AbstractTableModel {

    private List dataList;

    public DefaultModel(List dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getRowCount() {
        return dataList.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dataList.get(rowIndex);
    }
}
