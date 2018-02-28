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

import com.artigile.warehouse.dao.DetailBatchDAO;
import com.artigile.warehouse.dao.DetailGroupDAO;
import com.artigile.warehouse.domain.details.*;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailGroupTO;
import com.artigile.warehouse.utils.properties.Properties;
import com.artigile.warehouse.utils.transofmers.DetailBatchTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Shyrik, 26.12.2008
 */
@Transactional
public class DetailBatchService {
    
    private DetailBatchDAO detailBatchesDAO;
    private DetailGroupDAO detailGroupDAO;

    public DetailBatchService() {
    }

    public List<DetailBatchTO> getAllBatchesSortedByPriceNumber() {
        return DetailBatchTransformer.list(detailBatchesDAO.getAllSortedByPriceNumber());
    }

    public List<DetailBatchTO> getFilteredByCatalogGroupBatches(DetailGroupTO catalogGroup) {
        DetailGroup persistentGroup = getFirstPersistentCatalogGroup(catalogGroup);
        Collection<DetailType> allDetailTypesInGroup = getDetailTypesInGroups(persistentGroup, new HashSet<DetailType>());
        return DetailBatchTransformer.list(detailBatchesDAO.getByDetailTypes(allDetailTypesInGroup, catalogGroup.getFilterByGroupingFields()));
    }

    private DetailGroup getFirstPersistentCatalogGroup(DetailGroupTO catalogGroup) {
        if (!catalogGroup.isAutoGroup()){
            return detailGroupDAO.get(catalogGroup.getId());
        }
        else{
            return getFirstPersistentCatalogGroup(catalogGroup.getParentGroup());
        }
    }

    private Collection<DetailType> getDetailTypesInGroups(DetailGroup group, Collection<DetailType> detailTypes) {
        detailTypes.addAll(group.getDetailTypes());
        for (DetailGroup subGroup : group.getChildGroups()){
            getDetailTypesInGroups(subGroup, detailTypes);
        }
        return detailTypes;
    }

    public DetailBatch getDetailBatchById(long detailBatchId) {
        return detailBatchesDAO.get(detailBatchId);
    }

    public DetailBatch getDetailBatchByUid(String uid) {
        return detailBatchesDAO.getDetailByUid(uid);
    }

    public List<String> getDetailBatchUidsByIds(List<Long> ids) {
        return detailBatchesDAO.getUidsByIds(ids);
    }

    public void saveDetailBatch(DetailBatchTO detailBatchTO) {
        DetailBatch persistentDetailBatch = detailBatchesDAO.get(detailBatchTO.getId());
        if (persistentDetailBatch == null) {
            persistentDetailBatch = new DetailBatch();
        }
        DetailBatchTransformer.batch(persistentDetailBatch, detailBatchTO);

        detailBatchesDAO.save(persistentDetailBatch);
        refreshDetailBatchSortNumbersByModel(persistentDetailBatch.getModel());

        DetailBatchTransformer.batchTO(detailBatchTO, persistentDetailBatch);
    }

    public boolean isUniqueDetailBatch(DetailBatchTO detailBatchTO) {
        //Detail batch is unique, if there is no detail batches with same name and misc fields.
        List<DetailBatch> sameBatches = detailBatchesDAO.getSameBatches(detailBatchTO.getName(), detailBatchTO.getMisc(), detailBatchTO.getNotice());
        if (sameBatches.size() > 0 && detailBatchTO.isNew()){
            return false;
        }
        for (DetailBatch sameBatch : sameBatches){
            if (sameBatch.getId() != detailBatchTO.getId()){
                return false;
            }
        }
        return true;
    }

    public boolean isUniqueDetailBatchByBarCode(DetailBatchTO detailBatchTO) {
        //Detail batch is unique, if there is no detail batches with same bar code field.
        DetailBatch sameBatch = detailBatchesDAO.getBatchByBarCode(detailBatchTO.getBarCode());
        return sameBatch == null || (sameBatch.getId() == detailBatchTO.getId());
    }

    public boolean isUniqueDetailBatchByNomenclatureArticle(DetailBatchTO detailBatchTO) {
        //Detail batch is unique, if there is no detail batches with same nomenclature article field.
        List<DetailBatch> sameBatches = detailBatchesDAO.getBatchesByNomenclatureArticle(detailBatchTO.getNomenclatureArticle());
        if (sameBatches.size() > 0 && detailBatchTO.isNew()) {
            return false;
        }
        for (DetailBatch sameBatch : sameBatches) {
            if (sameBatch.getId() != detailBatchTO.getId()) {
                return false;
            }
        }
        return true;
    }

    public List<DetailBatchTO> getSameDetailBatches(DetailBatchTO detailBatchTO) {
        return DetailBatchTransformer.list(
                detailBatchesDAO.getSameBatches(
                    detailBatchTO.getName(),
                    detailBatchTO.getMisc(),
                    detailBatchTO.getNotice(),
                    detailBatchTO.getNomenclatureArticle(),
                    detailBatchTO.getBarCode()
                )
        );
    }

    public List<DetailBatchTO> getDetailBatches(String name, String misc, String notice) {
        return DetailBatchTransformer.list(detailBatchesDAO.getSameBatches(name, misc, notice));
    }

    public List<DetailBatchTO> getDetailBatchesByArticle(String article) {
        return DetailBatchTransformer.list(detailBatchesDAO.getBatchesByNomenclatureArticle(article));
    }

    public DetailBatchTO getDetailBatchByBarCode(String barcode) {
        return DetailBatchTransformer.batchTO(detailBatchesDAO.getBatchByBarCode(barcode));
    }

    public List<DetailBatchTO> getAvailableDetailBatches(DetailType detailType, Map<DetailFieldTO, String> fieldValues) {
        return DetailBatchTransformer.list(detailBatchesDAO.getAvailableDetailBatches(detailType, toFieldValues(fieldValues)));
    }

    private Map<DetailField, String> toFieldValues(Map<DetailFieldTO, String> fieldValues) {
        Map<DetailField, String> result = new HashMap<DetailField, String>();
        for (Map.Entry<DetailFieldTO, String> entry : fieldValues.entrySet()) {
            DetailFieldTO detailFieldTO = entry.getKey();
            DetailField detailField = new DetailField(detailFieldTO.getId());
            detailField.setFieldIndex(detailFieldTO.getFieldIndex());
            result.put(detailField, entry.getValue());
        }
        return result;
    }

    public void refreshDetailBatchSortNumbersByModel(DetailModel model) {
        //TODO: Either implement faster calculation or remove this feature completely.
        //detailBatchesDAO.refreshDetailBatchSortNumbersByModel(model);
    }

    /**
     * Refreshes detail batch calculated fields to prevent of storing stale data in the database.
     * @param detailModel detail model, which detail batches shoud be updated.
     */
    public void refreshDetailBatchCalculatedFieldsByModel(DetailModel detailModel) {
        List<DetailBatch> detailBatches = detailBatchesDAO.getBatchesByDetailModel(detailModel);
        for (DetailBatch detailBatch : detailBatches){
            //Entity -> DTO (calculation) -> Entity (update from DTO)
            DetailBatchTransformer.batch(detailBatch, DetailBatchTransformer.batchTO(detailBatch));
        }
    }

    public void deleteDetailBatch(long batchId) {
        DetailBatch persistentDetailBatch = detailBatchesDAO.get(batchId);
        if (persistentDetailBatch != null) {
            detailBatchesDAO.remove(persistentDetailBatch);
        }
    }

    public DetailBatchTO getBatch(long detailBatchId) {
        return DetailBatchTransformer.batchTO(detailBatchesDAO.get(detailBatchId));
    }

    //============================= BarCode generation ====================================

    private String barCodePrefix;
    private static final String PROPERTY_BAR_CODE_PREFIX = "barcode.prefix";

    private Integer barCodeArticleLength;
    private static final String PROPERTY_BAR_CODE_ARTICLE_LENGTH = "barcode.article.length";

    private Boolean barCodeGenerateControlNumber;
    private static final String PROPERTY_BAR_CODE_GENERATE_CONTROL_NUMBER = "barcode.generate.control.number";

    public String getBarCodePrefix() {
        if (barCodePrefix == null){
            barCodePrefix = Properties.getProperty(PROPERTY_BAR_CODE_PREFIX);
        }
        return barCodePrefix;
    }

    public void setBarCodePrefix(String barCodePrefix) {
        Properties.setProperty(PROPERTY_BAR_CODE_PREFIX, barCodePrefix);
        this.barCodePrefix = barCodePrefix;
    }

    public Integer getBarCodeArticleLength() {
        if (barCodeArticleLength == null){
            barCodeArticleLength = Properties.getPropertyAsInteger(PROPERTY_BAR_CODE_ARTICLE_LENGTH);
        }
        return barCodeArticleLength;
    }

    public void setBarCodeArticleLength(int barCodeArticleLength) {
        Properties.setProperty(PROPERTY_BAR_CODE_ARTICLE_LENGTH, String.valueOf(barCodeArticleLength));
        this.barCodeArticleLength = barCodeArticleLength;
    }

    public Boolean isBarCodeGenerateControlNumber() {
        if (barCodeGenerateControlNumber == null){
            barCodeGenerateControlNumber = Properties.getPropertyAsBoolean(PROPERTY_BAR_CODE_GENERATE_CONTROL_NUMBER);
        }
        return barCodeGenerateControlNumber;
    }

    public void setBarCodeGenerateControlNumber(boolean barCodeGenerateControlNumber) {
        Properties.setProperty(PROPERTY_BAR_CODE_GENERATE_CONTROL_NUMBER, String.valueOf(barCodeGenerateControlNumber));
        this.barCodeGenerateControlNumber = barCodeGenerateControlNumber;
    }

    public int getBarCodePrefixMaxLength() {
        return 16; //Max length of bar code prefix according to specification.
    }

    public int getBarCodeMinArticleLength() {
        return 3; //Min length of article number (taken from EAN13 specification).
    }

    public int getBarCodeMaxArticleLength() {
        return 16; //Max length of article number (just a big enough number).
    }

    /**
     * @return number of products without bar codes.
     */
    public int getNumberOfDetailBatchesWithoutBarCode() {
        DetailBatchFilter filter = new DetailBatchFilter();
        filter.setWithoutBarCode(true);
        return detailBatchesDAO.getDetailBatchesCountByFilter(filter);
    }

    /**
     * Generate bar codes for all products without bar code.
     */
    public void generateBarCodes() {
        DetailBatchFilter filter = new DetailBatchFilter();
        filter.setWithoutBarCode(true);
        List<DetailBatch> detailBatches = detailBatchesDAO.getDetailBatchesByFilter(filter);

        //Generate bar code for each of loaded products.
        long articleNum = detailBatchesDAO.getMaxUsedBarCodeArticle(getBarCodePrefix(), getBarCodeArticleLength(), isBarCodeGenerateControlNumber() ? 1 : 0);
        for (DetailBatch detailBatch : detailBatches){
            articleNum++;
            detailBatch.setBarCode(StringUtils.generateBarCode(getBarCodePrefix(), articleNum, getBarCodeArticleLength(), isBarCodeGenerateControlNumber()));
            detailBatchesDAO.update(detailBatch);
        }
    }

    /**
     * Generate new barcode that wll be unique.
     * @return new bar code.
     */
    public String generateBarCode() {
        long articleNum = detailBatchesDAO.getMaxUsedBarCodeArticle(getBarCodePrefix(), getBarCodeArticleLength(), isBarCodeGenerateControlNumber() ? 1 : 0);
        return StringUtils.generateBarCode(getBarCodePrefix(), articleNum + 1, getBarCodeArticleLength(), isBarCodeGenerateControlNumber());
    }

    //========================= Spring setters ============================================
    public void setDetailBatchDAO(DetailBatchDAO detailBatchesDAO) {
        this.detailBatchesDAO = detailBatchesDAO;
    }

    public void setDetailGroupDAO(DetailGroupDAO detailGroupDAO) {
        this.detailGroupDAO = detailGroupDAO;
    }
}
