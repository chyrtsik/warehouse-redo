/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.warehouse.warehousebatch;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.detail.DetailBatchService;
import com.artigile.warehouse.bl.warehouse.StoragePlaceService;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.menuitems.details.history.WarehouseBatchHistoryCommand;
import com.artigile.warehouse.gui.menuitems.details.reserves.WarehouseBatchReservesCommand;
import com.artigile.warehouse.gui.menuitems.warehouse.movedetail.MoveDetailForm;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;

/**
 * @author Borisok V.V., 04.01.2009
 */
public class WarehouseBatchEditingStrategy implements ReportEditingStrategy {

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenWarehouseBatchCommand());
            commands.add(new MoveItemInsideWarehouse());
            commands.add(new ChangeWarehouseBatchCountCommand());
            commands.add(new WarehouseBatchHistoryCommand());
            commands.add(new WarehouseBatchReservesCommand());
        }
    }

    private class OpenWarehouseBatchCommand extends PropertiesCommandBase {
        protected OpenWarehouseBatchCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            WarehouseBatchTO warehouseBatchTO = (WarehouseBatchTO) editingItem;
            PropertiesForm prop = new WarehouseBatchForm(warehouseBatchTO, getEditAvailability().isAvailable(context));
            if (Dialogs.runProperties(prop)) {
                getWarehouseBatchService().save(warehouseBatchTO);
                return true;
            }
            return false;
        }
    }

    private class MoveItemInsideWarehouse extends CustomCommand {
        protected MoveItemInsideWarehouse() {
            super(new ResourceCommandNaming("warehousebatch.command.move.inside.warehouse"), new PermissionCommandAvailability(PermissionType.MOVE_DETAIL_INSIDE_WAREHOUSE));
        }

        @Override
        public ReportCommandType getType() {
            return ReportCommandType.CUSTOM;
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            //Opening items of the selected posting.
            WarehouseBatchTO warehouseBatchTO = (WarehouseBatchTO) context.getCurrentReportItem();
            MoveDetailForm detailsMove = new MoveDetailForm(warehouseBatchTO);
            if (Dialogs.runProperties(detailsMove)) {
                try {
                    getWarehouseBatchService().moveWarehouseBatches(warehouseBatchTO.getId(), detailsMove.getTargetStoragePlaces(), detailsMove.getAmountsToMove());
                } catch (BusinessException e) {
                    throw new ReportCommandException(e);
                }
                return true;
            }
            return false;
        }
    }

    /**
     * Command for changing (fixing) count of wares at warehouse (change count of this exact warehouse batch).
     */
    private class ChangeWarehouseBatchCountCommand extends CustomCommand {

        protected ChangeWarehouseBatchCountCommand() {
            super(new ResourceCommandNaming("warehousebatch.command.changeCount"), new PermissionCommandAvailability(PermissionType.EDIT_WAREHOUSE_BATCH_ITEM_QUANTITY));
        }

        @Override
        protected boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            WarehouseBatchTO warehouseBatchTO = (WarehouseBatchTO) context.getCurrentReportItem();
            ChangeWarehouseBatchCountForm changeCountForm = new ChangeWarehouseBatchCountForm(warehouseBatchTO);
            if (Dialogs.runProperties(changeCountForm)) {
                try {
                    getWarehouseBatchService().performWareCountCorrection(warehouseBatchTO.getId(), changeCountForm.getCountDiff());
                } catch (BusinessException e) {
                    throw new ReportCommandException(e);
                }
                return true;
            }
            return false;
        }
    }

    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_WAREHOUSE_LIST);
    }

    private WarehouseBatchService getWarehouseBatchService() {
        return SpringServiceContext.getInstance().getWarehouseBatchService();
    }

    private DetailBatchService getDetailBatchService(){
        return SpringServiceContext.getInstance().getDetailBatchesService();
    }

    private StoragePlaceService getStoragePlaceService(){
        return SpringServiceContext.getInstance().getStoragePlaceService();
    }
}
