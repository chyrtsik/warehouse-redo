/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport.importing.commands;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.priceimport.PriceImportRequestSelectedForm;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class RequestPriceImportCommand extends CustomCommand {

    public RequestPriceImportCommand() {
        super(new ResourceCommandNaming("price.import.command.requestPriceList"),
                new PermissionCommandAvailability(PermissionType.REQUEST_PRICE_LIST));
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    @SuppressWarnings("unchecked")
    protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
        List<ContractorTO> contractorsRecipients = new ArrayList<ContractorTO>();
        for (ContractorPriceImportTO priceImport : (List<ContractorPriceImportTO>)context.getCurrentReportItems()) {
            if (!StringUtils.isStringNullOrEmpty(priceImport.getContractor().getEmail())) {
                contractorsRecipients.add(priceImport.getContractor());
            }
        }
        return Dialogs.runProperties(new PriceImportRequestSelectedForm(contractorsRecipients));
    }
}
