/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl;

import com.artigile.warehouse.adapter.spi.impl.configuration.ColumnRelationship;
import com.artigile.warehouse.adapter.spi.impl.configuration.RelationshipTableView;
import com.artigile.warehouse.adapter.spi.impl.configuration.TableHeaderRelationshipUtil;
import com.artigile.warehouse.adapter.spi.impl.excel.ExcelSheetReader;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author Valery Barysok, 6/26/11
 */

public class ExcelTableModelWorker extends SwingWorker<ExcelTableModel, Object> {

    private ExcelSheetReader reader;
    private RelationshipTableView tableView;
    private List<ColumnRelationship> columnRelationships;

    public ExcelTableModelWorker(ExcelSheetReader reader, RelationshipTableView tableView, List<ColumnRelationship> columnRelationships) {
        this.reader = reader;
        this.tableView = tableView;
        this.columnRelationships = (columnRelationships != null) // always must be initialized
                ? columnRelationships
                : new ArrayList<ColumnRelationship>();
    }

    @Override
    protected ExcelTableModel doInBackground() throws Exception {
        return ExcelTableModel.create(reader, 20);
    }

    @Override
    protected void done() {
        try {
            tableView.setModel(get());
            TableHeaderRelationshipUtil.setColumnRelationships(tableView, columnRelationships);
            tableView.getViewComponent().repaint();
        } catch (InterruptedException e) {
            LoggingFacade.logError(this, e);
        } catch (ExecutionException e) {
            LoggingFacade.logError(this, e);
        }
    }
}
