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

import com.artigile.swingx.filter.TableFilter;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

/**
 * <p>Implementation of a table filter that displays a set of editors
 * associated to each table's column.</p>
 *
 * <p>These editors are moved and resized as the table's columns are
 * resized, so this Swing component is better suited to be displayed
 * atop the JTable, or just below, using the same size -and resizing-
 * as the table itself.</p>
 *
 * @author Borisok V.V., 24.01.2009
 */
public class TableFilterHeader extends JPanel {

    /**
     * The columnsController is a glue component, controlling the filters associated to each column
     */
    FilterColumnsControllerPanel columnsController;
    /**
     * The associated table
     */
    JXTable table;

    /**
     * Revalidate automatically the controller when the table changes size
     */
    ComponentAdapter resizer = new ComponentAdapter() {

        @Override
        public void componentResized(ComponentEvent e) {
            columnsController.revalidate();
        }
    };

    public TableFilterHeader() {
        super(new BorderLayout());
    }

    /**
     * <p>Attachs the table where the filtering will be applied.</p>
     *
     * <p>It will be created a row of editors that follow the size
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
     * <p>Sets a new table filter.</p>
     *
     * <p>The filters associated to the initial TableFilter are transferred to the new
     * one.</p>
     */
    public void setTableFilter(TableFilter tableFilter) {
        columnsController.setTableFilter(tableFilter);
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
        columnsController = new FilterColumnsControllerPanel(table);
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
            columnsController.restoreFilters();
        } else {
            columnsController.resetFilters();
        }
    }

    public List<FilterColumnPanel> getFilteredColumns() {
        return columnsController.getFilteredColumns();
    }

    public void resetFilter() {
        columnsController.resetFilters();
    }
}
