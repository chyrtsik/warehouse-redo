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

import com.artigile.warehouse.bl.detail.DetailSerialNumberService;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.PropertiesCommandBase;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailSerialNumberTO;

/**
 * @author Aliaksandr Chyrtsik
 * @since 06.07.13
 */
public class OpenSerialNumberCommand extends PropertiesCommandBase {
    private SerialNumberList serialNumberList;

    public OpenSerialNumberCommand(SerialNumberList serialNumberList) {
        super(new PredefinedCommandAvailability(true));
        this.serialNumberList = serialNumberList;
    }

    @Override
    protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
        //Before editing we have to realod serial number from database to remove extra fields that can be added by report.
        DetailSerialNumberService serialNumberService = SpringServiceContext.getInstance().getDetailSerialNumberService();
        DetailSerialNumberTO serialNumberFromReport = (DetailSerialNumberTO) editingItem;
        DetailSerialNumberTO serialNumberToEdit = serialNumberService.loadSerialNumberTOById(serialNumberFromReport.getId());

        //Edit properties of the serial number.
        PropertiesForm prop = new SerialNumberForm(serialNumberToEdit, SerialNumberEditingStrategy.getEditAvailability().isAvailable(context));
        if (Dialogs.runProperties(prop)) {
            //Saving edited serial number.
            serialNumberService.saveSerialNumber(serialNumberToEdit);
            serialNumberFromReport.copyFrom(serialNumberToEdit);
            serialNumberList.prepareSerialNumberForReport(serialNumberFromReport);
            return true;
        }
        return false;
    }
}
