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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author Borisok V.V., 17.01.2009
 */
public class SortRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

    public SortRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        if (value instanceof SortItem) {
            SortItem sortItem = (SortItem) value;
            SortOrder sortOrder = sortItem.getSortOrder();
            if (sortOrder == SortOrder.ASCENDING) {
                setIcon(new ImageIcon(getClass().getResource("/images/ascending.png")));
            } else if (sortOrder ==SortOrder.DESCENDING) {
                setIcon(new ImageIcon(getClass().getResource("/images/descending.png")));
            }
        }
        return this;
    }
}
