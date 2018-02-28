/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport.importing;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.domain.dataimport.ImportStatus;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.menuitems.priceimport.importing.commands.*;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.date.DateUtils;
import com.artigile.warehouse.utils.dto.EmailConfigTO;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Editing strategy of the price-list imports.
 *
 * @author Valery Barysok, 6/11/11
 */

public class PriceImportEditingStrategy implements ReportEditingStrategy {

    // Interval between repeated requests to each contractor (in days)
    private static final int PRICE_LIST_REPEATED_REQUESTS_INTERVAL = 2;

    /**
     * If email service is not configured, then not used command dependent from him.
     */
    private static boolean emailServiceConfigured;
    static {
        EmailConfigTO appEmailConfig = SpringServiceContext.getInstance().getEmailConfigService().getAppEmailConfig();
        emailServiceConfigured = (appEmailConfig != null && appEmailConfig.isConfigured());
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreatePriceImportCommand());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreatePriceImportCommand());
        commands.add(new DeletePriceImportCommand());

        List<ContractorPriceImportTO> reportItems = context.getCurrentReportItems();
        if (reportItems.size() == 1) {
            commands.add(new OpenPriceImportCommand());
            ContractorPriceImportTO item = (ContractorPriceImportTO) context.getCurrentReportItem();
            if (item.getImportStatus().equals(ImportStatus.COMPLETED)){
                commands.add(new RollbackPriceImportCommand());
            }
        }

        // Request new price list command.
        if (emailServiceConfigured && containsAvailableForRequestingContractor(reportItems)) {
            commands.add(new RequestPriceImportCommand());
        }
    }

    /**
     * Checks that at least one contractor may receive request at new price list.
     *
     * @param priceImports Imports, which contains information about contractors
     * @return True - at least one contractor may receive request at new price list, false - if nobody may receive request
     */
    private boolean containsAvailableForRequestingContractor(List<ContractorPriceImportTO> priceImports) {
        Date priceListRequestDeadline = DateUtils.moveDatetime(DateUtils.now(), Calendar.DATE, -PRICE_LIST_REPEATED_REQUESTS_INTERVAL);
        for (ContractorPriceImportTO priceImport : priceImports) {
            Date priceListRequest = priceImport.getContractor().getPriceListRequest();
            if (!StringUtils.containsSymbols(priceImport.getContractor().getEmail())
                    && (priceListRequest == null || priceListRequest.before(priceListRequestDeadline))) {
                return true;
            }
        }
        return false;
    }

    public static AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_PRICE_IMPORT_LIST);
    }
}
