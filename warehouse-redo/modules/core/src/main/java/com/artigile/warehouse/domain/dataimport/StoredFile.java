/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.dataimport;

import javax.persistence.*;
import java.util.Date;

/**
 * Data of any file stored in the database.
 *
 * @author Valery Barysok, 29.05.2011
 */
@Entity
public class StoredFile {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date storeDate;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public StoredFile() {
    }

    public StoredFile(long id) {
        this.id = id;
    }

    public StoredFile(long id, String name, Date storeDate) {
        this.id = id;
        this.name = name;
        this.storeDate = storeDate;
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

    public Date getStoreDate() {
        return storeDate;
    }

    public void setStoreDate(Date storeDate) {
        this.storeDate = storeDate;
    }

    public long getVersion() {
        return version;
    }
}
