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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

/**
 * @author Valery Barysok, 6/20/11
 */

public class TableRelationshipHeader extends JPanel {

    /**
     * The columnsController is a glue component, controlling the columns associated to each column
     */
    RelationshipColumnsControllerPanel columnsController;
    /**
     * The associated table
     */
    JXTable table;

    private List<DomainColumn> domainColumns;

    /**
     * Revalidate automatically the controller when the table changes size
     */
    ComponentAdapter resizer = new ComponentAdapter() {

        @Override
        public void componentResized(ComponentEvent e) {
            columnsController.revalidate();
        }
    };

    public TableRelationshipHeader(List<DomainColumn> domainColumns) {
        super(new BorderLayout());
        this.domainColumns = domainColumns;
    }

    /**
     * <p>Attachs the table where the relationship will be applied.</p>
     *
     * <p>It will be created a row of comboboxes that follow the size
     * and position of each of the columns in the table.</p>
     */
    public void setTable(JXTable table) {
        if (this.table != null) {
            this.table.removeComponentListener(resizer);
        }
        this.table = table;
        if (table == null) {
            removeController();
        } else {
            this.table.addComponentListener(resizer);
            recreateController();
        }
    }

    /**
     * Returns the table currently attached
     */
    public JTable getTable() {
        return table;
    }

    /**
     * removes the current columnsController
     */
    private void removeController() {
        if (columnsController != null) {
            columnsController.finalization();
            remove(columnsController);
            columnsController = null;
        }
    }

    private void createController() {
        columnsController = new RelationshipColumnsControllerPanel(table, domainColumns);
        columnsController.setEnabled(isEnabled());
        add(columnsController, BorderLayout.CENTER);
    }

    /**
     * creates/recreates the current columnsController
     */
    private void recreateController() {
        removeController();
        createController();
        revalidate();
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            columnsController.restoreRelationships();
        } else {
            columnsController.resetRelationships();
        }
    }

    public List<RelationshipColumnPanel> getRelationshipColumns() {
        return columnsController.getRelationshipColumns();
    }
}
