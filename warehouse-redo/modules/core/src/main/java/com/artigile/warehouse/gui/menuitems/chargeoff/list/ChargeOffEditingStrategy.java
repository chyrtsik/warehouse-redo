/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.chargeoff.list;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.chargeoff.items.ChargeOffItemsEditor;
import com.artigile.warehouse.utils.dto.chargeoff.ChargeOffTOForReport;

/**
 * @author Shyrik, 17.10.2009
 */
public class ChargeOffEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        if (context.getCurrentReportItems().size() == 1){
            ReportCommand openChargeOffContentCommand = new OpenChargeOffContentCommand();
            commands.setDefaultCommandForRow(openChargeOffContentCommand);
            commands.add(openChargeOffContentCommand);
        }
    }

    private class OpenChargeOffContentCommand extends CustomCommand {
        protected OpenChargeOffContentCommand() {
            super(new ResourceCommandNaming("chargeOff.command.openContent"), new PermissionCommandAvailability(PermissionType.VIEW_CHARGE_OFF_CONTENT));
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            ChargeOffTOForReport chargeOff = (ChargeOffTOForReport)context.getCurrentReportItem();
            Plugin chargeOffContentEditor = new ChargeOffItemsEditor(chargeOff.getId());
            WareHouse.runPlugin(chargeOffContentEditor);
            return true;
        }
    }
}
