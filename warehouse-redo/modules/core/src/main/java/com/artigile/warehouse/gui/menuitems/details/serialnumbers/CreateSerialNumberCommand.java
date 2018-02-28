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

import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.CreateCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailSerialNumberTO;

/**
 * @author Aliaksandr Chyrtsik
 * @since 30.06.13
 */
public class CreateSerialNumberCommand extends CreateCommand {
    private SerialNumberList serialNumberList;

    public CreateSerialNumberCommand(SerialNumberList serialNumberList) {
        super(SerialNumberEditingStrategy.getEditAvailability());
        this.serialNumberList = serialNumberList;
    }

    @Override
    protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
        DetailSerialNumberTO serialNumber = new DetailSerialNumberTO();
        PropertiesForm prop = new SerialNumberForm(serialNumber, true);
        if (Dialogs.runProperties(prop)){
            SpringServiceContext.getInstance().getDetailSerialNumberService().saveSerialNumber(serialNumber);
            serialNumberList.prepareSerialNumberForReport(serialNumber);
            return serialNumber;
        }
        return null;
    }
}
