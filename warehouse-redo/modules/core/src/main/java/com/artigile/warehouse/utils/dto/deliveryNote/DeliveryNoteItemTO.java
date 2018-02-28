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

import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Shyrik, 05.11.2009
 */
public class DeliveryNoteItemTO {
    private static final String NULL_VAT_VALUE = I18nSupport.message("order.vat.null.text");
    private long id;
    private long number;
    private DeliveryNoteTOForReport deliveryNote;
    private DetailBatchTO detailBatch;
    private Date shelfLifeDate;
    private String warehouseBatchNotice;
    private long count;
    private MeasureUnitTO countMeas;
    private BigDecimal price;
    //Calculated fields.
    private BigDecimal totalPrice;
    private BigDecimal vat;
    private BigDecimal totalPriceWithVat;

    //============================ Calculated getters =========================
    public String getItemType(){
        return detailBatch.getType();
    }

    public String getItemName(){
        return detailBatch.getName();
    }

    public String getItemMisc(){
        return detailBatch.getMisc();
    }

    public String getItemMeas(){
        return detailBatch.getCountMeas().getSign();
    }

    public BigDecimal getTotalPrice(){
        return totalPrice;
    }

    public BigDecimal getVatRate(){
        return deliveryNote.getVatRate();
    }

    public Object getDisplayedVatRate(){
        return deliveryNote.getDisplayedVatRate();
    }

    public BigDecimal getVat(){
        return vat;
    }

    public BigDecimal getTotalPriceWithVat(){
        return totalPriceWithVat;
    }

    public BigDecimal getTotalVat() {
        if (totalPriceWithVat != null) {
            return totalPriceWithVat.subtract(totalPrice);
        } else {
            return null;
        }
    }

    public String getVatRateAsText() {
        if (vat == null || vat.compareTo(new BigDecimal(0)) == 0) {
            return NULL_VAT_VALUE;
        } else {
            return vat.divide(totalPrice).multiply(new BigDecimal(100)).intValue() + "";
        }
    }

    private void refreshTotalPrice() {
        //Calculate total price and taxes.
        BigDecimal price = getPrice();
        if (price != null){
            totalPrice = getPrice().multiply(BigDecimal.valueOf(getCount()));
            BigDecimal vatRate = deliveryNote.getVatRate();
            if (vatRate != null){
                vat = totalPrice.multiply(vatRate).divide(BigDecimal.valueOf(100));
                totalPriceWithVat = totalPrice.add(vat);
            }
            else{
                vat = null;
                totalPriceWithVat = null;
            }
        }
    }

    //============================ Getters and setters ==============================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public DeliveryNoteTOForReport getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(DeliveryNoteTOForReport deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public DetailBatchTO getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatchTO detailBatch) {
        this.detailBatch = detailBatch;
    }

    public Date getShelfLifeDate() {
        return shelfLifeDate;
    }

    public void setShelfLifeDate(Date shelfLifeDate) {
        this.shelfLifeDate = shelfLifeDate;
    }

    public String getWarehouseBatchNotice() {
        return warehouseBatchNotice;
    }

    public void setWarehouseBatchNotice(String warehouseBatchNotice) {
        this.warehouseBatchNotice = warehouseBatchNotice;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
        refreshTotalPrice();
    }

    public MeasureUnitTO getCountMeas() {
        return countMeas;
    }

    public void setCountMeas(MeasureUnitTO countMeas) {
        this.countMeas = countMeas;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        refreshTotalPrice();
    }

    public Integer getPackagesCount(){
        return null; //Implement when the first customer will want this field in delivery note.
    }

    public BigDecimal getWeight(){
        return null; //Implement when the first customer will want this field in delivery note.
    }
}
