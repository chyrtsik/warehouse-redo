/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport.contractorProduct;

import com.artigile.warehouse.bl.priceimport.ContractorProductFilter;
import com.artigile.warehouse.bl.util.collections.ContractorProductNavigableList;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;

import java.util.List;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class AllContractorProductList extends BaseContractorProductList {

    private ContractorProductEditingStrategy editingStrategy;

    /**
     * Local copy of the data for report table
     */
    private ContractorProductNavigableList contractorProductList;


    /* Constructor
    ------------------------------------------------------------------------------------------------------------------*/
    public AllContractorProductList(ContractorProductFilter filter) {
        super(filter);
        this.editingStrategy = new ContractorProductEditingStrategy(contractorColumnIndex);
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return editingStrategy;
    }

    @Override
    public List getReportData() {
        if (loadData || contractorProductList == null || contractorProductList.getAllElements().isEmpty()) {
            contractorProductList = new ContractorProductNavigableList(filter);
        }
        loadData = true;
        applyConversionCurrency(contractorProductList.getCurrentElements());
        calculateAdditionalPrices(contractorProductList.getCurrentElements());
        return contractorProductList.getCurrentElements();
    }


    /* Getters
    ------------------------------------------------------------------------------------------------------------------*/
    public ContractorProductNavigableList getContractorProductList() {
        return contractorProductList;
    }
}
