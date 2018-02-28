/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.priceimport.ContractorProduct;
import com.artigile.warehouse.utils.custom.types.VariantPrice;
import com.artigile.warehouse.utils.custom.types.VariantQuantity;
import com.artigile.warehouse.utils.dto.priceimport.ContractorProductTOForReport;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Valery.Barysok
 */
public class ContractorProductTransformer {
    
    private ContractorProductTransformer() {
    }

    private static ContractorProductTOForReport transform(ContractorProduct contractorProduct) {
        ContractorProductTOForReport contractorProductTOForReport = new ContractorProductTOForReport();
        update(contractorProductTOForReport, contractorProduct);
        return contractorProductTOForReport;
    }

    public static List<ContractorProductTOForReport> transformList(List<ContractorProduct> contractorProducts) {
        List<ContractorProductTOForReport> contractorProductsTO = new ArrayList<ContractorProductTOForReport>();
        for (ContractorProduct contractorProduct : contractorProducts){
            contractorProductsTO.add(transform(contractorProduct));            
        }
        return contractorProductsTO;
    }

    public static ContractorProduct transform(ContractorProductTOForReport contractorProductTOForReport) {
        ContractorProduct contractorProduct = new ContractorProduct();
        update(contractorProduct, contractorProductTOForReport);
        return contractorProduct;
    }

    public static void update(ContractorProductTOForReport contractorProductTOForReport, ContractorProduct contractorProduct) {
        contractorProductTOForReport.setId(contractorProduct.getId());
        contractorProductTOForReport.setName(contractorProduct.getName());
        contractorProductTOForReport.setDescription(contractorProduct.getDescription());
        contractorProductTOForReport.setYear(contractorProduct.getYear());
        contractorProductTOForReport.setQuantity(new VariantQuantity(contractorProduct.getQuantity()));
        contractorProductTOForReport.setWholesalePrice(new VariantPrice(contractorProduct.getWholesalePrice()));
        contractorProductTOForReport.setRetailPrice(new VariantPrice(contractorProduct.getRetailPrice()));
        contractorProductTOForReport.setPack(new VariantQuantity(contractorProduct.getPack()));
        contractorProductTOForReport.setProductType(contractorProduct.getProductType());
        contractorProductTOForReport.setPostingDate(contractorProduct.getPostingDate());
        contractorProductTOForReport.setCurrency(CurrencyTransformer.transformCurrency(contractorProduct.getCurrency()));
        contractorProductTOForReport.setMeasureUnit(MeasureUnitTransformer.transform(contractorProduct.getMeasureUnit()));
        contractorProductTOForReport.setSelected(contractorProduct.isSelected());
        contractorProductTOForReport.setPriceImport(ContractorPriceImportTransformer.transform(contractorProduct.getPriceImport()));
    }

    public static void update(ContractorProduct contractorProduct, ContractorProductTOForReport contractorProductTOForReport) {
        contractorProduct.setId(contractorProductTOForReport.getId());
        contractorProduct.setName(contractorProductTOForReport.getName());
        contractorProduct.setDescription(contractorProductTOForReport.getDescription());
        contractorProduct.setYear(contractorProductTOForReport.getYear());
        VariantQuantity quantity = contractorProductTOForReport.getQuantity();
        //TODO: eliminate ""
        contractorProduct.setQuantity(quantity != null ? quantity.getValue() : "");
        VariantPrice wholesalePrice = contractorProductTOForReport.getWholesalePrice();
        contractorProduct.setWholesalePrice(wholesalePrice != null ? wholesalePrice.getValue() : "");
        VariantPrice retailPrice = contractorProductTOForReport.getRetailPrice();
        contractorProduct.setRetailPrice(retailPrice != null ? retailPrice.getValue() : "");
        VariantQuantity pack = contractorProductTOForReport.getPack();
        contractorProduct.setPack(pack != null ? pack.getValue() : "");
        String productType = contractorProductTOForReport.getProductType();
        contractorProduct.setProductType(productType != null ? productType : "");
        contractorProduct.setPostingDate(contractorProductTOForReport.getPostingDate());
        contractorProduct.setCurrency(CurrencyTransformer.transformCurrency(contractorProductTOForReport.getCurrency()));
        contractorProduct.setMeasureUnit(MeasureUnitTransformer.transform(contractorProductTOForReport.getMeasureUnit()));
        contractorProduct.setSelected(contractorProductTOForReport.isSelected());
        contractorProduct.setPriceImport(ContractorPriceImportTransformer.transform(contractorProductTOForReport.getPriceImport()));
    }
}
