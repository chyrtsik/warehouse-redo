/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.reflect.fields.declared;

import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.utils.reflect.fields.ObjectFieldAccessor;

import java.lang.reflect.Method;

/**
 * Base class of accessor to an object field
 */
@SuppressWarnings("unchecked")
public abstract class DeclaredFieldAccessorBase implements ObjectFieldAccessor {
    protected Method fieldGetter; //Getter of the field, managed by accessor.
    protected Method fieldSetter; //Setter of the field, manager by acessor.
    protected String fieldName;   //Name of field (for error reporting purpose) .
    protected ObjectFieldAccessor nestedAccessor; //If not set, than accessor is the last accessor in the accessor's hirarchy.
    
    public DeclaredFieldAccessorBase(Class objectClass, String fieldName) throws IllegalArgumentException {
        this.fieldName = fieldName;

        try {
            this.fieldGetter = objectClass.getMethod(makeGetterName(fieldName));
            this.fieldGetter.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }

        try {
            this.fieldSetter = objectClass.getMethod(makeSetterName(fieldName), String.class);
            this.fieldSetter.setAccessible(true);
        } catch (NoSuchMethodException e) {
            this.fieldSetter = null;
        }
    }

    private String makeGetterName(String fieldName) {
        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
        if (getterName.endsWith("[]")) {
            getterName = getterName.substring(0, getterName.length() - 2);
        }
        return getterName;
    }

    private String makeSetterName(String fieldName) {
        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
        if (setterName.endsWith("[]")) {
            setterName = setterName.substring(0, setterName.length() - 2);
        }
        return setterName;
    }

    public void setNestedAccessor(ObjectFieldAccessor nestedAccessor) {
        this.nestedAccessor = nestedAccessor;
    }

    @Override
    public Object getFieldValue(Object object, int itemIndex) {
        if (object == null){
            return null;
        }

        if (nestedAccessor == null) {
            return doGetFieldValue(object, itemIndex);
        } else {
            return nestedAccessor.getFieldValue(doGetFieldValue(object, itemIndex), itemIndex);
        }
    }

    @Override
    public void setFieldValue(Object object, int itemIndex, Object value) {
        if (nestedAccessor == null) {
            doSetFieldValue(object, itemIndex, value);
        } else {
            nestedAccessor.setFieldValue(doGetFieldValue(object, itemIndex), itemIndex, value);
        }
    }

    protected Object invokeGetter(Object object) {
        try {
            return fieldGetter.invoke(object);
        } catch (Exception e) {
            String message = "Cannot get value of the field \"" + fieldName + "\". Reason: " + e.getMessage();
            LoggingFacade.logError(this, message, e);
            throw new RuntimeException(message, e);
        }
    }

    protected void invokeSetter(Object object, Object value) {
        try {
            fieldSetter.invoke(object, value);
        } catch (Exception e) {
            String message = "Cannot set value of the field \"" + fieldName + "\". Reason: " + e.getMessage();
            LoggingFacade.logError(this, message, e);
            throw new RuntimeException(message, e);
        }
    }

    @Override
    public Class getFieldValueClass(){
        if (nestedAccessor == null){
            return doGetFieldValueClass();
        }
        else{
            return nestedAccessor.getFieldValueClass();
        }
    }

    protected abstract Class doGetFieldValueClass();

    protected abstract Object doGetFieldValue(Object object, int itemIndex);

    protected abstract void doSetFieldValue(Object object, int itemIndex, Object value);
}
