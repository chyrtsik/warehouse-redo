package com.artigile.warehouse.utils.reflect.fields;

/**
 * Interface for accessing values of object fields.
 *
 * @author Aliaksandr.Chyrtsik, 19.12.12
 */
public interface ObjectFieldAccessor {
    /**
     * Retrieve field value.
     * @param object object instance which field should be retrieved.
     * @param itemIndex index of item in the list (ignored for non array fields).
     * @return value of object field.
     */
    Object getFieldValue(Object object, int itemIndex);

    /**
     * Set values of object field.
     * @param object object instancte which fields should be modified.
     * @param itemIndex index of object in the list (ignored for non array fields).
     * @param value value to be set.
     */
    void setFieldValue(Object object, int itemIndex, Object value);

    /**
     * Retrieve class of object field value.
     * @return class of value.
     */
    Class getFieldValueClass();

    /**
     * Retrieve count of items (if this field is array). Not 1 only for array fields and 1 for all other non array fields.
     * @param object object instance which count to examine.
     * @return count of items.
     */
    int getItemsCount(Object object);

    /**
     * Set nested accessor to be used. So that actual field value will be the value of it's nested accessor applied to
     * values returned by field.
     * @param nestedAccessor nested accessor.
     */
    void setNestedAccessor(ObjectFieldAccessor nestedAccessor);
}
