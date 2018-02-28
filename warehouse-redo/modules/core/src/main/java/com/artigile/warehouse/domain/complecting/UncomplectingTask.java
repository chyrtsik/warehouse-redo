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
import com.artigile.warehouse.domain.orders.OrderSubItem;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Shyrik, 12.06.2009
 */

/**
 * Uncomplecting task means a number of wares, that are to be pulled from the parcel.
 * Uncomplecting task often created when amount of ware in order changes (decreases) after warehouse
 * worker has been processed complecting task for this order item. It that kind of situations we should
 * tell worker, that he need to exclude some of wares from the parcel.
 */
@Entity
public class UncomplectingTask {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Type of uncomplecting task (what should warehouse worker do with this task).
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UncomplectingTaskType type;

    /**
     * Order sub item, for which this task was created.
     */
    @ManyToOne
    private OrderSubItem orderSubItem;

    /**
     * State of this uncomplecting task.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UncomplectingTaskState state;

    /**
     * Amount of ware to be processed.
     */
    private long countToProcess;

    /**
     * Worker, who has done this task.
     */
    @ManyToOne
    private User worker;

    /**
     * Date and time of task has been created.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    /**
     * Date and time of work has been done.
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date doneDate;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //===================================== Getters and setters ==============================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UncomplectingTaskType getType() {
        return type;
    }

    public void setType(UncomplectingTaskType type) {
        this.type = type;
    }

    public OrderSubItem getOrderSubItem() {
        return orderSubItem;
    }

    public void setOrderSubItem(OrderSubItem orderSubItem) {
        this.orderSubItem = orderSubItem;
    }

    public UncomplectingTaskState getState() {
        return state;
    }

    public void setState(UncomplectingTaskState state) {
        this.state = state;
    }

    public long getCountToProcess() {
        return countToProcess;
    }

    public void setCountToProcess(long countToProcess) {
        this.countToProcess = countToProcess;
    }

    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }

    public long getVersion() {
        return version;
    }
}
