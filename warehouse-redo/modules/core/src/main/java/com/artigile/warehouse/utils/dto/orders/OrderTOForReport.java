/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.orders;

import com.artigile.warehouse.domain.orders.OrderReservingType;
import com.artigile.warehouse.domain.orders.OrderState;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.LoadPlaceTO;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.money.MoneyUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Shyrik, 06.01.2009
 */

/**
 * Lightweight representation of the order (used to diaplay order in the large lists).
 */
public class OrderTOForReport extends EqualsByIdImpl {
    //Order fields.
    private long id;
    private OrderState state;
    private long number;
    private Date createDate;
    private UserTO createdUser;
    private BigDecimal totalPrice;
    private ContractorTO contractor;
    private LoadPlaceTO loadPlace;
    private Date loadDate;
    private CurrencyTO currency;
    private String notice;
    private OrderReservingType reservingType;
    private OrderProcessingInfoTO processingInfo;
    private BigDecimal vatRate;

    //Calculated fields (just for displaying).
    private BigDecimal vat;
    private BigDecimal totalPriceWithVat;
    private static final String NULL_VAT_VALUE = I18nSupport.message("order.vat.null.text");

    public OrderTOForReport(){
    }

    public OrderTOForReport(boolean init){
        if (init){
            init();
        }
    }

    //==================== Calculated fields =====================================

    public String getTotalPriceWords(){
        return getTotalPrice() != null ? MoneyUtils.getMoneyAmountInWords(getTotalPrice(), getCurrency()) : null;
    }

    public String getVatWords(){
        return getVat() != null ? MoneyUtils.getMoneyAmountInWords(getVat(), getCurrency()) : null;
    }

    public String getTotalPriceWithVatWords(){
        return getTotalPriceWithVat() != null ? MoneyUtils.getMoneyAmountInWords(getTotalPriceWithVat(), getCurrency()) : null;
    }

    public Object getDisplayedVatRate(){
        if (vatRate != null){
            return vatRate;
        }
        else {
            return NULL_VAT_VALUE;
        }
    }

    public String getVatRateAsText() {
        return vatRate == null ? NULL_VAT_VALUE : StringUtils.formatNumber(vatRate);
    }

    public BigDecimal getVat(){
        return vat;
    }

    public BigDecimal getTotalPriceWithVat(){
        return totalPriceWithVat;
    }

    //====================== Operations ==========================================
    public void copyFrom(OrderTOForReport src) {
        setId(src.getId());
        setState(src.getState());
        setNumber(src.getNumber());
        setCreateDate(src.getCreateDate());
        setCreatedUser(src.getCreatedUser());
        setTotalPrice(src.getTotalPrice());
        setVatRate(src.getVatRate());
        setContractor(src.getContractor());
        setLoadPlace(src.getLoadPlace());
        setLoadDate(src.getLoadDate());
        setCurrency(src.getCurrency());
        setNotice(src.getNotice());
        setReservingType(src.getReservingType());
    }

    public void init(){
        setState(OrderState.CONSTRUCTION);
        setCreateDate(new Date());
        setCreatedUser(WareHouse.getUserSession().getUser());
        setNumber(SpringServiceContext.getInstance().getOrdersService().getNextAvailableOrderNumber());
        setReservingType(OrderReservingType.IMMEDIATELY);
    }

    public void initAndCopyFrom(OrderTOForReport src){
        init();
        setVatRate(src.getVatRate());
        setContractor(src.getContractor());
        setLoadPlace(src.getLoadPlace());
        setLoadDate(src.getLoadDate());
        setCurrency(src.getCurrency());
        setNotice(src.getNotice());
        setReservingType(src.getReservingType());
    }

    public boolean equals(Object obj){
        if (obj instanceof OrderTOForReport){
            OrderTOForReport orderObj = (OrderTOForReport)obj;
            return getId() == orderObj.getId();
        }
        return super.equals(obj);
    }

    public boolean isNew() {
        return id == 0;
    }

    public boolean isEditableState() {
        switch (state) {
            case CONSTRUCTION:
            case READY_FOR_COLLECTION:
            case COLLECTION:
            case COLLECTION_PROBLEM:
            case COLLECTED:
            case READY_FOR_SHIPPING:
                return true;
            case SHIPPED:
            case SOLD:
                return false;        
        }
        
        return false;
    }

    /**
     * Recalculates value added tax (VAT).
     */
    private void refreshVatValues() {
        if (totalPrice == null){
            vat = null;
            totalPriceWithVat = null;
        }
        else if (vatRate != null){
            vat = totalPrice.multiply(vatRate).divide(BigDecimal.valueOf(100));
            totalPriceWithVat = totalPrice.add(vat);
        }
    }

    //==================== Getters and setters ===================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getCreateDateAsText(){
        return StringUtils.getDateFormat().format(getCreateDate());
    }

    public Date getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public UserTO getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(UserTO createdUser) {
        this.createdUser = createdUser;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        refreshVatValues();
    }

    public ContractorTO getContractor() {
        return contractor;
    }

    public void setContractor(ContractorTO contractor) {
        this.contractor = contractor;
    }

    public LoadPlaceTO getLoadPlace() {
        return loadPlace;
    }

    public void setLoadPlace(LoadPlaceTO loadPlace) {
        this.loadPlace = loadPlace;
    }

    public Date getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(Date loadDate) {
        this.loadDate = loadDate;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }
    
    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public OrderReservingType getReservingType() {
        return reservingType;
    }

    public void setReservingType(OrderReservingType reservingType) {
        this.reservingType = reservingType;
    }

    public OrderProcessingInfoTO getProcessingInfo() {
        return processingInfo;
    }

    public void setProcessingInfo(OrderProcessingInfoTO processingInfo) {
        this.processingInfo = processingInfo;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
        refreshVatValues();
    }
}
