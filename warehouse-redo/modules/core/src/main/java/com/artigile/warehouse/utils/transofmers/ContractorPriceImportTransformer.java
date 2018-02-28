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

import com.artigile.warehouse.domain.priceimport.ContractorPriceImport;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Valery.Barysok
 */
public class ContractorPriceImportTransformer {
    
    private ContractorPriceImportTransformer() {
    }

    public static ContractorPriceImportTO transform(ContractorPriceImport priceImport) {
        ContractorPriceImportTO priceImportTO = new ContractorPriceImportTO();
        update(priceImportTO, priceImport);
        return priceImportTO;
    }

    public static List<ContractorPriceImportTO> transformList(List<ContractorPriceImport> priceImports) {
        List<ContractorPriceImportTO> priceImportsTO = new ArrayList<ContractorPriceImportTO>();
        for (ContractorPriceImport priceImport : priceImports){
            priceImportsTO.add(transform(priceImport));            
        }
        return priceImportsTO;
    }

    public static void update(ContractorPriceImportTO priceImportTO, ContractorPriceImport priceImport) {
        //Common data import transformation.
        DataImportTransformer.update(priceImportTO, priceImport);
        //Contractor price specific transformation.
        priceImportTO.setItemCount(priceImport.getItemCount());
        priceImportTO.setIgnoredItemCount(priceImport.getIgnoredItemCount());
        priceImportTO.setContractor(ContractorTransformer.transformContractor(priceImport.getContractor()));
        priceImportTO.setCurrency(CurrencyTransformer.transformCurrency(priceImport.getCurrency()));
        priceImportTO.setMeasureUnit(MeasureUnitTransformer.transform(priceImport.getMeasureUnit()));
        priceImportTO.setConversionCurrency(CurrencyTransformer.transformCurrency(priceImport.getConversionCurrency()));
        priceImportTO.setConversionExchangeRate(priceImport.getConversionExchangeRate());
    }

    public static ContractorPriceImport transform(ContractorPriceImportTO priceImportTO) {
        if (priceImportTO == null){
            return null;
        }
        ContractorPriceImport priceImport = SpringServiceContext.getInstance().getContractorPriceImportService().getById(priceImportTO.getId());
        if (priceImport == null){
            priceImport = new ContractorPriceImport();
            update(priceImport, priceImportTO);
        }
        return priceImport;
    }

    public static void update(ContractorPriceImport priceImport, ContractorPriceImportTO priceImportTO) {
        //Common data import transformation.
        DataImportTransformer.update(priceImport, priceImportTO);
        //Contractor price specific transformation.
        priceImport.setItemCount(priceImportTO.getItemCount());
        priceImport.setIgnoredItemCount(priceImportTO.getIgnoredItemCount());
        priceImport.setContractor(ContractorTransformer.transformContractor(priceImportTO.getContractor()));
        priceImport.setCurrency(CurrencyTransformer.transformCurrency(priceImportTO.getCurrency()));
        priceImport.setMeasureUnit(MeasureUnitTransformer.transform(priceImportTO.getMeasureUnit()));
        priceImport.setConversionCurrency(CurrencyTransformer.transformCurrency(priceImportTO.getConversionCurrency()));
        priceImport.setConversionExchangeRate(priceImportTO.getConversionExchangeRate());
    }
}
