/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.details;

import com.artigile.warehouse.domain.details.DetailFieldType;
import com.artigile.warehouse.domain.details.DetailPredefinedFieldType;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

import java.util.Comparator;
import java.util.List;

/**
 * @author Shyrik, 14.12.2008
 */

/**
 * Representation of the detail's field.
 */
public class DetailFieldTO extends EqualsByIdImpl {
    /**
     * Sorting or fields support.
     */
    public static Comparator<DetailFieldTO> DISPLAY_ORDER_COMPARATOR = new Comparator<DetailFieldTO>(){
        @Override
        public int compare(DetailFieldTO first, DetailFieldTO second) {
            Long firstDisplayOrder = first.getDisplayOrder();
            Long secondDisplayOrder = second.getDisplayOrder();
            if (firstDisplayOrder == null && secondDisplayOrder == null){
                return 0;
            }
            else if (firstDisplayOrder == null){
                return -1;
            }
            else if (secondDisplayOrder == null){
                return 1;
            }
            else{
                return firstDisplayOrder.compareTo(secondDisplayOrder);
            }
        }
    };

    public static Comparator<DetailFieldTO> CATALOG_GROUPING_NUM_COMPARATOR = new Comparator<DetailFieldTO>(){
        @Override
        public int compare(DetailFieldTO first, DetailFieldTO second) {
            Integer firstGroupNum = first.getCatalogGroupNum();
            Integer secondGroupNum = second.getCatalogGroupNum();
            if (firstGroupNum == null && secondGroupNum == null){
                return 0;
            }
            else if (firstGroupNum == null){
                return -1;
            }
            else if (secondGroupNum == null){
                return 1;
            }
            else{
                return firstGroupNum.compareTo(secondGroupNum);
            }
        }
    };

    private long id;

    private String name;

    private boolean mandatory;

    private DetailFieldType type;

    private Long sortNum;

    private Integer catalogGroupNum;

    private List<String> enumValues;

    private String template;

    private boolean predefined;

    private DetailPredefinedFieldType predefinedType;

    private boolean unique;

    private Long displayOrder;
    
    private Integer fieldIndex;

    public DetailFieldTO() {
    }

    public boolean isNew() {
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

    public boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public DetailFieldType getType() {
        return type;
    }

    public void setType(DetailFieldType type) {
        this.type = type;
    }

    public Long getSortNum() {
        return sortNum;
    }

    public void setSortNum(Long sortNum) {
        this.sortNum = sortNum;
    }

    public Integer getCatalogGroupNum() {
        return catalogGroupNum;
    }

    public void setCatalogGroupNum(Integer catalogGroupNum) {
        this.catalogGroupNum = catalogGroupNum;
    }

    public List<String> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(List<String> enumValues) {
        this.enumValues = enumValues;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public boolean isPredefined() {
        return predefined;
    }

    public void setPredefined(boolean predefined) {
        this.predefined = predefined;
    }

    public DetailPredefinedFieldType getPredefinedType() {
        return predefinedType;
    }

    public void setPredefinedType(DetailPredefinedFieldType predefinedType) {
        this.predefinedType = predefinedType;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isUnique(){
        return unique;
    }

    public Long getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Integer getFieldIndex() {
        return fieldIndex;
    }

    public void setFieldIndex(Integer fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    @Override
    public int hashCode() {
        return isNew() ? (int) (id ^ (id >>> 32)) : getName().hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof DetailFieldTO){
            DetailFieldTO second = (DetailFieldTO) obj;
            if (isNew() || second.isNew()) {
                //New (not saved) fields should be compared first by name.
                if (getName() != null && getName().equals(second.getName())) {
                    return true;
                }
            }
        }
        return super.equals(obj);
    }
}
