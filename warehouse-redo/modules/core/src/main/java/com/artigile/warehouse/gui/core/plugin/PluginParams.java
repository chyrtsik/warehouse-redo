/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.plugin;

import com.artigile.warehouse.domain.admin.User;


/**
 * @author Shyrik, 04.12.2008
 */

/**
 * Class holds parameters that are passed to the plugin when it starts.
 */
public class PluginParams {


    /**
     * Edited User for User Form
     */
    private User editedUser;

    public PluginParams(User editedUser) {
        this.editedUser = editedUser;
    }

    public User getEditedUser() {
        return editedUser;
    }

    public void setEditedUser(User editedUser) {
        this.editedUser = editedUser;
    }
}
