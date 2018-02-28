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
import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.details.DetailField;
import com.artigile.warehouse.domain.details.DetailModel;
import com.artigile.warehouse.domain.details.DetailType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Shyrik, 26.12.2008
 */
public interface DetailBatchDAO extends EntityDAO<DetailBatch> {
    List<DetailBatch> getAllSortedByPriceNumber();

    List<DetailBatch> getByDetailTypes(Collection<DetailType> detailTypes, List<Object> groupingFieldsFilter);

    void refreshDetailBatchSortNumbersByModel(DetailModel model);

    List<DetailBatch> getSameBatches(String batchName, String batchMisc, String notice);

    List<DetailBatch> getSameBatches(String name, String misc, String notice, String nomenclatureArticle, String barCode);

    DetailBatch getBatchByBarCode(String barCode);

    List<DetailBatch> getBatchesByNomenclatureArticle(String nomenclatureArticle);

    List<DetailBatch> getBatchesByDetailModel(DetailModel detailModel);

    DetailBatch getDetailByUid(String uid);

    List<String> getUidsByIds(List<Long> ids);

    int getDetailBatchesCountByFilter(DetailBatchFilter filter);

    List<DetailBatch> getDetailBatchesByFilter(DetailBatchFilter filter);

    long getMaxUsedBarCodeArticle(String barCodePrefix, int barCodeArticleLength, int controlNumbers);

    List<DetailBatch> getAvailableDetailBatches(DetailType detailType, Map<DetailField, String> fieldValues);
}
