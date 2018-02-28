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

import com.artigile.warehouse.domain.contractors.Contact;
import com.artigile.warehouse.domain.contractors.Contractor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 21.12.2008
 */

@Entity
public class Warehouse {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Warehouse address (this field is used for documents when need to print loading and unloading point).
     */
    private String address;

    /**
     * Owner of the warehouse.
     */
    @ManyToOne
    private Contractor owner;

    /**
     * Warehouse worker responsible for this warehouse.
     */
    @ManyToOne
    private Contact responsible;

    /**
     * Note about warehouse.
     */
    private String notice;

    /**
     * Root storage places of the warehouse.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy="warehouse")
    private List<StoragePlace> storagePlaces = new ArrayList<StoragePlace>();

   /**
     * Usual printer name of the warehouse (for ordinary papers).
     */
    private String usualPrinter;

    /**
     * Name of the printer, that is used to print stickers.
     */
    private String stickerPrinter;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public Warehouse() {
    }

    //================================= Operations ========================================
    public StoragePlace findStoragePlaceBySign(String sign) {
        for (StoragePlace place : storagePlaces){
            StoragePlace result = place.findStoragePlaceBySign(sign);
            if (result != null){
                return result;
            }
        }
        return null;
    }

    //=============================== Getters and setters =================================
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Contractor getOwner() {
        return owner;
    }

    public void setOwner(Contractor owner) {
        this.owner = owner;
    }

    public Contact getResponsible() {
        return responsible;
    }

    public void setResponsible(Contact responsible) {
        this.responsible = responsible;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public List<StoragePlace> getStoragePlaces() {
        return storagePlaces;
    }

    public void setStoragePlaces(List<StoragePlace> storagePlaces) {
        this.storagePlaces = storagePlaces;
    }

    public String getUsualPrinter() {
        return usualPrinter;
    }

    public void setUsualPrinter(String usualPrinter) {
        this.usualPrinter = usualPrinter;
    }

    public String getStickerPrinter() {
        return stickerPrinter;
    }

    public void setStickerPrinter(String stickerPrinter) {
        this.stickerPrinter = stickerPrinter;
    }

    public long getVersion() {
        return version;
    }
}
