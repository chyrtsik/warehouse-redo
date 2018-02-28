/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.priceimport;

import com.artigile.warehouse.dao.util.filters.DataLimit;
import com.artigile.warehouse.dao.util.filters.DataOrder;
import com.artigile.warehouse.dao.util.filters.OrderType;
import com.artigile.warehouse.domain.priceimport.ContractorProduct;

/**
 *
 * @author Valery.Barysok
 */
public class ContractorProductFilter {

    private String nameMask;

    private boolean ignoreSpecialSymbols;

    /**
     * Position, selected by user
     */
    private Boolean selected;

    private Long contractorID;

    /**
     * Results limitation
     */
    private DataLimit dataLimit;

    /**
     * Results ordering
     */
    private DataOrder dataOrder;


    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    public ContractorProductFilter() {
        this.dataLimit = new DataLimit();
        this.dataOrder = new DataOrder(OrderType.DESC, ContractorProduct.postingDateF);
    }


    /* Getters and setters
    ------------------------------------------------------------------------------------------------------------------*/
    public String getNameMask() {
        return nameMask;
    }

    public void setNameMask(String nameMask) {
        this.nameMask = nameMask;
    }

    public DataLimit getDataLimit() {
        return dataLimit;
    }

    public void setDataLimit(DataLimit dataLimit) {
        this.dataLimit = dataLimit;
    }

    public boolean isIgnoreSpecialSymbols() {
        return ignoreSpecialSymbols;
    }

    public void setIgnoreSpecialSymbols(boolean ignoreSpecialSymbols) {
        this.ignoreSpecialSymbols = ignoreSpecialSymbols;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Long getContractorID() {
        return contractorID;
    }

    public void setContractorID(Long contractorID) {
        this.contractorID = contractorID;
    }

    public DataOrder getDataOrder() {
        return dataOrder;
    }

    public void setDataOrder(DataOrder dataOrder) {
        this.dataOrder = dataOrder;
    }
}
