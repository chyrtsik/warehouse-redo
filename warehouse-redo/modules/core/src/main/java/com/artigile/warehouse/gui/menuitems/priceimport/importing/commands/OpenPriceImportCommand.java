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
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.PropertiesCommandBase;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.priceimport.PriceImportPropertiesForm;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;

/**
 * Command for showing properties of price list import.
 *
 * @author Valery Barysok, 6/11/11
 */
public class OpenPriceImportCommand extends PropertiesCommandBase {

    public OpenPriceImportCommand() {
        super(new PredefinedCommandAvailability(true));
    }

    @Override
    protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
        ContractorPriceImportTO priceImport = (ContractorPriceImportTO) editingItem;
        PropertiesForm prop = new PriceImportPropertiesForm(priceImport, false);
        if (Dialogs.runProperties(prop)) {
            ContractorPriceImportService priceImportService = SpringServiceContext.getInstance().getContractorPriceImportService();
            priceImportService.updatePriceImport(priceImport);
            return true;
        }
        return false;
    }
}
