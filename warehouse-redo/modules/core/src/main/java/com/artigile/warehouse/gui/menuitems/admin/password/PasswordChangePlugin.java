/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.admin.password;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.Plugin;
import com.artigile.warehouse.gui.core.plugin.PluginParams;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;

/**
 * @author Shyrik, 19.02.2010
 */

/**
 * Plugin, that runs change password dialog.
 */
public class PasswordChangePlugin implements Plugin {
    @Override
    public void run(PluginParams params) {
        UserTO user = WareHouse.getUserSession().getUser();
        PasswordChangeForm changePasswordForm = new PasswordChangeForm(user.getPassword());
        if (Dialogs.runProperties(changePasswordForm)) {
            try {
                SpringServiceContext.getInstance().getUserService().updatePassword(user.getId(), changePasswordForm.getNewPassword());
            } catch (BusinessException e) {
                MessageDialogs.showWarning(e.getLocalizedMessage());
            }
        }
    }
}
