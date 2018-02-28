/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.format.predefined;

import com.artigile.warehouse.gui.core.report.format.ColumnFormatFactory;
import com.artigile.warehouse.utils.custom.types.VariantPrice;
import com.artigile.warehouse.utils.custom.types.VariantQuantity;
import com.artigile.warehouse.utils.formatter.FormatUtils;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.text.NumberFormat;

/**
 * Format factory for columns with numeric values.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class NumericColumnFormatFactory implements ColumnFormatFactory {

    /**
     * Singleton instance
     */
    private static NumericColumnFormatFactory instance;

    /**
     * Predefined format
     */
    private static final NumberFormat columnNumberFormat = FormatUtils.getDecimalFormatInstance("###,###.#######", ',', ' ');


    private NumericColumnFormatFactory() { /* Silence is gold */ }


    public static NumericColumnFormatFactory getInstance() {
        if (instance == null) {
            instance = new NumericColumnFormatFactory();
        }
        return instance;
    }

    @Override
    public String getFormattedValue(Object value) {
        if (value != null) {
            Number cellNumber = null;
            if (value instanceof VariantPrice) {
                cellNumber = ((VariantPrice) value).getPrice();
            } else if (value instanceof VariantQuantity) {
                cellNumber = ((VariantQuantity) value).getQuantity();
            } else {
                try {
                    cellNumber = (Number) value;
                } catch (ClassCastException e) {
                    LoggingFacade.logError(e);
                }
            }
            return cellNumber == null ? value.toString() : columnNumberFormat.format(cellNumber);
        }
        return null;
    }
}
