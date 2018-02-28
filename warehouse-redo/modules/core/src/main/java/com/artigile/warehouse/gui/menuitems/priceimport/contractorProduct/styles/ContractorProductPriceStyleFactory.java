/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport.contractorProduct.styles;

import com.artigile.warehouse.gui.core.report.style.Alignment;
import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.dto.priceimport.ContractorProductTOForReport;

import java.awt.*;

/**
 * Style of cells with prices in the contractor product list.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class ContractorProductPriceStyleFactory implements StyleFactory {

    @Override
    public Style getStyle(Object rowData) {
        ContractorProductTOForReport contractorProduct = (ContractorProductTOForReport) rowData;
        return new Style(new Color(contractorProduct.getCurrency().getAssociatedColor()), Alignment.RIGHT, Alignment.CENTER);
    }
}
