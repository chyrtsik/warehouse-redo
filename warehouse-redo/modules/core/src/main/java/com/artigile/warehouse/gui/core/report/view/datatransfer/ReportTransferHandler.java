/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.view.datatransfer;

import com.artigile.warehouse.gui.core.report.model.ReportModel;
import com.artigile.warehouse.gui.core.report.view.ReportView;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.util.List;

/**
 * @author Shyrik, 06.07.2009
 */

/**
 * Handler for transfer of report's data. It enables drag and drop features for the data,
 * that are shown in the report.
 */
public class ReportTransferHandler extends TransferHandler {
    /**
     * Model, that is a data source for data transfer operations.
     */
    private ReportModel reportModel;

    /**
     * View of the report.
     */
    private ReportView reportView;

    /**
     * Translator of the drop location.
     */
    private DropLocationTranslator locationTranslator;

    public ReportTransferHandler(ReportModel reportModel, ReportView reportView, DropLocationTranslator locationTranslator) {
        this.reportModel = reportModel;
        this.reportView = reportView;
        this.locationTranslator = locationTranslator;
    }

    //========================== Transferable implementation ==============================
    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        return new TransferableReportData(reportView.getSelectedItems());
    }

    @Override
    protected void exportDone(JComponent c, Transferable t, int action) {
        if (action == MOVE) {
            //Items should already have been moved. So we need do nothing here.
        }
    }

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDrop() && support.isDataFlavorSupported(TransferableReportData.listFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {
        List movedItems;

        try {
            movedItems = (List)support.getTransferable().getTransferData(TransferableReportData.listFlavor);
        } catch (Exception e) {
            LoggingFacade.logError(this, "Error when importing data received during drag and drop.", e);
            throw new RuntimeException(e);
        }

        //Move data in the report model.
        for (int i=movedItems.size()-1; i>=0; i--){
            Object movedItem = movedItems.get(i);
            reportModel.deleteItem(movedItem);
            reportModel.insertItem(movedItem, locationTranslator.translateLocationToItemIndex(support.getDropLocation()));
        }

        return true;
    }
}
