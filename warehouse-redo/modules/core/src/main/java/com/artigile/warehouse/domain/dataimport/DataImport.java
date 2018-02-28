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

import com.artigile.warehouse.domain.admin.User;
import com.artigile.warehouse.utils.ModelFieldsLengths;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * Base class of data import. Holds generic information about import.
 *
 * @author Aliaksandr.Chyrtsik, 02.11.11
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class DataImport {

    @Id
    @GeneratedValue
    protected long id;

    /**
     * Date and time of import begin date.
     */
    @Column(nullable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date importDate;

    /**
     * User-friendly description of this import.
     */
    private String description;

    /**
     * Data adapter used for data extraction.
     */
    @Column(nullable = false, length = ModelFieldsLengths.UID_LENGTH)
    private String adapterUid;

    /**
     * Configuration used for data adapter.
     */
    @Lob
    @Column(nullable = false)
    protected String adapterConf;

    /**
     * User launched this import.
     */
    @ManyToOne(optional = false)
    private User user;

    /**
     * Curreтt status of import.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImportStatus importStatus;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Stored files attached to this import (used when these files are required by import to be done).
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "dataImport_id")
    private Collection<StoredFile> storedFiles;

    //============================ Getters and setters ===================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdapterUid() {
        return adapterUid;
    }

    public void setAdapterUid(String adapterUid) {
        this.adapterUid = adapterUid;
    }

    public String getAdapterConf() {
        return adapterConf;
    }

    public void setAdapterConf(String adapterConf) {
        this.adapterConf = adapterConf;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ImportStatus getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }

    public Collection<StoredFile> getStoredFiles() {
        return storedFiles;
    }

    public void setStoredFiles(Collection<StoredFile> storedFiles) {
        this.storedFiles = storedFiles;
    }

    public long getVersion() {
        return version;
    }
}
