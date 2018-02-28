/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.detail;

import com.artigile.warehouse.utils.dto.details.DetailSerialNumberTO;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.utils.reflect.fields.dynamic.DynamicObjectFieldsProvider;
import com.artigile.warehouse.utils.reflect.fields.dynamic.DynamicObjectFieldsProviderFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Provider of dynamic serian number fields used in printing.
 *
 * @author Aliaksandr.Chyrtsik, 06.07.2013
 */
@Transactional(readOnly = true)
public class DetailSerialNumberDynamicFieldsProvider implements DynamicObjectFieldsProvider{
    private static final String PROVIDER_NAME = "serialNumberDynamicFieldsProvider";

    private DetailTypeService detailTypeService;

    public void initialize(){
        DynamicObjectFieldsProviderFactory.getInstance().registerProvider(PROVIDER_NAME, this);
    }

    @Override
    public List<String> getAvailableFields() {
        return detailTypeService.getAllUniqueSerialNumberFieldNames();
    }

    @Override
    public Class getFieldValueClass(String dynamicField) {
        //We no not provide strong typing for now -- the same named field for different
        //detail types can have different types.
        return Object.class;
    }

    @Override
    public Object getFieldValue(Object object, String dynamicField) {
        if (object == null){
            return null;
        }
        else if (object instanceof DetailSerialNumberTO){
            DetailSerialNumberTO serialNumber = (DetailSerialNumberTO)object;
            int fieldIndex = serialNumber.getFieldIndexByName(dynamicField);
            return fieldIndex == -1 ? null : serialNumber.getFieldValue(fieldIndex);
        }
        else{
            throw new IllegalArgumentException("Invalid object type passed to " + PROVIDER_NAME + ": " + object.toString());
        }
    }

    @Override
    public void setFieldValue(Object object, String dynamicField, Object value) {
        //Setting of fields values is not provided yet.
        LoggingFacade.logWarning(this, "Calling of not implemented method: setFieldValue");
    }

    public void setDetailTypeService(DetailTypeService detailTypeService) {
        this.detailTypeService = detailTypeService;
    }
}
