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
import com.artigile.warehouse.gui.core.report.controller.ColumnInfo;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSourceBase;
import com.artigile.warehouse.gui.core.report.controller.ReportEditingStrategy;
import com.artigile.warehouse.gui.core.report.controller.ReportInfo;
import com.artigile.warehouse.gui.core.report.format.predefined.NumericColumnFormatFactory;
import com.artigile.warehouse.gui.core.report.style.predefined.RightMiddleAlignStyleFactory;
import com.artigile.warehouse.gui.menuitems.priceimport.contractorProduct.styles.ContractorProductPostingDateStyleFactory;
import com.artigile.warehouse.gui.menuitems.priceimport.contractorProduct.styles.ContractorProductPriceStyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.custom.types.VariantPrice;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.priceimport.ContractorProductTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.math.MathUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class BaseContractorProductList extends ReportDataSourceBase {

    protected int contractorColumnIndex = -1;

    /**
     * Search filter
     */
    protected ContractorProductFilter filter;

    /**
     * Exchange rates between different currencies
     */
    private Map<Long, Map<Long, Double>> exchangeRateMap;

    /**
     * Extra charge on wholesale and retail prices
     */
    protected int extraCharge;

    /**
     * Currency of the price list
     */
    protected CurrencyTO conversionCurrency;

    /**
     * True - load data to the report table from database
     * False - use local copy of data for report table
     */
    protected boolean loadData = true;


    /* Constructor
    ------------------------------------------------------------------------------------------------------------------*/
    protected BaseContractorProductList(ContractorProductFilter filter) {
        this.filter = filter;
        this.exchangeRateMap = SpringServiceContext.getInstance().getCurencyExchangeService().getExchangeRateMap();
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    protected ReportInfo doGetReportInfo() {
        ReportInfo reportInfo = new ReportInfo(ContractorProductTOForReport.class);
        ContractorProductPriceStyleFactory contractorProductPriceStyleFactory = new ContractorProductPriceStyleFactory();
        ContractorProductPostingDateStyleFactory contractorProductPostingDateStyleFactory = new ContractorProductPostingDateStyleFactory();

        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.name"), "name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.description"), "description"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.year"), "year"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.quantity"), "quantity", RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.discount"), "priceImport.contractor.discount"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.wholesale.price"), "wholesalePrice", contractorProductPriceStyleFactory, NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.wholesale.price.discount"), "wholesaleDiscountPrice", contractorProductPriceStyleFactory, NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.wholesale.price.extraCharge"), "wholesalePriceWithExtraCharge", contractorProductPriceStyleFactory, NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.retail.price"), "retailPrice", contractorProductPriceStyleFactory, NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.retail.price.discount"), "retailDiscountPrice", contractorProductPriceStyleFactory, NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.retail.price.extraCharge"), "retailPriceWithExtraCharge", contractorProductPriceStyleFactory, NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.pack"), "pack", RightMiddleAlignStyleFactory.getInstance(), NumericColumnFormatFactory.getInstance()));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.product.type"), "productType"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.currency"), "currency.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.measure.unit"), "measureUnit.sign"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.price.import.contractor.name"), "priceImport.contractor.name"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.price.import.contractor.rating"), "priceImport.contractor.rating"));
        reportInfo.addColumn(new ColumnInfo(I18nSupport.message("contractor.product.list.posting.date"), "postingDate", contractorProductPostingDateStyleFactory));

        //Important! We remember index of contractor column to use custom default command for this column.
        contractorColumnIndex = reportInfo.getColumns().size() - 1;
        return reportInfo;
    }

    @Override
    public String getReportTitle() {
        return I18nSupport.message("contractor.product.list.title");
    }

    @Override
    public abstract ReportEditingStrategy getReportEditingStrategy();

    @Override
    public abstract List getReportData();

    protected void applyConversionCurrency(List<ContractorProductTOForReport> contractorProductList) {
        for (ContractorProductTOForReport contractorProductTO : contractorProductList) {
            if (contractorProductTO.getOriginalCurrency() != null) {
                // Get currency of conversion and exchange rate between original currency and the currency of conversion
                CurrencyTO positionConversionCurrency = (conversionCurrency != null)
                        ? conversionCurrency
                        : contractorProductTO.getPriceImport().getConversionCurrency();
                double exchangeRate = (conversionCurrency != null)
                        ? getExchangeRate(contractorProductTO.getOriginalCurrency(), positionConversionCurrency)
                        : contractorProductTO.getPriceImport().getConversionExchangeRate();
                Double wholesalePrice = contractorProductTO.getOriginalWholesalePrice().getPrice();
                if (wholesalePrice != null) {
                    double convertedWholesalePrice = wholesalePrice * exchangeRate;
                    contractorProductTO.getWholesalePrice().setPrice(convertedWholesalePrice);
                }
                Double retailPrice = contractorProductTO.getOriginalRetailPrice().getPrice();
                if (retailPrice != null) {
                    double convertedRetailPrice = retailPrice * exchangeRate;
                    contractorProductTO.getRetailPrice().setPrice(convertedRetailPrice);
                }
                contractorProductTO.setCurrency(positionConversionCurrency);
            }
        }
    }

    private double getExchangeRate(CurrencyTO fromCurrency, CurrencyTO toCurrency) {
        long fromID = fromCurrency.getId();
        long toID = toCurrency.getId();
        if (exchangeRateMap.containsKey(fromID) && exchangeRateMap.get(fromID).containsKey(toID)) {
            return exchangeRateMap.get(fromID).get(toID);
        } else {
            return 1.0;
        }
    }

    protected void calculateAdditionalPrices(List<ContractorProductTOForReport> contractorProductList) {
        for (ContractorProductTOForReport contractorProductTO : contractorProductList) {
            int contractorDiscount = contractorProductTO.getPriceImport().getContractor().getDiscount();
            // Wholesale price ...
            Double wholesalePrice = contractorProductTO.getWholesalePrice().getPrice();
            if (wholesalePrice != null) {
                // ... with extra charge
                double wholesalePriceWithExtraCharge = MathUtils.calculateSimpleExtraChargePrice(wholesalePrice, extraCharge);
                if (contractorProductTO.getWholesalePriceWithExtraCharge() != null) {
                    contractorProductTO.getWholesalePriceWithExtraCharge().setPrice(wholesalePriceWithExtraCharge);
                } else {
                    contractorProductTO.setWholesalePriceWithExtraCharge(new VariantPrice(wholesalePriceWithExtraCharge));
                }
                // ... with discount
                double wholesalePriceWithDiscount = MathUtils.calculateSimpleDiscountPrice(wholesalePrice, contractorDiscount);
                if (contractorProductTO.getWholesaleDiscountPrice() != null) {
                    contractorProductTO.getWholesaleDiscountPrice().setPrice(wholesalePriceWithDiscount);
                } else {
                    contractorProductTO.setWholesaleDiscountPrice(new VariantPrice(wholesalePriceWithDiscount));
                }
            }
            // Retail price ...
            Double retailPrice = contractorProductTO.getRetailPrice().getPrice();
            if (retailPrice != null) {
                // ... with extra charge
                double retailPriceWithExtraCharge = MathUtils.calculateSimpleExtraChargePrice(retailPrice, extraCharge);
                if (contractorProductTO.getRetailPriceWithExtraCharge() != null) {
                    contractorProductTO.getRetailPriceWithExtraCharge().setPrice(retailPriceWithExtraCharge);
                } else {
                    contractorProductTO.setRetailPriceWithExtraCharge(new VariantPrice(retailPriceWithExtraCharge));
                }
                // ... with discount
                double retailPriceWithDiscount = MathUtils.calculateSimpleDiscountPrice(retailPrice, contractorDiscount);
                if (contractorProductTO.getRetailDiscountPrice() != null) {
                    contractorProductTO.getRetailDiscountPrice().setPrice(retailPriceWithDiscount);
                } else {
                    contractorProductTO.setRetailDiscountPrice(new VariantPrice(retailPriceWithDiscount));
                }
            }
        }
    }


    /* Setters
    ------------------------------------------------------------------------------------------------------------------*/
    public void setExtraCharge(int extraCharge) {
        this.extraCharge = extraCharge;
    }

    public void setConversionCurrency(CurrencyTO conversionCurrency) {
        this.conversionCurrency = conversionCurrency;
    }

    public void setLoadData(boolean loadData) {
        this.loadData = loadData;
    }
}
