/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.details;

import com.artigile.warehouse.bl.detail.DetailSerialNumberService;
import com.artigile.warehouse.domain.details.DetailFieldType;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.parser.ParsedTemplate;
import com.artigile.warehouse.utils.parser.ParsedTemplateDataSource;

import java.util.*;

/**
 * Data transfer object for product serial numbers.
 * @author Aliaksandr Chyrtsik
 * @since 30.06.13
 */
public class DetailSerialNumberTO extends EqualsByIdImpl implements DetailFieldOwner, ParsedTemplateDataSource {
    private long id;

    /**
     * Product which this serial number belongs to.
     */
    private DetailBatchTO detail;

    /**
     * User configured fields of the detail model (with values).
     */
    private List<DetailFieldValueTO> fields = new ArrayList<DetailFieldValueTO>();

    /**
     * List of fields, ordered in the display order of fields (may be different to natural field order).
     */
    private List<DetailFieldValueTO> fieldsInDisplayOrder;

    //==================== Operations ============================

    /**
     * Copy serial number from given source object.
     * @param src serial number to be copied.
     */
    public void copyFrom(DetailSerialNumberTO src){
        setId(src.getId());
        setDetail(src.getDetail());
        fields.clear();
        for (DetailFieldValueTO field : src.getFields()){
            fields.add(new DetailFieldValueTO(field.getType(), this, field.getValue()));
        }
    }


    //======================== DetailFieldOwner implementation ============================
    @Override
    public void onValueChanged(DetailFieldValueTO sender) {
        for (DetailFieldValueTO field : fields){
            if (field != sender){
                field.onNeedRecalculate();
            }
        }
    }

    @Override
    public boolean isUniqueFieldValue(DetailFieldValueTO field, String value) {
        return true; //Unique fields are not supported for serial numbers (the only value checked for uniqueness is id).
    }

    //==================== ParsedTemplateDataSource implementation ========================
    private  static final int FIELD_INDEX_BASE = 10000;

    @Override
    public String calculateTemplate(String template) {
        ParsedTemplate parsedTemplate = detail.getModel().getType().parseTemplate(template, this);
        return parsedTemplate.calculate(this);
    }

    @Override
    public int getFieldIndexByName(String fieldName) {
        for (DetailFieldValueTO field : fields){
            if (field.getType().getName().equals(fieldName)){
                return FIELD_INDEX_BASE + field.getType().getFieldIndex();
            }
        }
        return detail.getFieldIndexByName(fieldName);
    }

    @Override
    public String getFieldValue(int index) {
        for (DetailFieldValueTO field : fields){
            if (field.getType().getFieldIndex() + FIELD_INDEX_BASE == index){
                return field.getDisplayValue();
            }
        }
        return detail.getFieldValue(index);
    }

    //===================== Getters for predefined field types ==========================
    public Long getCountInPackaging(){
        DetailFieldValueTO field = findFieldByType(DetailFieldType.COUNT_IN_PACKAGING);
        return (field != null && StringUtils.isNumberLong(field.getValue())) ? Long.valueOf(field.getValue()) : null;
    }

    public Date getShelfLifeDate(){
        DetailFieldValueTO field = findFieldByType(DetailFieldType.SHELF_LIFE);
        return field != null ? StringUtils.parseDate(field.getValue()) : null;
    }

    private DetailFieldValueTO findFieldByType(DetailFieldType fieldType) {
        for (DetailFieldValueTO field : fields){
            if (field.getType().getType() == fieldType){
                return field;
            }
        }
        return null;
    }

    //================== Getters and setters =====================
    public boolean isNew(){
        return id == 0;
    }

    public String getBarCode(){
        return DetailSerialNumberService.formatSerialNumberBarCode(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DetailBatchTO getDetail() {
        return detail;
    }

    public void setDetail(DetailBatchTO detail) {
        this.detail = detail;
        if (detail != null){
            List<DetailFieldValueTO> newFields = new ArrayList<DetailFieldValueTO>();
            for (DetailFieldTO field : detail.getModel().getType().getSerialNumberFields()){
                newFields.add(new DetailFieldValueTO(field, this));
            }
            setFields(newFields);
        }
    }

    public List<DetailFieldValueTO> getFields() {
        return fields;
    }

    public void setFields(List<DetailFieldValueTO> fields) {
        this.fields = fields;
        fieldsInDisplayOrder = null;
    }

    public List<DetailFieldValueTO> getFieldsInDisplayOrder(){
        if (fieldsInDisplayOrder == null) {
            //At first request we build ordered collection of fields.
            fieldsInDisplayOrder = new ArrayList<DetailFieldValueTO>(fields);
            Collections.sort(fieldsInDisplayOrder, DetailFieldValueTO.DISPLAY_ORDER_COMPARATOR);
        }
        return fieldsInDisplayOrder;
    }

    @Override
    public String toString() {
        return "DetailSerialNumberTO{" +
                "id=" + id +
                ", detail=" + detail.toString() +
                ", fields=" + (fields != null ? Arrays.toString(fields.toArray()) : null) +
                ", fieldsInDisplayOrder=" + (fieldsInDisplayOrder != null ? Arrays.toString(fieldsInDisplayOrder.toArray()) : null) +
                '}';
    }
}
