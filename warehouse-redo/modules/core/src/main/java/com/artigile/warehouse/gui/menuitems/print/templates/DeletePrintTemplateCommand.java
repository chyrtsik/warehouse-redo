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

import com.artigile.warehouse.gui.core.report.command.DeleteCommand;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;

/**
 * @author Aliaksandr Chyrtsik
 * @since 09.05.13
 */
public class DeletePrintTemplateCommand extends DeleteCommand {
    protected DeletePrintTemplateCommand() {
        super(TemplateListEditingStrategy.getEditAvailability());
    }

    @Override
    protected boolean doDelete(Object deletingItem) throws ReportCommandException {
        PrintTemplateInstanceTO templateInstance = (PrintTemplateInstanceTO)deletingItem;
        SpringServiceContext.getInstance().getPrintTemplateService().deleteTemplateInstance(templateInstance.getId());
        return true;
    }
}
