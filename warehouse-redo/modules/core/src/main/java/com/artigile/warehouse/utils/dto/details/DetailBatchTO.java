/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.details;

import com.artigile.warehouse.gui.utils.BarCodeImage;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.ManufacturerTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.parser.ParsedTemplate;
import com.artigile.warehouse.utils.parser.ParsedTemplateDataSource;

import java.math.BigDecimal;

/**
 * @author Shyrik, 26.12.2008
 */

public class DetailBatchTO extends EqualsByIdImpl implements DetailFieldOwner, ParsedTemplateDataSource {

    private long id;

    private String uidGoods;

    private DetailModelTO model;

    /**
     * Name is a calculated field of detait batch.
     */
    private DetailFieldValueTO name;

    /**
     * Misc is a calculated field of detait batch.
     */
    private DetailFieldValueTO misc;

    /**
     * Misc is a calculated field of detait batch.
     */
    private DetailFieldValueTO type;

    private String acceptance;

    private Integer year;

    private String barCode;

    private BarCodeImage barCodeImage;

    private String nomenclatureArticle;

    private ManufacturerTO manufacturer;

    private BigDecimal buyPrice;

    private BigDecimal sellPrice;

    private CurrencyTO currency;

    private BigDecimal sellPrice2;

    private CurrencyTO currency2;

    private BigDecimal sellPrice3;

    private CurrencyTO currency3;

    private BigDecimal sellPrice4;

    private CurrencyTO currency4;

    private BigDecimal sellPrice5;

    private CurrencyTO currency5;

    private long count;

    private long reservedCount;

    private MeasureUnitTO countMeas;

    private String notice;

    private String sortNum;

    private boolean needRecalculate;

    //================================== Operations =============================================
    public void copyFrom(DetailBatchTO src) {
        id = src.id;
        model = src.model;
        name = src.name;
        misc = src.misc;
        type = src.type;
        acceptance = src.acceptance;
        year = src.year;
        barCode = src.barCode;
        nomenclatureArticle = src.nomenclatureArticle;
        manufacturer = src.manufacturer;
        buyPrice = src.buyPrice;
        sellPrice = src.sellPrice;
        currency = src.currency;
        sellPrice2 = src.sellPrice2;
        currency2 = src.currency2;
        sellPrice3 = src.sellPrice3;
        currency3 = src.currency3;
        sellPrice4 = src.sellPrice4;
        currency4 = src.currency4;
        sellPrice5 = src.sellPrice5;
        currency5 = src.currency5;
        countMeas = src.countMeas;
        notice = src.notice;
        sortNum = src.sortNum;
        needRecalculate = src.needRecalculate;
        onChanged();
    }

    //============================= Calculated getters ==========================================
    public long getAvailCount(){
        return getCount() - getReservedCount();
    }

    //============================= Getters and setters =========================================

    public boolean isNew() {
        return id == 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUidGoods() {
        return uidGoods;
    }

    public void setUidGoods(String uidGoods) {
        this.uidGoods = uidGoods;
    }

    public DetailModelTO getModel() {
        return model;
    }

    public void setModel(DetailModelTO model) {
        this.model = model;
        onModelChanged();
    }

    public String getName() {
        return name == null ? "" : name.getValue();
    }

    public String getType() {
        return type == null ? "" : type.getValue();
    }

    public String getMisc() {
        return misc == null ? "" : misc.getValue();
    }

    public String getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(String acceptance) {
        this.acceptance = acceptance;
        onChanged();
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
        onChanged();
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
        this.barCodeImage = null;
    }

    public Object getBarCodeImage(){
        if (barCodeImage == null){
            barCodeImage = new BarCodeImage(barCode);
        }
        return barCodeImage.getImageData();
    }

    public String getNomenclatureArticle() {
        return nomenclatureArticle;
    }

    public void setNomenclatureArticle(String nomenclatureArticle) {
        this.nomenclatureArticle = nomenclatureArticle;
    }

    public ManufacturerTO getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufacturerTO manufacturer) {
        this.manufacturer = manufacturer;
        onChanged();
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
        onChanged();
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
        onChanged();
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
        onChanged();
    }

    public BigDecimal getSellPrice2() {
        return sellPrice2;
    }

    public void setSellPrice2(BigDecimal sellPrice2) {
        this.sellPrice2 = sellPrice2;
        onChanged();
    }

    public CurrencyTO getCurrency2() {
        return currency2;
    }

    public void setCurrency2(CurrencyTO currency2) {
        this.currency2 = currency2;
        onChanged();
    }

    public BigDecimal getSellPrice3() {
        return sellPrice3;
    }

    public void setSellPrice3(BigDecimal sellPrice3) {
        this.sellPrice3 = sellPrice3;
        onChanged();
    }

    public CurrencyTO getCurrency3() {
        return currency3;
    }

    public void setCurrency3(CurrencyTO currency3) {
        this.currency3 = currency3;
        onChanged();
    }

    public BigDecimal getSellPrice4() {
        return sellPrice4;
    }

    public void setSellPrice4(BigDecimal sellPrice4) {
        this.sellPrice4 = sellPrice4;
        onChanged();
    }

    public CurrencyTO getCurrency4() {
        return currency4;
    }

    public void setCurrency4(CurrencyTO currency4) {
        this.currency4 = currency4;
        onChanged();
    }

    public BigDecimal getSellPrice5() {
        return sellPrice5;
    }

    public void setSellPrice5(BigDecimal sellPrice5) {
        this.sellPrice5 = sellPrice5;
        onChanged();
    }

    public CurrencyTO getCurrency5() {
        return currency5;
    }

    public void setCurrency5(CurrencyTO currency5) {
        this.currency5 = currency5;
        onChanged();
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
        onChanged();
    }

    public long getReservedCount() {
        return reservedCount;
    }

    public void setReservedCount(long reservedCount) {
        this.reservedCount = reservedCount;
    }

    public MeasureUnitTO getCountMeas() {
        return countMeas;
    }

    public void setCountMeas(MeasureUnitTO countMeas) {
        this.countMeas = countMeas;
        onChanged();
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
        onChanged();
    }

    //=================================== Helpers ====================================
    private void onChanged() {
        //Process change notification to update value of the name field.
        if (name != null){
            name.onNeedRecalculate();
            misc.onNeedRecalculate();
            type.onNeedRecalculate();
        }
    }

    private void onModelChanged() {
        //Processing changing of the detail model.
        if (model == null){
            name = null;
            misc = null;
            type= null;
        }
        else{
            name = new DetailFieldValueTO(model.getType().getDetailBatchNameField(), this);
            misc = new DetailFieldValueTO(model.getType().getDetailBatchMiscField(), this);
            type = new DetailFieldValueTO(model.getType().getDetailBatchTypeField(), this);
        }
    }

    //============================== DetailFieldOwner implementation ======================
    @Override
    public void onValueChanged(DetailFieldValueTO sender) {
        model.onValueChanged(sender);
    }

    @Override
    public boolean isUniqueFieldValue(DetailFieldValueTO field, String value) {
        return model.isUniqueFieldValue(field, value);
    }

    @Override
    public String calculateTemplate(String template) {
        //Calculating of the detail batch name implemented as with the help of decoration model's calculate mechanism.
        ParsedTemplate parsedTemplate = model.getType().parseTemplate(template, this);
        return parsedTemplate.calculate(this);
    }

    //================================= ParsedTemplateDataSource implementation ========================
    private static final int acceptanceIndex = 1001;
    private static final String acceptanceFieldName = I18nSupport.message("detail.batches.list.acceptance");

    private static final int yearIndex = 1002;
    private static final String yearFieldName = I18nSupport.message("detail.batches.list.year");

    private static final int manufacturerIndex = 1003;
    private static final String manufacturerFieldName = I18nSupport.message("detail.batches.list.manufacturer");

    private static final int buyPriceIndex = 1004;
    private static final String buyPriceFieldName = I18nSupport.message("detail.batches.list.buyPrice");

    private static final int sellPriceIndex = 1005;
    private static final String sellPriceFieldName = I18nSupport.message("detail.batches.list.sellPrice");

    private static final int currencyIndex = 1006;
    private static final String currencyFieldName = I18nSupport.message("detail.batches.list.currency");

    private static final int countIndex = 1007;
    private static final String reservedCountFieldName = I18nSupport.message("detail.batches.list.availCount");

    private static final int reservedCountIndex = 1008;
    private static final String countFieldName = I18nSupport.message("detail.batches.list.reservedCount");

    private static final int countMeasIndex = 1009;
    private static final String countMeasFieldName = I18nSupport.message("detail.batches.list.countMeas");

    private static final int noticeIndex = 1010;
    private static final String noticeFieldName = I18nSupport.message("detail.batches.list.notice");

    private static final int sellPrice2Index = 1011;
    private static final String sellPrice2FieldName = I18nSupport.message("detail.batches.list.sellPrice2");

    private static final int currency2Index = 1012;
    private static final String currency2FieldName = I18nSupport.message("detail.batches.list.currency2");

    private static final int sellPrice3Index = 1013;
    private static final String sellPrice3FieldName = I18nSupport.message("detail.batches.list.sellPrice3");

    private static final int currency3Index = 1014;
    private static final String currency3FieldName = I18nSupport.message("detail.batches.list.currency3");

    private static final int sellPrice4Index = 1015;
    private static final String sellPrice4FieldName = I18nSupport.message("detail.batches.list.sellPrice4");

    private static final int currency4Index = 1016;
    private static final String currency4FieldName = I18nSupport.message("detail.batches.list.currency4");

    private static final int sellPrice5Index = 1017;
    private static final String sellPrice5FieldName = I18nSupport.message("detail.batches.list.sellPrice5");

    private static final int currency5Index = 1018;
    private static final String currency5FieldName = I18nSupport.message("detail.batches.list.currency5");

    private static final int barCodeIndex = 1019;
    private static final String barCodeFieldName = I18nSupport.message("detail.batches.list.barCode");

    private static final int nomenclatureArticleIndex = 1020;
    private static final String nomenclatureArticleFieldName = I18nSupport.message("detail.batches.list.nomenclatureArticle");

    @Override
    public int getFieldIndexByName(String fieldName) {
        if (fieldName.equals(acceptanceFieldName)) return acceptanceIndex;
        else if (fieldName.equals(yearFieldName)) return yearIndex;
        else if (fieldName.equals(manufacturerFieldName)) return manufacturerIndex;
        else if (fieldName.equals(buyPriceFieldName)) return buyPriceIndex;
        else if (fieldName.equals(sellPriceFieldName)) return sellPriceIndex;
        else if (fieldName.equals(currencyFieldName)) return currencyIndex;
        else if (fieldName.equals(countFieldName)) return countIndex;
        else if (fieldName.equals(reservedCountFieldName)) return reservedCountIndex;
        else if (fieldName.equals(countMeasFieldName)) return countMeasIndex;
        else if (fieldName.equals(noticeFieldName)) return noticeIndex;
        else if (fieldName.equals(sellPrice2FieldName)) return sellPrice2Index;
        else if (fieldName.equals(currency2FieldName)) return currency2Index;
        else if (fieldName.equals(sellPrice3FieldName)) return sellPrice3Index;
        else if (fieldName.equals(currency3FieldName)) return currency3Index;
        else if (fieldName.equals(sellPrice4FieldName)) return sellPrice4Index;
        else if (fieldName.equals(currency4FieldName)) return currency4Index;
        else if (fieldName.equals(sellPrice5FieldName)) return sellPrice5Index;
        else if (fieldName.equals(currency5FieldName)) return currency5Index;
        else if (fieldName.equals(barCodeFieldName)) return barCodeIndex;
        else if (fieldName.equals(nomenclatureArticleFieldName)) return nomenclatureArticleIndex;
        return model.getFieldIndexByName(fieldName);
    }

    @Override
    public String getFieldValue(int index) {
        switch (index){
            case acceptanceIndex: return getAcceptanceWithQuotas();
            case yearIndex: return getYear() == null ? "" : getYear().toString();
            case manufacturerIndex: return getManufacturer() == null ? "" : getManufacturer().getName();
            case buyPriceIndex: return getBuyPrice() == null ? "" : StringUtils.formatNumber(getBuyPrice());
            case sellPriceIndex: return getSellPrice() == null ? "" : StringUtils.formatNumber(getSellPrice());
            case currencyIndex: return getCurrency() == null ? "" : getCurrency().getSign();
            case countIndex: return String.valueOf(getCount());
            case reservedCountIndex: return String.valueOf(getReservedCount());
            case countMeasIndex: return getCountMeas() == null ? "" : getCountMeas().getSign();
            case noticeIndex: return getNotice();
            case sellPrice2Index: return getSellPrice2() == null ? "" : StringUtils.formatNumber(getSellPrice2());
            case currency2Index: return getCurrency2() == null ? "" : getCurrency2().getSign();
            case sellPrice3Index: return getSellPrice3() == null ? "" : StringUtils.formatNumber(getSellPrice3());
            case currency3Index: return getCurrency3() == null ? "" : getCurrency3().getSign();
            case sellPrice4Index: return getSellPrice4() == null ? "" : StringUtils.formatNumber(getSellPrice4());
            case currency4Index: return getCurrency4() == null ? "" : getCurrency4().getSign();
            case sellPrice5Index: return getSellPrice5() == null ? "" : StringUtils.formatNumber(getSellPrice5());
            case currency5Index: return getCurrency5() == null ? "" : getCurrency5().getSign();
            case barCodeIndex: return getBarCode() == null ? "" : getBarCode();
            case nomenclatureArticleIndex: return getNomenclatureArticle() == null ? "" : getNomenclatureArticle();
            default:
                return model.getFieldValue(index);
        }
    }

    private String getAcceptanceWithQuotas() {
        if (acceptance == null || acceptance.isEmpty()){
            return "";
        }
        else{
            return "\"" + acceptance + "\"";
        }
    }

    public String getSortNum() {
        return sortNum;
    }

    public void setSortNum(String sortNum) {
        this.sortNum = sortNum;
    }

    public boolean getNeedRecalculate() {
        return needRecalculate;
    }

    public void setNeedRecalculate(boolean needRecalculate) {
        this.needRecalculate = needRecalculate;
    }
}
