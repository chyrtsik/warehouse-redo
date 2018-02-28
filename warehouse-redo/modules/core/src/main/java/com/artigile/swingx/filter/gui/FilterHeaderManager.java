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

import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Filter header manager allows to easily install/uninstall filter header
 * in a table derived from JTable
 * Note: install method must be invoked after each addNotify called
 *
 * @author Borisok V.V., 24.01.2009
 */
public class FilterHeaderManager {

    private JXTable table;
    private JPanel headerAndFilterPanel;
    private TableFilterHeader tableFilterHeader;

    /**
     * Sets table to which the filter will be applied
     *
     * @param table table to which the filted will be applied.
     */
    public void install(JXTable table) {
        if (this.table != null) {
            throw new IllegalStateException("Filter header is already installed");
        }
        installHeader(table, true);
    }

    /**
     * Sets the filtered table to null.
     */
    public void uninstall() {
        if (table != null) {
            this.headerAndFilterPanel = null;
            this.tableFilterHeader = null;
            installHeader(table, false);
            this.table = null;
        }
    }

    protected void installHeader(JXTable table, boolean installFilterHeader) {
        Container p = table.getParent();
        if (p instanceof JViewport) {
            Container gp = p.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) gp;
                // Make certain we are the viewPort's view and not, for
                // example, the rowHeaderView of the scrollPane -
                // an implementor of fixed columns might do this.
                if (installFilterHeader) {
                    headerAndFilterPanel = createFilterHeader(table);
                    scrollPane.setColumnHeaderView(headerAndFilterPanel);
                } else {
                    scrollPane.setColumnHeaderView(table.getTableHeader());
                }
                this.table = table;
            }
        }
    }

    protected JPanel createFilterHeader(JXTable table) {
        JPanel tableAndFilterPanel = new JPanel(new BorderLayout());
        tableAndFilterPanel.add(table.getTableHeader(), BorderLayout.CENTER);
        tableFilterHeader = new TableFilterHeader();
        tableFilterHeader.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black),
                        BorderFactory.createEmptyBorder(1, 0, 0, 0)));
        tableFilterHeader.setTable(table);
        tableFilterHeader.setVisible(false);
        tableAndFilterPanel.add(tableFilterHeader, BorderLayout.SOUTH);

        return tableAndFilterPanel;
    }

    public void setVisible(boolean visible) {
        if (tableFilterHeader != null) {
            tableFilterHeader.setVisible(visible);
        }
    }

    public boolean getVisible() {
        return tableFilterHeader != null && tableFilterHeader.isVisible();
    }

    public List<FilterColumnPanel> getFilteredColumns() {
        return tableFilterHeader.getFilteredColumns();
    }

    public void resetFilter() {
        tableFilterHeader.resetFilter();
    }
}
