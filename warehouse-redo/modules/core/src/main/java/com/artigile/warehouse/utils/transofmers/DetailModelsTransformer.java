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

import com.artigile.warehouse.domain.details.DetailModel;
import com.artigile.warehouse.domain.details.DetailPredefinedFieldType;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;
import com.artigile.warehouse.utils.dto.details.DetailModelTO;
import com.artigile.warehouse.utils.reflect.SimpleObjectsFieldsProvider;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 21.12.2008
 */
public final class DetailModelsTransformer {
    private DetailModelsTransformer(){
    }

    /**
     * Provider for accessing detail model fields via reflections. Used for eliminating "switch" statement
     * with a big number or branches for every detail model field.
     */
    private static SimpleObjectsFieldsProvider detailModelFieldsProvider;

    static {
        //Initializing provider for the detail model fields.
        String[] fields = new String[DetailModel.MAX_FIELD_COUNT];
        for (int i=0; i < fields.length; i++){
            fields[i] = DetailModel.getFieldName(i+1);
        }
        detailModelFieldsProvider = new SimpleObjectsFieldsProvider(DetailModel.class, fields);
    }

    /**
     * Transfors list of detail models to it's form, that is usable for UI.
     * @param models - list of entities
     * @return
     */
    public static List<DetailModelTO> transformList(List<DetailModel> models) {
        List<DetailModelTO> modelsTO = new ArrayList<DetailModelTO>();
        for (DetailModel model : models){
            modelsTO.add(transform(model));
        }
        return modelsTO;
    }

    /**
     * Transform entity of the detail model to it's TO.
     * @param model
     * @return
     */
    public static DetailModelTO transform(DetailModel model) {
        if (model == null){
            return null;
        }
        
        DetailModelTO modelTO = new DetailModelTO();
        update(modelTO, model);
        return modelTO;
    }

    /**
     * Transform entity of the detail model to it's TO.
     * @param modelTO - out
     * @param model - in
     */
    public static void update(DetailModelTO modelTO, DetailModel model){
        if (modelTO == null || model == null){
            return;
        }

        modelTO.setId(model.getId());
        modelTO.setType(DetailTypesTransformer.transformDetailType(model.getDetailType()));
        modelTO.setFields(listDetailModelFieldsTO(modelTO, model));
    }

    /**
     * Transform detail model TO to the entity.
     * @param model - out
     * @param modelTO
     */
    public static void update(DetailModel model, DetailModelTO modelTO){
        detailModelFieldsFromTO(model, modelTO);
        model.setDetailType(DetailTypesTransformer.tranformDetailType(modelTO.getType()));
    }

    private static void detailModelFieldsFromTO(DetailModel model, DetailModelTO modelTO) {
        //Name field needs custom processing. Name is the first field of the detail (in detail type).
        model.setName(modelTO.getFieldActualValue(0));

        //Processing other fields of the detail model
        for (DetailFieldValueTO field : modelTO.getFields()){
            Integer fieldIndex = field.getType().getFieldIndex();
            if ( fieldIndex != null ){
                String value = field.getValue();
                detailModelFieldsProvider.setFieldValue(model, fieldIndex-1, value.isEmpty() ? null : value);
            }
        }
    }

    /**
     * Extract map of the fields of the detail model from the model entity.
     * @param modelTO - model TO
     * @param model - model entity
     * @return
     */
    private static List<DetailFieldValueTO> listDetailModelFieldsTO(DetailModelTO modelTO, DetailModel model) {
        List<DetailFieldTO> fieldTypesTO = DetailTypesTransformer.transformDetailTypeFieldList(model.getDetailType().getFields());
        List<DetailFieldValueTO> fieldsTO = new ArrayList<DetailFieldValueTO>(fieldTypesTO.size());

        //Processing other fields of the detail model
        for (DetailFieldTO fieldTypeTO: fieldTypesTO){
            if (fieldTypeTO.isPredefined()){
                if (DetailPredefinedFieldType.NAME.equals(fieldTypeTO.getPredefinedType())){
                    //Name field needs custom processing. Name is the first field of the detail (in detail type).
                    fieldsTO.add(new DetailFieldValueTO(fieldTypeTO, modelTO, model.getName()));
                }
                else{
                    throw new AssertionError(MessageFormat.format("Unsupported predefined field type. Field: {0}, model id: {1}", fieldTypeTO.getName(), model.getId()));
                }
            }
            else {
                int fieldIndex = fieldTypeTO.getFieldIndex();
                fieldsTO.add(new DetailFieldValueTO(fieldTypeTO, modelTO, (String)detailModelFieldsProvider.getFieldValue(model, fieldIndex-1)));
            }
        }

        return fieldsTO;
    }
}
