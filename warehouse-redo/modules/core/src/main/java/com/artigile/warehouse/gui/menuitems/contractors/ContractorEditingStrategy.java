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
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;

/**
 * @author IoaN, Dec 10, 2008
 */

public class ContractorEditingStrategy implements ReportEditingStrategy {

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateContractorCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        boolean singleContractorSelected = context.getCurrentReportItems().size() == 1;

        if (singleContractorSelected){
            commands.add(new CreateOrderForContractorCommand());
            commands.add(new ContractorHistoryCommand());
        }
        commands.add(new CreateContractorCommand());
        commands.add(new DeleteContractorCommand());
        if (singleContractorSelected){
            commands.add(new OpenContractorCommand());
        }
    }

    //====================================== Helpers ========================================
    public static AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_CONTRACTORS_LIST);
    }
}
