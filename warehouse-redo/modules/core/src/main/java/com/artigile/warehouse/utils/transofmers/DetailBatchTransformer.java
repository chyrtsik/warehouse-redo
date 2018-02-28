/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.bl.finance.CurrencyService;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 26.12.2008
 */
public final class DetailBatchTransformer {
    private DetailBatchTransformer(){
    }

    private static Format typeSortNumFormat = new DecimalFormat("00");
    private static Format modelSortNumFormat = new DecimalFormat("00000");
    private static Format batchSortNumFormat = new DecimalFormat("000");

    /**
     * Transforms list of detail batches to it's UI dimain model representation.
     *
     * @param batches
     * @return
     */
    public static List<DetailBatchTO> list(List<DetailBatch> batches) {
        List<DetailBatchTO> batchesTO = new ArrayList<DetailBatchTO>();
        for (DetailBatch batch : batches) {
            batchesTO.add(batchTO(batch));
        }
        return batchesTO;
    }

    public static DetailBatchTO batchTO(DetailBatch batch) {
        if (batch == null){
            return null;
        }

        DetailBatchTO batchTO = new DetailBatchTO();
        batchTO(batchTO, batch);
        return batchTO;
    }

    /**
     * Transform data from the detail batch entity to it's UI representation.
     *
     * @param batchTO - out
     * @param batch   - in
     */
    public static void batchTO(DetailBatchTO batchTO, DetailBatch batch) {
        if (batchTO == null || batch == null){
            return;
        }

        batchTO.setId(batch.getId());
        batchTO.setUidGoods(batch.getUidGoods());
        batchTO.setModel(DetailModelsTransformer.transform(batch.getModel()));
        batchTO.setAcceptance(batch.getAcceptance());
        batchTO.setYear(batch.getYear());
        batchTO.setBarCode(batch.getBarCode());
        batchTO.setNomenclatureArticle(batch.getNomenclatureArticle());
        batchTO.setManufacturer(ManufacturerTransformer.transform(batch.getManufacturer()));
        batchTO.setBuyPrice(batch.getBuyPrice());
        batchTO.setSellPrice(batch.getSellPrice());
        batchTO.setCurrency(CurrencyTransformer.transformCurrency(batch.getCurrency()));
        batchTO.setSellPrice2(batch.getSellPrice2());
        batchTO.setCurrency2(CurrencyTransformer.transformCurrency(batch.getCurrency2()));
        batchTO.setCount(batch.getCount());
        batchTO.setReservedCount(batch.getReservedCount());
        batchTO.setCountMeas(MeasureUnitTransformer.transform(batch.getCountMeas()));
        batchTO.setNotice(batch.getNotice());
        batchTO.setSortNum(getDetailBatchSortNum(batch));
        batchTO.setNeedRecalculate(batch.getNeedRecalculate());
    }

    private static String getDetailBatchSortNum(DetailBatch batch) {
        StringBuilder sortNum = new StringBuilder();

        if (batch.getModel().getDetailType().getSortNum() != null){
            sortNum.append(typeSortNumFormat.format(batch.getModel().getDetailType().getSortNum()));
        }
        else{
            sortNum.append("??");
        }
        sortNum.append(".");

        if (batch.getModel().getSortNum() != null){
            sortNum.append(modelSortNumFormat.format(batch.getModel().getSortNum()));
        }
        else{
            sortNum.append("?????");
        }
        sortNum.append(".");

        if (batch.getSortNum() != null){
            sortNum.append(batchSortNumFormat.format(batch.getSortNum()));
        }
        else{
            sortNum.append("???");
        }
        return sortNum.toString();
    }

    public static DetailBatch batch(DetailBatchTO batchTO) {
        if (batchTO == null){
            return null;
        }
        DetailBatch batch = SpringServiceContext.getInstance().getDetailBatchesService().getDetailBatchById(batchTO.getId());
        if (batch == null){
            batch = new DetailBatch();
        }
        batch(batch, batchTO);
        return batch;
    }

    /**
     * Transform detail batch UI object to the entity object.
     *
     * @param batch   - out, entity
     * @param batchTO - in
     */
    public static void batch(DetailBatch batch, DetailBatchTO batchTO) {
        if (batch == null || batchTO == null){
            return;
        }

        CurrencyService currencyService = SpringServiceContext.getInstance().getCurrencyService();

        batch.setModel(SpringServiceContext.getInstance().getDetailModelsService().getModelById(batchTO.getModel().getId()));
        batch.setName(batchTO.getName());
        batch.setAcceptance(batchTO.getAcceptance());
        batch.setYear(batchTO.getYear());
        batch.setBarCode(batchTO.getBarCode());
        batch.setNomenclatureArticle(batchTO.getNomenclatureArticle());
        batch.setManufacturer(batchTO.getManufacturer() == null ? null : SpringServiceContext.getInstance().getManufacturerService().getManufacturerById(batchTO.getManufacturer().getId()));
        batch.setBuyPrice(batchTO.getBuyPrice());
        batch.setSellPrice(batchTO.getSellPrice());
        batch.setCurrency(currencyService.getCurrencyById(batchTO.getCurrency().getId()));
        batch.setSellPrice2(batchTO.getSellPrice2());
        batch.setCurrency2(batchTO.getCurrency2() == null ? null : currencyService.getCurrencyById(batchTO.getCurrency2().getId()));
        batch.setCountMeas(SpringServiceContext.getInstance().getMeasureUnitService().getMeasureById(batchTO.getCountMeas().getId()));
        batch.setNotice(batchTO.getNotice());
        batch.setMisc(batchTO.getMisc());
        batch.setType(batchTO.getType());
    }
}
