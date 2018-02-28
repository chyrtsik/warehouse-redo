/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.reflect;

import com.artigile.warehouse.utils.reflect.fields.ObjectFieldAccessor;
import com.artigile.warehouse.utils.reflect.fields.declared.ArrayItemFieldAccessor;
import com.artigile.warehouse.utils.reflect.fields.declared.DeclaredFieldAccessorBase;
import com.artigile.warehouse.utils.reflect.fields.declared.SingleValueFieldAccessor;

import java.util.StringTokenizer;

/**
 * @author Shyrik, 06.12.2008
 */

/**
 * This class same to the ObjectFieldProvider, but is doesn't support
 * array fields and doesnt linked to the single object.
 */
public class SimpleObjectsFieldsProvider {
    /**
     * Accessors for object fields. Ancessors are ordered in the same order as
     * the objects fields, passes sto the constructor
     */
    private ObjectFieldAccessor[] objectFieldsAccessors;

    /**
     * Class, to which field access is being provided.
     */
    private Class objectClass;

    /**
     * Constructor
     * @param objectClass - class of all of the objects, that will be accessed through this provider
     * @param objectFields - names of the fields to be provided
     */
    public SimpleObjectsFieldsProvider(Class objectClass, String[] objectFields) {
        this.objectClass = objectClass;
        this.objectFieldsAccessors = new ObjectFieldAccessor[objectFields.length];

        //Creating accessors for fields, specified by their names
        for (int i = 0; i < objectFields.length; i++) {
            ObjectFieldAccessor accessor = createFieldAccessor(objectClass, objectFields[i]);
            objectFieldsAccessors[i] = accessor;
        }
    }

    /**
     * Returns class of the value by field index.
     * @param fieldIndex
     * @return
     */
    public Class getFieldValueClass(int fieldIndex) {
        return objectFieldsAccessors[fieldIndex].getFieldValueClass();
    }

    /**
     * Returns a value of the field. Field is referenced by it's index.
     * @param object - object to what's field access is now performed.
     * @param fieldIndex - index of the field needed.
     * @return
     */
    public Object getFieldValue(Object object, int fieldIndex) {
        return objectFieldsAccessors[fieldIndex].getFieldValue(object, 0);
    }

    /**
     * Sets value of the field. Field is referenced by it's index.
     * @param object - object to what's field access is now performed.
     * @param fieldIndex - index of the field.
     * @param value - new value of the field.
     */
    public void setFieldValue(Object object, int fieldIndex, Object value) {
        objectFieldsAccessors[fieldIndex].setFieldValue(object, 0, value);
    }

    /**
     * Use this method to check, if the fields provider is able to provide access to the concrete object.
     * @param object
     * @return
     */
    public boolean canProcessObject(Object object) {
        return objectClass.isInstance(object);
    }

    // ==================== Impementation ==================================
    private ObjectFieldAccessor createFieldAccessor(Class objectClass, String objectField) {
        StringTokenizer tokenizer = new StringTokenizer(objectField, ".", false);
        if (!tokenizer.hasMoreTokens()) {
            throw new IllegalArgumentException("Invalid object field name");
        }

        //Creating a hierarchy of accessors
        DeclaredFieldAccessorBase rootAccessor = createAccessor(objectClass, tokenizer.nextToken());
        DeclaredFieldAccessorBase lastAccessor = rootAccessor;
        while (tokenizer.hasMoreTokens()) {
            DeclaredFieldAccessorBase nestedAccessor = createAccessor(lastAccessor.getFieldValueClass(), tokenizer.nextToken());
            lastAccessor.setNestedAccessor(nestedAccessor);
            lastAccessor = nestedAccessor;
        }

        return rootAccessor;
    }

    private DeclaredFieldAccessorBase createAccessor(Class objectClass, String fieldName) {
        if (isArrayItemField(fieldName)){
            return new ArrayItemFieldAccessor(objectClass, fieldName);
        }
        else{
            return new SingleValueFieldAccessor(objectClass, fieldName);
        }
    }

    private boolean isArrayItemField(String fieldName) {
        return fieldName.matches("\\S+\\[\\d+\\]");
    }
}
