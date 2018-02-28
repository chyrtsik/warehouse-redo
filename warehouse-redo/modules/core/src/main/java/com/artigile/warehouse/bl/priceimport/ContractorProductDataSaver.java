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

import com.artigile.warehouse.adapter.spi.DataSaverAdapter;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.custom.types.VariantPrice;
import com.artigile.warehouse.utils.custom.types.VariantQuantity;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;
import com.artigile.warehouse.utils.dto.priceimport.ContractorProductTOForReport;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.artigile.warehouse.bl.priceimport.ContractorProductDomainColumnType.*;

/**
 * Component for saving of importer contractor product data.
 *
 * @author Aliaksandr.Chyrtsik, 14.07.11
 */
public class ContractorProductDataSaver extends DataSaverAdapter {

    private ContractorPriceImportTO priceImport;

    private int importedProductsCount;
    private int ignoredProductsCount;

    private ContractorProductService contractorProductService = SpringServiceContext.getInstance().getContractorProductService();

    private PriceImportParsingUtils priceImportParsingUtils = new PriceImportParsingUtils();

    private Map<String, String> sourceDataRowMap;


    /* Constructors
    ------------------------------------------------------------------------------------------------------------------*/
    public ContractorProductDataSaver(ContractorPriceImportTO priceImport) {
        this.priceImport = priceImport;
        this.sourceDataRowMap = new LinkedHashMap<String, String>();
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void setSourceDataRow(Map<String, String> sourceDataRow) {
        if (!sourceDataRowMap.isEmpty()) {
            sourceDataRowMap.clear();
        }
        sourceDataRowMap.putAll(sourceDataRow);
    }

    @Override
    public void saveDataRow(Map<String, String> dataRow) {
        ContractorProductTOForReport product = new ContractorProductTOForReport();

        product.setCurrency(priceImport.getCurrency());
        product.setPriceImport(priceImport);
        product.setMeasureUnit(priceImport.getMeasureUnit());

        product.setName(dataRow.get(NAME.getDomainColumn().getId()));
        product.setDescription(dataRow.get(DESCRIPTION.getDomainColumn().getId()));
        product.setPostingDate(priceImport.getImportDate());
        product.setPack(parseQuantityValue(dataRow.get(PACK.getDomainColumn().getId())));
        product.setProductType(dataRow.get(PRODUCT_TYPE.getDomainColumn().getId()));
        product.setQuantity(parseQuantityValue(dataRow.get(QUANTITY.getDomainColumn().getId())));
        product.setRetailPrice(parsePriceValue(dataRow.get(RETAIL_PRICE.getDomainColumn().getId())));
        product.setWholesalePrice(parsePriceValue(dataRow.get(WHOLESALE_PRICE.getDomainColumn().getId())));
        product.setYear(dataRow.get(YEAR.getDomainColumn().getId()));
        product.setSourceData(ContractorProductSourceDataParser.format(sourceDataRowMap));

        if (validForSaving(product)) {
            // Save imported product
            try {
                contractorProductService.saveContractorProductWithFlush(product);
                importedProductsCount++;
            }
            // Catches all exceptions of the product creation
            catch (Exception e) {
                LoggingFacade.logError(StringUtils.buildString(this.getClass().getSimpleName(),
                        ".saveDataRow - Failed save product. Name: ", product.getName(),
                        " Quantity: ", product.getQuantity()), e);
                ignoredProductsCount++;
            }
        } else {
            ignoredProductsCount++;
        }
    }

    private boolean validForSaving(ContractorProductTOForReport product) {
        return StringUtils.containsSymbols(product.getName()) && product.getQuantity() != null;
    }

    private VariantQuantity parseQuantityValue(String value) {
        if (value != null) {
            return new VariantQuantity(priceImportParsingUtils.removeMeasureUnit(value));
        }
        return null;
    }

    private VariantPrice parsePriceValue(String value) {
        if (value != null) {
            return new VariantPrice(priceImportParsingUtils.removeCurrency(value));
        }
        return null;
    }


    /* Getters
    ------------------------------------------------------------------------------------------------------------------*/
    public int getImportedProductsCount() {
        return importedProductsCount;
    }

    public int getIgnoredProductsCount() {
        return ignoredProductsCount;
    }
}
