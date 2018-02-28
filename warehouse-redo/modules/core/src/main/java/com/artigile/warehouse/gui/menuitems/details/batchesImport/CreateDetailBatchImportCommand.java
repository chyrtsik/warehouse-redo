/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.batchesImport;

import com.artigile.warehouse.bl.detail.DetailBatchImportService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.report.command.CreateCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchImportTO;

/**
 * Command for launching of new detail batch import.
 *
 * @author Aliaksandr.Chyrtsik, 06.11.11
 */
public class CreateDetailBatchImportCommand extends CreateCommand {
    protected CreateDetailBatchImportCommand() {
        super(new ResourceCommandNaming("detail.batch.import.command.create"), new PermissionCommandAvailability(PermissionType.EDIT_DETAIL_BATCHES_IMPORT));
    }

    @Override
    protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
        DetailBatchImportTO detailBatchImport = new DetailBatchImportTO();
        CreateDetailBatchImportForm prop = new CreateDetailBatchImportForm(detailBatchImport, true);

        PropertiesDialog propertiesDialog = new PropertiesDialog(prop, "data.import.create.import.button");
        propertiesDialog.setResizable(true);
        if (propertiesDialog.run()) {
            //Perform a new detail batch import.
            DetailBatchImportService detailBatchImportService = SpringServiceContext.getInstance().getDetailBatchImportService();
            detailBatchImportService.performDetailBatchImport(detailBatchImport);
            return detailBatchImport;
        }

        return null;
    }
}
