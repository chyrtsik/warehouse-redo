/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.command;

import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.core.print.PrintingPreparator;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.naming.NamingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import javax.print.PrintException;

/**
 * @author Shyrik, 26.01.2009
 */

/**
 * Base universal command for printing actions of a single currenct report item.
 */
public abstract class CurrentReportItemPrintingCommandBase extends CustomCommand {
    /**
     * Template document type to be processed.
     */
    private PrintTemplateType templateType;

    /**
     * Is set, performs some opetations (external loading, for example) before printing.
     */
    private PrintingPreparator preparator;

    /**
     * @param templateType         - type of the documant template, used for printing.
     * @param availabilityStrategy - atrategy for checking comand availability.
     */
    public CurrentReportItemPrintingCommandBase(PrintTemplateType templateType, NamingStrategy namingStrategy, AvailabilityStrategy availabilityStrategy) {
        this(templateType, namingStrategy, availabilityStrategy, defaultPreparator);
    }

    /**
     * @param templateType         - type of the documant template, used for printing.
     * @param availabilityStrategy - atrategy for checking comand availability.
     * @param preparator           - object, that prepares printable item before it to be printed.
     */
    public CurrentReportItemPrintingCommandBase(PrintTemplateType templateType, NamingStrategy namingStrategy,
                                                AvailabilityStrategy availabilityStrategy, PrintingPreparator preparator) {
        super(namingStrategy, availabilityStrategy);
        this.templateType = templateType;
        this.preparator = preparator;
    }

    @Override
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        try {
            Object itemPreparedForPrinting = preparator.prepareForPrinting(context.getCurrentReportItem());
            doPrintingAction(itemPreparedForPrinting, templateType);
        }
        catch (PrintException e) {
            LoggingFacade.logError(this, e);
            throw new ReportCommandException(e);
        }
        return true;
    }

    /**
     * Implementation must perform preper printing command.
     *
     * @param itemPreparedForPrinting - item to be processed.
     * @param templateType
     */
    protected abstract void doPrintingAction(Object itemPreparedForPrinting, PrintTemplateType templateType) throws PrintException;

    //==================================== Helpers ======================================
    private static final PrintingPreparator defaultPreparator = new PrintingPreparator() {
        public Object prepareForPrinting(Object objectForPrinting) {
            //Just do nothing, because it is default implementation.
            return objectForPrinting;
        }
    };

}
