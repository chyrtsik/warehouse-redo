/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.details.catalog;

import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;

/**
 * Factory for creating detail batches list when catalog is viewied.
 *
 * @author Aliaksandr.Chyrtsik, 26.10.11
 */
public interface DetailCatalogBatchesListFactory {
    /**
     * Create detail batches list for given catalog group.
     * @param catalogGroup catalog group (for details filtering).
     * @return detail batches list for given catalog group.
     */
    ReportDataSource createDetailBatchesDataSource(DetailGroupTO catalogGroup);
}
