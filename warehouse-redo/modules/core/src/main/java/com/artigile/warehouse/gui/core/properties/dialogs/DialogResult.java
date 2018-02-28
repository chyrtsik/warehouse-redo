/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs;

/**
 * @author Shyrik, 08.01.2009
 */

/**
 * Result of running dialog.
 */
public class DialogResult {
    /**
     * Result of showing dialog (true, if user pressed OK).
     */
    protected boolean dialogResult;

    public DialogResult(boolean dialogResult) {
        this.dialogResult = dialogResult;
    }

    public boolean isOk() {
        return dialogResult;
    }

    public void setDialogResult(boolean dialogResult) {
        this.dialogResult = dialogResult;
    }
}
