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

import com.artigile.warehouse.bl.common.exceptions.ItemNotExistsException;
import com.artigile.warehouse.dao.*;
import com.artigile.warehouse.domain.details.*;
import com.artigile.warehouse.domain.sticker.StickerPrintParam;
import com.artigile.warehouse.utils.dto.details.DetailTypeTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTOForReport;
import com.artigile.warehouse.utils.transofmers.DetailTypesTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 14.12.2008
 */

/**
 * Sevice for working with detail types.
 */
@Transactional
public class DetailTypeService {
    private DetailTypeDAO detailTypeDAO;
    private DetailFieldDAO detailFieldDAO;
    private DetailModelDAO detailModelDAO;
    private DetailSerialNumberDAO detailSerialNumberDAO;
    private StickerPrintParamDAO stickerPrintParamDAO;

    private DetailModelService detailModelService;
    private DetailSerialNumberService detailSerialNumberService;


    public DetailTypeService() {
    }

    /**
     * @return maximum number of fields, that are available for detail type.
     */
    public int getMaxDetailFieldsCount() {
        return DetailModel.MAX_FIELD_COUNT;
    }

    /**
     * @return all field types available for products.
     */
    public DetailFieldType[] getAvailableDetailFields() {
        return DetailModel.AVAILABLE_FIELDS;
    }

    /**
     * @return maximum number of fields, that are available for detail type.
     */
    public int getMaxSerialNumberFieldsCount() {
        return DetailSerialNumber.MAX_FIELD_COUNT;
    }

    /**
     * @return all field types available for product serial numbers.
     */
    public DetailFieldType[] getAvailableSerialNumberFields() {
        return DetailSerialNumber.AVAILABLE_FIELDS;
    }

    /**
     * Makes list of all detail types.
     *
     * @return
     */
    public List<DetailTypeTOForReport> getAllDetailTypes() {
        return DetailTypesTransformer.transformDetailTypeListForReport(detailTypeDAO.getAllSorted("name", true));
    }

    /**
     * Checks, if the given detail type name will be unique
     *
     * @param name - name to be checked
     * @param id   - identifier of the detail type, what this id belongs to.
     * @return
     */
    public boolean isUniqueDetailTypeName(String name, long id) {
        DetailType persistentType = detailTypeDAO.getDetailTypeByName(name);
        return persistentType == null || persistentType.getId() == id;
    }

    /**
     * Gets full data of the detail (used for editing data of the detail type).
     *
     * @param detailTypeTO
     * @return
     * @throws ItemNotExistsException - if there is no corresponding persistent item.
     */
    public DetailTypeTO getDetailTypeFullTO(DetailTypeTOForReport detailTypeTO) {
        DetailType persistentDetailType = detailTypeDAO.get(detailTypeTO.getId());
        DetailTypeTO detailTypeFullTO = new DetailTypeTO();
        DetailTypesTransformer.updateDetailType(detailTypeFullTO, persistentDetailType);
        return detailTypeFullTO;
    }

    /**
     * Gets list of detail types, which are not linked with detail groups.
     *
     * @return
     */
    public List<DetailTypeTO> getAllDetailTypesFull() {
        return DetailTypesTransformer.transformDetailTypeList(detailTypeDAO.getAll());
    }

    /**
     * Saves data of the detail type.
     *
     * @param detailTypeFull
     */
    public void saveDetailType(DetailTypeTO detailTypeFull) {
        //1. Saving detail type.
        DetailType persistentDetailType = detailTypeDAO.get(detailTypeFull.getId());
        if (persistentDetailType == null) {
            persistentDetailType = new DetailType();
        }
        List<DetailField> oldFields = persistentDetailType.getFields();
        List<DetailField> oldSerialNumberFields = persistentDetailType.getSerialNumberFields();
        List<StickerPrintParam> oldStickerPrintParams = persistentDetailType.getStickerPrintParams();

        detailTypeFull.initNewFieldIndexes();
        detailTypeFull.initNewSerialNumberFieldIndexes();
        DetailTypesTransformer.updateDetailType(persistentDetailType, detailTypeFull);

        detailTypeDAO.save(persistentDetailType);

        //2. Deleting unused detail fields.
        for (StickerPrintParam oldStickerPrintParam : oldStickerPrintParams) {
            if (!persistentDetailType.getStickerPrintParams().contains(oldStickerPrintParam)) {
                stickerPrintParamDAO.remove(oldStickerPrintParam);
            }
        }

        //3. Deleting unused detail fields.
        List<Integer> removedFieldIndexes = new ArrayList<Integer>();
        for (DetailField oldField : oldFields) {
            if (!persistentDetailType.getFields().contains(oldField)) {
                //Old field is not used and must me deleted.
                removedFieldIndexes.add(oldField.getFieldIndex());
                detailFieldDAO.remove(oldField);
            }
        }

        if (removedFieldIndexes.size() > 0){
            detailModelDAO.removeFields(persistentDetailType.getId(), removedFieldIndexes);
        }

        //4. Deleting unused serial number fields.
        List<Integer> removedSerialNumberFieldIndexes = new ArrayList<Integer>();
        for (DetailField oldSerialNumberField : oldSerialNumberFields) {
            if (!persistentDetailType.getSerialNumberFields().contains(oldSerialNumberField)) {
                //Old field is not used and must me deleted.
                removedSerialNumberFieldIndexes.add(oldSerialNumberField.getFieldIndex());
                detailFieldDAO.remove(oldSerialNumberField);
            }
        }
        if (removedSerialNumberFieldIndexes.size() > 0) {
            detailSerialNumberDAO.removeFields(persistentDetailType.getId(), removedSerialNumberFieldIndexes);
        }


        //5. Updating detail model calculated fields.
        detailModelService.refreshModelsCalculatedFieldsByDetailType(persistentDetailType);

        //6. Updating sort orders of detail models of this detail type.
        detailModelService.refreshModelsSortNumbersByDetailType(persistentDetailType);

        //7. Update serial number calculated fields.
        detailSerialNumberService.refreshCalculatedFieldsByDetailType(persistentDetailType);

        DetailTypesTransformer.updateDetailType(detailTypeFull, persistentDetailType);
    }

    /**
     * Deletes detail type.
     *
     * @param id
     */
    public void deleteDetailType(long id) {
        DetailType persistentDetailType = detailTypeDAO.get(id);
        if (persistentDetailType != null) {
            detailTypeDAO.remove(persistentDetailType);
        }
    }

    /**
     * Gets field of detail type by it's id.
     *
     * @param fieldId
     * @return
     */
    public DetailField getDetailFieldById(long fieldId) {
        return detailFieldDAO.get(fieldId);
    }

    public List<String> getAllUniqueDetailFieldNames() {
        return detailFieldDAO.findAllUniqueDetailFieldNames();
    }

    public List<String> getAllUniqueSerialNumberFieldNames() {
        return detailFieldDAO.findAllUniqueSerialNumberFieldNames();
    }

    public DetailType getDetailTypeById(long detailTypeId) {
        return detailTypeDAO.get(detailTypeId);
    }

    public DetailType getDetailTypeByFieldId(long fieldId) {
        return detailTypeDAO.getDetailTypeByFieldId(fieldId);
    }

    //================ Spring setters ========================================
    public void setDetailTypeDAO(DetailTypeDAO detailTypeDAO) {
        this.detailTypeDAO = detailTypeDAO;
    }

    public void setDetailFieldDAO(DetailFieldDAO detailFieldDAO) {
        this.detailFieldDAO = detailFieldDAO;
    }

    public void setDetailModelDAO(DetailModelDAO detailModelDAO) {
        this.detailModelDAO = detailModelDAO;
    }

    public void setDetailSerialNumberDAO(DetailSerialNumberDAO detailSerialNumberDAO) {
        this.detailSerialNumberDAO = detailSerialNumberDAO;
    }

    public void setStickerPrintParamDAO(StickerPrintParamDAO stickerPrintParamDAO) {
        this.stickerPrintParamDAO = stickerPrintParamDAO;
    }

    public void setDetailModelService(DetailModelService detailModelService) {
        this.detailModelService = detailModelService;
    }

    public void setDetailSerialNumberService(DetailSerialNumberService detailSerialNumberService) {
        this.detailSerialNumberService = detailSerialNumberService;
    }
}
