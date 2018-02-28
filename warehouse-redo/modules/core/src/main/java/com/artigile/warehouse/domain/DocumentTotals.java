/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain;

import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Non an entity. Read only object. Contains totals for one concrete document (movement, order and so on).
 * @author Aliaksandr Chyrtsik
 * @since 02.06.13
 */
public class DocumentTotals {
    /**
     * Total prices of the document. More than one record exists only if 2 or more currencies are used at the sale time.
     */
    private Map<CurrencyTO, BigDecimal> totalPrices;

    /**
     * Total counts of the document. More than one record exists when items are measures in different units.
     */
    private Map<MeasureUnitTO, BigDecimal> totalCounts;

    public DocumentTotals(Map<CurrencyTO, BigDecimal> totalPrices, Map<MeasureUnitTO, BigDecimal> totalCounts) {
        this.totalPrices = totalPrices;
        this.totalCounts = totalCounts;
    }

    public Map<CurrencyTO, BigDecimal> getTotalPrices() {
        return totalPrices;
    }

    public Map<MeasureUnitTO, BigDecimal> getTotalCounts() {
        return totalCounts;
    }
}
