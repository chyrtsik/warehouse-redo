/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.details;

import com.artigile.warehouse.domain.details.DetailFieldType;
import com.artigile.warehouse.domain.details.DetailModel;
import com.artigile.warehouse.domain.details.DetailPredefinedFieldType;
import com.artigile.warehouse.domain.details.DetailSerialNumber;
import com.artigile.warehouse.utils.dto.sticker.StickerPrintParamTO;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.parser.ParsedTemplate;
import com.artigile.warehouse.utils.parser.ParsedTemplateDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Shyrik, 14.12.2008
 */

/**
 * Full representation of the details data, that is used to edit data of the detail.
 */
public class DetailTypeTO extends DetailTypeTOForReport {

    private List<DetailFieldTO> fields = new ArrayList<DetailFieldTO>();

    private List<DetailFieldTO> fieldsInDisplayOrder = null;

    private List<DetailFieldTO> groupingFields = null;

    private DetailFieldTO detailBatchNameField;

    private DetailFieldTO detailBatchMiscField;

    private DetailFieldTO detailBatchTypeField;

    private List<DetailFieldTO> serialNumberFields = new ArrayList<DetailFieldTO>();

    private List<DetailFieldTO> serialNumberFieldsInDisplayOrder = null;

    private List<StickerPrintParamTO> stickerPrintParams = new ArrayList<StickerPrintParamTO>();

    private boolean printSerialNumbers;

    private PrintTemplateInstanceTO printTemplateInstance;

    //======================== Constructor ========================================
    public DetailTypeTO(){ }

    //=========================== Helpers and manipulators ========================

    /**
     * Creates list of predefined fields for the detail type.
     */
    public void initPredefinedFields() {
        //Init name field.
        DetailFieldTO nameField = new DetailFieldTO();
        nameField.setPredefined(true);
        nameField.setPredefinedType(DetailPredefinedFieldType.NAME);
        nameField.setUnique(true);
        nameField.setName(I18nSupport.message("detail.field.predefined.name"));
        nameField.setMandatory(true);
        nameField.setType(DetailFieldType.TEMPLATE_TEXT);
        nameField.setTemplate(I18nSupport.message("detail.field.predefined.name.template"));
        fields.add(nameField);

        //Init detail batch name field.
        detailBatchNameField = new DetailFieldTO();
        detailBatchNameField.setName(I18nSupport.message("detail.field.predefined.detailBatchName"));
        detailBatchNameField.setType(DetailFieldType.TEMPLATE_TEXT);
        detailBatchNameField.setTemplate(I18nSupport.message("detail.field.predefined.detailBatchName.template"));

        detailBatchMiscField = new DetailFieldTO();
        detailBatchMiscField.setName(I18nSupport.message("detail.field.predefined.detailBatchMisc"));
        detailBatchMiscField.setType(DetailFieldType.TEMPLATE_TEXT);
        detailBatchMiscField.setTemplate(I18nSupport.message("detail.field.predefined.detailBatchMisc.template"));

        detailBatchTypeField = new DetailFieldTO();
        detailBatchTypeField.setName(I18nSupport.message("detail.field.predefined.detailTypeMisc"));
        detailBatchTypeField.setType(DetailFieldType.TEMPLATE_TEXT);
        detailBatchTypeField.setTemplate(I18nSupport.message("detail.field.predefined.detailBatchMisc.template"));
    }

    /**
     * Initialization of field indexes. This method should be used to obtain
     * field indexes (indexes of the columns in database) for custom detail type fields.
     */
    public void initNewFieldIndexes() {
        for (DetailFieldTO field : fields){
            if (field.getPredefinedType() == null && field.getFieldIndex() == null){
                field.setFieldIndex(getNewFieldIndex(fields, DetailModel.MAX_FIELD_COUNT));
            }
        }
    }

    /**
     * Initialization of serial number field indexes. This method should be used to obtain
     * field indexes (indexes of the columns in database) for custom serial number fields.
     */
    public void initNewSerialNumberFieldIndexes() {
        for (DetailFieldTO field : serialNumberFields){
            if (field.getPredefinedType() == null && field.getFieldIndex() == null){
                field.setFieldIndex(getNewFieldIndex(serialNumberFields, DetailSerialNumber.MAX_FIELD_COUNT));
            }
        }
    }

    private int getNewFieldIndex(List<DetailFieldTO> fields, int maxFieldsCount) {
        //Search next unused field index.
        for (int i=1; i<=maxFieldsCount; i++){
            boolean index_used = false;
            for (DetailFieldTO field : fields){
                if (field.getFieldIndex() != null && field.getFieldIndex() == i){
                    index_used = true;
                    break;
                }
            }
            if (!index_used){
                return i;
            }
        }
        throw new RuntimeException("DetailTypeTO.getNewFieldIndex: to many fields. Could not generate new field index value.");
    }

    /**
     * Parses template string into objest, that is able to evaluate template value.
     * @param template
     * @return
     */
    public ParsedTemplate parseTemplate(String template, ParsedTemplateDataSource dataSource) {
        //TODO: do caching of the ParsedTemplate objects here (this is the main idea of implementing this method here).
        ParsedTemplate parsedTemplate = new ParsedTemplate();
        parsedTemplate.parse(template, dataSource);
        return parsedTemplate;
    }

    //===================== Getters and setters ===================================
    public List<DetailFieldTO> getFields() {
        return fields;
    }

    public void setFields(List<DetailFieldTO> fields) {
        this.fields = fields;
        this.fieldsInDisplayOrder = null;
        this.groupingFields = null;
    }

    public List<DetailFieldTO> getFieldsInDisplayOrder() {
        if (fieldsInDisplayOrder == null) {
            fieldsInDisplayOrder = new ArrayList<DetailFieldTO>(fields);
            Collections.sort(fieldsInDisplayOrder, DetailFieldTO.DISPLAY_ORDER_COMPARATOR);
        }
        return fieldsInDisplayOrder;
    }

    public List<DetailFieldTO> getSerialNumberFields() {
        return serialNumberFields;
    }

    public void setSerialNumberFields(List<DetailFieldTO> serialNumberFields) {
        this.serialNumberFields = serialNumberFields;
        this.serialNumberFieldsInDisplayOrder = null;
    }

    public List<DetailFieldTO> getSerialNumberFieldsInDisplayOrder() {
        if (serialNumberFieldsInDisplayOrder == null) {
            serialNumberFieldsInDisplayOrder = new ArrayList<DetailFieldTO>(serialNumberFields);
            Collections.sort(serialNumberFieldsInDisplayOrder, DetailFieldTO.DISPLAY_ORDER_COMPARATOR);
        }
        return serialNumberFieldsInDisplayOrder;
    }

    public List<DetailFieldTO> getSortedGroupingFields() {
        if (groupingFields == null) {
            //At first request we build ordered collection of grouping fields.
            groupingFields = new ArrayList<DetailFieldTO>(fields.size());
            for (DetailFieldTO field : fields){
                if (field.getCatalogGroupNum() != null){
                    groupingFields.add(field);
                }
            }
            Collections.sort(groupingFields, DetailFieldTO.CATALOG_GROUPING_NUM_COMPARATOR);
        }
        return groupingFields;
    }

    public DetailFieldTO getDetailBatchNameField() {
        return detailBatchNameField;
    }

    public void setDetailBatchNameField(DetailFieldTO detailBatchNameField) {
        this.detailBatchNameField = detailBatchNameField;
    }

    public DetailFieldTO getDetailBatchMiscField() {
        return detailBatchMiscField;
    }

    public void setDetailBatchMiscField(DetailFieldTO detailBatchMiscField) {
        this.detailBatchMiscField = detailBatchMiscField;
    }

    public DetailFieldTO getDetailBatchTypeField() {
        return detailBatchTypeField;
    }

    public void setDetailBatchTypeField(DetailFieldTO detailBatchTypeField) {
        this.detailBatchTypeField = detailBatchTypeField;
    }

    public List<StickerPrintParamTO> getStickerPrintParams() {
        return stickerPrintParams;
    }

    public void setStickerPrintParams(List<StickerPrintParamTO> stickerPrintParams) {
        this.stickerPrintParams = stickerPrintParams;
    }

    public boolean getPrintSerialNumbers() {
        return printSerialNumbers;
    }

    public void setPrintSerialNumbers(boolean printSerialNumbers) {
        this.printSerialNumbers = printSerialNumbers;
    }

    public PrintTemplateInstanceTO getPrintTemplateInstance() {
        return printTemplateInstance;
    }

    public void setPrintTemplateInstance(PrintTemplateInstanceTO printTemplateInstance) {
        this.printTemplateInstance = printTemplateInstance;
    }
}
