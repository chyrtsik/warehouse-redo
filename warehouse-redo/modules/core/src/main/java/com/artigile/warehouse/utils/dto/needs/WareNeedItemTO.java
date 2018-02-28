/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.needs;

import com.artigile.warehouse.domain.needs.WareNeedItemState;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;

/**
 * @author Shyrik, 28.02.2009
 */
public class WareNeedItemTO extends EqualsByIdImpl {
    private long id;

    private WareNeedItemState state;

    private WareNeedTO wareNeed;

    private Long number;

    private Long subNumber;

    private Date createDateTime;

    private UserTO createdUser;

    private DetailBatchTO detailBatch;

    private String name;

    private String misc;

    private Long count;

    private Long minYear;

    private BigDecimal maxPrice;

    private CurrencyTO currency;

    private String notice;

    private ContractorTO customer;

    private ContractorTO supplier;

    private BigDecimal buyPrice;

    private CurrencyTO buyCurrency;

    private boolean autoCreated;

    //======================================= Constructors ==============================================
    public WareNeedItemTO(WareNeedTO wareNeed, DetailBatchTO detailBatch, boolean init) {
        this.wareNeed = wareNeed;
        this.detailBatch = detailBatch;
        if (init){
            init();
        }
    }

    public void init() {
        setState(WareNeedItemState.getInitialState());
        setCreateDateTime(new Date());
        setCreatedUser(WareHouse.getUserSession().getUser());
    }

    public WareNeedItemTO(WareNeedTO wareNeed, boolean init){
        this(wareNeed, null, init);
    }

    //====================================== Calculated getters =========================================
    public boolean canBeDeleted() {
        return state == WareNeedItemState.NOT_FOUND;
    }

    public boolean isEditableState() {
        switch (state) {
            case NOT_FOUND:
                return true;
            case FOUND:
            case ORDERED:
            case SHIPPED:
            case CLOSED:
                return false;
        }
        return false;
    }

    public boolean isText(){
        return detailBatch == null;
    }

    public String getCreateDate(){
        return createDateTime == null ? null : StringUtils.getDateFormat().format(createDateTime);
    }

    public String getCreateTime(){
        return createDateTime == null ? null : StringUtils.getTimeFormat().format(createDateTime);
    }

    /**
     * Full number of need item in form "number.subnumber".
     */
    private BigDecimal fullNumber;

    public BigDecimal getFullNumber(){
        if (fullNumber != null){
            return fullNumber;
        }
        if (number != null){
            fullNumber = subNumber == null ? new BigDecimal(number) : StringUtils.parseStringToBigDecimal(MessageFormat.format("{0}.{1}", number, subNumber));
        }
        return fullNumber;
    }

    public String getNeedType() {
        return isText() ? "" : detailBatch.getType();
    }

    public String getNeedName(){
        return isText() ? getName() : detailBatch.getName();
    }

    public String getNeedMisc(){
        return isText() ? getMisc() : detailBatch.getMisc();
    }

    public MeasureUnitTO getCountMeas() {
        return isText() ? null : detailBatch.getCountMeas();
    }

    //==================================== Getters and setters ==========================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WareNeedItemState getState() {
        return state;
    }

    public void setState(WareNeedItemState state) {
        this.state = state;
    }

    public WareNeedTO getWareNeed() {
        return wareNeed;
    }

    public void setWareNeed(WareNeedTO wareNeed) {
        this.wareNeed = wareNeed;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
        this.fullNumber = null;
    }

    public Long getSubNumber() {
        return subNumber;
    }

    public void setSubNumber(Long subNumber) {
        this.subNumber = subNumber;
        this.fullNumber = null;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public UserTO getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(UserTO createdUser) {
        this.createdUser = createdUser;
    }

    public DetailBatchTO getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatchTO detailBatch) {
        this.detailBatch = detailBatch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMisc() {
        return misc;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getMinYear() {
        return minYear;
    }

    public void setMinYear(Long minYear) {
        this.minYear = minYear;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
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

    public ContractorTO getCustomer() {
        return customer;
    }

    public void setCustomer(ContractorTO customer) {
        this.customer = customer;
    }

    public ContractorTO getSupplier() {
        return supplier;
    }

    public void setSupplier(ContractorTO supplier) {
        this.supplier = supplier;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public CurrencyTO getBuyCurrency() {
        return buyCurrency;
    }

    public void setBuyCurrency(CurrencyTO buyCurrency) {
        this.buyCurrency = buyCurrency;
    }

    public boolean getAutoCreated() {
        return autoCreated;
    }

    public void setAutoCreated(boolean autoCreated) {
        this.autoCreated = autoCreated;
    }
}
