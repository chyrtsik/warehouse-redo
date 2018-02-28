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

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.details.DetailField;
import com.artigile.warehouse.domain.details.DetailFieldType;
import com.artigile.warehouse.domain.details.DetailModel;
import com.artigile.warehouse.domain.details.DetailType;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shyrik, 21.12.2008
 */
public class DetailModelDAOImpl extends GenericEntityDAO<DetailModel> implements DetailModelDAO {
    @Override
    public DetailModel getDetailModelByName(String name) {
        List models = getSession()
            .createCriteria(DetailModel.class)
            .add(Restrictions.eq("name", name)).list();

        if (models.size() == 0){
            return null;
        }
        else{
            return (DetailModel)models.get(0);
        }
    }

    @Override
    public void removeFields(long detailTypeId, List<Integer> removedFieldIndexes) {
        //Execute batch updates for every deleted field. Just clear values of columns where removed fields ware stored.
        for (int indexToClear : removedFieldIndexes){
            clearDetailModelFieldValues(detailTypeId, indexToClear);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DetailModel> getDetailModelsByName(String modelName) {
        return getSession()
            .createCriteria(DetailModel.class)
            .add(Restrictions.eq("name", modelName))
            .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DetailModel> getDetailModelsByType(DetailType detailType) {
        return getSession()
            .createCriteria(DetailModel.class)
            .add(Restrictions.eq("detailType", detailType))
            .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void refreshModelSortNumbersByDetailType(DetailType detailType) {
        //1. Building query for retrieving models according to sort rules.
        Map<Long, Integer> sortNumToFieldIndex = new HashMap<Long, Integer>();
        for (int i=0; i<detailType.getFields().size(); i++){
            DetailField field = detailType.getFields().get(i);
            if (field.getSortNum() != null){
                sortNumToFieldIndex.put(field.getSortNum(), i);
            }
        }

        StringBuilder queryString = new StringBuilder();
        queryString.append("from DetailModel where detailType_id=")
                   .append(detailType.getId());


        boolean isFirst = true;
        for (Long sortNum : sortNumToFieldIndex.keySet()){
            DetailField field = detailType.getFields().get(sortNumToFieldIndex.get(sortNum));
            int fieldIndex = field.getFieldIndex() == null ? 0 : field.getFieldIndex();
            String fieldName;
            if (field.getType().equals(DetailFieldType.NUMBER)){
                //Number field should be cast to numbers, when sorting. This is because all values are stored as text.
                fieldName = MessageFormat.format(" CAST({0}, big_decimal) ASC ", DetailModel.getFieldName(fieldIndex));
            }
            else{
                fieldName = MessageFormat.format(" {0} ASC ", DetailModel.getFieldName(fieldIndex));
            }

            if (isFirst){
                isFirst = false;
                queryString.append(" order by ");
            }
            else{
                queryString.append(" , ");
            }
            queryString.append(fieldName);
        }

        Query query = getSession().createQuery(queryString.toString());
        List<DetailModel> models = query.list();

        //2. Updating models' sort numbers.
        //List<DetailModel> models = criteria.list();
        for (int i=0; i<models.size(); i++){
            DetailModel model = models.get(i);
            if (model.getSortNum() == null || model.getSortNum() != i+1){
                models.get(i).setSortNum(i+1);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getAllFieldsValuesCombinations(long detailType, List<DetailField> fields) {
        StringBuilder columnsListBuilder = new StringBuilder();
        for (DetailField field : fields){
            if (columnsListBuilder.length() > 0){
                columnsListBuilder.append(", ");
            }
            if (field.getType().equals(DetailFieldType.NUMBER)){
                //Number field should be cast to numbers, when sorting. This is because all values are stored as text.
                String columnName = MessageFormat.format("CAST(model.{0}, big_decimal)", DetailModel.getFieldName(field.getFieldIndex()));
                columnsListBuilder.append(columnName);
            }
            else{
                String columnName = MessageFormat.format("model.{0}", DetailModel.getFieldName(field.getFieldIndex()));
                columnsListBuilder.append(columnName);
            }
        }

        String fieldsList = columnsListBuilder.toString();
        StringBuilder selectString = new StringBuilder()
                .append("select distinct ").append(fieldsList)
                .append(" from DetailModel as model where model.detailType.id=").append(detailType)
                .append(" order by ").append(fieldsList);

        Query query = getSession().createQuery(selectString.toString());
        List result = query.list();

        if (fields.size() == 1){
            //This is a special case. Wrap all returned values into array as expected.
            List wrappedResult = new ArrayList(result.size());
            for (Object value : result){
                wrappedResult.add(new Object[]{value});
            }
            result = wrappedResult;
        }

        return result;
    }

    @Override
    public List<String> getAvailableDetailFieldValues(DetailType detailType, DetailField detailField, Map<DetailField, String> fieldValues) {
        int fieldIndex = detailField.getFieldIndex() == null ? 0 : detailField.getFieldIndex();
        Criteria criteria = getSession().createCriteria(DetailModel.class)
                .setProjection(Projections.distinct(Projections.property(DetailModel.getFieldName(fieldIndex))))
                .add(Restrictions.eq("detailType.id", detailType.getId()));
        criteria.createCriteria("detailBatches");
        for (Map.Entry<DetailField, String> entry : fieldValues.entrySet()) {
            int entryFieldIndex = entry.getKey().getFieldIndex() == null ? 0 : entry.getKey().getFieldIndex();
            String fieldName = DetailModel.getFieldName(entryFieldIndex);
            Object value = entry.getValue();
            criteria.add(value != null ? Restrictions.eq(fieldName, value) : Restrictions.isNull(fieldName));
        }
        return criteria.list();
    }

    @Override
    public List<DetailModel> getAvailableDetailModelsByValues(long detailTypeId, Map<DetailField, String> fieldValues) {
        Criteria criteria = getSession().createCriteria(DetailModel.class)
                .add(Restrictions.eq("detailType.id", detailTypeId));
        for (Map.Entry<DetailField, String> entry : fieldValues.entrySet()) {
            int entryFieldIndex = entry.getKey().getFieldIndex() == null ? 0 : entry.getKey().getFieldIndex();
            String fieldName = DetailModel.getFieldName(entryFieldIndex);
            Object value = entry.getValue();
            criteria.add(value != null ? Restrictions.eq(fieldName, value) : Restrictions.isNull(fieldName));
        }

        return criteria.list();
    }

    private void clearDetailModelFieldValues(long detailTypeId, int fieldToClearIndex) {
        String updateQuery = MessageFormat.format("update DetailModel set {0} = null where detailType_id = :type_id",
            DetailModel.getFieldName(fieldToClearIndex));

        getSession().createQuery(updateQuery)
                    .setLong("type_id", detailTypeId)
                    .executeUpdate();
    }
}
