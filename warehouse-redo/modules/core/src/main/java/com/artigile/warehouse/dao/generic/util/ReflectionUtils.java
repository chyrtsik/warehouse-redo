/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao.generic.util;

import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author IoaN, 22.11.2008
 */
@SuppressWarnings("unchecked")
public class ReflectionUtils {

    /**
     * Invokes method using reflection mechanism. It is assumed that user knows that method
     * exists in the class and is accessible, and so NoSuchMethodException,
     * IllegalArgumentException, IllegalAccessException and SecurityException that
     * are likely to happen are swallowed.
     *
     * @param object      Object that owns the method.
     * @param objectClass Class of the object.
     * @param methodName  Name of the method to invoke.
     * @param parameters  Method parameters.
     * @return result of invocation
     * @throws java.lang.reflect.InvocationTargetException
     *          if the underlying method
     *          throws an exception.
     */
    public static Object invokeMethod(Object object, Class objectClass,
                                      String methodName, Parameter... parameters) throws InvocationTargetException {
        Object result = null;
        try {
            Class[] parameterTypes = getParameterTypes(parameters);
            Object[] parameterValues = getParameterValues(parameters);
            Method method = objectClass.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            result = method.invoke(object, parameterValues);
        } catch (SecurityException e) {
            LoggingFacade.logError(
                String.format("Security exception on invoking method [%1$s.%2$s]",
                    object.getClass().getName(), methodName), e
            );
        } catch (NoSuchMethodException e) {
            LoggingFacade.logError(
                String.format("Method [%1$s] is not found in class %2$s",
                methodName, object.getClass().getName()), e
            );
        } catch (IllegalArgumentException e) {
            LoggingFacade.logError(
                String.format("Illegal argument exception on invoking method [%1$s.%2$s]",
                object.getClass().getName(), methodName), e
            );
        } catch (IllegalAccessException e) {
            LoggingFacade.logError(
                String.format("Illegal access exception on invoking method [%1$s.%2$s]",
                object.getClass().getName(), methodName), e
            );
        }
        return result;
    }

    /**
     * Obtains value of the field using reflection mechanism. It is assumed that user
     * knows that field exists in the class and is accessible, and so NoSuchFieldException,
     * IllegalAccessException and SecurityException that
     * are likely to happen are swallowed.
     *
     * @param object    Object that owns the field.
     * @param fieldName Name of the field
     * @return value that the field contains
     */
    public static Object getFieldValue(Object object, String fieldName) {
        Object result = null;
        boolean isFound = false;
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                isFound = true;
                field.setAccessible(true);
                try {
                    result = field.get(object);
                } catch (IllegalArgumentException e) {
                    LoggingFacade.logError(
                        String.format("Illegal argument exception on retrieving value of field [%1$s] of class %2$s",
                            fieldName, object.getClass().getName()), e
                    );
                } catch (IllegalAccessException e) {
                    LoggingFacade.logError(
                        String.format("Illegal access exception on retrieving value of field [%1$s] of class %2$s",
                            fieldName, object.getClass().getName()), e
                    );
                }
            }
        }
        if (!isFound) {
            LoggingFacade.logError(
                String.format("Field [%1$s] is not found in class %2$s",
                    fieldName, object.getClass().getName())
            );
        }
        return result;
    }

    /**
     * Obtains value of the entity type using reflection mechanism for given
     * generic type object. It is assumed that user knows that given object
     * class parameterized only by entity type.
     *
     * @param object -
     *               the given generic type object that parameterized only by
     *               entity type
     * @return value of the entity type for given generic type object.
     */
    public static Type getEntityType(Object object) {

        Type type = object.getClass().getGenericSuperclass();

        if (type instanceof ParameterizedType) {

            ParameterizedType paramType = (ParameterizedType) type;
            return paramType.getActualTypeArguments()[0];
        } else {

            throw new IllegalArgumentException("Could not guess entity type by reflection.");
        }
    }

    /**
     * Returns collection of all fields declared by the class of the given
     * object.
     *
     * @param object the given object that type used in order to retrieve
     *               collection of all declared fields
     * @return collection of all fields declared by the class of the given
     *         object
     */
    public static Collection<Field> getDeclaredFields(Object object) {

        Class<?> clazz = object.getClass();
        List<Field> fields = new ArrayList<Field>();

        do {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        } while (clazz != null);

        return fields;
    }

    private static Class[] getParameterTypes(Parameter... parameters) {
        Class[] parameterTypes = new Class[parameters.length];
        int counter = 0;
        for (Parameter parameter : parameters) {
            parameterTypes[counter] = parameter.getClazz();
            counter++;
        }
        return parameterTypes;
    }

    private static Object[] getParameterValues(Parameter... parameters) {
        Object[] parameterValues = new Object[parameters.length];
        int counter = 0;
        for (Parameter parameter : parameters) {
            parameterValues[counter] = parameter.getValue();
            counter++;
        }
        return parameterValues;
    }

    public static class Parameter {
        /**
         * Parameter value.
         */
        private Object value;

        /**
         * Parameter class.
         */
        private Class clazz;

        /**
         * Constructor.
         *
         * @param value Parameter value.
         * @param clazz Parameter class.
         */
        public Parameter(Object value, Class clazz) {
            this.value = value;
            this.clazz = clazz;
        }

        /**
         * Gets a value of 'value' property.
         *
         * @return the value of 'value' property
         */
        public Object getValue() {
            return value;
        }

        /**
         * Gets a value of 'clazz' property.
         *
         * @return the value of 'clazz' property
         */
        public Class getClazz() {
            return clazz;
        }

    }

}
