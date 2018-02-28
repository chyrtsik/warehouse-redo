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

import com.artigile.warehouse.bl.detail.DetailModelService;
import com.artigile.warehouse.domain.details.DetailPredefinedFieldType;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.parser.ParsedTemplate;
import com.artigile.warehouse.utils.parser.ParsedTemplateDataSource;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Object, that represents full detail data for the UI code.
 * @author Shyrik, 21.12.2008
 */
public class DetailModelTO extends EqualsByIdImpl implements DetailFieldOwner, ParsedTemplateDataSource {
    private long id;

    /**
     * Type of the detail.
     */
    private DetailTypeTO type;

    /**
     * User configured fields of the detail model (with values).
     */
    private List<DetailFieldValueTO> fields = new ArrayList<DetailFieldValueTO>();

    /**
     * List of fields, ordered in the display order of fields (may be different to natural field order).
     */
    private List<DetailFieldValueTO> fieldsInDisplayOrder = null;


    //========================= Manipulators ===================================

    /**
     * Makes a copy of the detail model.
     * @return
     */
    public void copyFrom(DetailModelTO src){
        setId(src.getId());
        setType(src.getType());
        fields.clear();
        for (DetailFieldValueTO field: src.getFields()){
            fields.add(new DetailFieldValueTO(field.getType(), this, field.getValue()));
        }
    }

    /**
     * Checks, if given detail model is similar to this model.
     * @param secondModel
     * @return
     */
    public boolean isSameModel(DetailModelTO secondModel) {
        if (getType().getId() != secondModel.getType().getId()){
            return false;
        }
        for (int i=0; i<getFields().size(); i++){
            DetailFieldValueTO field = getFields().get(i);
            DetailFieldValueTO secondField = secondModel.getFields().get(i);
            if (!field.getValue().equals(secondField.getValue())){
                return false;
            }
        }
        return true;
    }

    //========================== Model field owner implementation ==================
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
        DetailModelService modelsService = SpringServiceContext.getInstance().getDetailModelsService();
        return modelsService.isUniqueFieldValue(this, field.getType(), value);
    }

    @Override
    public String calculateTemplate(String template) {
        ParsedTemplate parsedTemplate = type.parseTemplate(template, this);
        return parsedTemplate.calculate(this);
    }


    //========================= ParsedTemplateDataSource implementation ==============================
    @Override
    public int getFieldIndexByName(String fieldName) {
        for (DetailFieldValueTO field : fields){
            if (field.getType().getName().equals(fieldName)){
                Integer fieldIndex = field.getType().getFieldIndex();
                return fieldIndex == null ? 0 : fieldIndex;
            }
        }
        return -1;
    }

    @Override                                                                                                                 
    public String getFieldValue(int index) {
        return getFieldByIndex(index).getDisplayValue();
    }

    public String getFieldActualValue(int index) {
        return getFieldByIndex(index).getValue();
    }

    private DetailFieldValueTO getFieldByIndex(int index) {
        for (DetailFieldValueTO field : fields){
            Integer fieldIndex = field.getType().getFieldIndex();
            if (fieldIndex == null && index == 0 || fieldIndex != null && fieldIndex == index){
                return field;
            }
        }
        throw new IllegalArgumentException(MessageFormat.format("Field index {0} is illegal. Detail model id: {1}", index, id));
    }

    //========================= Gettters and setters ===========================

    /**
     * Init list fields of the detail to comform given detail type.
     * @param newType - new detail type
     */
    public void setType(DetailTypeTO newType){
        if (type != null && type.getId() == newType.getId()){
            return;
        }

        type = newType;

        List<DetailFieldValueTO> newFields = new ArrayList<DetailFieldValueTO>();
        for (DetailFieldTO field : type.getFields()){
            newFields.add(new DetailFieldValueTO(field, this));
        }
        setFields(newFields);
    }

    public DetailTypeTO getType(){
        return type;        
    }

    public boolean isNew(){
        return id == 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getName() {
        for (DetailFieldValueTO field : fields){
            if (DetailPredefinedFieldType.NAME.equals(field.getType().getPredefinedType())){
                return field.getValue();
            }
        }
        return I18nSupport.message("detail.type.error");
    }

    public List<DetailFieldValueTO> getFields() {
        return fields;
    }

    public void setFields(List<DetailFieldValueTO> fields){
        this.fields = fields;
        fieldsInDisplayOrder = null; //We need to refresh this list.
    }

    public List<DetailFieldValueTO> getFieldsInDisplayOrder(){
        if (fieldsInDisplayOrder == null) {
            //At first request we build ordered collection of fields.
            fieldsInDisplayOrder = new ArrayList<DetailFieldValueTO>(fields);
            Collections.sort(fieldsInDisplayOrder, DetailFieldValueTO.DISPLAY_ORDER_COMPARATOR);
        }
        return fieldsInDisplayOrder;
    }

    public DetailFieldValueTO getFieldByName(String fieldName) {
        for (DetailFieldValueTO field : fields){
            if (field.getType().getName().equals(fieldName)){
                return field;
            }
        }
        return null;
    }

    public boolean validate() {
        for (DetailFieldValueTO field : fields){
            if (!field.isCalculated() && !field.validateValue(field.getValue())){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "DetailModelTO{" +
                "id=" + id +
                ", type=" + type +
                ", fields=" + (fields != null ? Arrays.toString(fields.toArray()) : null) +
                ", fieldsInDisplayOrder=" + (fieldsInDisplayOrder != null ? Arrays.toString(fieldsInDisplayOrder.toArray()) : null) +
                '}';
    }
}
