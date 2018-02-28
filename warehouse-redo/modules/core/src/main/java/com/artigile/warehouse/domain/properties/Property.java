/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.properties;

import javax.persistence.*;

/**
 * @author Valery Barysok, 28.12.2009
 */

@Entity
public class Property {

    @Id
    @GeneratedValue
    private long id;

    /**
     * The key to be placed into the property list.
     */
    @Column(name="propKey", nullable = false, unique = true)
    private String key;

    /**
     * The value corresponding to key.
     */
    @Column(name="propValue")
    private String value;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public Property() {
    }

    public Property(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getVersion() {
        return version;
    }
}
