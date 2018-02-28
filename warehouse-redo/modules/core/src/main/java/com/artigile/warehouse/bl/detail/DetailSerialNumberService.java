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

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.dao.DetailSerialNumberDAO;
import com.artigile.warehouse.domain.details.DetailField;
import com.artigile.warehouse.domain.details.DetailSerialNumber;
import com.artigile.warehouse.domain.details.DetailType;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailSerialNumberTO;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.utils.transofmers.DetailSerialNumberTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for operatios with serial numbers.
 * @author Aliaksandr Chyrtsik
 * @since 17.06.13
 */
@Transactional(rollbackFor = BusinessException.class)
public class DetailSerialNumberService {
    private DetailSerialNumberDAO detailSerialNumberDAO;

    private static final String SERIAL_NUMBER_BARCODE_PREFIX = "SN:id:";

    /**
     * Generate bar code string for specified serial number.
     * @param serialNumberId serial number identifier.
     * @return bar code of this serial number.
     */
    public static String formatSerialNumberBarCode(long serialNumberId){
        return SERIAL_NUMBER_BARCODE_PREFIX + serialNumberId;
    }

    /**
     * Parse bar code with extracting serial number identifier.
     * @param barCode bar code to parse.
     * @return identifier of serial number in bar code or null is barcode is not a serial number barcode.
     */
    public static Long parseSerialNumberBarCode(String barCode){
        if (barCode.startsWith(SERIAL_NUMBER_BARCODE_PREFIX)){
            String id = barCode.substring(SERIAL_NUMBER_BARCODE_PREFIX.length());
            return StringUtils.isNumberLong(id) ? Long.valueOf(id) : null;
        }
        return null;
    }

    /**
     * Create new or update existing serial number.
     * @param serialNumberTO serial number to be saved.
     */
    public void saveSerialNumber(DetailSerialNumberTO serialNumberTO) {
        DetailSerialNumber persistentSerialNumber = detailSerialNumberDAO.get(serialNumberTO.getId());
        if (persistentSerialNumber== null ){
            persistentSerialNumber = new DetailSerialNumber();
        }
        DetailSerialNumberTransformer.update(persistentSerialNumber, serialNumberTO);
        detailSerialNumberDAO.save(persistentSerialNumber);
        DetailSerialNumberTransformer.update(serialNumberTO, persistentSerialNumber);
    }

    /**
     * Delete serial number from database.
     * @param serialNumberId id of serial number to be deleted.
     */
    public void deleteSerialNumber(long serialNumberId) {
        DetailSerialNumber persistentSerialNumber = detailSerialNumberDAO.get(serialNumberId);
        if (persistentSerialNumber != null){
            detailSerialNumberDAO.remove(persistentSerialNumber);
        }
    }

    /**
     * Recalculate serial numbers calculated fields for the whole given detail type.
     * @param detailType detail type which serial numbers should be recalculated.
     */
    public void refreshCalculatedFieldsByDetailType(DetailType detailType) {
        //TODO: Not implemented. Implement later of first time this feature will be used.
        LoggingFacade.logWarning(this, "Usage of not implemented feature: batch recalculation of serial numbers fields.");
    }

    public List<DetailSerialNumberTO> getAll() {
        //TODO: Implement proper paging and filtering as soon as this is used by real client.
        return DetailSerialNumberTransformer.transformList(detailSerialNumberDAO.getAll());
    }

    public List<String> getAvailableDetailFieldValues(long detailTypeId, DetailFieldTO detailField, Map<DetailFieldTO, String> fieldValues, Map<DetailFieldTO, String> serialFieldValues) {
        return detailSerialNumberDAO.getAvailableDetailFieldValues(detailTypeId,
                SpringServiceContext.getInstance().getDetailTypesService().getDetailFieldById(detailField.getId()),
                toFieldValues(fieldValues), toFieldValues(serialFieldValues));
    }

    public List<DetailSerialNumberTO> getDetailSerialNumbers(long detailBatchId, Map<DetailFieldTO, String> serialFieldValues) {
        return DetailSerialNumberTransformer.transformList(detailSerialNumberDAO.getDetailSerialNumbers(detailBatchId, toFieldValues(serialFieldValues)));
    }

    public List<String> getLastUsedValues(long detailTypeId, DetailFieldTO detailField, Map<DetailFieldTO, String> fieldValues, Map<DetailFieldTO, String> serialFieldValues, int limit) {
        return detailSerialNumberDAO.getLastUsedValues(detailTypeId,
                SpringServiceContext.getInstance().getDetailTypesService().getDetailFieldById(detailField.getId()),
                toFieldValues(fieldValues), toFieldValues(serialFieldValues), limit);
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

    public DetailSerialNumberTO loadSerialNumberTOById(long serialNumberId) {
        return DetailSerialNumberTransformer.transform(detailSerialNumberDAO.get(serialNumberId));
    }

    public void setDetailSerialNumberDAO(DetailSerialNumberDAO detailSerialNumberDAO) {
        this.detailSerialNumberDAO = detailSerialNumberDAO;
    }
}
