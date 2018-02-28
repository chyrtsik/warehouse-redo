/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.basedirectory.measureunit;

import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;

import java.awt.*;

/**
 * @author Shyrik, 08.08.2009
 */
public class MeasureUnitStyleFactory implements StyleFactory {
    private Style defaultMeasureStyle = new Style(new Color(200, 255, 200));

    @Override
    public Style getStyle(Object rowData) {
        MeasureUnitTO measureUnit = (MeasureUnitTO) rowData;
        return measureUnit.getDefaultMeasureUnit() ? defaultMeasureStyle : null;
    }
}
