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

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.priceimport.ContractorPriceImportService;
import com.artigile.warehouse.gui.core.report.command.DeleteCommand;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.priceimport.importing.PriceImportEditingStrategy;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;

/**
 * Command for deleting of price list import.
 *
 * @author Valery Barysok, 6/11/11
 */
public class DeletePriceImportCommand extends DeleteCommand {
    public DeletePriceImportCommand() {
        super(PriceImportEditingStrategy.getEditAvailability());
    }

    @Override
    protected boolean doDelete(Object deletingItem) throws ReportCommandException {
        try {
            ContractorPriceImportService priceImportService = SpringServiceContext.getInstance().getContractorPriceImportService();
            priceImportService.deletePriceImport((ContractorPriceImportTO) deletingItem);
        } catch (BusinessException e) {
            throw new ReportCommandException(e);
        }
        return true;
    }
}