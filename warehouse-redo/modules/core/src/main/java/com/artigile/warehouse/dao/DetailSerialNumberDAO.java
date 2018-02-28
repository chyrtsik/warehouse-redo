/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.details.DetailField;
import com.artigile.warehouse.domain.details.DetailSerialNumber;

import java.util.List;
import java.util.Map;

/**
 * @author Aliaksandr Chyrtsik
 * @since 17.06.13
 */
public interface DetailSerialNumberDAO extends EntityDAO<DetailSerialNumber> {
    /**
     * Update database structure after deletion of fields of serial numbers.
     * @param detailTypeId detail type, whose serial number fields have need deleted.
     * @param removedFieldIndexes indexes of removed fields. Should be sorted.
     */
    void removeFields(long detailTypeId, List<Integer> removedFieldIndexes);

    List<String> getAvailableDetailFieldValues(long detailTypeId, DetailField detailField, Map<DetailField, String> detailFields, Map<DetailField, String> serialDetailFields);

    List<String> getLastUsedValues(long detailTypeId, DetailField detailField, Map<DetailField, String> detailFields, Map<DetailField, String> serialDetailFields, int limit);

    List<DetailSerialNumber> getDetailSerialNumbers(long detailBatchId, Map<DetailField, String> fieldValues);
}
