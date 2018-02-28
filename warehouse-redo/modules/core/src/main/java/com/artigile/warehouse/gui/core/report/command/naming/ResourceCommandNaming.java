/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.command.naming;

import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author Shyrik, 20.12.2008
 */

/**
 * This strategy used to give command a simple name from the resource.
 */
public class ResourceCommandNaming implements NamingStrategy {
    private String resName;

    public ResourceCommandNaming(@PropertyKey(resourceBundle = "i18n.warehouse")String resName) {
        this.resName = resName;
    }

    @Override
    public String getName(ReportCommandContext context) {
        return I18nSupport.message(resName);
    }
}
