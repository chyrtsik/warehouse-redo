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
import com.artigile.warehouse.utils.Copiable;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.needs.WareNeedItemTO;

import java.math.BigDecimal;

/**
 * @author Shyrik, 02.03.2009
 */
public class PurchaseItemTO extends EqualsByIdImpl implements Copiable {
    private long id;

    private Long number;

    private PurchaseTOForReport purchase;

    private BigDecimal price;

    private Long count;

    private MeasureUnitTO countMeas;

    private WareNeedItemTO wareNeedItem;

    private String name;

    private String misc;

    private String notice;

    //================================ Constructors============================================
    public PurchaseItemTO(PurchaseTOForReport purchase){
        this.purchase = purchase;
    }

    public PurchaseItemTO(PurchaseTOForReport purchase, WareNeedItemTO wareNeedItem) {
        this.purchase = purchase;
        this.wareNeedItem = wareNeedItem;
        this.count = wareNeedItem.getCount();
        this.countMeas = wareNeedItem.getCountMeas();
    }

    //================================ Calculated getters =====================================
    public boolean isNew() {
        return id == 0;
    }

    public boolean isText(){
        return wareNeedItem == null;
    }

    public String getItemType(){
        return isText() ? "" : wareNeedItem.getNeedType();
    }

    public String getItemName(){
        return isText() ? getName() : wareNeedItem.getNeedName();
    }

    public String getItemMisc(){
        return isText() ? getMisc() : wareNeedItem.getNeedMisc();
    }

    public BigDecimal getTotalPrice(){
        if (price == null || count == null){
            return null;
        }
        return price.multiply(new BigDecimal(count));
    }

    public boolean canBeDeleted() {
        return canBeEdited();
    }

    public boolean canBeEdited() {
        return purchase.getState() == PurchaseState.CONSTRUCTION;
    }

    //================================ Getters and setters ====================================
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

    public PurchaseTOForReport getPurchase() {
        return purchase;
    }

    public void setPurchase(PurchaseTOForReport purchase) {
        this.purchase = purchase;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public MeasureUnitTO getCountMeas() {
        return countMeas;
    }

    public void setCountMeas(MeasureUnitTO countMeas) {
        this.countMeas = countMeas;
    }

    public WareNeedItemTO getWareNeedItem() {
        return wareNeedItem;
    }

    public void setWareNeedItem(WareNeedItemTO wareNeedItem) {
        this.wareNeedItem = wareNeedItem;
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

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public void copyFrom(Object source) {
        PurchaseItemTO sourceItem = (PurchaseItemTO)source;

        assert(getId() == sourceItem.getId());
        assert(getPurchase().getId() == sourceItem.getPurchase().getId());

        setNumber(sourceItem.getNumber());
        setPrice(sourceItem.getPrice());
        setCount(sourceItem.getCount());
        setCountMeas(sourceItem.getCountMeas());
        setWareNeedItem(sourceItem.getWareNeedItem());
        setName(sourceItem.getName());
        setMisc(sourceItem.getMisc());
        setNotice(sourceItem.getNotice());
    }
}
