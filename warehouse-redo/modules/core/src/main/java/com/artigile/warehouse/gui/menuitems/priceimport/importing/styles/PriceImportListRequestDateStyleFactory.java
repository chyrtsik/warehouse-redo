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
public class PriceImportListRequestDateStyleFactory implements StyleFactory {

    /**
     * Interval between repeated requests to each contractor (in days)
     */
    private static final int PRICE_LIST_REPEATED_REQUESTS_INTERVAL = 2;

    private static final Style DISABLE_REPEATED_REQUEST_STYLE = new Style(new Color(0, 204, 0));

    
    @Override
    public Style getStyle(Object rowData) {
        Date priceListRequest = ((ContractorPriceImportTO) rowData).getContractor().getPriceListRequest();
        Date requestDeadline = DateUtils.moveDatetime(DateUtils.now(), Calendar.DATE, -PRICE_LIST_REPEATED_REQUESTS_INTERVAL);
        return (priceListRequest != null && priceListRequest.after(requestDeadline))
                ? DISABLE_REPEATED_REQUEST_STYLE
                : null;
    }
}
