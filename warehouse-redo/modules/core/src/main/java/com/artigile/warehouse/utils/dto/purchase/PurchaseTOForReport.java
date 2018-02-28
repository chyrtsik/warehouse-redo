/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.purchase;

import com.artigile.warehouse.domain.purchase.PurchaseState;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.LoadPlaceTO;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Shyrik, 01.03.2009
 */
public class PurchaseTOForReport extends EqualsByIdImpl {
    private long id;

    private Long number;

    private PurchaseState state;

    private Date createDate;

    private UserTO createdUser;

    private ContractorTO contractor;

    private LoadPlaceTO loadPlace;

    private CurrencyTO currency;

    private BigDecimal totalPrice;

    private String notice;

    //===================================== Constructors ================================================
    public PurchaseTOForReport(boolean init) {
        if (init){
            state = PurchaseState.getInitialState();
            createDate = new Date();
            createdUser = WareHouse.getUserSession().getUser();
            number = SpringServiceContext.getInstance().getPurchaseService().getNextAvailablePurchaseNumber();
        }
    }

    //======================================= Calculated getters ========================================
    public String getCreateDateAsText(){
        return StringUtils.getDateFormat().format(getCreateDate());
    }

    public boolean canBeDeleted() {
        return canBeEdited();
    }

    public boolean canBeEdited() {
        return state == PurchaseState.CONSTRUCTION; 
    }

    //======================================= Getters and setters =======================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public PurchaseState getState() {
        return state;
    }

    public void setState(PurchaseState state) {
        this.state = state;
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

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
