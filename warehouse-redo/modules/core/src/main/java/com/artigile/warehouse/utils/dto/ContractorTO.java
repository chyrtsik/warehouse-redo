/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto;

import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

import java.util.Date;

/**
 * @author Ihar, Dec 10, 2008
 */

public class ContractorTO extends EqualsByIdImpl {

    private long id;

    private String uidContractor;

    private String name;

    private String fullName;

    private Integer rating;

    private String legalAddress;

    private String postalAddress;

    private String phone;

    private LoadPlaceTO defaultShippingAddress;

    private String country;

    private CurrencyTO defaultCurrency;

    private String webSiteURL;
    
    private String email;

    private String bankShortData;

    private String bankFullData;

    private String bankAccount;

    private String bankCode;

    private String bankAddress;

    private String unp;

    private String okpo;

    private String notice;
    
    private int discount;

    private Date priceListRequest;


    public ContractorTO() {
    }

    public ContractorTO(long id, String name, Integer rating, String legalAddress, LoadPlaceTO defaultShippingAddress, String country, CurrencyTO defaultCurrency, String webSiteURL, String bankShortData, String bankFullData, String bankAccount, String bankCode, String unp, String okpo, String notice) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.legalAddress = legalAddress;
        this.defaultShippingAddress = defaultShippingAddress;
        this.country = country;
        this.defaultCurrency = defaultCurrency;
        this.webSiteURL = webSiteURL;
        this.bankShortData = bankShortData;
        this.bankFullData = bankFullData;
        this.bankAccount = bankAccount;
        this.bankCode = bankCode;
        this.unp = unp;
        this.okpo = okpo;
        this.notice = notice;
    }

    //======================= Operations and extended getters ==============================
    /**
     * Getting account of update with given currency.
     * @param currencyId
     * @return
     */
    public AccountTO getAccount(long currencyId) {
        return SpringServiceContext.getInstance().getContractorService().getAccountByContractorAndCurrency(getId(), currencyId);
    }

    //============================== Getters and setters ===================================
    public boolean isNew() {
        return id == 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUidContractor() {
        return uidContractor;
    }

    public void setUidContractor(String uidContractor) {
        this.uidContractor = uidContractor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Deprecated
    public String getAddress(){
        //Method for backward compatibility (this getter is used in print templates).
        //Find out how to eliminate this method (probably need to implement less decoupled data model for printing).
        return getLegalAddress();
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public String getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
        this.postalAddress = postalAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LoadPlaceTO getDefaultShippingAddress() {
        return defaultShippingAddress;
    }

    public void setDefaultShippingAddress(LoadPlaceTO defaultShippingAddress) {
        this.defaultShippingAddress = defaultShippingAddress;
    }

    public CurrencyTO getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(CurrencyTO defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebSiteURL() {
        return webSiteURL;
    }

    public void setWebSiteURL(String webSiteURL) {
        this.webSiteURL = webSiteURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBankShortData() {
        return bankShortData;
    }

    public void setBankShortData(String bankShortData) {
        this.bankShortData = bankShortData;
    }

    public String getBankFullData() {
        return bankFullData;
    }

    public void setBankFullData(String bankFullData) {
        this.bankFullData = bankFullData;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getUnp() {
        return unp;
    }

    public void setUnp(String unp) {
        this.unp = unp;
    }

    public String getOkpo() {
        return okpo;
    }

    public void setOkpo(String okpo) {
        this.okpo = okpo;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Date getPriceListRequest() {
        return priceListRequest;
    }

    public void setPriceListRequest(Date priceListRequest) {
        this.priceListRequest = priceListRequest;
    }
}
