/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.detail;

import com.artigile.warehouse.adapter.spi.DataSaverAdapter;
import com.artigile.warehouse.bl.dataimport.DataImportException;
import com.artigile.warehouse.bl.dataimport.DataImportUtils;
import com.artigile.warehouse.bl.directory.MeasureUnitService;
import com.artigile.warehouse.utils.MiscUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;
import com.artigile.warehouse.utils.dto.details.DetailModelTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.util.List;
import java.util.Map;

/**
 * Saved for storing of imported detail batches.
 *
 * @author Aliaksandr.Chyrtsik, 06.11.11
 */
public class DetailBatchDataSaver extends DataSaverAdapter {
    /**
     * All supported detail batch fields (available for import).
     */
    static final String DOMAIN_COLUMN_ACCEPTANCE = "detail.batch.ACCEPTANCE";
    static final String DOMAIN_COLUMN_YEAR = "detail.batch.YEAR";
    static final String DOMAIN_COLUMN_BARCODE = "detail.batch.BARCODE";
    static final String DOMAIN_COLUMN_ARTICLE = "detail.batch.ARTICLE";
    static final String DOMAIN_COLUMN_MANUFACTURER = "detail.batch.MANUFACTURER";
    static final String DOMAIN_COLUMN_BUY_PRICE = "detail.batch.BUY_PRICE";
    static final String DOMAIN_COLUMN_SELL_PRICE = "detail.batch.SELL_PRICE";
    static final String DOMAIN_COLUMN_NOTICE = "detail.batch.NOTICE";
    static final String DOMAIN_COLUMN_MEASURE_UNIT = "detail.batch.MEASURE_UNIT";

    private static final String MODEL_DOMAIN_COLUMN_PREFIX = "detail.model.";

    static String formatModelDomainColumnId(String name) {
        return MODEL_DOMAIN_COLUMN_PREFIX + name;
    }

    /**
     * Configuration for proper saving of detail batches being imported.
     */
    private DetailTypeTO detailType;
    private CurrencyTO currency;
    private MeasureUnitTO measureUnit;

    /**
     * Current import state
     */
    private int insertedCount;
    private int updatedCount;
    private int errorsCount;

    private DetailModelService detailModelService = SpringServiceContext.getInstance().getDetailModelsService();
    private DetailBatchService detailBatchService = SpringServiceContext.getInstance().getDetailBatchesService();
    private MeasureUnitService measureUnitService = SpringServiceContext.getInstance().getMeasureUnitService();

    public DetailBatchDataSaver(DetailTypeTO detailType, CurrencyTO currency, MeasureUnitTO measureUnit) {
        this.detailType = detailType;
        this.currency = currency;
        this.measureUnit = measureUnit;
    }

    public int getInsertedCount() {
        return insertedCount;
    }

    public int getUpdatedCount() {
        return updatedCount;
    }

    public int getErrorsCount() {
        return errorsCount;
    }

    @Override
    public void saveDataRow(Map<String, String> dataRow) {
        try{
            //1. Parse detail model.
            DetailModelTO detailModel = importDetailModel(dataRow);

            //2. Parse detail batch for found detail model.
            DetailBatchTO detailBatch = parseDetailBatch(detailModel, dataRow);

            //3. List detail batch with an existing record or create new one.
            List<DetailBatchTO> sameDetailBatches = detailBatchService.getSameDetailBatches(detailBatch);
            if (sameDetailBatches.isEmpty()) {
                //Create new detail batch.
                detailBatchService.saveDetailBatch(detailBatch);
                insertedCount++;

                //TODO: implement result logging here (list of import results).
            }
            else {
                //Update existing detail batch.
                if (sameDetailBatches.size() > 1) {
                    throw new DataImportException("detail.batch.import.error.more.than.one.same.batch");
                }

                detailBatch = updateDetailBatch(sameDetailBatches.get(0), detailBatch);
                detailBatchService.saveDetailBatch(detailBatch);
                updatedCount++;

                //TODO: implement result logging here (list of import results).
            }
        }
        catch (DataImportException e) {
            //This is allowed -- due to import configuration not all items may be parsed properly.
            errorsCount++;

            //TODO: implement result logging here (list of import results).
            LoggingFacade.logInfo(e);
        }
        catch (Exception e) {
            //This exception is not expected. Log it (but do not stop import process).
            LoggingFacade.logError(this, "Unexpected error during detail batch parsing. Detail batch: " + DataImportUtils.formatDataRow(dataRow), e);
            errorsCount++;
        }
    }

    private DetailBatchTO updateDetailBatch(DetailBatchTO targetBatch, DetailBatchTO importedBatch) {
        if (!MiscUtils.objectsEquals(targetBatch.getAcceptance(), importedBatch.getAcceptance())) {
            targetBatch.setAcceptance(importedBatch.getAcceptance());
        }
        if (!MiscUtils.objectsEquals(targetBatch.getYear(), importedBatch.getYear())) {
            targetBatch.setYear(importedBatch.getYear());
        }
        if (!MiscUtils.objectsEquals(targetBatch.getBarCode(), importedBatch.getBarCode())) {
            targetBatch.setBarCode(importedBatch.getBarCode());
        }
        if (!MiscUtils.objectsEquals(targetBatch.getNomenclatureArticle(), importedBatch.getNomenclatureArticle())) {
            targetBatch.setNomenclatureArticle(importedBatch.getNomenclatureArticle());
        }
        if (!MiscUtils.objectsEquals(targetBatch.getManufacturer(), importedBatch.getManufacturer())) {
            targetBatch.setManufacturer(importedBatch.getManufacturer());
        }
        if (!MiscUtils.objectsEquals(targetBatch.getBuyPrice(), importedBatch.getBuyPrice())) {
            targetBatch.setBuyPrice(importedBatch.getBuyPrice());
        }
        if (!MiscUtils.objectsEquals(targetBatch.getSellPrice(), importedBatch.getSellPrice())) {
            targetBatch.setSellPrice(importedBatch.getSellPrice());
        }
        if (!MiscUtils.objectsEquals(targetBatch.getCountMeas(), importedBatch.getCountMeas())) {
            targetBatch.setCountMeas(importedBatch.getCountMeas());
        }
        if (!MiscUtils.objectsEquals(targetBatch.getNotice(), importedBatch.getNotice())) {
            targetBatch.setNotice(importedBatch.getNotice());
        }
        return targetBatch;
    }

    //============================ Parsing helpers ==================================================

    private DetailBatchTO parseDetailBatch(DetailModelTO detailModel, Map<String, String> dataRow) throws DataImportException {
        DetailBatchTO detailBatch = new DetailBatchTO();

        detailBatch.setModel(detailModel);
        detailBatch.setCurrency(currency);
        String measUnit = DataImportUtils.parseStringValue(dataRow.get(DOMAIN_COLUMN_MEASURE_UNIT), I18nSupport.message("detail.batches.list.measure.unit"));
        MeasureUnitTO measureUnitTO = measureUnitService.findByName(measUnit);
        if (measureUnitTO == null) {
            measureUnitTO = measureUnitService.findBySign(measUnit);
        }
        detailBatch.setCountMeas(measureUnitTO != null ? measureUnitTO : measureUnit);

        detailBatch.setAcceptance(DataImportUtils.parseStringValue(dataRow.get(DOMAIN_COLUMN_ACCEPTANCE), I18nSupport.message("detail.batches.list.acceptance")));
        detailBatch.setYear(DataImportUtils.parseIntegerValue(dataRow.get(DOMAIN_COLUMN_YEAR), I18nSupport.message("detail.batches.list.year")));
        detailBatch.setBarCode(DataImportUtils.parseStringValue(dataRow.get(DOMAIN_COLUMN_BARCODE), I18nSupport.message("detail.batches.list.barCode")));
        detailBatch.setNomenclatureArticle(DataImportUtils.parseStringValue(dataRow.get(DOMAIN_COLUMN_ARTICLE), I18nSupport.message("detail.batches.list.nomenclatureArticle")));
        detailBatch.setManufacturer(DataImportUtils.parseManufacturerValue(dataRow.get(DOMAIN_COLUMN_MANUFACTURER), I18nSupport.message("detail.batches.list.manufacturer")));
        detailBatch.setBuyPrice(DataImportUtils.parseBigDecimalValue(dataRow.get(DOMAIN_COLUMN_BUY_PRICE), I18nSupport.message("detail.batches.list.buyPrice")));
        detailBatch.setSellPrice(DataImportUtils.parseBigDecimalValue(dataRow.get(DOMAIN_COLUMN_SELL_PRICE), I18nSupport.message("detail.batches.list.sellPrice")));
        detailBatch.setNotice(DataImportUtils.parseStringValue(dataRow.get(DOMAIN_COLUMN_NOTICE), I18nSupport.message("detail.batches.list.notice")));

        return detailBatch;
    }

    private DetailModelTO importDetailModel(Map<String, String> dataRow) throws DataImportException {
        //1. Parse detail model.
        DetailModelTO detailModel = parseDetailModel(dataRow);
        if (!detailModel.validate()) {
            throw new DataImportException(I18nSupport.message("detail.batch.import.error.invalid.detail.model", detailModel));
        }

        //2. Link with existing model or create new one.
        List<DetailModelTO> sameModels = detailModelService.getSameModels(detailModel);
        if (sameModels.isEmpty()) {
            //Create new model.
            detailModelService.saveDetailModel(detailModel, false);
        }
        else {
            //Link to an existing model.
            if (sameModels.size() > 1) {
                //Cannot determine which model to user.
                throw new DataImportException(I18nSupport.message("detail.batch.import.error.more.than.one.same.model"));
            }
            detailModel = sameModels.get(0);
        }

        return detailModel;
    }

    private DetailModelTO parseDetailModel(Map<String, String> dataRow) throws DataImportException {
        DetailModelTO detailModel = new DetailModelTO();
        detailModel.setType(detailType);

        for (String domainColumnId : dataRow.keySet()) {
            if (!domainColumnId.startsWith(MODEL_DOMAIN_COLUMN_PREFIX)) {
                //Skip all non model fields.
                continue;
            }

            String modelFieldName = domainColumnId.substring(MODEL_DOMAIN_COLUMN_PREFIX.length());
            DetailFieldValueTO field = detailModel.getFieldByName(modelFieldName);
            if (field == null) {
                //Error in import configuration? Not existing model field.
                throw new DataImportException(I18nSupport.message("detail.batch.import.error.model.field.not.exists", modelFieldName));
            }

            String fieldValue = dataRow.get(domainColumnId);
            if (fieldValue != null) {
                fieldValue = fieldValue.trim();
            }

            if (!field.validateValue(fieldValue)) {
                //Not valid value of model field.
                throw new DataImportException(I18nSupport.message("detail.batch.import.error.invalid.model.field.value", modelFieldName, fieldValue));
            }

            field.setValue(fieldValue);
        }

        return detailModel;
    }
}
