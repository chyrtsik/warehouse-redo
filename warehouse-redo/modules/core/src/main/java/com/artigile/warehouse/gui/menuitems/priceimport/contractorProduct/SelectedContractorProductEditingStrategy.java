/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport.contractorProduct;

import com.artigile.warehouse.gui.core.report.command.ReportCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.ReportCommandList;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.EmailConfigTO;
import com.artigile.warehouse.utils.dto.priceimport.ContractorProductTOForReport;

import java.util.List;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class SelectedContractorProductEditingStrategy extends BaseContractorProductEditingStrategy
        implements ReportEditingStrategy {

    /**
     * If email service is not configured, then not used command dependent from him.
     */
    private static boolean emailServiceConfigured;
    static {
        EmailConfigTO appEmailConfig = SpringServiceContext.getInstance().getEmailConfigService().getAppEmailConfig();
        emailServiceConfigured = (appEmailConfig != null && appEmailConfig.isConfigured());
    }


    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    public SelectedContractorProductEditingStrategy(int contractorColumnIndex) {
        setContractorColumnIndex(contractorColumnIndex);
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        //No commands are supported.
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        List<ContractorProductTOForReport> reportItems = context.getCurrentReportItems();
        commands.add(new DeselectContractorProductCommand());

        // Request at purchase command
        if (emailServiceConfigured && isValidProducts(reportItems)) {
            commands.add(new RequestSelectedPositionsPurchaseCommand());
        }

        if (reportItems.size() == 1) {
            //Contractor properties command is default command for contractor column.
            ReportCommand openContractorCommand = new ViewContractorDetailsCommand();
            commands.add(openContractorCommand);
            commands.setDefaultCommandForColumn(getContractorColumnIndex(), openContractorCommand);
            commands.add(new OpenContractorProductCommand());
        }
    }

    private boolean isValidProducts(List<ContractorProductTOForReport> products) {
        ContractorTO firstContractor = products.get(0).getPriceImport().getContractor();
        if (StringUtils.containsSymbols(firstContractor.getEmail())) {
            long firstContractorID = firstContractor.getId();
            for (ContractorProductTOForReport product : products) {
                if (product.getPriceImport().getContractor().getId() != firstContractorID) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
