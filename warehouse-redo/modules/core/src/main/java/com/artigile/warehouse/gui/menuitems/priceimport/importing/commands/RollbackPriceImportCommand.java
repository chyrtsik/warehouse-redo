/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport.importing.commands;

import com.artigile.warehouse.bl.priceimport.ContractorPriceImportService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.command.ReportCommandBase;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandType;
import com.artigile.warehouse.gui.core.report.command.availability.MultipleAndCriteriaCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.priceimport.importing.PriceImportEditingStrategy;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * Command for performing import rollback (contractor's data are rolled back to the date specified).
 *
 * @author Aliaksandr.Chyrtsik, 13.07.11
 */
public class RollbackPriceImportCommand extends ReportCommandBase{
    public RollbackPriceImportCommand() {
        super(new ResourceCommandNaming("price.import.command.RollbackPriceImport"),
                new MultipleAndCriteriaCommandAvailability(
                        PriceImportEditingStrategy.getEditAvailability(),
                        new PermissionCommandAvailability(PermissionType.EDIT_PRICE_IMPORT_ROLLBACK))
        );
    }

    @Override
    public ReportCommandType getType() {
        return ReportCommandType.CUSTOM;
    }

    @Override
    public boolean execute(ReportCommandContext context) throws ReportCommandException {
        ContractorPriceImportTO priceImport = (ContractorPriceImportTO)context.getCurrentReportItem();

        String confirmTitle = I18nSupport.message("price.import.rollback.confirmation.title");
        String confirmMessage = I18nSupport.message("price.import.rollback.confirmation.message",
                priceImport.getContractor().getName(), priceImport.getImportDate());

        if (MessageDialogs.showConfirm(confirmTitle, confirmMessage)){
            ContractorPriceImportService priceImportService = SpringServiceContext.getInstance().getContractorPriceImportService();
            priceImportService.rollbackPriceListToImport(priceImport);
            return true;
        }

        return false;
    }
}
