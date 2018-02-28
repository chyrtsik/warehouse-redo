/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.column.relationship.gui;

import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Valery Barysok, 6/20/11
 */

public class RelationshipHeaderManager {

    private JXTable table;
    private JPanel headerAndRelationshipPanel;
    private TableRelationshipHeader tableRelationshipHeader;
    private List<DomainColumn> domainColumns;

    public RelationshipHeaderManager(List<DomainColumn> domainColumns) {
        this.domainColumns = domainColumns;
    }

    public void install(JXTable table) {
        if (this.table != null) {
            throw new IllegalStateException("Relationship header is already installed");
        }
        installHeader(table, true);
    }

    public void uninstall() {
        if (table != null) {
            this.headerAndRelationshipPanel = null;
            this.tableRelationshipHeader = null;
            installHeader(table, false);
            this.table = null;
        }
    }

    protected void installHeader(JXTable table, boolean installRelationshipHeader) {
        Container p = table.getParent();
        if (p instanceof JViewport) {
            Container gp = p.getParent();
            if (gp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) gp;
                // Make certain we are the viewPort's view and not, for
                // example, the rowHeaderView of the scrollPane -
                // an implementor of fixed columns might do this.
                if (installRelationshipHeader) {
                    headerAndRelationshipPanel = createRelationshipHeader(table);
                    scrollPane.setColumnHeaderView(headerAndRelationshipPanel);
                } else {
                    scrollPane.setColumnHeaderView(table.getTableHeader());
                }
                this.table = table;
            }
        }
    }

    protected JPanel createRelationshipHeader(JXTable table) {
        JPanel tableAndRelationshipPanel = new JPanel(new BorderLayout());
        tableAndRelationshipPanel.add(table.getTableHeader(), BorderLayout.CENTER);
        tableRelationshipHeader = new TableRelationshipHeader(domainColumns);
        tableRelationshipHeader.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black),
                        BorderFactory.createEmptyBorder(1, 0, 0, 0)));
        tableRelationshipHeader.setTable(table);
        tableAndRelationshipPanel.add(tableRelationshipHeader, BorderLayout.SOUTH);

        return tableAndRelationshipPanel;
    }

    public void setVisible(boolean visible) {
        if (tableRelationshipHeader != null) {
            tableRelationshipHeader.setVisible(visible);
        }
    }

    public boolean getVisible() {
        return tableRelationshipHeader != null && tableRelationshipHeader.isVisible();
    }

    public List<RelationshipColumnPanel> getRelationshipColumns() {
        return tableRelationshipHeader.getRelationshipColumns();
    }
}