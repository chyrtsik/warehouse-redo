/*
 * Copyright (c) 2007-2011 Artigile.
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
import com.artigile.warehouse.domain.details.DetailModel;
import com.artigile.warehouse.domain.details.DetailType;

import java.util.List;
import java.util.Map;

/**
 * @author Shyrik, 21.12.2008
 */
public interface DetailModelDAO extends EntityDAO<DetailModel> {
    /**
     * Used for searching model by it's name field.
     * @param name Detail model name value.
     * @return Entity for given model name.
     */
    DetailModel getDetailModelByName(String name);

    /**
     * Cell this method to update database structure after deletion of detail type's field.
     * @param detailTypeId Detail type, whose fields have need deleted.
     * @param removedFieldIndexes Indexes of removed fields. Should be sorted.
     */
    void removeFields(long detailTypeId, List<Integer> removedFieldIndexes);

    /**
     * Gets list of detail models by name.
     * @param modelName
     * @return
     */
    List<DetailModel> getDetailModelsByName(String modelName);

    /**
     * Gets list of detail models by given detail type.
     * @param detailType
     * @return
     */
    List<DetailModel> getDetailModelsByType(DetailType detailType);

    /**
     * Refreshes sort numbers of the models of given detail type.
     * @param detailType
     */
    void refreshModelSortNumbersByDetailType(DetailType detailType);

    /**
     * Loads all combinations of fields values.
     *
     * @param detailType detail type which fields are checked.
     * @param fields fields to be checked (should be fields of single detail type).
     * @return list of unique combinations of the field
     */
    List<Object[]> getAllFieldsValuesCombinations(long detailType, List<DetailField> fields);

    List<String> getAvailableDetailFieldValues(DetailType detailType, DetailField detailField, Map<DetailField, String> fieldValues);

    List<DetailModel> getAvailableDetailModelsByValues(long detailTypeId, Map<DetailField, String> fieldValues);
}
