/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.details;

import com.artigile.warehouse.utils.EnumConst;

import javax.persistence.*;
import java.util.Comparator;

/**
 * Base class for all detail fields. It provide common functionality for most types of detail fields.
 * @author Shyrik, 14.12.2008
 */
@Entity
public class DetailField {

    public static Comparator<DetailField> CATALOG_GROUPING_NUM_COMPARATOR = new Comparator<DetailField>(){
        @Override
        public int compare(DetailField first, DetailField second) {
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

    @Id
    @GeneratedValue
    private long id;

    /**
     * If true, field is predefined and cannot be deleted, it's name cannot be changed.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean predefined;

    /**
     * Field index (part of the column name in the database). For example, index = 1 means "field1" column.
     * Exception: fieldIndex = 0 means column "name". 
     */
    private Integer fieldIndex;

    /**
     * Order of the field in the list of detail  fields.
     */
    private Long displayOrder;

    /**
     * Type of the predefined field.
     */
    @Enumerated(value = EnumType.STRING)
    private DetailPredefinedFieldType predefinedType;

    /**
     * Type of the field.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private DetailFieldType type;

    /**
     * Flag, indicates, if the field is mandatory, or not.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean mandatory;

    /**
     * Flag, indicates, whe the field value must to be unique.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean uniqueValue;

    /**
     * Name of the field.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Number (order), in which field is user in sorting of the details list.
     */
    private Long sortNum;

    /**
     * Number (order) in which this field is used to group similar details in details catalog group.
     * Such catalog groups are created automatically basing on values of fields.
     */
    private Integer catalogGroupNum;

    /**
     * Values of enumeration (is field type is enumeration). Values if separated with "\n" char.
     *  max length 64Kb
     */
    @Column(length = EnumConst.TEXT_MAX_LENGTH, columnDefinition = "TEXT")
    private String enumValues;

    /**
     * Template, used to gain field value is similar to the template, used in String.Format. But argument numbers
     * in the template must be replaced with field names.
     * Example:
     *   Template "{Model}-{Voltage}{Voltage measure unit} {Year} {Manufacturer}", when field values are:
     *     - Model = "K10"
     *     - Voltage = "16"
     *     - Voltage measure unit = "V"
     *     - Year = "2006"
     *     - Manufacturer = "Sony"
     *   Gives such field value: "K10-10V 2006 Sony".
     */
    private String template;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public DetailField() {
    }

    public DetailField(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DetailFieldType getType() {
        return type;
    }

    public void setType(DetailFieldType type) {
        this.type = type;
    }

    public boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isPredefined() {
        return predefined;
    }

    public void setPredefined(boolean predefined) {
        this.predefined = predefined;
    }

    public Long getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public DetailPredefinedFieldType getPredefinedType() {
        return predefinedType;
    }

    public void setPredefinedType(DetailPredefinedFieldType predefinedType) {
        this.predefinedType = predefinedType;
    }

    public boolean isUniqueValue() {
        return uniqueValue;
    }

    public void setUniqueValue(boolean uniqueValue) {
        this.uniqueValue = uniqueValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(String enumValues) {
        this.enumValues = enumValues;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Integer getFieldIndex(){
        return fieldIndex;
    }

    public void setFieldIndex(Integer fieldIndex) {
        this.fieldIndex = fieldIndex;
    }

    public static Comparator<DetailField> getCATALOG_GROUPING_NUM_COMPARATOR() {
        return CATALOG_GROUPING_NUM_COMPARATOR;
    }
}
