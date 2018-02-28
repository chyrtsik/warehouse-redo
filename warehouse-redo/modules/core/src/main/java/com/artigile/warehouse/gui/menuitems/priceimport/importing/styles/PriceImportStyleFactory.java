/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport.importing.styles;

import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.date.DateUtils;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;

import java.awt.*;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class PriceImportStyleFactory implements StyleFactory {

    /**
     * Styles for price-lists, which...
     *
     * ... was imported less than one week ago
     */
    private static final Style ONE_WEEK_IMPORTED_PRICE = new Style(new Color(200, 255, 200));

    /**
     * .. was imported from one to two weeks ago
     */
    private static final Style TWO_WEEKS_IMPORTED_PRICE = new Style(new Color(240, 240, 0));

    /**
     * ... was imported more than two weeks ago
     */
    private static final Style MORE_THAN_TWO_WEEKS_IMPORTED_PRICE = new Style(new Color(212, 212, 212));

    private Date now;
    private Date weekAgo;
    private Date twoWeeksAgo;


    /* Constructor
    ------------------------------------------------------------------------------------------------------------------*/
    public PriceImportStyleFactory() {
        now = DateUtils.now();
        weekAgo = DateUtils.moveDatetime(now, Calendar.WEEK_OF_MONTH, -1);
        twoWeeksAgo = DateUtils.moveDatetime(now, Calendar.WEEK_OF_MONTH, -2);
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public Style getStyle(Object rowData) {
        Date importDate = ((ContractorPriceImportTO) rowData).getImportDate();
        if (importDate != null) {
            if (DateUtils.isBetweenDates(importDate, weekAgo, now, true)) {
                return ONE_WEEK_IMPORTED_PRICE;
            } else if (DateUtils.isBetweenDates(importDate, twoWeeksAgo, weekAgo, true)) {
                return TWO_WEEKS_IMPORTED_PRICE;
            } else if (importDate.before(twoWeeksAgo)) {
                return MORE_THAN_TWO_WEEKS_IMPORTED_PRICE;
            }
        }
        return null;
    }
}
