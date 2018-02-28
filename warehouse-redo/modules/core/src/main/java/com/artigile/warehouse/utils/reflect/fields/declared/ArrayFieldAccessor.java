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

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Accessor to a field, that is container of elements (array, List<...> and etc.).
 */
public class ArrayFieldAccessor extends DeclaredFieldAccessorBase {
    /**
     * Access strategy to items in array. Implementation depends on type of container.
     */
    private ContainerAccessStrategy accessStrategy;

    private interface ContainerAccessStrategy {
        public Object doGetFieldValue(Object object, int itemIndex);
        public void doSetFieldValue(Object object, int itemIndex, Object value);
        public int getItemsCount(Object object);
        public Class getFieldValueClass();
    }

    public ArrayFieldAccessor(Class objectClass, String fieldName) {
        super(objectClass, fieldName);

        if (List.class.isAssignableFrom(fieldGetter.getReturnType())){
            //Container is some king of List.
            accessStrategy = new ContainerAccessStrategy() {
                public Object doGetFieldValue(Object object, int itemIndex) {
                    List list = (List)invokeGetter(object);
                    return (list == null || list.size() == 0) ? null : list.get(itemIndex);
                }
                @SuppressWarnings("unchecked")
                public void doSetFieldValue(Object object, int itemIndex, Object value) {
                    List list = (List)invokeGetter(object);
                    if (list != null){
                        list.set(itemIndex, value);
                    }
                }
                public int getItemsCount(Object object) {
                    List list = (List)invokeGetter(object);
                    return list == null ? 1 : list.size();
                }
                public Class getFieldValueClass() {
                    ParameterizedType retType = (ParameterizedType)fieldGetter.getGenericReturnType();
                    return (Class)retType.getActualTypeArguments()[0];
                }
            };
        }
        else{
            //container considered to be plain Java array.
            accessStrategy = new ContainerAccessStrategy() {
                public Object doGetFieldValue(Object object, int itemIndex) {
                    Object[] array = (Object[]) invokeGetter(object);
                    return array.length == 0 ? null : array[itemIndex];
                }
                public void doSetFieldValue(Object object, int itemIndex, Object value) {
                    ((Object[]) invokeGetter(object))[itemIndex] = value;
                }
                public int getItemsCount(Object object) {
                    return ((Object[]) invokeGetter(object)).length;
                }
                public Class getFieldValueClass() {
                    //Returning class of array's item
                    return fieldGetter.getReturnType().getComponentType();
                }
            };
        }
    }

    @Override
    protected Object doGetFieldValue(Object object, int itemIndex) {
        return accessStrategy.doGetFieldValue(object, itemIndex);
    }

    @Override
    protected void doSetFieldValue(Object object, int itemIndex, Object value){
        accessStrategy.doSetFieldValue(object, itemIndex, value);
    }

    @Override
    public int getItemsCount(Object object) {
        return accessStrategy.getItemsCount(object);
    }

    @Override
    public Class doGetFieldValueClass() {
        return accessStrategy.getFieldValueClass();
    }
}
