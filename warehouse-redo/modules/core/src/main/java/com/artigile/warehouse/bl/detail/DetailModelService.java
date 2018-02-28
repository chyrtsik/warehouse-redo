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

import com.artigile.warehouse.dao.DetailModelDAO;
import com.artigile.warehouse.domain.details.DetailField;
import com.artigile.warehouse.domain.details.DetailModel;
import com.artigile.warehouse.domain.details.DetailPredefinedFieldType;
import com.artigile.warehouse.domain.details.DetailType;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailModelTO;
import com.artigile.warehouse.utils.transofmers.DetailModelsTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shyrik, 21.12.2008
 */
@Transactional
public class DetailModelService {

    private DetailModelDAO detailModelsDAO;

    public DetailModelService() {
    }

    public List<DetailModelTO> getAllModels() {
        return DetailModelsTransformer.transformList(detailModelsDAO.getAll());
    }

    public DetailModel getModelById(long modelId) {
        return detailModelsDAO.get(modelId);
    }

    public void saveDetailModel(DetailModelTO detailModelTO) {
        saveDetailModel(detailModelTO, true);
    }

    public void saveDetailModel(DetailModelTO detailModelTO, boolean refresh) {
        DetailModel persistentDetailModel = detailModelsDAO.get(detailModelTO.getId());
        if (persistentDetailModel== null ){
            persistentDetailModel = new DetailModel();
        }
        DetailModelsTransformer.update(persistentDetailModel, detailModelTO);

        detailModelsDAO.save(persistentDetailModel);

        if (refresh) {
            refreshModelsSortNumbersByDetailType(persistentDetailModel.getDetailType());
        }
        SpringServiceContext.getInstance().getDetailBatchesService().refreshDetailBatchSortNumbersByModel(persistentDetailModel);

        DetailModelsTransformer.update(detailModelTO, persistentDetailModel);
    }

    public void deleteDetailModel(long detailModelId) {
        DetailModel persistentDetailModel = detailModelsDAO.get(detailModelId);
        if (persistentDetailModel != null ){
            detailModelsDAO.remove(persistentDetailModel);
        }
    }

    /**
     * Checks, the value of the field will be unique.
     * @param field
     * @param value
     * @return
     */
    public boolean isUniqueFieldValue(DetailModelTO model, DetailFieldTO field, String value) {
        if (field.isPredefined() && field.getPredefinedType() == DetailPredefinedFieldType.NAME){
            return isUniqueDetailModelName(value, model.getId());
        }
        throw new RuntimeException("isUniqueFieldValue method is not excpect field '" + field.getName() + "'");
    }

    /**
     * Checks, if detail model has unique field values.
     * @param detailModelTO model to be checked.
     * @return
     */
    public boolean isUniqueDetailModel(DetailModelTO detailModelTO) {
        List<DetailModelTO> sameModels = getSameModels(detailModelTO);
        for (DetailModelTO sameModel : sameModels){
            if (detailModelTO.getId() != sameModel.getId()){
                if (detailModelTO.isSameModel(sameModel)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns a list of same models, as the given one.
     * @param detailModelTO
     * @return
     */
    public List<DetailModelTO> getSameModels(DetailModelTO detailModelTO) {
        return DetailModelsTransformer.transformList(detailModelsDAO.getDetailModelsByName(detailModelTO.getName()));
    }

    /**
     * Refreshes sort numbers of the detail models withing given detail type.
     * @param detailType
     */
    public void refreshModelsSortNumbersByDetailType(DetailType detailType) {
        //TODO: Implement either faster calculation or remove this feature completely.
        //detailModelsDAO.refreshModelSortNumbersByDetailType(detailType);
    }

    /**
     * Refreshes detail models calculated field values (usually used when detail type has been changed).
     * Used to prevent from storing slate values of calculated fields in database.
     * @param detailType
     */
    public void refreshModelsCalculatedFieldsByDetailType(DetailType detailType) {
        List<DetailModel> detailModels = detailModelsDAO.getDetailModelsByType(detailType);
        for (DetailModel detailModel : detailModels){
            //Entity -> DTO (calculation) -> Entity (update from DTO)
            DetailModelsTransformer.update(detailModel, DetailModelsTransformer.transform(detailModel));
            //To prevent detail batch fields of the current detail model to become out of date
            //we should update their calculated fields too.
            SpringServiceContext.getInstance().getDetailBatchesService().refreshDetailBatchCalculatedFieldsByModel(detailModel);
        }
    }

    public List<String> getAvailableDetailFieldValues(DetailType detailType, DetailFieldTO detailField, Map<DetailFieldTO, String> fieldValues) {
        return detailModelsDAO.getAvailableDetailFieldValues(detailType, SpringServiceContext.getInstance().getDetailTypesService().getDetailFieldById(detailField.getId()), toFieldValues(fieldValues));
    }

    public List<DetailModelTO> getAvailableDetailModelsByValues(long detailTypeId, Map<DetailFieldTO, String> fieldValues) {
        return DetailModelsTransformer.transformList(detailModelsDAO.getAvailableDetailModelsByValues(detailTypeId, toFieldValues(fieldValues)));
    }

    private Map<DetailField, String> toFieldValues(Map<DetailFieldTO, String> fieldValues) {
        Map<DetailField, String> result = new HashMap<DetailField, String>();
        for (Map.Entry<DetailFieldTO, String> entry : fieldValues.entrySet()) {
            DetailFieldTO detailFieldTO = entry.getKey();
            DetailField detailField = new DetailField(detailFieldTO.getId());
            detailField.setFieldIndex(detailFieldTO.getFieldIndex());
            result.put(detailField, entry.getValue());
        }
        return result;
    }

    private boolean isUniqueDetailModelName(String name, long modelId) {
        DetailModel persistentModel = detailModelsDAO.getDetailModelByName(name);
        return persistentModel == null || persistentModel.getId() == modelId;
    }

    //================ Spring setters ===================================
    public void setDetailModelDAO(DetailModelDAO detailModelsDAO) {
        this.detailModelsDAO = detailModelsDAO;
    }
}
