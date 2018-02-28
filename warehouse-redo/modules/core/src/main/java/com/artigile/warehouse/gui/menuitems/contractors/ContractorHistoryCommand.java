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

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.MultipleAndCriteriaCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.contractors.accounts.AccountOperationList;
import com.artigile.warehouse.utils.dto.ContractorTO;

/**
 * @author Shyrik, 18.04.2010
 */

/**
 * Command for browsing history of operations with contractor balance.
 */
public class ContractorHistoryCommand extends CustomCommand {
    protected ContractorHistoryCommand() {
        super(new ResourceCommandNaming("contractors.list.command.showAccountHistoryForContractor"),
              new MultipleAndCriteriaCommandAvailability(
                      new PermissionCommandAvailability(PermissionType.VIEW_ACCOUNT_OPERATIONS_HISTORY),
                      new PermissionCommandAvailability(PermissionType.VIEW_CONTRACTOR_BALANCE)
              )
        );
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        ContractorTO contractor = (ContractorTO)context.getCurrentReportItem();
        AccountOperationList accountOperationList = new AccountOperationList(contractor);
        WareHouse.runReportPlugin(accountOperationList);
        return true;
    }
}
