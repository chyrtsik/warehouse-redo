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

import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

/**
 * @author Shyrik, 06.07.2009
 */
public class TransferableReportData implements Transferable {
    /**
     * List of report items being transferred.
     */
    private List reportItems;

    /**
     * Supported flavors.
     */
    public static DataFlavor listFlavor;

    static {
        try {
            listFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=java.util.List");
        } catch (ClassNotFoundException e) {
            LoggingFacade.logError("Error while creating data flavor for drag and drop.", e);
            throw new RuntimeException("Unexpected error", e);
        }
    }

    public TransferableReportData(List reportItems) {
        this.reportItems = reportItems;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{listFlavor};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(listFlavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        assert(flavor.equals(listFlavor));
        return reportItems;
    }
}
