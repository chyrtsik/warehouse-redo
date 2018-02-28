/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.needs;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 25.02.2009
 */

/**
 * Filter for loading only ware need items, that are available for viewing and editing.
 */
@FilterDef(
    name = "wareNeedItemsAvailableForEditing",
    defaultCondition = "state < 3 and auto_created = 0"
)

/**
 * Ware need -- list of wares, that are needed by the organization.
 */
@Entity
public class WareNeed {

    @Id
    @GeneratedValue
    private long id;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * List of the wares, that are needed.
     */
    @Filter(name = "wareNeedItemsAvailableForEditing")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "wareNeed")
    private List<WareNeedItem> items = new ArrayList<WareNeedItem>();

    //=========================== Getters and setters =======================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<WareNeedItem> getItems() {
        return items;
    }

    public void setItems(List<WareNeedItem> items) {
        this.items = items;
    }

    public long getVersion() {
        return version;
    }
}
