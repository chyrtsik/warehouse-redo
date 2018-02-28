/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.view.cell;

import javax.swing.*;

/**
 * @author Shyrik, 16.02.2009
 */
public class ReportCellEditor extends DefaultCellEditor {
    public ReportCellEditor(JTextField textField) {
        super(textField);
        clickCountToStart = 1;
    }

    public ReportCellEditor(JCheckBox checkBox) {
        super(checkBox);
    }

    public ReportCellEditor(JComboBox comboBox) {
        super(comboBox);
    }
}
