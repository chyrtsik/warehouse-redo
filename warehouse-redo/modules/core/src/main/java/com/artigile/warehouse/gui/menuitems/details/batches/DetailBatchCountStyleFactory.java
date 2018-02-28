/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.batches;

import com.artigile.warehouse.gui.core.report.style.Alignment;
import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;

import java.awt.*;

/**
 * Valery Borisok, 26.10.2009
 */
public class DetailBatchCountStyleFactory implements StyleFactory {
    private Style needRecalculateStyle = new Style(null, Color.RED, Alignment.RIGHT, Alignment.CENTER);

    @Override
    public Style getStyle(Object rowData) {
        if (rowData instanceof DetailBatchTO) {
            DetailBatchTO detailBatch = (DetailBatchTO) rowData;
            if (detailBatch.getNeedRecalculate()) {
                return needRecalculateStyle;
            }
        }
        return null;
    }
}
