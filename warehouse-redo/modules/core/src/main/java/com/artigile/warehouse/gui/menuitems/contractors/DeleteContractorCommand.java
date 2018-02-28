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
import com.artigile.warehouse.gui.core.report.command.DeleteCommand;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ContractorTO;

/**
 * @author Shyrik, 18.04.2010
 */

/**
 * Command for deleting contrator.
 */
public class DeleteContractorCommand extends DeleteCommand {
    public DeleteContractorCommand() {
        super(ContractorEditingStrategy.getEditAvailability());
    }

    @Override
    protected boolean doDelete(Object deletingItem) throws ReportCommandException {
        ContractorService contractorService = SpringServiceContext.getInstance().getContractorService();
        contractorService.deleteContractor((ContractorTO) deletingItem);
        return true;
    }
}
