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

import com.artigile.warehouse.bl.detail.DetailBatchHistoryFilter;
import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.details.DetailBatchOperation;
import com.artigile.warehouse.utils.date.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Aliaksandr.Chyrtsik, 02.10.11
 */
public class DetailBatchHistoryDAOImpl extends GenericEntityDAO<DetailBatchOperation> implements DetailBatchHistoryDAO {
    @SuppressWarnings("unchecked")
    @Override
    public List<DetailBatchOperation> loadStockChangeHistory(Date periodStart, Date periodEnd, DetailBatchHistoryFilter filter) {
        Criteria criteria = createFilteredCriteria(filter);
        if (periodStart != null){
            criteria.add(Restrictions.ge("operationDateTime", DateUtils.getDayBegin(periodStart)));
        }
        if (periodEnd != null){
            criteria.add(Restrictions.lt("operationDateTime", DateUtils.getDayBegin(DateUtils.addDays(periodEnd, 1))));
        }
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }

    private Criteria createFilteredCriteria(DetailBatchHistoryFilter filter) {
        Criteria criteria = getSession().createCriteria(DetailBatchOperation.class);
        if (filter != null){
            if (filter.getDetailBatchId() != null){
                criteria.add(Restrictions.eq("detailBatch.id", filter.getDetailBatchId()));
            }
            if (filter.getStoragePlaceId() != null){
                criteria.add(Restrictions.eq("storagePlace.id", filter.getStoragePlaceId()));
            }
            if (filter.getWarehouseId() != null){
                criteria.createAlias("storagePlace", "sp");
                criteria.add(Restrictions.eq("sp.warehouse.id", filter.getWarehouseId()));
            }
        }
        return criteria;
    }

    @Override
    public List<DetailBatchOperation> loadStockReport(Date date, DetailBatchHistoryFilter filter) {
        if (date == null){
            throw new IllegalArgumentException("Date of stock report is required.");
        }

        StringBuilder queryBuilder = new StringBuilder("{alias}.id in (SELECT MAX(o.id) FROM detailbatchoperation o ");
        if (filter != null){
            if (filter.getWarehouseId() != null){
                queryBuilder.append(", storageplace sp ");
            }
        }
        queryBuilder.append(" WHERE operationDateTime < date('").append(new SimpleDateFormat("yyyy-MM-dd").format(date)).append("') ");
        if (filter != null){
            if (filter.getDetailBatchId() != null){
                queryBuilder.append(" AND o.detailBatch_id = ").append(filter.getDetailBatchId());
            }
            if (filter.getStoragePlaceId() != null){
                queryBuilder.append(" AND o.storagePlace_id = ").append(filter.getStoragePlaceId());
            }
            if (filter.getWarehouseId() != null){
                queryBuilder.append(" AND o.storagePlace_id = sp.id AND sp.warehouse_id = ").append(filter.getWarehouseId());
            }
        }
        queryBuilder.append(" GROUP BY detailBatch_id, postingItemOfChangedWarehouseBatch_id)");

        Criteria criteria = createFilteredCriteria(filter);
        criteria.add(Restrictions.sqlRestriction(queryBuilder.toString()));
        criteria.addOrder(Order.asc("id"));

        return criteria.list();
    }
}
