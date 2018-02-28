package com.artigile.warehouse.utils.reflect.fields.dynamic;

import java.util.List;

/**
 * Interface of provider used to access objects with dynamic fields.
 * Dynamic means defined at runtime (at contrast to fields accessible via getters).
 *
 * @author Aliaksandr.Chyrtsik, 18.12.12
 */
public interface DynamicObjectFieldsProvider {
    /**
     * Get all available dynamic fields (user friendly names are returned).
     * @return names of all available fields.
     */
    List<String> getAvailableFields();

    /**
     * Get class of field value.
     * @param dynamicField dynamic field name.
     * @return class of field value.
     */
    Class getFieldValueClass(String dynamicField);

    /**
     * Get value of object dynamic field.
     * @param object object to retrieve field value.
     * @param dynamicField name of a field to get value.
     * @return value of a field (value type depends on concrete field).
     */
    Object getFieldValue(Object object, String dynamicField);

    /**
     * Set value of dynamic field.
     * @param object object which field needs to be updated.
     * @param dynamicField field to be updated.
     * @param value value to be set.
     */
    void setFieldValue(Object object, String dynamicField, Object value);
}
