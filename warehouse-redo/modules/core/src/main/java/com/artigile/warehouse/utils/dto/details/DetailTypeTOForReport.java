/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.details;

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

/**
 * @author Shyrik, 14.12.2008
 */

/**
 * Short detail representation, used in reports (lists of detail types).
 */
public class DetailTypeTOForReport extends EqualsByIdImpl {
    private long id;

    private String name;

    private String nameTemplate;

    private String description;

    private String nameInPrice;

    private String miscInPrice;

    private String typeInPrice;

    //======================= Constructors ====================================
    public DetailTypeTOForReport(){ }

    public DetailTypeTOForReport(final DetailTypeTO fullForm){
        setName(fullForm.getName());
        setNameTemplate(fullForm.getNameTemplate());
        setDescription(fullForm.getDescription());
        setNameInPrice(fullForm.getNameInPrice());
        setMiscInPrice(fullForm.getMiscInPrice());
        setTypeInPrice(fullForm.getTypeInPrice());
    }

    //=========================== Operations ==================================
    public boolean equals(Object obj){
        if (obj instanceof DetailTypeTOForReport){
            DetailTypeTOForReport secondObj = (DetailTypeTOForReport)obj;
            return getId() == secondObj.getId();
        }
        return super.equals(obj);
    }

    //======================== Getters and setters ============================
    public boolean isNew(){
        return id == 0;
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

    public String getNameTemplate() {
        return nameTemplate;
    }

    public void setNameTemplate(String nameTemplate) {
        this.nameTemplate = nameTemplate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameInPrice() {
        return nameInPrice;
    }

    public void setNameInPrice(String nameInPrice) {
        this.nameInPrice = nameInPrice;
    }

    public String getMiscInPrice() {
        return miscInPrice;
    }

    public void setMiscInPrice(String miscInPrice) {
        this.miscInPrice = miscInPrice;
    }

    public String getTypeInPrice() {
        return typeInPrice;
    }

    public void setTypeInPrice(String typeInPrice) {
        this.typeInPrice = typeInPrice;
    }
}
