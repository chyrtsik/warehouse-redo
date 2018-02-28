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

import com.artigile.warehouse.domain.details.DetailFieldType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.menuitems.details.types.DetailTypesUtils;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.StringUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;

/**
 * Data of one field dynamic field. It holds value and references field to to calculate and present value properly.
 * @author Shyrik, 21.12.2008
 */

public class DetailFieldValueTO implements Cloneable {
    /**
     * Sorting or fields support.
     */
    public static Comparator<DetailFieldValueTO> DISPLAY_ORDER_COMPARATOR = new Comparator<DetailFieldValueTO>(){
        @Override
        public int compare(DetailFieldValueTO first, DetailFieldValueTO second) {
            Long firstDisplayOrder = first.getType().getDisplayOrder();
            Long secondDisplayOrder = second.getType().getDisplayOrder();
            if (firstDisplayOrder == null && secondDisplayOrder == null) {
                return 0;
            }
            else if (firstDisplayOrder == null) {
                return -1;
            }
            else if (secondDisplayOrder == null) {
                return 1;
            }
            else {
                return firstDisplayOrder.compareTo(secondDisplayOrder);
            }
        }
    };

    /**
     * Owner of the field, to what this value belongs to.
     */
    private DetailFieldOwner fieldOwner;

    /**
     * Type of the field.
     */
    private DetailFieldTO type;

    /**
     * Value of the field.
     */
    private String value;

    /**
     * True, when calculated field value has been calculated and stored in value field.
     */
    private boolean valueCalculated;

    //==================== Constructors ===================================
    public DetailFieldValueTO(DetailFieldTO type, DetailFieldOwner fieldOwner, String value){
        this.type = type;
        this.fieldOwner = fieldOwner;
        this.value = (value == null) ? "" : value; //No null values in the presentation tier.
    }

    public DetailFieldValueTO(DetailFieldTO type, DetailFieldOwner fieldOwner){
        this.type = type;
        this.fieldOwner = fieldOwner;
        this.value = "";
    }

    @Override
    public DetailFieldValueTO clone() {
        return new DetailFieldValueTO(type, fieldOwner, value);
    }

    //=================== Getters and setters =============================
    public DetailFieldTO getType() {
        return type;
    }

    /**
     * Returns internal representation of value. This value is used for controls, may
     * be stored in database, but it not for displaying to user.
     * @return
     */
    public String getValue() {
        if (isCalculated()) {
           //Field is calculated. Returns only calculated value.
           return calculateValue();
        }
        else {
            //Plain value.
            return value;
        }
    }

    /**
     * Returns number representation of value (used for numeric fields).
     * @return
     */
    public BigDecimal getValueNumber() {
        String stringValue = getValue();
        return StringUtils.isNumber(stringValue) ? StringUtils.parseStringToBigDecimal(stringValue) : null;
    }

    /**
     * Returns boolean representation of value (used for boolean fields).
     * @return
     */
    public Boolean getValueBoolean() {
        String stringValue = getValue();
        return DetailTypesUtils.getStringAsBoolean(stringValue);
    }

    /**
     * Returns name of field for strict typified field value (getter for such field returns value of type,
     * according to the declared type of field (text, number of boolean)).
     * @return
     */
    public String getTypifiedValueFieldName() {
        DetailFieldType valueType = type.getType();
        if (valueType == DetailFieldType.NUMBER ||
            valueType == DetailFieldType.INTEGER_NUMBER ||
            valueType == DetailFieldType.COUNT_IN_PACKAGING)
        {
            return "valueNumber"; //getValueNumber
        }
        else if (valueType == DetailFieldType.CURRENT_USER){
            return "displayValue";
        }
        else {
            return "value"; //getValue
        }
    }

    /**
     * Returns value, that are intended to be shown to user. It may differ from internal
     * representation of this value.
     * @return user friendly version of a value.
     */
    public String getDisplayValue() {
        return getDisplayValue(getValue(), type.getType(), null, false);
    }

    public static String getDisplayValue(String value, DetailFieldType detailFieldType, String defaultValue, boolean showValue) {
        if (detailFieldType == DetailFieldType.BOOLEAN) {
            if (showValue) {
                return value != null ? value : defaultValue;
            }
            else {
                //Boolean field is displayed as the name of the field, if it is set.
                Boolean boolValue = DetailTypesUtils.getStringAsBoolean(value);
                return (boolValue == null || !boolValue) ? "" : detailFieldType.getName();
            }
        }
        else if (detailFieldType == DetailFieldType.CURRENT_USER) {
            //We display readable name of user instead of user identifier.
            return DetailTypesUtils.getUserDisplayName(value);
        }

        //By default display value is the same as it's internal implementation.
        return value != null ? value : defaultValue;
    }

    public void setValue(String newValue) {
        if (isCalculated()) {
            //Set value of the calculated field is a non sense.
        }
        else {
            value = newValue;
            fieldOwner.onValueChanged(this);
        }
    }

    /**
     * Check if given value is valid to be stored in this field.
     * @param fieldValue value to be checked.
     * @return true if value is valid and false if not.
     */
    public boolean validateValue(String fieldValue) {
        //1. General checks.
        if (isCalculated()) {
            //Values are not allowed to be for calculated fields at all.
            return false;
        }
        else if (!StringUtils.hasValue(fieldValue)) {
            //Empty values are allowed for non mandatory fields only.
            return !type.getMandatory();
        }

        //2. Value format check (depending on concrete type).
        DetailFieldType fieldType = type.getType();
        if (fieldType.equals(DetailFieldType.TEXT)) {
            return fieldValue.length() <= ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH;
        }
        else if (fieldType.equals(DetailFieldType.NUMBER)) {
            return StringUtils.isNumber(fieldValue);
        }
        else if (fieldType.equals(DetailFieldType.INTEGER_NUMBER)) {
            return StringUtils.isNumberLong(fieldValue);
        }
        else if (fieldType.equals(DetailFieldType.BOOLEAN)) {
            try{
                DetailTypesUtils.getStringAsBoolean(fieldValue);
                return true;
            }
            catch (IllegalArgumentException e) {
                return false;
            }
        }
        else if (fieldType.equals(DetailFieldType.INTEGER_NUMBER)) {
            return StringUtils.parseDate(fieldValue) != null;
        }
        else if (fieldType.equals(DetailFieldType.ENUM)) {
            for (String value : type.getEnumValues()) {
                if (value.equalsIgnoreCase(fieldValue)) {
                    return true;
                }
            }
            return false;
        }
        else{
            throw new AssertionError("Forgot to place validation for new field type here? Field type: " + fieldType.name());
        }
    }

    //=========================== Helpers ===================================
    /**
     * Returns true, if given value of the field is unique.
     * @param value
     * @return
     */
    public boolean isValueUnique(String value) {
        return fieldOwner.isUniqueFieldValue(this, value);
    }

    /**
     * Used to inform the field, that if it's value is calculated, than is should be recalculated.
     */
    public void onNeedRecalculate() {
        valueCalculated = false;
    }

    private String calculateValue() {
        if (valueCalculated){
            //Caches calculated value.
            return value;
        }

        DetailFieldType fieldType = type.getType();
        if (fieldType == DetailFieldType.TEMPLATE_TEXT){
            value = fieldOwner.calculateTemplate(type.getTemplate());
        }
        else if (fieldType == DetailFieldType.CURRENT_DATE){
            value = StringUtils.hasValue(value) ? value : StringUtils.formatDate(Calendar.getInstance().getTime());
        }
        else if (fieldType == DetailFieldType.CURRENT_TIME){
            value = StringUtils.hasValue(value) ? value : StringUtils.formatTime(Calendar.getInstance().getTime());
        }
        else if (fieldType == DetailFieldType.CURRENT_USER){
            value = StringUtils.hasValue(value) ? value : String.valueOf(WareHouse.getUserSession().getUser().getId());
        }
        valueCalculated = true;
        return value;
    }

    public boolean isCalculated() {
        DetailFieldType fieldType = type.getType();
        return fieldType == DetailFieldType.TEMPLATE_TEXT || fieldType == DetailFieldType.CURRENT_DATE ||
               fieldType == DetailFieldType.CURRENT_TIME || fieldType == DetailFieldType.CURRENT_USER;
    }

    @Override
    public String toString() {
        return "DetailFieldValueTO{" +
                ", type=" + type +
                ", value='" + value + '\'' +
                ", valueCalculated=" + valueCalculated +
                '}';
    }
}
