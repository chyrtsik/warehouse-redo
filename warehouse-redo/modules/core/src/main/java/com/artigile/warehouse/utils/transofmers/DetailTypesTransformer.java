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

import com.artigile.warehouse.domain.details.DetailField;
import com.artigile.warehouse.domain.details.DetailType;
import com.artigile.warehouse.domain.printing.PrintTemplateInstance;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTOForReport;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;

import java.util.*;

/**
 * @author Shyrik, 14.12.2008
 */

/**
 * Transformer of the detail types data.
 */
public final class DetailTypesTransformer {
    private DetailTypesTransformer(){
    }

    private static final String ENUM_VALUE_DELIMITER = "\n";

    /**
     * Transforms list of entities to the list of forms for displaying in the lists.
     *
     * @param detailTypes
     * @return
     */
    public static List<DetailTypeTOForReport> transformDetailTypeListForReport(List<DetailType> detailTypes) {
        List<DetailTypeTOForReport> detailTypeTOs = new ArrayList<DetailTypeTOForReport>(detailTypes.size());
        for (DetailType item : detailTypes) {
            detailTypeTOs.add(transformDetailTypeForReport(item));
        }
        return detailTypeTOs;
    }

    /**
     * Transforms list of entities to the list of full TOs.
     *
     * @param detailTypes
     * @return
     */
    public static List<DetailTypeTO> transformDetailTypeList(Collection<DetailType> detailTypes) {
        List<DetailTypeTO> detailTypesFullTO = new ArrayList<DetailTypeTO>();
        for (DetailType item : detailTypes) {
            detailTypesFullTO.add(transformDetailType(item));
        }
        return detailTypesFullTO;
    }

    /**
     * Transform list of detail type TOs to the list of entities.
     *
     * @param detailTypesFullTO
     * @return
     */
    public static Set<DetailType> transformDetailTypeListFromTO(List<DetailTypeTO> detailTypesFullTO) {
        Set<DetailType> detailTypes = new HashSet<DetailType>();
        for (DetailTypeTO item : detailTypesFullTO) {
            detailTypes.add(tranformDetailType(item));
        }
        return detailTypes;
    }

    /**
     * Transforms data of the entity info the data of the displaiying form of the detail type.
     *
     * @param detailType
     * @return
     */
    public static DetailTypeTOForReport transformDetailTypeForReport(DetailType detailType) {
        if (detailType == null){
            return null;
        }
        DetailTypeTOForReport detailTypeTOForReport = new DetailTypeTOForReport();
        updateDetailType(detailTypeTOForReport, detailType);
        return detailTypeTOForReport;
    }

    private static void updateDetailType(DetailTypeTOForReport detailTypeTOForReport, DetailType detailType) {
        detailTypeTOForReport.setId(detailType.getId());
        detailTypeTOForReport.setName(detailType.getName());
        detailTypeTOForReport.setNameTemplate(detailType.getFields().get(0).getTemplate());
        detailTypeTOForReport.setDescription(detailType.getDescription());
        detailTypeTOForReport.setNameInPrice(detailType.getDetailBatchNameField().getTemplate());
        detailTypeTOForReport.setMiscInPrice(detailType.getDetailBatchMiscField().getTemplate());
        detailTypeTOForReport.setTypeInPrice(detailType.getDetailBatchTypeField().getTemplate());
    }

    /**
     * Transforms data of the entity info the data of the form of the detail type, that is user for the ediding.
     *
     * @param detailTypeFullTO (out)
     * @param detailType       (in)
     */
    public static void updateDetailType(DetailTypeTO detailTypeFullTO, DetailType detailType) {
        detailTypeFullTO.setId(detailType.getId());
        detailTypeFullTO.setName(detailType.getName());

        List<DetailField> fields = detailType.getFields();
        detailTypeFullTO.setNameTemplate(fields.isEmpty() ? "" : fields.get(0).getTemplate());
        detailTypeFullTO.setDescription(detailType.getDescription());
        detailTypeFullTO.setDetailBatchNameField(transformDetailTypeField(detailType.getDetailBatchNameField()));
        detailTypeFullTO.setDetailBatchMiscField(transformDetailTypeField(detailType.getDetailBatchMiscField()));
        detailTypeFullTO.setDetailBatchTypeField(transformDetailTypeField(detailType.getDetailBatchTypeField()));
        detailTypeFullTO.setFields(transformDetailTypeFieldList(fields));

        detailTypeFullTO.setNameInPrice(detailTypeFullTO.getDetailBatchNameField().getTemplate());
        detailTypeFullTO.setMiscInPrice(detailTypeFullTO.getDetailBatchMiscField().getTemplate());
        detailTypeFullTO.setTypeInPrice(detailTypeFullTO.getDetailBatchTypeField().getTemplate());

        detailTypeFullTO.setSerialNumberFields(transformDetailTypeFieldList(detailType.getSerialNumberFields()));
        detailTypeFullTO.setStickerPrintParams(StickerPrintParamTransformer.transformList(detailType.getStickerPrintParams()));
        detailTypeFullTO.setPrintSerialNumbers(detailType.getPrintSerialNumbers());

        detailTypeFullTO.setPrintTemplateInstance(PrintTemplateTransformer.transformPrintTemplateInstance(detailType.getPrintTemplateInstance()));
    }

    /**
     * Transforms data of the entity info the data of the form of the detail type, that is user for the editing.
     *
     * @param detailType (in, entity)
     * @return
     */
    public static DetailTypeTO transformDetailType(DetailType detailType) {
        if (detailType == null){
            return null;
        }
        DetailTypeTO fullTO = new DetailTypeTO();
        updateDetailType(fullTO, detailType);
        return fullTO;
    }

    /**
     * Transforms data to the persistet detais type data.
     *
     * @param detailTypeFullTO (out)
     * @param detailType       (in)
     */
    public static void updateDetailType(DetailType detailType, DetailTypeTO detailTypeFullTO) {
        detailType.setId(detailTypeFullTO.getId());
        detailType.setName(detailTypeFullTO.getName());
        detailType.setDescription(detailTypeFullTO.getDescription());
        detailType.setDetailBatchNameField(fieldFromTO(detailTypeFullTO.getDetailBatchNameField()));
        detailType.setDetailBatchMiscField(fieldFromTO(detailTypeFullTO.getDetailBatchMiscField()));
        detailType.setDetailBatchTypeField(fieldFromTO(detailTypeFullTO.getDetailBatchTypeField()));
        detailType.setFields(transformAndUpdateDetailTypeFieldsListFromTO(detailTypeFullTO.getFields()));
        detailType.setSerialNumberFields(transformAndUpdateDetailTypeFieldsListFromTO(detailTypeFullTO.getSerialNumberFields()));
        detailType.setStickerPrintParams(StickerPrintParamTransformer.transformTOList(detailTypeFullTO.getStickerPrintParams(), detailType));
        detailType.setPrintSerialNumbers(detailTypeFullTO.getPrintSerialNumbers());
        PrintTemplateInstanceTO printTemplateInstance = detailTypeFullTO.getPrintTemplateInstance();
        PrintTemplateInstance templateInstance = printTemplateInstance != null ?
                SpringServiceContext.getInstance().getPrintTemplateService().getTemplateInstanceById(printTemplateInstance.getId()) : null;
        detailType.setPrintTemplateInstance(templateInstance);
    }

    /**
     * Transforms data to the persistet detais type data.
     *
     * @param detailTypeTO
     * @return
     */
    public static DetailType tranformDetailType(DetailTypeTO detailTypeTO) {
        return transformDetailType((DetailTypeTOForReport)detailTypeTO);
    }

    public static DetailType transformDetailType(DetailTypeTOForReport detailTypeTO) {
        if (detailTypeTO == null){
            return null;
        }
        DetailType detailType = SpringServiceContext.getInstance().getDetailTypesService().getDetailTypeById(detailTypeTO.getId());
        if (detailType == null){
            detailType = new DetailType();
        }
        return detailType;
    }

    /**
     * Transforms full form of the detail type TO to the it's lightweight representation.
     *
     * @param detailType     (out)
     * @param detailTypeFull (in)
     */
    public static void updateDetailType(DetailTypeTOForReport detailType, DetailTypeTO detailTypeFull) {
        detailType.setId(detailTypeFull.getId());
        detailType.setName(detailTypeFull.getName());
        detailType.setNameTemplate(detailTypeFull.getNameTemplate());
        detailType.setDescription(detailTypeFull.getDescription());
        detailType.setNameInPrice(detailTypeFull.getNameInPrice());
        detailType.setMiscInPrice(detailTypeFull.getMiscInPrice());
        detailType.setTypeInPrice(detailTypeFull.getTypeInPrice());
    }

    public static DetailTypeTOForReport transformDetailType(DetailTypeTO detailTypeFullTO) {
        DetailTypeTOForReport typeTO = new DetailTypeTOForReport();
        updateDetailType(typeTO, detailTypeFullTO);
        return typeTO;
    }

    /**
     * Transforms list of detail type fields entities to the list of field forms.
     *
     * @param fields
     * @return
     */
    public static List<DetailFieldTO> transformDetailTypeFieldList(List<DetailField> fields) {
        List<DetailFieldTO> fieldTOs = new ArrayList<DetailFieldTO>(fields.size());
        for (DetailField field : fields) {
            fieldTOs.add(transformDetailTypeField(field));
        }
        return fieldTOs;
    }

    /**
     * Transforms list of detail type fields TO to the fields entities.
     *
     * @param fieldTOs
     * @return
     */
    public static List<DetailField> transformAndUpdateDetailTypeFieldsListFromTO(List<DetailFieldTO> fieldTOs) {
        List<DetailField> fields = new ArrayList<DetailField>();
        for (DetailFieldTO fieldTO : fieldTOs) {
            fields.add(fieldFromTO(fieldTO));
        }
        return fields;
    }

    /**
     * Transform field TO to the field Entity
     *
     * @param fieldTO
     * @return
     */
    private static DetailField fieldFromTO(DetailFieldTO fieldTO) {
        DetailField field;
        if (fieldTO.isNew()) {
            field = new DetailField();
        } else {
            field = SpringServiceContext.getInstance().getDetailTypesService().getDetailFieldById(fieldTO.getId());
        }

        field.setId(fieldTO.getId());
        field.setName(fieldTO.getName());
        field.setType(fieldTO.getType());
        field.setMandatory(fieldTO.getMandatory());
        field.setSortNum(fieldTO.getSortNum());
        field.setCatalogGroupNum(fieldTO.getCatalogGroupNum());
        field.setEnumValues(StringUtils.listToDelimitedString(fieldTO.getEnumValues(), ENUM_VALUE_DELIMITER));
        field.setTemplate(fieldTO.getTemplate());
        field.setPredefined(fieldTO.isPredefined());
        field.setPredefinedType(fieldTO.getPredefinedType());
        field.setUniqueValue(fieldTO.isUnique());
        field.setDisplayOrder(fieldTO.getDisplayOrder());
        field.setFieldIndex(fieldTO.getFieldIndex());

        return field;
    }

    /**
     * Transfors detail field entity to the it's gui representation.
     *
     * @param field
     * @return
     */
    public static DetailFieldTO transformDetailTypeField(DetailField field) {
        if (field == null) {
            return null;
        }

        DetailFieldTO detailFieldTO = new DetailFieldTO();
        detailFieldTO.setId(field.getId());
        detailFieldTO.setName(field.getName());
        detailFieldTO.setType(field.getType());
        detailFieldTO.setMandatory(field.getMandatory());
        detailFieldTO.setSortNum(field.getSortNum());
        detailFieldTO.setCatalogGroupNum(field.getCatalogGroupNum());
        detailFieldTO.setEnumValues(StringUtils.delimitedStringToList(field.getEnumValues(), ENUM_VALUE_DELIMITER));
        detailFieldTO.setTemplate(field.getTemplate());
        detailFieldTO.setPredefined(field.isPredefined());
        detailFieldTO.setPredefinedType(field.getPredefinedType());
        detailFieldTO.setUnique(field.isUniqueValue());
        detailFieldTO.setDisplayOrder(field.getDisplayOrder());
        detailFieldTO.setFieldIndex(field.getFieldIndex());
        return detailFieldTO;
    }
}
