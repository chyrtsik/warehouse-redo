/*
 * Copyright (c) 2007-2012 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.print.templates;

import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;

import java.awt.*;

/**
 * @author Aliaksandr.Chyrtsik, 9/11/12
 */
public class PrintTemplateItemStyleFactory implements StyleFactory {
    private Style defaultTemplateStyle = new Style(new Color(200, 255, 200));

    @Override
    public Style getStyle(Object rowData) {
        PrintTemplateInstanceTO templateItem = (PrintTemplateInstanceTO) rowData;
        return templateItem.getDefaultTemplate() ? defaultTemplateStyle : null;
    }
}
