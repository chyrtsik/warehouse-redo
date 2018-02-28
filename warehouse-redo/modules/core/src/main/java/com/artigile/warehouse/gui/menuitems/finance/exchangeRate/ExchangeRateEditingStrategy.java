/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.finance.exchangeRate;

import com.artigile.warehouse.bl.finance.CurrencyExchangeService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.report.command.PropertiesCommandBase;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ExchangeRateTO;

/**
 * @author Shyrik, 09.12.2008
 */
public class ExchangeRateEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenExchangeRateCommand());
        }
    }

    //==================== Commands ===========================================
    private class OpenExchangeRateCommand extends PropertiesCommandBase {
        protected OpenExchangeRateCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            ExchangeRateTO rate = (ExchangeRateTO) editingItem;
            PropertiesForm prop = new ExchangeRateForm(rate, getEditAvailability().isAvailable(context));
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                //Saving new exchange rate.
                getExchangeService().saveRate(rate);
                return true;
            }
            return false;
        }
    }

    //====================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_EXCHANGE_RATES);
    }

    private CurrencyExchangeService getExchangeService() {
        return SpringServiceContext.getInstance().getExchangeService();
    }
}
