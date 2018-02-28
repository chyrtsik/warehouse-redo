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
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.report.command.PropertiesCommandBase;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;
import com.artigile.warehouse.utils.logging.LoggingFacade;

/**
 * @author Aliaksandr Chyrtsik
 * @since 09.05.13
 */
public class OpenTemplateCommand extends PropertiesCommandBase {
    protected OpenTemplateCommand() {
        super(new PredefinedCommandAvailability(true));
    }

    @Override
    protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
        //Open editor for printing printing.
        PrintTemplateInstanceTO template = (PrintTemplateInstanceTO) editingItem;
        PropertiesForm prop = new TemplateSettingsForm(template, TemplateListEditingStrategy.getEditAvailability().isAvailable(context));
        PropertiesDialog propDialog = new PropertiesDialog(prop);
        if (propDialog.run()) {
            //Saving edited printing settings.
            try {
                SpringServiceContext.getInstance().getPrintTemplateService().saveTemplateInstance(template);
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
            return true;
        }
        return false;
    }
}
