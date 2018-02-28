/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto;

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

/**
 * @author IoaN, Jan 3, 2009
 */

public class LoadPlaceTO extends EqualsByIdImpl {

    private long id;

    private String name;

    private String description;

    public LoadPlaceTO() {
    }

    public LoadPlaceTO(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public boolean equals(Object obj){
        if (obj instanceof LoadPlaceTO){
            LoadPlaceTO placeObj = (LoadPlaceTO)obj;
            return getId() == placeObj.getId();
        }
        return super.equals(obj);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
