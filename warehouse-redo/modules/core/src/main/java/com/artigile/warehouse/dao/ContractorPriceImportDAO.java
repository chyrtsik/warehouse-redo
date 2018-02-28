/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.bl.priceimport.PriceImportFilter;
import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.priceimport.ContractorPriceImport;

import java.util.List;

/**
 *
 * @author Valery.Barysok
 */
public interface ContractorPriceImportDAO extends EntityDAO<ContractorPriceImport> {

    /**
     * Loads a list of price list imports using given filter.
     *
     * @param filter filter to be used.
     * @return list of results or empty list of no results are loaded.
     */
    List<ContractorPriceImport> getListByFilter(PriceImportFilter filter);

    /**
     * @return All imports, grouped by contractor's ID
     */
    List<ContractorPriceImport> getListGroupedByContractorID();

    void removePrevContractorProducts(long priceImportId);

    int getTotalItemCount(long priceImportId);
}
