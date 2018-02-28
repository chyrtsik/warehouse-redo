/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.print;

import com.artigile.warehouse.domain.printing.PrintTemplateFieldMapping;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.utils.reflect.ObjectFieldsProvider;
import com.artigile.warehouse.utils.reflect.ObjectFieldsProviderUtils;
import com.artigile.warehouse.utils.reflect.fields.dynamic.DynamicObjectFieldsProvider;
import com.artigile.warehouse.utils.reflect.fields.dynamic.DynamicObjectFieldsProviderFactory;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Shyrik, 25.01.2009
 */

/**
 * "Wrapper" of the object fields provider, used for printing.
 */
public class PrintTemplateFieldsProvider {
    private ObjectFieldsProvider objectFieldsProvider;

    private List<PrintTemplateFieldMapping> imageFieldsMapping = new ArrayList<PrintTemplateFieldMapping>();
    private List<PrintTemplateFieldMapping> objectFieldsMapping = new ArrayList<PrintTemplateFieldMapping>();

    public PrintTemplateFieldsProvider(Object object, List<PrintTemplateFieldMapping>fieldsMapping) {
        //1. Prepare dynamic fields.
        fieldsMapping = PrintTemplateFieldsProvider.preProcessFieldMapping(fieldsMapping);

        //2. Split objects fields and image fields.
        for (PrintTemplateFieldMapping fieldMapping : fieldsMapping){
            if (fieldMapping.isObjectField()){
                objectFieldsMapping.add(fieldMapping);
            }
            else if (fieldMapping.isImageField()){
                imageFieldsMapping.add(fieldMapping);
            }
        }

        //3. Initialize component to access object field values.
        List<String> objectFields = new ArrayList<String>(objectFieldsMapping.size());
        for (PrintTemplateFieldMapping objectFieldMapping : objectFieldsMapping){
            objectFields.add(objectFieldMapping.getObjectField());
        }
        objectFieldsProvider = new ObjectFieldsProvider(object, objectFields);
    }

    public static List<PrintTemplateFieldMapping> preProcessFieldMapping(List<PrintTemplateFieldMapping> fieldsMapping) {
        List<PrintTemplateFieldMapping> processedFieldsMapping = new ArrayList<PrintTemplateFieldMapping>();
        for (PrintTemplateFieldMapping fieldMapping : fieldsMapping){
            if (ObjectFieldsProviderUtils.isDynamicField(fieldMapping.getObjectField())){
                processedFieldsMapping.addAll(enumerateDynamicFields(fieldMapping));
            }
            else{
                processedFieldsMapping.add(fieldMapping);
            }
        }
        return processedFieldsMapping;
    }

    private static Collection<PrintTemplateFieldMapping> enumerateDynamicFields(PrintTemplateFieldMapping fieldMapping) {
        String providerName = ObjectFieldsProviderUtils.parseDynamicFieldProviderName(fieldMapping.getObjectField());

        DynamicObjectFieldsProvider provider = DynamicObjectFieldsProviderFactory.getInstance().createProvider(providerName);
        if (provider == null){
            throw new IllegalArgumentException(MessageFormat.format(
                    "Invalid dynamic fields provider in mapping: {0}", fieldMapping.getObjectField())
            );
        }

        List<String> dynamicFields = provider.getAvailableFields();

        //Replace original "mapping" (this was a template indication that here we have dynamic fields) with concrete
        //fields available at this type.
        List<PrintTemplateFieldMapping> dynamicFieldsMapping = new ArrayList<PrintTemplateFieldMapping>(dynamicFields.size());
        for (String dynamicField : dynamicFields){
            String objectField = ObjectFieldsProviderUtils.formatDynamicObjectFieldName(
                    fieldMapping.getObjectField(), providerName, dynamicField
            );
            String[] reportFields = ObjectFieldsProviderUtils.formatDynamicReportFieldName(fieldMapping.getReportField(), dynamicField);
            for (String reportField : reportFields){
                dynamicFieldsMapping.add(new PrintTemplateFieldMapping(objectField, reportField));
            }
        }
        return dynamicFieldsMapping;
    }

    /**
     * Gets list of all report field names.
     * @return all fields of report.
     */
    public List<String> getReportFields() {
        List<String> reportFields = new ArrayList<String>();
        for (PrintTemplateFieldMapping fieldMapping : objectFieldsMapping){
            reportFields.add(fieldMapping.getReportField());
        }
        for (PrintTemplateFieldMapping fieldMapping : imageFieldsMapping){
            reportFields.add(fieldMapping.getReportField());
        }
        return reportFields;
    }

    /**
     * Gets values of the field
     * @param fieldIndex index of field to be calculated (column in table).
     * @param itemIndex index of item in data list.
     * @return value of given data field.
     */
    public Object getFieldValue(int fieldIndex, int itemIndex) {
        if (fieldIndex < objectFieldsMapping.size()){
            //Object's field.
            return objectFieldsProvider.getFieldValue(fieldIndex, itemIndex);
        }
        else{
            //Image field.
            return getImageFieldValue(fieldIndex - objectFieldsMapping.size());
        }
    }

    private Object getImageFieldValue(int imageIndex) {
        try {
            return ImageIO.read(new ByteArrayInputStream(imageFieldsMapping.get(imageIndex).getImage().getImageData()));
        } catch (IOException e) {
            LoggingFacade.logError(this, "Error accessing image from print.", e);
            throw new RuntimeException("Error when accessing image: " + e.getMessage());
        }
    }

    /**
     * Get list of al items to print (total items that will be printed in report).
     *
     * @return items count to print.
     */
    public int getItemsCount() {
        return objectFieldsProvider.getItemsCount();
    }
}
