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
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.report.command.CreateCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.priceimport.PriceImportForm;
import com.artigile.warehouse.gui.menuitems.priceimport.importing.PriceImportEditingStrategy;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;

/**
 * Command for launching new price list import.
 *
 * @author Aliaksandr.Chyrtsik, 13.07.11
 */
public class CreatePriceImportCommand extends CreateCommand {

    public CreatePriceImportCommand() {
        super(new ResourceCommandNaming("price.import.command.NewPriceImport"), PriceImportEditingStrategy.getEditAvailability());
    }

    @Override
    protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
        ContractorPriceImportTO priceImport = new ContractorPriceImportTO();
        PriceImportForm prop = new PriceImportForm(priceImport, true);

        PropertiesDialog propertiesDialog = new PropertiesDialog(prop, "data.import.create.import.button");
        propertiesDialog.setResizable(true);
        if (propertiesDialog.run()) {
            //Perform a new price-list import.
            ContractorPriceImportService priceImportService = SpringServiceContext.getInstance().getContractorPriceImportService();
            priceImportService.performPriceListImport(priceImport);
            return priceImport;
        }

        return null;
    }
}
