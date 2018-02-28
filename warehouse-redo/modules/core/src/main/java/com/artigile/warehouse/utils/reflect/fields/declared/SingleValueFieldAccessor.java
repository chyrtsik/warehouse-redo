/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.reflect.fields.declared;

/**
 * Accessor to a simple-value field (non array)
 */
public class SingleValueFieldAccessor extends DeclaredFieldAccessorBase {
    public SingleValueFieldAccessor(Class objectClass, String fieldName) {
        super(objectClass, fieldName);
    }

    @Override
    protected Object doGetFieldValue(Object object, int itemIndex) {
        return invokeGetter(object);
    }

    @Override
    protected void doSetFieldValue(Object object, int itemIndex, Object value){
        invokeSetter(object, value);
    }

    @Override
    public int getItemsCount(Object object) {
        return 1;
    }

    @Override
    public Class doGetFieldValueClass() {
        return fieldGetter.getReturnType();
    }
}
