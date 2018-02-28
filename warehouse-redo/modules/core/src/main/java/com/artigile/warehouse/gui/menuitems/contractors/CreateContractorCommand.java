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

import com.artigile.warehouse.bl.contractors.ContractorService;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.report.command.CreateCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ContractorTO;

/**
 * @author Shyrik, 18.04.2010
 */

/**
 * Command for creating new contrator.
 */
public class CreateContractorCommand extends CreateCommand {
    public CreateContractorCommand() {
        super(ContractorEditingStrategy.getEditAvailability());
    }

    @Override
    protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
        ContractorTO contractorTO = new ContractorTO();
        PropertiesForm prop = new ContractorForm(contractorTO, true);
        PropertiesDialog propDialog = new PropertiesDialog(prop);
        if (propDialog.run()) {
            ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();
            contractorService.create(contractorTO, ((ContractorForm) prop).getUpdatedContacts(), ((ContractorForm) prop).getUpdatedShippings());
            return contractorTO;
        }
        return null;
    }
}
