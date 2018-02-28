/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.orders;

import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 06.01.2009
 */

/**
 * Filter for loading only non deleted order items.
 */
@FilterDef(
    name = "nonDeletedOrderSubItems",
    defaultCondition = "deleted <> 1"
)

/**
 * Item of the order.
 */
@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Order, to which this items belongs to.
     */
    @ManyToOne(optional = false)
    private Order order;

    /**
     * Number of the item in the order.
     */
    @Column(nullable = false)
    private long number;

    /**
     * State of the item.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderItemState state;

    /**
     * Result of processing this order item.
     */
    @Enumerated(value = EnumType.STRING)
    private OrderItemProcessingResult processingResult;

    /**
     * True, if order item has been deleted from the order. It is hidden now, but it cannot be deleted, because
     * it may have complecting tasks, related to this order item.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean deleted;

    /**
     * Price of the item (in the target currency). May differ from sell price, showed in price list.
     */
    @Column(precision = ModelFieldsLengths.MAX_LENGTH_DOUBLE_PRECISION,
            scale = ModelFieldsLengths.MAX_LENGTH_DOUBLE_SCALE,
            nullable = false)
    private BigDecimal price;

    /**
     * Count of the goods in this price items.
     */
    @Column(nullable = false, name = "amount")
    private long amount;

    /**
     * Detail batch item of this order item.
     */
    @ManyToOne
    private DetailBatch detailBatch;

    /**
     * Text, if the order item is only text string (haven't entry in the detail batches list).
     */
    private String text;

    /**
     * Notice text for order item.
     */
    private String notice;

    /**
     * True, if item is reserved, False, if not and null, of there is no reserving is needed.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean reserved;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * List of real goods from the warehouse, which excpected to be put into this order item.
     * If order item is text, the list of subitems will be empty.
     */
    @Filter(name = "nonDeletedOrderSubItems")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderItem")
    private List<OrderSubItem> subItems = new ArrayList<OrderSubItem>();

    //====================================== Calculated getters ==========================================
    public boolean isDetail(){
        return detailBatch != null;
    }

    public boolean isProcessed(){
        return !isDeleted() &&
               !getState().isBefore(OrderItemState.PROCESSED) &&
                OrderItemProcessingResult.COMPLECTED.equals(getProcessingResult());
    }

    public boolean isEditableState() {
        switch (state) {
            case NOT_PROCESSED:
            case PROCESSING:
            case PROCESSED:
            case READY_FOR_SHIPPING:
                return true;
            case SHIPPED:
            case SOLD:
                return false;
        }
        return false;
    } 

    //======================================= Getters and setters ========================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public DetailBatch getDetailBatch() {
        return detailBatch;
    }

    public void setDetailBatch(DetailBatch detailBatch) {
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

    public boolean getReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public long getVersion() {
        return version;
    }

    public List<OrderSubItem> getSubItems() {
        return subItems;
    }

    public void setSubItems(List<OrderSubItem> subItems) {
        this.subItems = subItems;
    }
}
