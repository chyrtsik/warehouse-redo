/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.reflect.fields.declared;

/**
 * @author Shyrik, 27.02.2009
 */

/**
 * Accessor to the field, that is an item with given index of some container.
 * Used for field mapping strings, such as: "obj[1].obj.name". Class ArrayItemFieldAccessor
 * will be used for providing access to first the field in given example.
 * This class simply uses ArrayFieldAccessor with predefined index, that, according to an
 * axample, will be 1.
 */
public class ArrayItemFieldAccessor extends DeclaredFieldAccessorBase {
    /**
     * Index of the container's item, to which this field provides access.
     */
    private int arrayItemIndex;

    /**
     * Acessor, that is used to access container items.
     */
    private ArrayFieldAccessor arrayAccessor;

    public ArrayItemFieldAccessor(Class objectClass, String fieldName) {
        super(objectClass, parseFieldName(fieldName));
        this.arrayItemIndex = parseItemIndex(fieldName);
        this.arrayAccessor = new ArrayFieldAccessor(objectClass, parseFieldName(fieldName) + "[]");
    }

    private static String parseFieldName(String fieldName) {
        //Extracting o field name from the field full name (template is "field[index]").
        return fieldName.substring(0, fieldName.indexOf("["));
    }

    private int parseItemIndex(String fieldName) {
        //Parsing a number from the field name (template is "field[index]").
        return Integer.valueOf(fieldName.substring(fieldName.indexOf("[") + 1, fieldName.indexOf("]")));
    }

    @Override
    public int getItemsCount(Object object) {
        return 1;
    }

    @Override
    public Class doGetFieldValueClass() {
        return arrayAccessor.getFieldValueClass();
    }

    @Override
    protected Object doGetFieldValue(Object object, int itemIndex) {
        return arrayAccessor.getFieldValue(object, arrayItemIndex);
    }

    @Override
    protected void doSetFieldValue(Object object, int itemIndex, Object value) {
        arrayAccessor.setFieldValue(object, arrayItemIndex, value);
    }
}
