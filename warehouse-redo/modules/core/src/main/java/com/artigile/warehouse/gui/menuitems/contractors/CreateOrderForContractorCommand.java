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

import com.artigile.warehouse.gui.core.report.command.ReportCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandParametersContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandType;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.orders.CreateOrderCommand;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Shyrik, 18.04.2010
 */

/**
 * Command for creating order for current contractor (selected in report).
 */
public class CreateOrderForContractorCommand implements ReportCommand {
    private CreateOrderCommand createOrderCommand = new CreateOrderCommand();

    @Override
    public ReportCommandType getType() {
        return ReportCommandType.CUSTOM;
    }

    @Override
    public boolean execute(ReportCommandContext context) throws ReportCommandException {
        //Manually executing command of creating order.
        return createOrderCommand.execute(new ReportCommandParametersContext(context.getCurrentReportItem()));
    }

    @Override
    public String getName(ReportCommandContext context) {
        return I18nSupport.message("contractors.list.command.createOrderforContractor");
    }

    @Override
    public boolean isAvailable(ReportCommandContext context) {
        return createOrderCommand.isAvailable(context);
    }
}
