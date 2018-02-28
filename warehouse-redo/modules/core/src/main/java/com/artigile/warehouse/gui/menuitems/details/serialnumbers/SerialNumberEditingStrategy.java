/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.serialnumbers;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.core.print.PrintingPreparator;
import com.artigile.warehouse.gui.core.report.command.PrintCurrentReportItemCommand;
import com.artigile.warehouse.gui.core.report.command.PrintPreviewCurrentReportItemCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.utils.dto.details.DetailSerialNumberTO;

import java.util.Arrays;

/**
 * @author Aliaksandr Chyrtsik
 * @since 30.06.13
 */
public class SerialNumberEditingStrategy implements ReportEditingStrategy {
    private SerialNumberList serialNumberList;

    public SerialNumberEditingStrategy(SerialNumberList serialNumberList){
        this.serialNumberList = serialNumberList;
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        //To change body of implemented methods use File | Settings | File Templates.
        commands.add(new CreateSerialNumberCommand(serialNumberList));
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateSerialNumberCommand(serialNumberList));
        commands.add(new DeleteSerialNumberCommand());

        boolean singleItemSelected = context.getCurrentReportItems().size() == 1;
        if (singleItemSelected){
            commands.add(new OpenSerialNumberCommand(serialNumberList));
            commands.add(new PrintPreviewCurrentReportItemCommand(PrintTemplateType.TEMPLATE_SERIAL_NUMBER_LIST, getPrintAvailability(), getPrintPreparator()));
            commands.add(new PrintCurrentReportItemCommand(PrintTemplateType.TEMPLATE_SERIAL_NUMBER_LIST, getPrintAvailability(), getPrintPreparator()));
        }
    }

    public static AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_SERIAL_NUMBERS);
    }

    private AvailabilityStrategy getPrintAvailability() {
        return new PredefinedCommandAvailability(true);
    }

    private PrintingPreparator getPrintPreparator() {
        return new PrintingPreparator() {
            @Override
            public Object prepareForPrinting(Object objectForPrinting) {
                return new SerialNumbersForPrinting(Arrays.asList((DetailSerialNumberTO)objectForPrinting));
            }
        };
    }
}
