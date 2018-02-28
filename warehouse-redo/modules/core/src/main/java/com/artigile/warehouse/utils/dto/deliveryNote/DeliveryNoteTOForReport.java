/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.deliveryNote;

import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.chargeoff.ChargeOffTOForReport;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.postings.PostingTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.money.MoneyUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Shyrik, 01.11.2009
 */
public class DeliveryNoteTOForReport extends EqualsByIdImpl {
    private static final String NULL_VAT_VALUE = I18nSupport.message("order.vat.null.text");
    private long id;
    private DeliveryNoteState state;
    private long number;
    private Date documentDate;
    private long documentNumber;
    private String documentName;
    private ChargeOffTOForReport chargeOff;
    private WarehouseTOForReport destinationWarehouse;
    private ContractorTO contractor;
    private PostingTOForReport posting;
    private String notice;
    private CurrencyTO currency;
    private BigDecimal totalPrice;
    private BigDecimal vatRate;
    private String blankNumber;

    private String carBrand;
    private String carStateNumber;
    private String driverFullName;
    private String carOwner;
    private String carTrailer;

    //Calculated fields (just for displaying).
    private BigDecimal vat;
    private BigDecimal totalPriceWithVat;
    private int totalQuantity;

    //========================= Calculated getters ========================
    public Date getShipmentDate(){
        return getChargeOff() != null ? getChargeOff().getPerformDate() : null;
    }

    public UserTO getShipmentUser(){
        return getChargeOff() != null ? getChargeOff().getPerformer() : null;
    }

    public String getShipmentReason(){
        return (getChargeOff() != null && getChargeOff().getReason() != null) ? getChargeOff().getReason().getName() : null;
    }

    public Date getReceiveDate(){
        return getPosting() != null ? getPosting().getCreateDate() : null;
    }

    public UserTO getReceiveUser(){
        return getPosting() != null ? getPosting().getCreatedUser() : null;
    }

    public String getFromLocation(){
        if ( getChargeOff() != null ){
            return I18nSupport.message("deliveryNote.list.location.warehouseTemplate", getChargeOff().getWarehouse().getName());
        }
        return null;
    }

    public String getToLocation(){
        if ( getDestinationWarehouse() != null ){
            return I18nSupport.message("deliveryNote.list.location.warehouseTemplate", getDestinationWarehouse().getName());
        }
        else if ( getContractor() != null ){
            return I18nSupport.message("deliveryNote.list.location.contractorTemplate", getContractor().getName());
        }
        return null;
    }

    public ContractorTO getShipper(){
        if (getChargeOff().getWarehouse().getOwner() != null){
            return getChargeOff().getWarehouse().getOwner();
        }
        else{
            return null;
        }
    }

    public BigDecimal getVat() {
        return vat;
    }

    public Object getDisplayedVatRate(){
        if (vatRate != null){
            return vatRate;
        }
        else {
            return NULL_VAT_VALUE;
        }
    }

    public String getVatRateAsText(){
        if (vatRate != null){
            return StringUtils.formatNumber(vatRate);
        }
        else {
            return NULL_VAT_VALUE;
        }
    }

    public BigDecimal getTotalPriceWithVat(){
        return totalPriceWithVat;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Date getChargeOffDate() {
        return chargeOff.getPerformDate();
    }


    public String getTotalVatText() {
        return getTotalPriceWithVat() != null ? MoneyUtils.getMoneyAmountInWords(getVat(), getCurrency()) : null;
    }

    public String getTotalSumVatRateText() {
        return getTotalPriceWithVat() != null ? MoneyUtils.getMoneyAmountInWords(getTotalPriceWithVat(), getCurrency()) : null;

    }

    public String getDisplayCar() {
        boolean emptyBrand = StringUtils.isStringNullOrEmpty(carBrand);
        boolean emptyStateNumber = StringUtils.isStringNullOrEmpty(carStateNumber);
        if (!emptyBrand && !emptyStateNumber) {
            return carBrand + " " + carStateNumber;
        }
        else if (!emptyBrand) {
            return carBrand;
        }
        else if (!emptyStateNumber) {
            return carStateNumber;
        }

        return "";
    }

    public String getDocument() {
        return I18nSupport.message("deliveryNote.document.template", getDocumentName(), getDocumentNumber(), getDocumentDate());
    }

    public ContractorTO getShippingCustomer(){
        return null; //Implement when the first customer will want to print this in delivery note.
    }

    public String getWaybillNumber(){
        return null; //Implement when the first customer will want this field.
    }

    public Integer getRidesCount(){
        return 1; //Implement field in delivery note when the first customer will want this field to be editable.
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

    //====================== Getters and setters ==========================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DeliveryNoteState getState() {
        return state;
    }

    public void setState(DeliveryNoteState state) {
        this.state = state;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Date getDocumentDate(){
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public long getDocumentNumber(){
        return documentNumber;
    }

    public void setDocumentNumber(long documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentName(){
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public ChargeOffTOForReport getChargeOff() {
        return chargeOff;
    }

    public void setChargeOff(ChargeOffTOForReport chargeOff) {
        this.chargeOff = chargeOff;
    }

    public WarehouseTOForReport getDestinationWarehouse() {
        return destinationWarehouse;
    }

    public void setDestinationWarehouse(WarehouseTOForReport destinationWarehouse) {
        this.destinationWarehouse = destinationWarehouse;
    }

    public ContractorTO getContractor() {
        if (contractor != null){
            return contractor;
        }
        else if (getDestinationWarehouse() != null){
            return getDestinationWarehouse().getOwner();
        }
        else{
            return null;
        }
    }

    public void setContractor(ContractorTO contractor) {
        this.contractor = contractor;
    }

    public PostingTOForReport getPosting() {
        return posting;
    }

    public void setPosting(PostingTOForReport posting) {
        this.posting = posting;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
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
        refreshVatValues();
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
        refreshVatValues();
    }

    public String getBlankNumber() {
        return blankNumber;
    }

    public void setBlankNumber(String blankNumber) {
        this.blankNumber = blankNumber;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarStateNumber() {
        return carStateNumber;
    }

    public void setCarStateNumber(String carStateNumber) {
        this.carStateNumber = carStateNumber;
    }

    public String getDriverFullName() {
        return driverFullName;
    }

    public void setDriverFullName(String driverFullName) {
        this.driverFullName = driverFullName;
    }

    public String getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner;
    }

    public String getCarTrailer() {
        return carTrailer;
    }

    public void setCarTrailer(String carTrailer) {
        this.carTrailer = carTrailer;
    }

    public String getRecipientNameAddress() {
        return getContractor() == null ? null : getContractor().getName() + ", " + getContractor().getLegalAddress();
    }

    public String getLoadingPoint() {
        return getChargeOff().getWarehouse().getAddress();
    }

    public String getUnloadingPoint() {
        return getDestinationWarehouse() == null ? null : getDestinationWarehouse().getAddress();
    }
}
