/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.command;

import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.core.print.PrintFacade;
import com.artigile.warehouse.gui.core.print.PrintingPreparator;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;

import javax.print.PrintException;

/**
 * @author Shyrik, 26.01.2009
 */
/**
 * Command for printing preview of single currenct report item.
 */
public class PrintPreviewCurrentReportItemCommand extends CurrentReportItemPrintingCommandBase {
    public PrintPreviewCurrentReportItemCommand(PrintTemplateType templateType, AvailabilityStrategy availabilityStrategy) {
        super(templateType, new ResourceCommandNaming("printing.command.printPreviewCurrentItem"), availabilityStrategy);
    }

    public PrintPreviewCurrentReportItemCommand(PrintTemplateType templateType, AvailabilityStrategy availabilityStrategy, PrintingPreparator preparator) {
        super(templateType, new ResourceCommandNaming("printing.command.printPreviewCurrentItem"), availabilityStrategy, preparator);
    }

    @Override
    protected void doPrintingAction(Object objectForPrinting, PrintTemplateType templateType) throws PrintException {
        PrintFacade.printPreview(objectForPrinting, templateType);
    }
}
