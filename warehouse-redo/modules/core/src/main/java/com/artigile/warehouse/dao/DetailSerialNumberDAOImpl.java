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

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.details.DetailField;
import com.artigile.warehouse.domain.details.DetailModel;
import com.artigile.warehouse.domain.details.DetailSerialNumber;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author Aliaksandr Chyrtsik
 * @since 17.06.13
 */
public class DetailSerialNumberDAOImpl extends GenericEntityDAO<DetailSerialNumber> implements DetailSerialNumberDAO {
    @Override
    public void removeFields(long detailTypeId, List<Integer> removedFieldIndexes) {
        //Execute batch updates for every deleted field. Just clear values of columns where removed fields ware stored.
        for (int indexToClear : removedFieldIndexes){
            clearSerialNumbersFieldValues(detailTypeId, indexToClear);
        }
    }

    @Override
    public List<String> getAvailableDetailFieldValues(long detailTypeId, DetailField detailField, Map<DetailField, String> detailFields, Map<DetailField, String> serialDetailFields) {
        return createAvailableValues(detailTypeId, detailField, detailFields, serialDetailFields).list();
    }

    @Override
    public List<String> getLastUsedValues(long detailTypeId, DetailField detailField, Map<DetailField, String> detailFields, Map<DetailField, String> serialDetailFields, int limit) {
        Criteria criteria = createAvailableValues(detailTypeId, detailField, detailFields, serialDetailFields);
        criteria.addOrder(Order.desc("id")).setMaxResults(limit);
        return criteria.list();
    }

    @Override
    public List<DetailSerialNumber> getDetailSerialNumbers(long detailBatchId, Map<DetailField, String> fieldValues) {
        Criteria criteria = getSession().createCriteria(DetailSerialNumber.class);
        criteria.add(Restrictions.eq("detail.id", detailBatchId));
        for (Map.Entry<DetailField, String> entry : fieldValues.entrySet()) {
            int entryFieldIndex = entry.getKey().getFieldIndex() == null ? 0 : entry.getKey().getFieldIndex();
            String fieldName = DetailSerialNumber.getFieldName(entryFieldIndex);
            Object value = entry.getValue();
            criteria.add(value != null ? Restrictions.eq(fieldName, value) : Restrictions.isNull(fieldName));
        }
        return criteria.list();
    }

    private Criteria createAvailableValues(long detailTypeId, DetailField detailField, Map<DetailField, String> detailFields, Map<DetailField, String> serialDetailFields) {
        int fieldIndex = detailField.getFieldIndex() == null ? 0 : detailField.getFieldIndex();
        Criteria criteria = getSession().createCriteria(DetailSerialNumber.class)
                .setProjection(Projections.distinct(Projections.property(DetailSerialNumber.getFieldName(fieldIndex))));
        for (Map.Entry<DetailField, String> entry : serialDetailFields.entrySet()) {
            int entryFieldIndex = entry.getKey().getFieldIndex() == null ? 0 : entry.getKey().getFieldIndex();
            String fieldName = DetailSerialNumber.getFieldName(entryFieldIndex);
            Object value = entry.getValue();
            criteria.add(value != null ? Restrictions.eq(fieldName, value) : Restrictions.isNull(fieldName));
        }

        Criteria modelCriteria = criteria.createCriteria("detail").createCriteria("model");
        modelCriteria.add(Restrictions.eq("detailType.id", detailTypeId));
        for (Map.Entry<DetailField, String> entry : detailFields.entrySet()) {
            int entryFieldIndex = entry.getKey().getFieldIndex() == null ? 0 : entry.getKey().getFieldIndex();
            String fieldName = DetailModel.getFieldName(entryFieldIndex);
            Object value = entry.getValue();
            modelCriteria.add(value != null ? Restrictions.eq(fieldName, value) : Restrictions.isNull(fieldName));
        }

        return criteria;
    }

    private void clearSerialNumbersFieldValues(long detailTypeId, int fieldIndexToClear) {
        String updateQuery = MessageFormat.format(
                "update detailserialnumber " +
                "  inner join detailbatch b on b.id = detailserialnumber.detail_id " +
                "  inner join detailmodel m on m.id = b.model_id " +
                "  set detailserialnumber.{0} = null " +
                "  where m.detailtype_id = :type_id",
                DetailSerialNumber.getFieldName(fieldIndexToClear));

        getSession().createSQLQuery(updateQuery)
                .setLong("type_id", detailTypeId)
                .executeUpdate();
    }
}
