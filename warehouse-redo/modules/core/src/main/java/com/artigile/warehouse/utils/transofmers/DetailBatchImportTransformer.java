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

import com.artigile.warehouse.domain.details.DetailBatchImport;
import com.artigile.warehouse.utils.dto.details.DetailBatchImportTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aliaksandr.Chyrtsik, 06.11.11
 */
public final class DetailBatchImportTransformer {
    private DetailBatchImportTransformer(){
    }

    public static List<DetailBatchImportTO> transformList(List<DetailBatchImport> detailBatchImports) {
        List<DetailBatchImportTO> detailBatchImportTOs = new ArrayList<DetailBatchImportTO>(detailBatchImports.size());
        for (DetailBatchImport detailBatchImport : detailBatchImports){
            detailBatchImportTOs.add(transform(detailBatchImport));
        }
        return detailBatchImportTOs;
    }

    public static DetailBatchImportTO transform(DetailBatchImport detailBatchImport) {
        DetailBatchImportTO detailBatchImportTO = new DetailBatchImportTO();
        update(detailBatchImportTO, detailBatchImport);
        return detailBatchImportTO;
    }

    public static void update(DetailBatchImportTO detailBatchImportTO, DetailBatchImport detailBatchImport) {
        //Common data import transformation.
        DataImportTransformer.update(detailBatchImportTO, detailBatchImport);
        //Detail batch import specific transformation.
        detailBatchImportTO.setDetailType(DetailTypesTransformer.transformDetailTypeForReport(detailBatchImport.getDetailType()));
        detailBatchImportTO.setCurrency(CurrencyTransformer.transformCurrency(detailBatchImport.getCurrency()));
        detailBatchImportTO.setMeasureUnit(MeasureUnitTransformer.transform(detailBatchImport.getMeasureUnit()));

        detailBatchImportTO.setInsertedItemsCount(detailBatchImport.getInsertedItemsCount());
        detailBatchImportTO.setUpdatedItemsCount(detailBatchImport.getUpdatedItemsCount());
        detailBatchImportTO.setErrorItemsCount(detailBatchImport.getErrorItemsCount());
    }

    public static void update(DetailBatchImport detailBatchImport, DetailBatchImportTO detailBatchImportTO) {
        //Common data import fields.
        DataImportTransformer.update(detailBatchImport, detailBatchImportTO);

        //Detail batch import specific fields.
        detailBatchImport.setDetailType(DetailTypesTransformer.transformDetailType(detailBatchImportTO.getDetailType()));
        detailBatchImport.setCurrency(CurrencyTransformer.transformCurrency(detailBatchImportTO.getCurrency()));
        detailBatchImport.setMeasureUnit(MeasureUnitTransformer.transform(detailBatchImportTO.getMeasureUnit()));
    }
}
