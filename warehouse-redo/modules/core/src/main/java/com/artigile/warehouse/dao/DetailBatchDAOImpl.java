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

import com.artigile.warehouse.bl.detail.DetailBatchFilter;
import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.details.*;
import com.artigile.warehouse.utils.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Shyrik, 26.12.2008
 */
public class DetailBatchDAOImpl extends GenericEntityDAO<DetailBatch> implements DetailBatchDAO {
    @SuppressWarnings("unchecked")
    @Override
    public List<DetailBatch> getAllSortedByPriceNumber() {
        return getSession()
            .createCriteria(DetailBatch.class)
            .createAlias("model", "m")
            .createAlias("m.detailType", "t")
            .addOrder(Order.asc("t.sortNum"))
            .addOrder(Order.asc("m.sortNum"))
            .addOrder(Order.asc("sortNum"))
            .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DetailBatch> getByDetailTypes(Collection<DetailType> detailTypes, List<Object> groupingFieldsFilter) {
        if (groupingFieldsFilter != null){
            return loadByDetailTypesWithFieldsFilter(groupingFieldsFilter, detailTypes);
        }
        else{
            return loadByDetailTypes(detailTypes);
        }
    }

    @SuppressWarnings("unchecked")
    private List<DetailBatch> loadByDetailTypesWithFieldsFilter(List<Object> groupingFieldsFilter, Collection<DetailType> allDetailTypesInGroup) {
        //Filter by field values. Load details per detail type (filter is type-specific).
        List<DetailBatch> loadedBatches = new ArrayList<DetailBatch>();

        for (DetailType detailType : allDetailTypesInGroup){
            List<DetailField> groupingFields = detailType.getSortedGroupingFields();
            if (groupingFields.size() < groupingFieldsFilter.size()){
                //Details of this type are ignored -- filter has more values then supported by detail type.
                //Current subgroup is for more complicated detail type (with more grouping fields).
                continue;
            }

            //Construct query with filtering by parameters values.
            StringBuilder queryBuilder = new StringBuilder()
                    .append(" select b from DetailBatch as b ")
                    .append(" inner join b.model as m ")
                    .append(" inner join m.detailType as t ")
                    .append(" where t.id = ").append(detailType.getId());

            for (int i=0; i<groupingFieldsFilter.size(); i++){
                Object filterValue = groupingFieldsFilter.get(i);
                DetailField field = groupingFields.get(i);
                String fieldName = DetailModel.getFieldName(field.getFieldIndex());
                if (field.getType().equals(DetailFieldType.NUMBER) && isNumberFilterValue(filterValue)){
                    queryBuilder.append(" and cast(m.").append(fieldName)
                                .append(", big_decimal) = :").append(fieldName);
                }
                else{
                    queryBuilder.append(" and m.").append(fieldName)
                            .append(" = :").append(fieldName);
                }
            }

            queryBuilder.append(" order by t.sortNum, m.sortNum, b.sortNum");

            //Bind parameters and execute query.
            Query query = getSession().createQuery(queryBuilder.toString());
            for(int i=0; i<groupingFieldsFilter.size(); i++){
                DetailField field = groupingFields.get(i);
                String fieldName = DetailModel.getFieldName(field.getFieldIndex());
                Object fieldValue = groupingFieldsFilter.get(i);

                if (field.getType().equals(DetailFieldType.NUMBER) && isNumberFilterValue(fieldValue)){
                    query.setParameter(fieldName, fieldValue);
                }
                else{
                    query.setParameter(fieldName, fieldValue.toString());
                }
            }

            loadedBatches.addAll(query.list());
        }

        return loadedBatches;
    }

    private boolean isNumberFilterValue(Object filterValue) {
        return filterValue instanceof Number;
    }

    @SuppressWarnings("unchecked")
    private List<DetailBatch> loadByDetailTypes(Collection<DetailType> allDetailTypesInGroup) {
        if (allDetailTypesInGroup.isEmpty()){
            return new ArrayList<DetailBatch>();
        }

        //Load all detail types with one query.
        Criteria criteria = getSession().createCriteria(DetailBatch.class);

        criteria.createAlias("model", "m")
                .createAlias("m.detailType", "t")
                .add(Restrictions.in("m.detailType", allDetailTypesInGroup))
                .addOrder(Order.asc("t.sortNum"))
                .addOrder(Order.asc("m.sortNum"))
                .addOrder(Order.asc("sortNum"));

        return criteria.list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void refreshDetailBatchSortNumbersByModel(DetailModel model) {
        List<DetailBatch> batches = getSession()
            .createCriteria(DetailBatch.class)
            .add(Restrictions.eq("model", model))
            .addOrder(Order.asc("name"))
            .addOrder(Order.asc("misc"))
            .addOrder(Order.asc("notice"))
            .addOrder(Order.asc("sellPrice"))
            .list();

        for (int i=0; i<batches.size(); i++){
            DetailBatch batch = batches.get(i);
            if (batch.getSortNum() == null || batch.getSortNum() != i+1){
                batch.setSortNum(i+1);    
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DetailBatch> getSameBatches(String batchName, String batchMisc, String batchNotice) {
        return getSession()
            .createCriteria(DetailBatch.class)
            .add(Restrictions.eq("name", batchName))
            .add(Restrictions.eq("misc", batchMisc))
            .add(Restrictions.eq("notice", batchNotice))
            .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DetailBatch> getSameBatches(String name, String misc, String notice, String nomenclatureArticle, String barCode) {
        return getSession()
            .createCriteria(DetailBatch.class)
            .add(Restrictions.disjunction()
                .add(Restrictions.conjunction()
                    .add(Restrictions.eq("name", name))
                    .add(misc != null ? Restrictions.eq("misc", misc) : Restrictions.isNull("misc"))
                    .add(notice != null ? Restrictions.eq("notice", notice) : Restrictions.isNull("notice"))
                )
                .add(Restrictions.eq("nomenclatureArticle", nomenclatureArticle))
                .add(Restrictions.eq("barCode", barCode))
            ).list();
    }

    @Override
    public DetailBatch getBatchByBarCode(String barCode) {
        return (DetailBatch)getSession()
            .createCriteria(DetailBatch.class)
            .add(Restrictions.eq("barCode", barCode))
            .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DetailBatch> getBatchesByNomenclatureArticle(String nomenclatureArticle) {
        return getSession()
            .createCriteria(DetailBatch.class)
            .add(Restrictions.eq("nomenclatureArticle", nomenclatureArticle))
            .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DetailBatch> getBatchesByDetailModel(DetailModel detailModel) {
        return getSession()
            .createCriteria(DetailBatch.class)
            .add(Restrictions.eq("model", detailModel))
            .list();
    }

    @Override
    public DetailBatch getDetailByUid(String uid) {
        return (DetailBatch) getSession().createCriteria(DetailBatch.class)
                .add(Restrictions.eq("uidGoods", uid))
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getUidsByIds(List<Long> ids) {
        org.hibernate.Criteria criteria = getSession().createCriteria(DetailBatch.class);
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("uidGoods"));
        criteria.setProjection(projectionList);
        criteria.add(Restrictions.in("id", ids));
        return criteria.list();
    }

    @Override
    public int getDetailBatchesCountByFilter(DetailBatchFilter filter) {
        Criteria criteria = getSession().createCriteria(DetailBatch.class);
        criteria.setProjection(Projections.rowCount());
        criteria = getFilteredCriteria(criteria, filter);
        return ((Long)criteria.uniqueResult()).intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<DetailBatch> getDetailBatchesByFilter(DetailBatchFilter filter) {
        Criteria criteria = getSession().createCriteria(DetailBatch.class);
        criteria = getFilteredCriteria(criteria, filter);
        return criteria.list();
    }

    @Override
    public long getMaxUsedBarCodeArticle(String barCodePrefix, int barCodeArticleLength, int controlNumbers) {
        Query query = getSession().createSQLQuery(
                "SELECT MAX(SUBSTR(d.barcode, LENGTH(:prefix)+1, :articleLength)) " +
                "  FROM detailbatch d " +
                "  WHERE d.barcode LIKE CONCAT(:prefix, '%') " +
                "    AND LENGTH(d.barcode) = LENGTH(:prefix) + :articleLength + :controlNumbers")
                .setString("prefix", barCodePrefix)
                .setInteger("articleLength", barCodeArticleLength)
                .setInteger("controlNumbers", controlNumbers);
        String result = (String)query.uniqueResult();
        return StringUtils.isNumberLong(result) ? Long.valueOf(result) : 0;
    }

    @Override
    public List<DetailBatch> getAvailableDetailBatches(DetailType detailType, Map<DetailField, String> fieldValues) {
        Criteria criteria = getSession().createCriteria(DetailBatch.class)
                .createCriteria("model");
        criteria.add(Restrictions.eq("detailType.id", detailType.getId()));
        for (Map.Entry<DetailField, String> entry : fieldValues.entrySet()) {
            int entryFieldIndex = entry.getKey().getFieldIndex() == null ? 0 : entry.getKey().getFieldIndex();
            String fieldName = DetailModel.getFieldName(entryFieldIndex);
            Object value = entry.getValue();
            criteria.add(value != null ? Restrictions.eq(fieldName, value) : Restrictions.isNull(fieldName));
        }
        return criteria.list();
    }

    private Criteria getFilteredCriteria(Criteria criteria, DetailBatchFilter filter) {
        if (filter != null){
            if (filter.isWithoutBarCode()){
                criteria.add(Restrictions.disjunction()
                        .add(Restrictions.isNull("barCode"))
                        .add(Restrictions.eq("barCode", ""))
                );
            }
        }
        return criteria;
    }
}
