/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.details.outofstock;

import com.artigile.warehouse.gui.utils.BarCodeImage;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.ManufacturerTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldOwner;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;
import com.artigile.warehouse.utils.dto.details.DetailModelTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.parser.ParsedTemplate;
import com.artigile.warehouse.utils.parser.ParsedTemplateDataSource;

import java.math.BigDecimal;

public class OutOfStockProductTO extends EqualsByIdImpl {

    private long productId; //detail batch id
    private String productName;
    private long available;
    private double enoughForMonths;
    private long countToOrder;
    private long orderedCount;

    public OutOfStockProductTO(long productId, String productName, long available, double enoughForMonths, long countToOrder, long orderedCount) {
        this.productId = productId;
        this.productName = productName;
        this.available = available;
        this.enoughForMonths = enoughForMonths;
        this.countToOrder = countToOrder;
        this.orderedCount = orderedCount;
    }

    public long getId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public long getAvailable() {
        return available;
    }

    public double getEnoughForMonths() {
        return enoughForMonths;
    }

    public long getCountToOrder() {
        return countToOrder;
    }

    public long getOrderedCount() {
        return orderedCount;
    }
}
