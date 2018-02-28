/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.reflect;


import com.artigile.warehouse.utils.reflect.fields.ObjectFieldAccessor;
import com.artigile.warehouse.utils.reflect.fields.declared.ArrayFieldAccessor;
import com.artigile.warehouse.utils.reflect.fields.declared.SingleValueFieldAccessor;
import com.artigile.warehouse.utils.reflect.fields.dynamic.DynamicFieldAccessor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Shyrik
 * Date: 25.11.2008
 * Time: 20:33:23
 */

/**
 * Class provides access to fields of the objects of the given class and objects,
 * linked with these objects by string that is called "field name".
 * <p/>
 * The features of the fields naming are listed below:
 * <ol>
 * <li> Property of the object must be adderessed by property name inself. For example,
 * "name" means "object.getName".
 * </li>
 * <li> Property of any linked object must be addressed by property name, representing
 * linked object, and property name of the linked object. For example, "customer.address.city"
 * means "object.getCustomer().getAddress().getCity()".
 * </li>
 * <li>Array property must be addressed by property name with "[]". For example,
 * "articles[].name" means "object.getArticles()[itemIndex].getName()". Variable "itemIndex" generated
 * automatically and represents the index of the item needed.
 * </li>
 * </ol>
 * <p/>
 * Implementation of this class imposes some restrictions on the printable objects:
 * <ol>
 * <li>Properties of printable object must have getters.</li>
 * <li>If printable object has more, than one array property for printing, size of
 * arrays must be equal. For example, if object "order" contains properties "articles"
 * (goods for buying) and "notices" (additional information about every article in order),
 * class thinks, that every "article" has a corresponding "notice" and vise versa.</li>
 * </ol>
 */
public class ObjectFieldsProvider {
    private Object object; //Object, access to what fields is provided
    private ObjectFieldAccessor[] objectFieldsAccessors; //Accessors for object fields. Ancessors are ordered in the same order as the objects fields, passes sto the constructor
    private int itemsCount; //Maximun cound of items, provided by given object

    //Pattern for parsing fields in access string.
    private static final Pattern fieldPattern = java.util.regex.Pattern.compile("(\\{[^\\}]+\\})|([^\\.]+)");

    public ObjectFieldsProvider(Object object, List<String> objectFields) {
        this.object = object;
        this.objectFieldsAccessors = new ObjectFieldAccessor[objectFields.size()];
        this.itemsCount = 1;

        //Creating accessors for fields, specified by their names
        for (int i = 0; i < objectFields.size(); i++) {
            ObjectFieldAccessor accessor = createFieldAccessor(object, objectFields.get(i));
            int accessorItemsCount = accessor.getItemsCount(object);
            if (accessorItemsCount > 1) {
                if (itemsCount > 1 && itemsCount != accessorItemsCount) {
                    throw new IllegalArgumentException("All arrays in the data set should contain the same number of items.");
                }
                if (itemsCount == 1) {
                    itemsCount = accessorItemsCount;
                }
            }
            objectFieldsAccessors[i] = accessor;
        }
    }

    private ObjectFieldAccessor createFieldAccessor(Object object, String objectField) {
        Matcher fieldMatcher = fieldPattern.matcher(objectField);
        if (!fieldMatcher.find()){
            throw new IllegalArgumentException("Invalid object field name: " + objectField);
        }

        //Creating a hierarchy of accessors
        ObjectFieldAccessor rootAccessor = createAccessor(object.getClass(), fieldMatcher.group());
        ObjectFieldAccessor lastAccessor = rootAccessor;
        while (fieldMatcher.find()) {
            ObjectFieldAccessor nestedAccessor = createAccessor(lastAccessor.getFieldValueClass(), fieldMatcher.group());
            lastAccessor.setNestedAccessor(nestedAccessor);
            lastAccessor = nestedAccessor;
        }

        return rootAccessor;
    }

    private ObjectFieldAccessor createAccessor(Class objectClass, String fieldName) {
        if (isArrayField(fieldName)) {
            return new ArrayFieldAccessor(objectClass, fieldName);
        }
        else if (isDynamicField(fieldName)){
            return new DynamicFieldAccessor(fieldName);
        }
        else {
            return new SingleValueFieldAccessor(objectClass, fieldName);
        }
    }

    private boolean isArrayField(String fieldName) {
        return fieldName.endsWith("[]");
    }

    private boolean isDynamicField(String fieldName) {
        return ObjectFieldsProviderUtils.isDynamicFieldFormatted(fieldName);
    }

    public Object getFieldValue(int fieldIndex, int itemIndex) {
        return objectFieldsAccessors[fieldIndex].getFieldValue(object, itemIndex);
    }

    public Class getFieldValueClass(int fieldIndex) {
        return objectFieldsAccessors[fieldIndex].getFieldValueClass();
    }

    public int getItemsCount() {
        return itemsCount;
    }
}

