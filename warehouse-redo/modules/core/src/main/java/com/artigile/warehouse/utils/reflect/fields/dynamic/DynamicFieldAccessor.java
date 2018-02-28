package com.artigile.warehouse.utils.reflect.fields.dynamic;

import com.artigile.warehouse.utils.reflect.ObjectFieldsProviderUtils;
import com.artigile.warehouse.utils.reflect.fields.ObjectFieldAccessor;

/**
 * This implementation accesses dynamic fields of object.
 *
 * @author Aliaksandr.Chyrtsik, 19.12.12
 */
public class DynamicFieldAccessor implements ObjectFieldAccessor {
    private DynamicObjectFieldsProvider provider;
    private String dynamicField;

    public DynamicFieldAccessor(String objectField) {
        String providerName = ObjectFieldsProviderUtils.parseDynamicFieldProviderName(objectField);
        provider = DynamicObjectFieldsProviderFactory.getInstance().createProvider(providerName);
        if (provider == null){
            throw new IllegalArgumentException("Cannot field dynamic field provider specified. Field: " + objectField);
        }

        dynamicField = ObjectFieldsProviderUtils.parseDynamicFieldName(objectField);
        if (dynamicField == null){
            throw new IllegalArgumentException("Invalid dynamic field name. Field: " + objectField);
        }
    }

    @Override
    public Object getFieldValue(Object object, int itemIndex) {
        return provider.getFieldValue(object, dynamicField);
    }

    @Override
    public void setFieldValue(Object object, int itemIndex, Object value) {
        provider.setFieldValue(object, dynamicField, value);
    }

    @Override
    public Class getFieldValueClass() {
        return provider.getFieldValueClass(dynamicField);
    }

    @Override
    public int getItemsCount(Object object) {
        return 1;
    }

    @Override
    public void setNestedAccessor(ObjectFieldAccessor nestedAccessor) {
        throw new IllegalArgumentException(
                "Nested accessors are not supported for dynamic fields. Field: " +
                dynamicField + ". Please recheck objectField access string."
        );
    }
}
