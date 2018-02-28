/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.warehouse;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 21.12.2008
 */

@Entity
public class StoragePlace {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Sign of the storage place. Must be unique in the scope of the concrete warehouse.
     */
    @Column(nullable = false)
    private String sign;

    /**
     * Percent of filling the storage place.
     */
    @Range(min = 0, max = 100)
    private Long fillingDegree;

    /**
     * Notice about storage place.
     */
    private String notice;

    /**
     * If true, this storage place is available for direct postings detail into it.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean availableForPosting;

    /**
     * Children storage places.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentStoragePlace")
    private List<StoragePlace> storagePlaces = new ArrayList<StoragePlace>();

    /**
     * Parent storage place.
     */
    @ManyToOne
    private StoragePlace parentStoragePlace;

    /**
     * Warehouse, to what this concrete storage space belongs to.
     */
    @ManyToOne(optional = false)
    private Warehouse warehouse;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public StoragePlace() {
    }

    public StoragePlace(long id) {
        this.id = id;
    }

    public StoragePlace(long id, String sign, Long fillingDegree, String notice, boolean availableForPosting, Warehouse warehouse, StoragePlace parentStoragePlaceID) {
        this.id = id;
        this.sign = sign;
        this.fillingDegree = fillingDegree;
        this.notice = notice;
        this.availableForPosting = availableForPosting; 
        this.warehouse = warehouse;
        this.parentStoragePlace = parentStoragePlaceID;
    }

    //================================ Operations (manipulators) ==========================
    public void deleteStoragePlace() {
        if (getParentStoragePlace() != null){
            getParentStoragePlace().getStoragePlaces().remove(this);
            setParentStoragePlace(null);
        }
        getWarehouse().getStoragePlaces().remove(this);
        setWarehouse(null);
    }

    public void addStoragePlace(Warehouse warehouse, String parentSign) {
        if (parentSign != null) {
            StoragePlace parent = warehouse.findStoragePlaceBySign(parentSign);
            parent.getStoragePlaces().add(this);
            setParentStoragePlace(parent);
        }
        warehouse.getStoragePlaces().add(this);
        setWarehouse(warehouse);
    }

    public StoragePlace findStoragePlaceBySign(String sign) {
        if (sign.equals(getSign())) {
            return this;
        }
        for (StoragePlace child : storagePlaces) {
            StoragePlace result = child.findStoragePlaceBySign(sign);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    //============================= Getters and setters ===================================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getFillingDegree() {
        return fillingDegree;
    }

    public void setFillingDegree(Long fillingDegree) {
        this.fillingDegree = fillingDegree;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public boolean getAvailableForPosting() {
        return availableForPosting;
    }

    public void setAvailableForPosting(boolean availableForPosting) {
        this.availableForPosting = availableForPosting;
    }

    public List<StoragePlace> getStoragePlaces() {
        return storagePlaces;
    }

    public void setStoragePlaces(List<StoragePlace> storagePlaces) {
        this.storagePlaces = storagePlaces;
    }

    public StoragePlace getParentStoragePlace() {
        return parentStoragePlace;
    }

    public void setParentStoragePlace(StoragePlace parentStoragePlace) {
        this.parentStoragePlace = parentStoragePlace;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public boolean isAvailableForPosting() {
        return availableForPosting;
    }
}
