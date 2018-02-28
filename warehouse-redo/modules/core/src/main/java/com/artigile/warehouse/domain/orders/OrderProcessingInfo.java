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

import javax.persistence.*;

/**
 * @author Shyrik, 25.06.2009
 */

/**
 * Holds information about order, that allows:
 * <OL>
 *   <LI>speed up loading some necessary order info due to eliminating of loading full order content;
 *   <LI>update order processing progress safely (without worrying about concurrent updates), because this info
 * is stored in another table and it isn't so important, as order's data, such as total price.
 * </OL>
 */

@Entity
public class OrderProcessingInfo {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Order, to which this information belongs to.
     */
    @OneToOne(optional = false)
    private Order order;

    /**
     * Count of fully processed order items.
     */
    @Column(nullable = false)
    private long complectedItemsCount;

    /**
     * Total count of order items.
     */
    @Column(nullable = false)
    private long itemsCount;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //================================== Getters and setters =================================
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

    public Long getComplectedItemsCount() {
        return complectedItemsCount;
    }

    public void setComplectedItemsCount(Long complectedItemsCount) {
        this.complectedItemsCount = complectedItemsCount;
    }

    public long getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(long itemsCount) {
        this.itemsCount = itemsCount;
    }

    public long getVersion() {
        return version;
    }
}
