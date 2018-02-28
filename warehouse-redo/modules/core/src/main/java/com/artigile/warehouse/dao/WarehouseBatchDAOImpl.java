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

import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchFilter;
import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.details.DetailModel;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Borisok V.V., 29.12.2008
 */
@SuppressWarnings("unchecked")
public class WarehouseBatchDAOImpl extends GenericEntityDAO<WarehouseBatch> implements WarehouseBatchDAO {

    @Override
    public List<WarehouseBatch> getSimilarBatches(DetailBatch detailBatch, StoragePlace storagePlace) {
        return (List<WarehouseBatch>) getSession().createCriteria(WarehouseBatch.class)
                .add(Restrictions.eq("detailBatch", detailBatch))
                .add(Restrictions.eq("storagePlace", storagePlace))
                .list();
    }

    @Override
    public List<WarehouseBatch> getWarehouseBatchesForDetailBatch(DetailBatch detailBatch) {
        return (List<WarehouseBatch>) getSession().createCriteria(WarehouseBatch.class)
                .createAlias("detailBatch", "db")
                .add(Restrictions.eq("detailBatch", detailBatch))
                .list();
    }

    @Override
    public List<WarehouseBatch> getWarehouseBatchesForDetailModel(DetailModel detailModel) {
        return (List<WarehouseBatch>) getSession()
            .createCriteria(WarehouseBatch.class)
            .createAlias("detailBatch", "db")
            .createAlias("db.model", "dm")
            .add(Restrictions.eq("db.model", detailModel))
            .list();
    }

    @Override
    public List<WarehouseBatch> getWarehouseBatchesByStoragePlace(StoragePlace storagePlace) {
        return (List<WarehouseBatch>) getSession().createCriteria(WarehouseBatch.class)
                .createAlias("storagePlace", "sp")
                .add(Restrictions.eq("storagePlace", storagePlace))
                .list();
    }

    @Override
    public List<WarehouseBatch> getWarehouseBatches(DetailBatch detailBatch, StoragePlace storagePlace, String notice) {
        return getSession().createCriteria(WarehouseBatch.class)
                .add(Restrictions.eq("detailBatch", detailBatch))
                .add(Restrictions.eq("storagePlace", storagePlace))
                .add(Restrictions.eq("notice", notice))
                .list();
    }

    @Override
    public List<WarehouseBatch> getWarehouseBatchesByPostingItem(long postingItemId) {
        return getSession().createCriteria(WarehouseBatch.class)
                .add(Restrictions.eq("postingItem.id", postingItemId))
                .list();
    }

    @Override
    public List<WarehouseBatch> getByFilterSortedByPostingDate(WarehouseBatchFilter filter) {
        Criteria criteria = buildFilteredCriteria(filter);
        criteria.addOrder(Order.asc("postingItem.id"));
        return criteria.list();
    }

    @Override
    public List<WarehouseBatch> getByFilter(WarehouseBatchFilter filter) {
        return buildFilteredCriteria(filter).list();
    }

    private Criteria buildFilteredCriteria(WarehouseBatchFilter filter) {
        Criteria criteria = getSession().createCriteria(WarehouseBatch.class);
        if (filter != null){
            if (filter.getWarehouseId() != null){
                criteria.createAlias("storagePlace", "sp");
                criteria.createAlias("sp.warehouse", "wh");
                criteria.add(Restrictions.eq("wh.id", filter.getWarehouseId()));
            }
        }
        return criteria;
    }
}
