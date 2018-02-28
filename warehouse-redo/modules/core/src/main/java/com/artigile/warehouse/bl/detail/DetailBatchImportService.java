/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.detail;

import com.artigile.warehouse.adapter.spi.DataImportContext;
import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.dataimport.DataImportListener;
import com.artigile.warehouse.bl.dataimport.DataImportService;
import com.artigile.warehouse.dao.DetailBatchImportDAO;
import com.artigile.warehouse.dao.DetailTypeDAO;
import com.artigile.warehouse.domain.details.DetailBatchImport;
import com.artigile.warehouse.domain.details.DetailField;
import com.artigile.warehouse.domain.details.DetailFieldType;
import com.artigile.warehouse.domain.details.DetailType;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchImportTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.DetailBatchImportTransformer;
import com.artigile.warehouse.utils.transofmers.DetailTypesTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.artigile.warehouse.bl.detail.DetailBatchDataSaver.*;

/**
 * Service for working with detail batch imports.
 *
 * @author Aliaksandr.Chyrtsik, 06.11.11
 */
@Transactional(rollbackFor = BusinessException.class)
public class DetailBatchImportService {
    private DetailBatchImportDAO detailBatchImportDAO;
    private DetailTypeDAO detailTypeDAO;
    private DataImportService dataImportService;

    public DetailBatchImportService() {
    }

    public void initialize(){
        //Register import listener to synchronize results of detail batch import with related data import.
        dataImportService.addDataImportListener(new DataImportListener() {
            @Override
            public void onBeforeCompleteImport(long dataImportId, DataImportContext importContext) throws BusinessException {
                DetailBatchImport detailBatchImport = detailBatchImportDAO.get(dataImportId);
                if (detailBatchImport != null) {
                    //When detail batch import is completed store total counts of items imported.
                    DetailBatchDataSaver dataSaver = (DetailBatchDataSaver) importContext.getDataSaver();
                    detailBatchImport.setInsertedItemsCount(dataSaver.getInsertedCount());
                    detailBatchImport.setUpdatedItemsCount(dataSaver.getUpdatedCount());
                    detailBatchImport.setErrorItemsCount(dataSaver.getErrorsCount());
                }
            }

            @Override
            public void onAfterCompleteImport(long dataImportId, DataImportContext importContext) throws BusinessException {
                //No synchronization is required at this stage.
            }
        });
    }

    public List<DetailBatchImportTO> getAll() {
        return DetailBatchImportTransformer.transformList(detailBatchImportDAO.getAll());
    }

    /**
     * Enumerate all domain columns fr import for given detail type.
     * @param detailTypeId detail type for import.
     * @return list of domain columns.
     */
    public List<DomainColumn> getDomainColumnsForDetailType(long detailTypeId) {
        List<DomainColumn> domainColumns = new ArrayList<DomainColumn>();

        //1. Fields of detail type are available for import.
        DetailType detailType = detailTypeDAO.get(detailTypeId);
        for (DetailField field : detailType.getFields()){
            if (!field.getType().equals(DetailFieldType.TEMPLATE_TEXT)){
                //Only not calculated fields may be imported.
                domainColumns.add(new DomainColumn(formatModelDomainColumnId(field.getName()), field.getName(), field.getMandatory(), field.getType().equals(DetailFieldType.TEXT), " "));
            }
        }

        //2. Detail batch special fields are also available.
        domainColumns.add(new DomainColumn(DOMAIN_COLUMN_ACCEPTANCE, I18nSupport.message("detail.batches.list.acceptance"), false));
        domainColumns.add(new DomainColumn(DOMAIN_COLUMN_YEAR, I18nSupport.message("detail.batches.list.year"), false));
        domainColumns.add(new DomainColumn(DOMAIN_COLUMN_BARCODE, I18nSupport.message("detail.batches.list.barCode"), false));
        domainColumns.add(new DomainColumn(DOMAIN_COLUMN_ARTICLE, I18nSupport.message("detail.batches.list.nomenclatureArticle"), false));
        domainColumns.add(new DomainColumn(DOMAIN_COLUMN_MANUFACTURER, I18nSupport.message("detail.batches.list.manufacturer"), false));
        domainColumns.add(new DomainColumn(DOMAIN_COLUMN_BUY_PRICE, I18nSupport.message("detail.batches.list.buyPrice"), false));
        domainColumns.add(new DomainColumn(DOMAIN_COLUMN_SELL_PRICE, I18nSupport.message("detail.batches.list.sellPrice"), false));
        domainColumns.add(new DomainColumn(DOMAIN_COLUMN_MEASURE_UNIT, I18nSupport.message("detail.batches.list.measure.unit"), false));
        domainColumns.add(new DomainColumn(DOMAIN_COLUMN_NOTICE, I18nSupport.message("detail.batches.list.notice"), false, true, " "));

        return domainColumns;
    }

    /**
     * Launch new detail bath import (import is launched in background).
     * @param detailBatchImportTO new import configuration.
     */
    public void performDetailBatchImport(DetailBatchImportTO detailBatchImportTO) {
        //Store import data.
        DetailBatchImport detailBatchImport = saveDetailBatchImportWithFlush(detailBatchImportTO);
        DetailTypeTO detailTypeTO = DetailTypesTransformer.transformDetailType(detailBatchImport.getDetailType());

        //Launch import.
        dataImportService.performDataImportInBackground(
                detailBatchImport.getId(),
                new DetailBatchDataSaver(detailTypeTO, detailBatchImportTO.getCurrency(), detailBatchImportTO.getMeasureUnit()),
                I18nSupport.message("detail.batch.import.display.name.format", detailBatchImport.getDetailType().getName())
        );

        SpringServiceContext.getInstance().getDetailModelsService().refreshModelsSortNumbersByDetailType(detailBatchImport.getDetailType());
    }

    private DetailBatchImport saveDetailBatchImportWithFlush(DetailBatchImportTO detailBatchImportTO) {
        DetailBatchImport detailBatchImport = detailBatchImportDAO.get(detailBatchImportTO.getId());
        if (detailBatchImport == null) {
            //Initialize new detail batch import.
            detailBatchImport = new DetailBatchImport();
            dataImportService.initializeImport(detailBatchImport);
        }

        DetailBatchImportTransformer.update(detailBatchImport, detailBatchImportTO);
        detailBatchImportDAO.save(detailBatchImport);
        detailBatchImportDAO.flush();
        detailBatchImportDAO.refresh(detailBatchImport);
        DetailBatchImportTransformer.update(detailBatchImportTO, detailBatchImport);

        return detailBatchImport;
    }

    //======================= Spring setters ========================================
    public void setDetailBatchImportDAO(DetailBatchImportDAO detailBatchImportDAO) {
        this.detailBatchImportDAO = detailBatchImportDAO;
    }

    public void setDetailTypeDAO(DetailTypeDAO detailTypeDAO) {
        this.detailTypeDAO = detailTypeDAO;
    }

    public void setDataImportService(DataImportService dataImportService) {
        this.dataImportService = dataImportService;
    }
}
