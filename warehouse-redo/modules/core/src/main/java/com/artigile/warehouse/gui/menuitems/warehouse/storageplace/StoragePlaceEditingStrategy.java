/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.warehouse.storageplace;

import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTO;

/**
 * @author Borisok V.V., 26.12.2008
 */
public class StoragePlaceEditingStrategy implements ReportEditingStrategy {
    /**
     * Editing warehouse.
     */
    private WarehouseTO warehouseTO;

    public StoragePlaceEditingStrategy(WarehouseTO warehouseTO) {
        this.warehouseTO = warehouseTO;
    }

    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateRootStoragePlaceCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateStoragePlaceCommand());
        commands.add(new DeleteStoragePlaceCommand());
        if (context.getCurrentReportItems().size() == 1){
            commands.add(new OpenStoragePlaceCommand());
        }
    }

    //=================================== Commands =============================================
    private class CreateRootStoragePlaceCommand extends CreateCommand {
        protected CreateRootStoragePlaceCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            //Creates new root storage place.
            StoragePlaceTO newStoragePlace = new StoragePlaceTO();
            PropertiesForm prop = new StoragePlaceForm(newStoragePlace, true, new StoragePlaceChecker());
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                return newStoragePlace;
            }
            return null;
        }
    }

    private class CreateStoragePlaceCommand extends CreateCommand {
        protected CreateStoragePlaceCommand() {
            super(getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            //Creates new child (non root) storage place.
            StoragePlaceTO parentPlace = (StoragePlaceTO) context.getCurrentReportItem();

            StoragePlaceTO childStoragePlace = new StoragePlaceTO();
            childStoragePlace.setParentStoragePlace(parentPlace);
            childStoragePlace.setWarehouse(parentPlace.getWarehouse());

            PropertiesForm prop = new StoragePlaceForm(childStoragePlace, true, new StoragePlaceChecker());
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            if (propDialog.run()) {
                return childStoragePlace;
            }
            return null;
        }
    }

    private class DeleteStoragePlaceCommand extends DeleteCommand {
        protected DeleteStoragePlaceCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object item) throws ReportCommandException {
            return true;
        }
    }

    private class OpenStoragePlaceCommand extends PropertiesCommandBase {
        protected OpenStoragePlaceCommand() {
            super(new PredefinedCommandAvailability(true));
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            StoragePlaceTO storagePlace = (StoragePlaceTO) editingItem;
            PropertiesForm prop = new StoragePlaceForm(storagePlace, getEditAvailability().isAvailable(context), new StoragePlaceChecker());
            PropertiesDialog propDialog = new PropertiesDialog(prop);
            return propDialog.run();
        }
    }

    //=============================== Helpers ============================================
    private AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_STORAGE_PLACES_TREE);
    }

    public class StoragePlaceChecker {
        /**
         * Checks, if the given sign of the storage place for the entry warehouse.
         *
         * @param sign                - new sign of the storage place.
         * @param editingStoragePlace - storage place, which sign is being checked.
         * @return true if storage place can by updated or created, otherwise returns false.
         */
        public boolean isUniqueSign(String sign, StoragePlaceTO editingStoragePlace) {
            StoragePlaceTO samePlace = warehouseTO.findStoragePlaceBySign(sign);
            return samePlace == null || samePlace.equals(editingStoragePlace);
        }
    }
}
