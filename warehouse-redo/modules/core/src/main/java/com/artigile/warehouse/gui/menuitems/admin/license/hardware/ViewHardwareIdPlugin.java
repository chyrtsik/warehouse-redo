/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.admin.license.hardware;

import com.artigile.warehouse.bl.license.LicenseService;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.plugin.PluginParams;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * Plugin for displaying hardware identifier..
 *
 * @author Aliaksandr.Chyrtsik, 07.08.11
 */
public class ViewHardwareIdPlugin implements Plugin {
    @Override
    public void run(PluginParams params) {
        LicenseService licenseService = SpringServiceContext.getInstance().getLicenseService();
        String hardwareId = licenseService.getCurrentHardwareId();
        if (hardwareId == null){
            MessageDialogs.showWarning(I18nSupport.message("hardwareId.error.cannot.generate.hardware.id"));
        }
        else{
            Dialogs.runProperties(new HardwareIdForm(hardwareId));
        }
    }
}
