/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.bl.detail.DetailBatchHistoryFilter;
import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.details.DetailBatchOperation;

import java.util.Date;
import java.util.List;

/**
 * @author Aliaksandr.Chyrtsik, 02.10.11
 */
public interface DetailBatchHistoryDAO extends EntityDAO<DetailBatchOperation>{
    /**
     * Load report about changes of items in stock withing specified period.
     * @param periodStart start of the period (null to load all items from the beginning).
     * @param periodEnd end of the period (null to load all items till the end).
     * @param filter filter to load report only for some particular items.
     * @return list of all changes made in stock.
     */
    List<DetailBatchOperation> loadStockChangeHistory(Date periodStart, Date periodEnd, DetailBatchHistoryFilter filter);

    /**
     * Load stock report (numbers of items in stock) at given date.
     * @param date date of stock report (not null).
     * @param filter filter to load only particular items.
     * @return the last operations (final count of each operation will give number of items in stock).
     */
    List<DetailBatchOperation> loadStockReport(Date date, DetailBatchHistoryFilter filter);
}
