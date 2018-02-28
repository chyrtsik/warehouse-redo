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

import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author Shyrik, 14.12.2008
 */

/**
 * Types of the detail fields (type of the expected field value).
 */
public enum DetailFieldType {
    /**
     * Field is number.
     */
    NUMBER("detail.field.properties.type.number", true, true),

    /**
     * Field is an integer number (long in java).
     */
    INTEGER_NUMBER("detail.field.properties.type.integer.number", true, true),

    /**
     * Field is boolean flag.
     */
    BOOLEAN("detail.field.properties.type.boolean", true, true),

    /**
     * Field is text.
     */
    TEXT("detail.field.properties.type.text", true, true),

    /**
     * Field is enumeration (limited set of values).
     */
    ENUM("detail.field.properties.type.enum", true, true),

    /**
     * Field is text, calculated with the help of the text template.
     */
    TEMPLATE_TEXT("detail.field.properties.type.template", true, true),

    /**
     * Field is date.
     */
    DATE("detail.field.properties.type.date", true, true),

    /**
     * Shelf life of product. When set this field can be used by system for shelf life calculations.
     */
    SHELF_LIFE("detail.field.properties.type.shelf.life", false, false),

    /**
     * Number of product items in once packaging. When set may be used by a system to calculate number of items in stock.
     */
    COUNT_IN_PACKAGING("detail.field.properties.type.count.in.packaging", false, false),

    /**
     * Current date. Read only calculated field to mark product with date when is was created.
     */
    CURRENT_DATE("detail.field.properties.type.current.date", false, false),

    /**
     * Current time. Read only calculated field to mark product with time in seconds when is was created.
     */
    CURRENT_TIME("detail.field.properties.type.current.time", false, false),

    /**
     * Current user. Read only field to store record about the user created this concrete item (applicable to serial numbers).
     */
    CURRENT_USER("detail.field.properties.type.current.user", false, false);

    private String name;
    private boolean unlimitedNumberOfFields;
    private boolean editableByUser;

    private DetailFieldType(@PropertyKey(resourceBundle = "i18n.warehouse") String nameRes,
                            boolean unlimitedNumberOfFields, boolean editableByUser){
        this.name = I18nSupport.message(nameRes);
        this.unlimitedNumberOfFields = unlimitedNumberOfFields;
        this.editableByUser = editableByUser;
    }

   /**
     * @return user friendly name of field type.
     */
    public String getName() {
        return name;
    }

    /**
     * @return true when any number of fields of this type can exist and false if only one field of such type is allowed.
     */
    public boolean isUnlimitedNumberOfFields() {
        return unlimitedNumberOfFields;
    }

    /**
     * @return true is field is editable by user and false if field is calculated by system some how.
     */
    public boolean isEditableByUser() {
        return editableByUser;
    }
}
