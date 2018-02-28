/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.complecting;

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.domain.chargeoff.ChargeOffItem;
import com.artigile.warehouse.domain.movement.MovementItem;
import com.artigile.warehouse.domain.orders.OrderItem;
import com.artigile.warehouse.domain.orders.OrderSubItem;

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Shyrik, 28.04.2009
 */

/**
 * Represents task, given to warehouse worker, when processing the client's order.
 */
@Entity
public class ComplectingTask {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Order sub items, for which this task was created.
     */
    @ManyToOne
    private OrderSubItem orderSubItem;

    /*
     * Movement item, for which this complecting task was created.
     */
    @OneToOne
    private MovementItem movementItem;

    /**
     * State of this complectins task.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ComplectingTaskState state;

    /**
     * Needed count of the ware.
     */
    private long neededCount;

    /**
     * Found count of wares.
     */
    private Long foundCount;

    /**
     * True, if task has been printed by worker, and False, is not.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean printed;

    /**
     * True, if task sticker has been printed by worker, and False, is not.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean stickerPrinted;

    /**
     * Worker, who began doing this task.
     */
    @ManyToOne
    private User worker;

    /**
     * Date and time of work begin.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date workBegin;

    /**
     * Date and time of work end.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date workEnd;

    /*
     * Charge off item, that has been created after end of the complecting task.
     */
    @OneToOne
    private ChargeOffItem chargeOffItem;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //=========================== Special getters and manipulators ==========================
    /**
     * Gives a textual desctiption of complecting task, that allows user to identify it easily.
     * @return
     */
    public String describe() {
        OrderItem orderItem = orderSubItem != null ? orderSubItem.getOrderItem() : null;
        List<OrderSubItem> subItems = orderItem != null ? orderItem.getSubItems() : null;
        if (subItems != null && subItems.size() > 1){
            return MessageFormat.format("{0}.{1}[{2}]",
                    orderItem.getOrder().getNumber(),
                    orderItem.getNumber(),
                    orderSubItem.getNumber());
        }
        else{
            return orderItem != null ? MessageFormat.format("{0}.{1}",
                    orderItem.getOrder().getNumber(),
                    orderItem.getNumber()) : null;
        }
    }

    public boolean canBeCancelled() {
        return state.equals(ComplectingTaskState.NOT_PROCESSED);
    }

    public boolean isProcessed() {
        return getState().equals(ComplectingTaskState.PROCESSED) ||
               getState().equals(ComplectingTaskState.READY_FOR_SHIPPING) ||
               getState().equals(ComplectingTaskState.SHIPPED);
    }

    //============================ Getters and setters ======================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OrderSubItem getOrderSubItem() {
        return orderSubItem;
    }

    public void setOrderSubItem(OrderSubItem orderSubItem) {
        this.orderSubItem = orderSubItem;
    }

    public MovementItem getMovementItem() {
        return movementItem;
    }

    public void setMovementItem(MovementItem movementItem) {
        this.movementItem = movementItem;
    }

    public ComplectingTaskState getState() {
        return state;
    }

    public void setState(ComplectingTaskState state) {
        this.state = state;
    }

    public long getNeededCount() {
        return neededCount;
    }

    public void setNeededCount(long neededCount) {
        this.neededCount = neededCount;
    }

    public Long getFoundCount() {
        return foundCount;
    }

    public void setFoundCount(Long foundCount) {
        this.foundCount = foundCount;
    }

    public boolean isPrinted() {
        return printed;
    }

    public void setPrinted(boolean printed) {
        this.printed = printed;
    }

    public boolean isStickerPrinted() {
        return stickerPrinted;
    }

    public void setStickerPrinted(boolean stickerPrinted) {
        this.stickerPrinted = stickerPrinted;
    }

    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }

    public Date getWorkBegin() {
        return workBegin;
    }

    public void setWorkBegin(Date workBegin) {
        this.workBegin = workBegin;
    }

    public Date getWorkEnd() {
        return workEnd;
    }

    public void setWorkEnd(Date workEnd) {
        this.workEnd = workEnd;
    }

    public ChargeOffItem getChargeOffItem() {
        return chargeOffItem;
    }

    public void setChargeOffItem(ChargeOffItem chargeOffItem) {
        this.chargeOffItem = chargeOffItem;
    }

    public long getVersion() {
        return version;
    }
}
