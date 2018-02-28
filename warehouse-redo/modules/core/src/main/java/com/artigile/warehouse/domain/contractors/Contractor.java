/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.contractors;

import com.artigile.warehouse.domain.finance.Account;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.utils.ModelFieldsLengths;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 01.12.2008
 */

/**
 * Contractor -- any organization (firm etc., person), with whom the trading
 * firm was or are in contact
 *
 */
@Entity
public class Contractor {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Unique identification number of contractor in global directory.
     */
    @Column(unique = true, updatable = false, length = ModelFieldsLengths.UID_LENGTH)
    private String uidContractor;

    /**
     * Name of update.
     */
    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "fullname")
    private String fullname;

    /**
     * Rating of contractor
     */
    private Integer rating;

    /**
     * Addres of the main office (may be legal address).
     */
    @Column(name = "legal_address")
    private String legalAddress;

    @Column(name = "postal_address")
    private String postalAddress;

    @Column(name = "phone")
    private String phone;

    /**
     * Default shipping address.
     */
    @ManyToOne
    private LoadPlace defaultShippingAddress;

    /**
     * Country of the update.
     */
    private String country;

    /**
     * Currency, default for this update.
     */
    @ManyToOne
    private Currency defaultCurrency;

    /**
     * URL of update's website.
     */
    private String webSiteURL;

    /**
     * E-mail address of this contractor
     */
    private String email;

    /**
     * Short data about update's bank.
     */
    private String bankShortData;

    /**
     * Full data about update's bank (for official papers).
     */
    private String bankFullData;

    /**
     * Settlement account of update.
     */
    private String bankAccount;

    /**
     * Bank code
     */
    private String bankCode;

    /**
     * Bank address.
     */
    private String bankAddress;


    /**
     * UNP
     */
    private String unp;

    /**
     * OKPO
     */
    private String okpo;

    /**
     * Notice about update.
     */
    private String notice;

    /**
     * Discount for this contractor (in percent)
     */
    private int discount;

    /**
     * Last datetime of request price list of this contractor
     */
    private Date priceListRequest;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Contacts of update.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contractor")
    @OrderBy("fullName")
    private List<Contact> contacts = new ArrayList<Contact>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contractor")
    private List<Account> accounts = new ArrayList<Account>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contractor")
    private List<Shipping> shippings = new ArrayList<Shipping>();

    public Contractor() {
    }

    public Contractor(String name, Integer rating, String legalAddress, LoadPlace defaultShippingAddress, String country, Currency defaultCurrency, String webSiteURL, String bankShortData, String bankFullData, String bankAccount, String bankCode, String unp, String okpo, String notice) {
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

    //============= Getters and Setters ========================

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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
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

    public LoadPlace getDefaultShippingAddress() {
        return defaultShippingAddress;
    }

    public void setDefaultShippingAddress(LoadPlace defaultShippingAddress) {
        this.defaultShippingAddress = defaultShippingAddress;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
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

    public long getVersion() {
        return version;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public List<Shipping> getShippings() {
        return shippings;
    }

    public void setShippings(List<Shipping> shippings) {
        this.shippings = shippings;
    }
}
