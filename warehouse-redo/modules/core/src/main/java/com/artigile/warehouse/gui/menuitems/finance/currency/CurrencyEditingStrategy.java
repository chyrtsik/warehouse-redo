/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.finance.currency;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.finance.CurrencyService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.CurrencyTO;

/**
 * @author Shyrik, 08.08.2009
 */
public class CurrencyEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateCurrencyCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateCurrencyCommand());
        commands.add(new DeleteCurrencyCommand());
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenCurrencyCommand());
        }
    }

    //==================== Commands ===========================================
    private class CreateCurrencyCommand extends CreateCommand {
        protected CreateCurrencyCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            CurrencyTO currencyTO = new CurrencyTO();
            PropertiesForm prop = new CurrencyForm(currencyTO, true);
            if (Dialogs.runProperties(prop)) {
                try {
                    getCurrencyService().save(currencyTO);
                } catch (BusinessException e) {
                    throw new ReportCommandException(e);
                }
                return currencyTO;
            }
            return null;
        }
    }

    private class DeleteCurrencyCommand extends DeleteCommand {
        protected DeleteCurrencyCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            try {
                getCurrencyService().remove((CurrencyTO) deletingItem);
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return true;
        }
    }

    private class OpenCurrencyCommand extends PropertiesCommandBase {
        protected OpenCurrencyCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            CurrencyTO currencyTO = (CurrencyTO) editingItem;
            PropertiesForm prop = new CurrencyForm(currencyTO, getEditAvailability().isAvailable(context));
            if (Dialogs.runProperties(prop)) {
                try {
                    getCurrencyService().save(currencyTO);
                } catch (BusinessException e) {
                    throw new ReportCommandException(e);
                }
                return true;
            }
            return false;
        }
    }

    //====================================== Helpers ========================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_CURRENCY_LIST);
    }

    private CurrencyService getCurrencyService() {
        return SpringServiceContext.getInstance().getCurrencyService();
    }
}
