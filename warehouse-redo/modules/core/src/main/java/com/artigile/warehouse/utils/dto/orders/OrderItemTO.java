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

import com.artigile.warehouse.domain.orders.OrderItemProcessingResult;
import com.artigile.warehouse.domain.orders.OrderItemState;
import com.artigile.warehouse.utils.Copiable;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 07.01.2009
 */

public class OrderItemTO extends EqualsByIdImpl implements Copiable{
    private long id;
    private OrderTOForReport order;
    private OrderItemState state;
    private OrderItemProcessingResult processingResult;
    private long number;
    private BigDecimal price;
    private long count;
    private DetailBatchTO detailBatch;
    private String text;
    private String notice;
    private boolean reserved;
    private List<OrderSubItemTO> subItems = new ArrayList<OrderSubItemTO>();

    //Calculated fields.
    private BigDecimal totalPrice;
    private BigDecimal vat;
    private BigDecimal totalPriceWithVat;

    public OrderItemTO(){
    }

    /**
     * Use this constructor to create detail order item.
     */
    public OrderItemTO(OrderTOForReport order, DetailBatchTO detailBatch){
        this.order = order;
        this.detailBatch = detailBatch;
        init();
    }

    /**
     * Use this constructor to create text order item.
     */
    public OrderItemTO(OrderTOForReport order){
        this.order = order;
        init();
    }

    /**
     * Create a new order item and initialize it from given prototype. This is used to copy order items.
     */
    public OrderItemTO(OrderTOForReport order, OrderItemTO prototype) {
        this.order = order;
        this.detailBatch = prototype.getDetailBatch();
        init();
        setNumber(prototype.getNumber());
        setPrice(SpringServiceContext.getInstance().getExchangeService().convert(
                order.getCurrency().getId(), prototype.getOrder().getCurrency().getId(), prototype.getPrice()
        ));
        setCount(prototype.getCount());
        setText(prototype.getText());
        setNotice(prototype.getNotice());
    }

    private void init() {
        //Initialising new processed order item.
        if (isDetailItem()){
            price = SpringServiceContext.getInstance().getExchangeService().convert(
                order.getCurrency().getId(), detailBatch.getCurrency().getId(), detailBatch.getSellPrice()
            );
        }
        state = OrderItemState.getInitialState();
        processingResult = null;
    }

    //================================= Calculated getters ==========================================
    public String getType() {
        return isTextItem() ? "" : getDetailBatch().getType();
    }

    public String getName() {
        return isTextItem() ? getText() : getDetailBatch().getName();
    }

    public String getMisc() {
        return isTextItem() ? "" : getDetailBatch().getMisc();
    }

    public String getMeasureSign() {
        return isTextItem() ? "" : getDetailBatch().getCountMeas().getSign();
    }

    public boolean isTextItem() {
        //There are two kinds of order items: detail from details batch list and text.
        return getDetailBatch() == null;
    }

    public boolean isDetailItem() {
        return !isTextItem();
    }

    public boolean isSameItem(OrderItemTO itemToCompare) {
        if (isDetailItem() && itemToCompare.isDetailItem()){
            return getDetailBatch().getId() == itemToCompare.getDetailBatch().getId();
        }
        else if (isTextItem() && itemToCompare.isTextItem()){
            return getText().equals(itemToCompare.getText());
        }
        return false;
    }

    public boolean isEditableState() {
        return getOrder().isEditableState();
    }

    public BigDecimal getTotalPrice(){
        return totalPrice;
    }

    public Object getDisplayedVatRate(){
        return order.getDisplayedVatRate();
    }

    public BigDecimal getVatRate(){
        return order.getVatRate();
    }

    public BigDecimal getVat(){
        return vat;
    }

    public BigDecimal getTotalPriceWithVat(){
        return totalPriceWithVat;
    }

    /**
     * Calculates total price and taxes.
     */
    private void refreshTotalPrice() {
        BigDecimal price = getPrice();
        if (price != null){
            totalPrice = getPrice().multiply(BigDecimal.valueOf(getCount()));
            BigDecimal vatRate = getVatRate();
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

    //==================================== Getters and setters =======================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderTOForReport getOrder() {
        return order;
    }

    public void setOrder(OrderTOForReport order) {
        this.order = order;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public OrderItemState getState() {
        return state;
    }

    public void setState(OrderItemState state) {
        this.state = state;
    }

    public OrderItemProcessingResult getProcessingResult() {
        return processingResult;
    }

    public void setProcessingResult(OrderItemProcessingResult processingResult) {
        this.processingResult = processingResult;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        refreshTotalPrice();
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
        refreshTotalPrice();
    }

    public DetailBatchTO getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatchTO detailBatch) {
        this.detailBatch = detailBatch;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public List<OrderSubItemTO> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<OrderSubItemTO> subItems) {
        this.subItems = subItems;
    }

    @Override
    public void copyFrom(Object source) {
        OrderItemTO sourceItem = (OrderItemTO)source;

        assert(getId() == sourceItem.getId());
        assert(getOrder().getId() == sourceItem.getOrder().getId());

        setNumber(sourceItem.getNumber());
        setState(sourceItem.getState());
        setProcessingResult(sourceItem.getProcessingResult());
        setPrice(sourceItem.getPrice());
        setCount(sourceItem.getCount());
        setDetailBatch(sourceItem.getDetailBatch());
        setText(sourceItem.getText());
        setNotice(sourceItem.getNotice());
        setReserved(sourceItem.isReserved());
        setSubItems(sourceItem.getSubItems());
    }
}
