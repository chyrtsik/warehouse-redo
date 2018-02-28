/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.details.DetailSerialNumber;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;
import com.artigile.warehouse.utils.dto.details.DetailSerialNumberTO;
import com.artigile.warehouse.utils.reflect.SimpleObjectsFieldsProvider;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aliaksandr Chyrtsik
 * @since 30.06.13
 */
public class DetailSerialNumberTransformer {
    /**
     * Provider for accessing serial number fields via reflections. Used for eliminating "switch" statement
     * with a big number or branches for every detail model field.
     */
    private static SimpleObjectsFieldsProvider serialNumberFieldsProvider;

    static {
        //Initializing provider for the detail model fields.
        String[] fields = new String[DetailSerialNumber.MAX_FIELD_COUNT];
        for (int i=0; i < fields.length; i++){
            fields[i] = DetailSerialNumber.getFieldName(i+1);
        }
        serialNumberFieldsProvider = new SimpleObjectsFieldsProvider(DetailSerialNumber.class, fields);
    }

    public static List<DetailSerialNumberTO> transformList(List<DetailSerialNumber> serialNumbers) {
        List<DetailSerialNumberTO> serialNumberTOs = new ArrayList<DetailSerialNumberTO>(serialNumbers.size());
        for (DetailSerialNumber serialNumber : serialNumbers){
            serialNumberTOs.add(transform(serialNumber));
        }
        return serialNumberTOs;
    }

    public static DetailSerialNumberTO transform(DetailSerialNumber serialNumber) {
        if (serialNumber == null){
            return null;
        }
        DetailSerialNumberTO serialNumberTO = new DetailSerialNumberTO();
        update(serialNumberTO, serialNumber);
        return serialNumberTO;
    }

    public static void update(DetailSerialNumberTO serialNumberTO, DetailSerialNumber serialNumber){
        serialNumberTO.setId(serialNumber.getId());
        serialNumberTO.setDetail(DetailBatchTransformer.batchTO(serialNumber.getDetail()));
        serialNumberTO.setFields(transformFields(serialNumberTO, serialNumber));
    }

    private static List<DetailFieldValueTO> transformFields(DetailSerialNumberTO serialNumberTO, DetailSerialNumber serialNumber) {
        List<DetailFieldTO> fieldTypesTO = DetailTypesTransformer.transformDetailTypeFieldList(serialNumber.getDetail().getModel().getDetailType().getSerialNumberFields());
        List<DetailFieldValueTO> fieldsTO = new ArrayList<DetailFieldValueTO>(fieldTypesTO.size());

        for (DetailFieldTO fieldTypeTO: fieldTypesTO){
            if (fieldTypeTO.isPredefined()){
                //Predefined fields are not supported for serial numbers at all.
                throw new AssertionError(MessageFormat.format("Unsupported predefined field type. Field: {0}, serial number id: {1}", fieldTypeTO.getName(), serialNumber.getId()));
            }
            else {
                int fieldIndex = fieldTypeTO.getFieldIndex();
                fieldsTO.add(new DetailFieldValueTO(fieldTypeTO, serialNumberTO, (String)serialNumberFieldsProvider.getFieldValue(serialNumber, fieldIndex - 1)));
            }
        }

        return fieldsTO;
    }

    public static void update(DetailSerialNumber serialNumber, DetailSerialNumberTO serialNumberTO){
        serialNumber.setId(serialNumberTO.getId());
        serialNumber.setDetail(DetailBatchTransformer.batch(serialNumberTO.getDetail()));
        updateFields(serialNumber, serialNumberTO);
    }

    private static void updateFields(DetailSerialNumber serialNumber, DetailSerialNumberTO serialNumberTO) {
        for (DetailFieldValueTO field : serialNumberTO.getFields()){
            Integer fieldIndex = field.getType().getFieldIndex();
            if (fieldIndex != null){
                String value = field.getValue();
                serialNumberFieldsProvider.setFieldValue(serialNumber, fieldIndex-1, value.isEmpty() ? null : value);
            }
        }
    }
}
