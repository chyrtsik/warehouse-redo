/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport.contractorProduct;

import com.artigile.warehouse.gui.core.report.command.ReportCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;

/**
 * @author Valery Barysok, 6/5/11
 */

public class ContractorProductEditingStrategy extends BaseContractorProductEditingStrategy implements ReportEditingStrategy {

    public ContractorProductEditingStrategy(int contractorColumnIndex) {
        setContractorColumnIndex(contractorColumnIndex);
    }


    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        //No commands are supported.
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new SelectContractorProductCommand());
        commands.add(new DeleteContractorProductCommand());
        if (context.getCurrentReportItems().size() == 1) {
            //Contractor properties command is default command for contractor column.
            ReportCommand openContractorCommand = new ViewContractorDetailsCommand();
            commands.add(openContractorCommand);
            commands.setDefaultCommandForColumn(getContractorColumnIndex(), openContractorCommand);
            commands.add(new OpenContractorProductCommand());
        }
    }
}
