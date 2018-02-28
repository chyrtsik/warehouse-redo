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
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.custom.types.VariantPrice;
import com.artigile.warehouse.utils.dto.priceimport.ContractorProductTOForReport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class SelectedContractorProductList extends BaseContractorProductList {

    private SelectedContractorProductEditingStrategy editingStrategy;

    /**
     * Local copy of the data for report table
     */
    private List<ContractorProductTOForReport> contractorProductList;


    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    public SelectedContractorProductList(ContractorProductFilter filter) {
        super(filter);
        this.editingStrategy = new SelectedContractorProductEditingStrategy(contractorColumnIndex);
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public ReportEditingStrategy getReportEditingStrategy() {
        return editingStrategy;
    }

    @Override
    public List getReportData() {
        if (loadData || contractorProductList == null || contractorProductList.isEmpty()) {
            contractorProductList = new ArrayList<ContractorProductTOForReport>(SpringServiceContext.getInstance()
                    .getContractorProductService().getListByFilter(filter));
            for (ContractorProductTOForReport contractorProduct : contractorProductList) {
                contractorProduct.setOriginalWholesalePrice(new VariantPrice(contractorProduct.getWholesalePrice().getPrice()));
                contractorProduct.setOriginalRetailPrice(new VariantPrice(contractorProduct.getRetailPrice().getPrice()));
                contractorProduct.setOriginalCurrency(contractorProduct.getCurrency());
            }
        }
        loadData = true;
        applyConversionCurrency(contractorProductList);
        calculateAdditionalPrices(contractorProductList);
        return contractorProductList;
    }
}
