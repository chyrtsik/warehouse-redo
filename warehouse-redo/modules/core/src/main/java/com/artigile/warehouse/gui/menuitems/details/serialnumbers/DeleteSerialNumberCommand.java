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

import com.artigile.warehouse.gui.core.report.command.DeleteCommand;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailSerialNumberTO;

/**
 * @author Aliaksandr Chyrtsik
 * @since 06.07.13
 */
public class DeleteSerialNumberCommand extends DeleteCommand{
    public DeleteSerialNumberCommand() {
        super(SerialNumberEditingStrategy.getEditAvailability());
    }

    @Override
    protected boolean doDelete(Object deletingItem) throws ReportCommandException {
        //Delete chosen serial number.
        DetailSerialNumberTO serialNumber = (DetailSerialNumberTO) deletingItem;
        SpringServiceContext.getInstance().getDetailSerialNumberService().deleteSerialNumber(serialNumber.getId());
        return true;
    }
}
