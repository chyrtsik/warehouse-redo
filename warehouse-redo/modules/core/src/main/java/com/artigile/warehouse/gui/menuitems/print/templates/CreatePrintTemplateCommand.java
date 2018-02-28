/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.print.templates;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.CreateCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;

/**
 * @author Aliaksandr Chyrtsik
 * @since 09.05.13
 */
public class CreatePrintTemplateCommand extends CreateCommand {
    protected CreatePrintTemplateCommand() {
        super(TemplateListEditingStrategy.getEditAvailability());
    }

    @Override
    protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
        PrintTemplateInstanceTO templateInstance = new PrintTemplateInstanceTO();
        if (Dialogs.runProperties(new TemplateSettingsForm(templateInstance, true))){
            try {
                SpringServiceContext.getInstance().getPrintTemplateService().saveTemplateInstance(templateInstance);
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return templateInstance;
        }
        return null;
    }
}
