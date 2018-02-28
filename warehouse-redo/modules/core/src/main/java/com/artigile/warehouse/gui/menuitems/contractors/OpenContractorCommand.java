/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.contractors;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.PropertiesCommandBase;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ContractorTO;

/**
 * @author Shyrik, 18.04.2010
 */

/**
 * command for editing contractor properties.
 */
public class OpenContractorCommand extends PropertiesCommandBase {
    public OpenContractorCommand() {
        super(new PredefinedCommandAvailability(true));
    }

    @Override
    protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
        ContractorTO contractor = (ContractorTO) editingItem;
        ContractorForm prop = new ContractorForm(contractor, ContractorEditingStrategy.getEditAvailability().isAvailable(context));
        if (Dialogs.runProperties(prop)) {
            ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();
            try {
                contractorService.updateContractorFullData(contractor, prop.getUpdatedContacts(), prop.getUpdatedShippings(), prop.getAccountActions());
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return true;
        }
        return false;
    }
}
