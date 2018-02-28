/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.postings;

import com.artigile.warehouse.adapter.spi.DataSaverAdapter;
import com.artigile.warehouse.bl.dataimport.DataImportException;
import com.artigile.warehouse.bl.dataimport.DataImportUtils;
import com.artigile.warehouse.bl.detail.DetailBatchService;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;
import com.artigile.warehouse.utils.dto.postings.PostingTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data saver used for posting items import.
 *
 * @author Aliaksandr.Chyrtsik, 07.11.11
 */
public class PostingItemsDataSaver extends DataSaverAdapter {
    static final String DOMAIN_COLUMN_NAME = "NAME";
    static final String DOMAIN_COLUMN_MISC = "MISC";
    static final String DOMAIN_COLUMN_NOTICE = "NOTICE";
    static final String DOMAIN_COLUMN_NOMENCLATURE_ARTICLE = "NOMENCLATURE_ARTICLE";
    static final String DOMAIN_COLUMN_BARCODE = "BARCODE";
    static final String DOMAIN_COLUMN_COUNT = "COUNT";
    static final String DOMAIN_COLUMN_BUY_PRICE = "BUY_PRICE";
    static final String DOMAIN_COLUMN_POSTING_NOTICE = "POSTING_NOTICE";

    private PostingTOForReport posting;
    private PostingItemsIdentityType itemsIdentityType;
    private CurrencyTO currency;
    private MeasureUnitTO measureUnit;
    private StoragePlaceTO storagePlace;

    private PostingService postingService = SpringServiceContext.getInstance().getPostingsService();
    private DetailBatchService detailBatchService = SpringServiceContext.getInstance().getDetailBatchesService();

    /**
     * Count of items imported successfully.
     */
    private int importedItemsCount;

    /**
     * Items with errors.
     */
    private List<PostingItemImportError> errorItems = new ArrayList<PostingItemImportError>();

    public PostingItemsDataSaver(PostingTOForReport posting,
                                 PostingItemsIdentityType itemsIdentityType,
                                 CurrencyTO currency,
                                 MeasureUnitTO measureUnit,
                                 StoragePlaceTO storagePlace) {
        this.posting = posting;
        this.itemsIdentityType = itemsIdentityType;
        this.currency = currency;
        this.measureUnit = measureUnit;
        this.storagePlace = storagePlace;
    }

    public int getImportedItemsCount() {
        return importedItemsCount;
    }

    public List<PostingItemImportError> getErrorItems() {
        return errorItems;
    }

    @Override
    public void saveDataRow(Map<String, String> dataRow) {
        try{
            PostingItemTO postingItem = parsePostingItem(dataRow);
            postingService.addItemToPosting(postingItem);
            importedItemsCount++;
        }
        catch(DataImportException e){
            //This error is expected (some positions may be parsed with errors).
            errorItems.add(new PostingItemImportError(e.getLocalizedMessage(), new HashMap<String, String>(dataRow)));
        }
        catch(Exception e){
            LoggingFacade.logError(this, "Unexpected error during posting item import. Item:" + DataImportUtils.formatDataRow(dataRow), e);
            errorItems.add(new PostingItemImportError(e.getLocalizedMessage(), new HashMap<String, String>(dataRow)));
        }
    }

    private PostingItemTO parsePostingItem(Map<String, String> dataRow) throws DataImportException {
        DetailBatchTO detailBatch = parseDetailBatch(dataRow);
        PostingItemTO postingItem = new PostingItemTO(posting, detailBatch);

        postingItem.setOriginalCurrency(currency);
        postingItem.setStoragePlace(storagePlace);
        postingItem.setCount(DataImportUtils.parseLongValue(dataRow.get(DOMAIN_COLUMN_COUNT), I18nSupport.message("posting.items.import.item.field.count")));
        postingItem.setOriginalPrice(DataImportUtils.parseBigDecimalValue(dataRow.get(DOMAIN_COLUMN_BUY_PRICE), I18nSupport.message("posting.items.import.item.field.buyPrice")));
        postingItem.setNotice(DataImportUtils.parseStringValue(dataRow.get(DOMAIN_COLUMN_POSTING_NOTICE), I18nSupport.message("posting.items.import.item.field.postingNotice")));

        return postingItem;
    }

    private DetailBatchTO parseDetailBatch(Map<String, String> dataRow) throws DataImportException {
        List<DetailBatchTO> foundDetailBatches;
        if (itemsIdentityType.equals(PostingItemsIdentityType.NAME_MISC_NOTICE)){
            //Find detail batch by name, misc and notice fields.
            String name = DataImportUtils.parseStringValue(dataRow.get(DOMAIN_COLUMN_NAME), I18nSupport.message("posting.items.import.item.field.name"));
            String misc = DataImportUtils.parseStringValue(dataRow.get(DOMAIN_COLUMN_MISC), I18nSupport.message("posting.items.import.item.field.misc"));
            String notice = DataImportUtils.parseStringValue(dataRow.get(DOMAIN_COLUMN_NOTICE), I18nSupport.message("posting.items.import.item.field.notice"));
            foundDetailBatches = detailBatchService.getDetailBatches(name, misc, notice);
        }
        else if (itemsIdentityType.equals(PostingItemsIdentityType.NOMENCLATURE_ARTICLE)){
            //Find detail batch by nomenclature article.
            String article = DataImportUtils.parseStringValue(dataRow.get(DOMAIN_COLUMN_NOMENCLATURE_ARTICLE), I18nSupport.message("posting.items.import.item.field.nomenclatureArticle"));
            foundDetailBatches = detailBatchService.getDetailBatchesByArticle(article);
        }
        else if (itemsIdentityType.equals(PostingItemsIdentityType.BARCODE)){
            //Find detail batch by barcode.
            String barcode = DataImportUtils.parseStringValue(dataRow.get(DOMAIN_COLUMN_BARCODE), I18nSupport.message("posting.items.import.item.field.barCode"));
            DetailBatchTO detailBatch = detailBatchService.getDetailBatchByBarCode(barcode);
            foundDetailBatches = new ArrayList<DetailBatchTO>();
            if (detailBatch != null){
                foundDetailBatches.add(detailBatch);
            }
        }
        else{
            throw new AssertionError("Forgot to implement new identity type support? Identity type: " + itemsIdentityType.name());
        }

        if (foundDetailBatches.isEmpty()){
            throw new DataImportException(I18nSupport.message("posting.items.import.error.cannot.find.detail.batch"));
        }
        else if (foundDetailBatches.size() > 1){
            throw new DataImportException(I18nSupport.message("posting.items.import.error.more.than.one.detail.batch"));
        }

        return foundDetailBatches.get(0);
    }
}
