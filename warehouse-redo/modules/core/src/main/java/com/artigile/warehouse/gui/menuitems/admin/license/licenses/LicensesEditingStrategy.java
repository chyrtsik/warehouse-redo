/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.admin.license.licenses;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.license.LicenseService;
import com.artigile.warehouse.domain.admin.PermissionType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.command.*;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.command.availability.PermissionCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.license.LicenseTO;

/**
 * @author Aliaksandr.Chyrtsik, 04.08.11
 */
public class LicensesEditingStrategy implements ReportEditingStrategy {
    @Override
    public void getCommandsForReport(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateLicenseCommand());
    }

    @Override
    public void getCommandsForItem(ReportCommandList commands, ReportCommandContext context) {
        commands.add(new CreateLicenseCommand());
        commands.add(new DeleteLicenseCommand());
        if (context.getCurrentReportItems().size() == 1){
            LicenseTO licence = (LicenseTO)context.getCurrentReportItem();
            if (licence.isValidLicenseData()){
                commands.add(new OpenLicenseCommand());
            }
        }
    }

    private class CreateLicenseCommand extends CreateCommand{
        protected CreateLicenseCommand() {
            super(new ResourceCommandNaming("license.command.create"), getEditAvailability());
        }

        @Override
        protected Object doCreate(ReportCommandContext context) throws ReportCommandException {
            //Adds new license to system database.
            AddLicenseForm addLicenseForm = new AddLicenseForm();
            if (!Dialogs.runProperties(addLicenseForm)){
                return null;
            }

            LicenseService licenseService = SpringServiceContext.getInstance().getLicenseService();
            try {
                LicenseTO createdLicense = licenseService.addLicense(addLicenseForm.getLicenseData());
                WareHouse.refreshMenuTree();
                return createdLicense;
            } catch (BusinessException e) {
                throw new ReportCommandException(e);
            }
        }
    }

    private class DeleteLicenseCommand extends DeleteCommand{
        protected DeleteLicenseCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doDelete(Object deletingItem) throws ReportCommandException {
            //Deletes license from database.
            LicenseTO license = (LicenseTO)deletingItem;
            LicenseService licenseService = SpringServiceContext.getInstance().getLicenseService();
            licenseService.deleteLicense(license.getId());
            WareHouse.refreshMenuTree();
            return true;
        }
    }

    private class OpenLicenseCommand extends PropertiesCommandBase{
        protected OpenLicenseCommand() {
            super(getEditAvailability());
        }

        @Override
        protected boolean doProperties(Object editingItem, ReportCommandContext context) throws ReportCommandException {
            //Opens properties of current license. All data are read only.
            LicenseTO license = (LicenseTO)editingItem;
            PropertiesForm licenseProp = new LicensePropertiesForm(license);
            Dialogs.runProperties(licenseProp);
            return false;
        }
    }

    private static AvailabilityStrategy getEditAvailability() {
        return new PermissionCommandAvailability(PermissionType.EDIT_LICENSES);
    }
}
