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

import com.artigile.warehouse.bl.priceimport.ContractorProductService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.contractors.OpenContractorCommand;
import com.artigile.warehouse.gui.menuitems.priceimport.ContractorProductForm;
import com.artigile.warehouse.gui.menuitems.priceimport.ContractorProductSelectedPositionsPurchaseForm;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.priceimport.ContractorProductTOForReport;

import java.util.List;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class BaseContractorProductEditingStrategy implements ReportEditingStrategy {

    private int contractorColumnIndex;

    protected ContractorProductService getContractorProductService() {
        return SpringServiceContext.getInstance().getContractorProductService();
    }

    /* Commands
    ------------------------------------------------------------------------------------------------------------------*/
    protected class ViewContractorDetailsCommand extends CustomCommand {

        protected ViewContractorDetailsCommand() {
            super(new ResourceCommandNaming("contractor.product.command.contractorData"), new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            //Delegate command execution to original open contractor command.
            ContractorProductTOForReport product = (ContractorProductTOForReport) context.getCurrentReportItem();
            ReportCommandContext contextForContractorCommand = new ReportCommandCurrentItemContext(product.getPriceImport().getContractor());
            ReportCommand command = new OpenContractorCommand();
            return command.execute(contextForContractorCommand);
        }
    }

    protected class OpenContractorProductCommand extends PropertiesCommandBase {

        protected OpenContractorProductCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            ContractorProductTOForReport contractorProduct = (ContractorProductTOForReport) editingItem;
            PropertiesForm prop = new ContractorProductForm(contractorProduct, false);
            if (Dialogs.runProperties(prop)) {
                getContractorProductService().saveContractorProductWithFlush(contractorProduct);
                return true;
            }

            return false;
        }
    }

    protected class DeleteContractorProductCommand extends DeleteCommand {

        protected DeleteContractorProductCommand() {
            super(new PermissionCommandAvailability(PermissionType.EDIT_CONTRACTOR_PRODUCT_LIST));
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            getContractorProductService().deleteContractorProduct(((ContractorProductTOForReport) deletingItem).getId());
            return true;
        }
    }

    protected class SelectContractorProductCommand extends CustomCommand {

        protected SelectContractorProductCommand() {
            super(new ResourceCommandNaming("contractor.product.command.selectPosition"), new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            List<ContractorProductTOForReport> contractorProductList = context.getCurrentReportItems();
            if (contractorProductList != null) {
                for (ContractorProductTOForReport contractorProduct : contractorProductList) {
                    getContractorProductService().selectContractorProduct(contractorProduct.getId());
                }
                return true;
            } else {
                return false;
            }
        }
    }

    protected class DeselectContractorProductCommand extends CustomCommand {

        protected DeselectContractorProductCommand() {
            super(new ResourceCommandNaming("contractor.product.command.deselectPosition"), new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            List<ContractorProductTOForReport> contractorProductList = context.getCurrentReportItems();
            if (contractorProductList != null) {
                for (ContractorProductTOForReport contractorProduct : contractorProductList) {
                    getContractorProductService().deselectContractorProduct(contractorProduct.getId());
                    context.getReportModel().deleteItem(contractorProduct);
                }
                return true;
            } else {
                return false;
            }
        }
    }

    protected class RequestSelectedPositionsPurchaseCommand extends CustomCommand {

        protected RequestSelectedPositionsPurchaseCommand() {
            super(new ResourceCommandNaming("contractor.product.command.requestSelectedPositionsPurchase"),
                    new PermissionCommandAvailability(PermissionType.REQUEST_SELECTED_POSITIONS_PURCHASE));
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            return Dialogs.runProperties(new ContractorProductSelectedPositionsPurchaseForm(context.getCurrentReportItems()));
        }
    }

    /* Setters and getters
    ------------------------------------------------------------------------------------------------------------------*/
    protected int getContractorColumnIndex() {
        return contractorColumnIndex;
    }

    protected void setContractorColumnIndex(int contractorColumnIndex) {
        this.contractorColumnIndex = contractorColumnIndex;
    }
}
