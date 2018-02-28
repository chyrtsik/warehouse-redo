/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.postings;

import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.bl.common.verifications.Verifications;
import com.artigile.warehouse.bl.dataimport.DataImportFinishListener;
import com.artigile.warehouse.bl.dataimport.DataImportService;
import com.artigile.warehouse.bl.finance.CurrencyExchangeService;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchChangeDocument;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.dao.DetailBatchDAO;
import com.artigile.warehouse.dao.PostingDAO;
import com.artigile.warehouse.dao.PostingItemDAO;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteItem;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.postings.Posting;
import com.artigile.warehouse.domain.postings.PostingItem;
import com.artigile.warehouse.domain.postings.PostingState;
import com.artigile.warehouse.domain.purchase.PurchaseItem;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;
import com.artigile.warehouse.utils.dto.postings.PostingTO;
import com.artigile.warehouse.utils.dto.postings.PostingTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.PostingsTransformer;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.artigile.warehouse.bl.postings.PostingItemsDataSaver.*;

/**
 * @author Shyrik, 02.02.2009
 */

/**
 * Service class for working with postings.
 */
@Transactional(rollbackFor = BusinessException.class)
public class PostingService {
    
    private PostingDAO postingDAO;

    private PostingItemDAO postingItemDAO;

    private DetailBatchDAO detailBatchDAO;

    public PostingService() { /* Default constructor */ }

    //======================= Listeners support ====================================
    /**
     * List of listeners on action of changing posting states.
     */
    private ArrayList<PostingChangeListener> listeners = new ArrayList<PostingChangeListener>();

    public void addListener(PostingChangeListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    /**
     * Notify that new posting is created.
     * @param posting New posting which created.
     * @throws BusinessException
     */
    private void firePostingCreated(Posting posting) throws BusinessException {
        for (PostingChangeListener listener : listeners){
            listener.onPostingCreated(posting);
        }
    }

    /**
     * Notify that posting is deleted.
     * @param posting Old posting which deleted.
     * @throws BusinessException
     */
    private void firePostingDeleted(Posting posting) throws BusinessException {
        for (PostingChangeListener listener : listeners){
            listener.onPostingDeleted(posting);
        }
    }

    /**
     * Notify that posting is completed.
     * @param posting Posting which completed.
     * @throws BusinessException
     */
    private void firePostingCompleted(Posting posting) throws BusinessException {
        for (PostingChangeListener listener : listeners){
            listener.onPostingCompleted(posting);
        }
    }

    //============================== Operations =====================================

    public List<PostingTOForReport> getAllPostings() {
        return PostingsTransformer.transformPostingList(postingDAO.getAll());
    }

    public PostingTOForReport getPostingForReport(long postingId) {
        return PostingsTransformer.transformPostingForReport(postingDAO.get(postingId));
    }

    public PostingTO getPostingFullData(long postingId) {
        return PostingsTransformer.transform(postingDAO.get(postingId));
    }

    public void savePosting(PostingTOForReport posting) {
        Posting persistentPosting = postingDAO.get(posting.getId());
        if (persistentPosting == null){
            persistentPosting = new Posting();
        }
        else if (persistentPosting.getCurrency().getId() != posting.getCurrency().getId()){
            //We needs to recalculate prices of the items in the posting in the new currency.
            CurrencyExchangeService exchange = SpringServiceContext.getInstance().getCurencyExchangeService();
            long oldCurrencyId = persistentPosting.getCurrency().getId();
            long newCurrencyId = posting.getCurrency().getId();
            for (PostingItem item : persistentPosting.getItems()){
                item.setPrice(exchange.convert(newCurrencyId, oldCurrencyId, item.getPrice()));
            }
        }

        PostingsTransformer.update(persistentPosting, posting);
        postingDAO.save(persistentPosting);
        postingDAO.flush();
        postingDAO.refresh(persistentPosting);
        PostingsTransformer.transformPostingForReport(posting, persistentPosting);
    }

    public void saveFullPosting(PostingTO posting) {
        savePosting(posting);
        List<PostingItemTO> items = posting.getItems();
        for (PostingItemTO item : items) {
            savePostingItem(item);
        }
    }

    public void deletePosting(PostingTOForReport postingToDelete) throws BusinessException {
        Posting persistentPosting = postingDAO.get(postingToDelete.getId());
        firePostingDeleted(persistentPosting);
        postingDAO.remove(persistentPosting);
    }

    public boolean isPostingEditable(long postingId){
        return postingDAO.get(postingId).getState().equals(PostingState.CONSTRUCTION);
    }

    /**
     * Make posting 'completed'.
     * @param postingId identifier of posting document to be completed.
     * @throws BusinessException
     */
    public void completePosting(long postingId) throws BusinessException {
        Posting persistentPosting = postingDAO.get(postingId);
        completePosting(persistentPosting);
    }

    public void completePosting(Posting posting) throws BusinessException {
        //1. Verify, is posting is ready to be completed.
        VerificationResult verificationResult = Verifications.performVerification(posting, new BeforePostingCompleteVerification());
        if (verificationResult.isFailed()){
            throw new BusinessException(verificationResult.getFailReason());
        }

        //2. Make posting completed.
        //2.1. New state of the posting.
        posting.setState(PostingState.COMPLETED);
        postingDAO.save(posting);

        //2.2. Creating wares at the warehouse and and warehouse's income againnst the posting.
        WarehouseBatchService batchService = SpringServiceContext.getInstance().getWarehouseBatchService();
        for (PostingItem item : posting.getItems()){
            WarehouseBatchChangeDocument document = WarehouseBatchChangeDocument.createPostingDocument(item);
            batchService.performWareIncome(item.getDetailBatch(), item.getAmount(), item.getStoragePlace(), item.getNotice(), document);
        }

        //2.3. Storing new cell and buy price for detail batches.
        if (posting.getCurrency() != null){
            for (PostingItem item : posting.getItems()){
                item.getDetailBatch().setCurrency(item.getPosting().getCurrency());
                if (item.getPrice() != null){
                    item.getDetailBatch().setBuyPrice(item.getPrice());
                }
                if (item.getSalePrice() != null){
                    item.getDetailBatch().setSellPrice(item.getSalePrice());
                }
            }
        }

        //3. Notice purchase, that posting has been finished.
        firePostingCompleted(posting);
    }

    /**
     * Creates posting from purchase.
     * @param newPostingTO
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    public void createPostingFromPurchase(PostingTOForReport newPostingTO) throws BusinessException {
        //1. Creating new posting.
        Posting posting = new Posting();
        PostingsTransformer.update(posting, newPostingTO);
        postingDAO.save(posting);

        //2. Filling posting list from the purchase.
        CurrencyExchangeService exchangeService = SpringServiceContext.getInstance().getCurencyExchangeService();
        for (PurchaseItem purchaseItem : posting.getPurchase().getItems()){
            if (!purchaseItem.isText()){
                PostingItem postingItem = new PostingItem();

                postingItem.setPosting(posting);
                postingItem.setNumber(postingItemDAO.getNextAvailableNumber(posting.getId()));
                postingItem.setStoragePlace(posting.getDefaultStoragePlace());
                postingItem.setDetailBatch(purchaseItem.getWareNeedItem().getDetailBatch());
                postingItem.setOriginalPrice(purchaseItem.getPrice());
                postingItem.setOriginalCurrency(purchaseItem.getPurchase().getCurrency());
                postingItem.setAmount(purchaseItem.getAmount());
                postingItem.setNotice(purchaseItem.getNotice());

                long originalCurrencyId = postingItem.getOriginalCurrency().getId();
                long targetCurrencyId = posting.getCurrency().getId();
                if (originalCurrencyId == targetCurrencyId){
                    postingItem.setPrice(postingItem.getOriginalPrice());
                }
                else{
                    postingItem.setPrice(exchangeService.convert(targetCurrencyId, originalCurrencyId, postingItem.getOriginalPrice()));
                }

                posting.getItems().add(postingItem);
            }
        }

        //3. Refreshing posting total price.
        postingDAO.flush();
        postingDAO.refresh(posting);

        //4. Notify about creating posting against it.
        firePostingCreated(posting);

        //5. Refreshing DTO with fresh posting data.
        PostingsTransformer.transformPostingForReport(newPostingTO, posting);
    }

    /**
     * Creates posting for delivery note.
     * @param newPostingTO
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    public void createPostingFromDeliveryNote(PostingTOForReport newPostingTO) throws BusinessException {
        //1. Creating new posting.
        Posting posting = new Posting();
        PostingsTransformer.update(posting, newPostingTO);
        postingDAO.save(posting);

        //2. Filling posting list from the purchase.
        CurrencyExchangeService exchangeService = SpringServiceContext.getInstance().getCurencyExchangeService();
        for (DeliveryNoteItem deliveryNoteItem : posting.getDeliveryNote().getItems()){
            PostingItem postingItem = new PostingItem();

            postingItem.setPosting(posting);
            postingItem.setNumber(postingItemDAO.getNextAvailableNumber(posting.getId()));
            postingItem.setStoragePlace(deliveryNoteItem.getStoragePlace() != null ? deliveryNoteItem.getStoragePlace() : posting.getDefaultStoragePlace());
            postingItem.setDetailBatch(deliveryNoteItem.getDetailBatch());
            postingItem.setOriginalPrice(deliveryNoteItem.getPrice());
            postingItem.setOriginalCurrency(deliveryNoteItem.getDeliveryNote().getCurrency());
            postingItem.setAmount(deliveryNoteItem.getAmount());
            postingItem.setShelfLifeDate(deliveryNoteItem.getShelfLifeDate());
            postingItem.setNotice(deliveryNoteItem.getWarehouseBatchNotice());

            long originalCurrencyId = postingItem.getOriginalCurrency().getId();
            long targetCurrencyId = posting.getCurrency().getId();
            if (originalCurrencyId == targetCurrencyId){
                postingItem.setPrice(postingItem.getOriginalPrice());
            }
            else{
                postingItem.setPrice(exchangeService.convert(targetCurrencyId, originalCurrencyId, postingItem.getOriginalPrice()));
            }

            posting.getItems().add(postingItem);
        }

        //3. Refreshing posting total price.
        postingDAO.flush();
        postingDAO.refresh(posting);

        //4. Notify purchase about creating posting against it.
        firePostingCreated(posting);

        //5. Refreshing DTO with fresh posting data.
        PostingsTransformer.transformPostingForReport(newPostingTO, posting);
    }

    /**
     * Use this method to obtain next available unique number of the posting. 
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long getNextAvailablePostingNumber() {
        return postingDAO.getNextAvailablePostingNumber();
    }

    /**
     * Checks, is given posting number will be unique.
     * @param number
     * @param postingId
     * @return
     */
    public boolean isUniquePostingNumber(long number, long postingId) {
        Posting samePosting = postingDAO.getPostingByNumber(number);
        return samePosting == null || samePosting.getId() == postingId;
    }


    public Posting getPostingById(long postingId) {
        return postingDAO.get(postingId);
    }

    /**
     * Adds new item to the posting.
     * @param newPostingItemTO
     */
    public void addItemToPosting(PostingItemTO newPostingItemTO) {
        //1. Adding item to posting.
        PostingItem newPostingItem = PostingsTransformer.transformItem(newPostingItemTO);
        PostingsTransformer.updateItem(newPostingItem, newPostingItemTO);
        if (newPostingItem.getNumber() == 0){
            newPostingItem.setNumber(postingItemDAO.getNextAvailableNumber(newPostingItem.getPosting().getId()));
        }
        postingItemDAO.save(newPostingItem);
        postingDAO.flush();
        postingDAO.refresh(newPostingItem.getPosting());

        //3. Synchronize DTO data with lates entity data.
        PostingsTransformer.updateItem(newPostingItemTO, newPostingItem);
    }

    /**
     * Deletes item from the posting.
     * @param postingItemId
     */
    public void deleteItemFromPosting(long postingItemId) {
        PostingItem persistentPostingItem = postingItemDAO.get(postingItemId);

        if (persistentPostingItem != null){
            List<PostingItem> items = persistentPostingItem.getPosting().getItems();

            //1. Deletes item from the posting.
            items.remove(persistentPostingItem);
            postingItemDAO.remove(persistentPostingItem);

            //2. Updating numbers of the items, next to the deteting item.
            for (int i=0; i<items.size(); i++ ){
                items.get(i).setNumber(i+1);
            }

            //3. Refreshing posting total price.
            postingDAO.flush();
            postingDAO.refresh(persistentPostingItem.getPosting());
        }
    }

    /**
     * Saves posting item (new or already existing)
     * @param postingItemTO
     */
    public void savePostingItem(PostingItemTO postingItemTO) {
        //1. Saving changes, made in posting item.
        PostingItem persistentPostingItem = postingItemDAO.get(postingItemTO.getId());
        if (persistentPostingItem == null){
            persistentPostingItem = new PostingItem();
        }

        //2. Save and refreshing posting total price.
        PostingsTransformer.updateItem(persistentPostingItem, postingItemTO);
        postingItemDAO.save(persistentPostingItem);
        postingDAO.flush();
        postingDAO.refresh(persistentPostingItem.getPosting());
        PostingsTransformer.updateItem(postingItemTO, persistentPostingItem);

        //3. Synchronize DTO data with lates entity data.
        PostingsTransformer.updateItem(postingItemTO, persistentPostingItem);
    }

    /**
     * Searches for the same item already have been placed to the posting.
     *
     * @param postingId identifier of posting to check.
     * @param detailBatchId identifier of detail batch to look in the posting.
     * @param shelfLifeDate
     * @return the same posting item or null.
     */
    public PostingItemTO getSamePostingItem(long postingId, long detailBatchId, Date shelfLifeDate) {
        return PostingsTransformer.transformItem(postingItemDAO.findSamePostingItem(postingId, detailBatchId, shelfLifeDate));
    }

    /**
     * Search for unclassified posting item with the same barcode id.
     * @param postingId identifier of posting to check.
     * @param barCode bar code to be searched.
     * @return the same posting item or null.
     */
    public PostingItemTO getSameUnclassifiedPostingItem(long postingId, String barCode) {
        return PostingsTransformer.transformItem(postingItemDAO.findSameUnclassifiedPostingItem(postingId, barCode));
    }

    /**
     * Attach detail batch to posting item. This operation makes posting item valid for further processing.
     * @param postingItemId posting item to attach detail batch.
     * @param detailBatchId detail batch to be attached.
     */
    public void attachDetailBatchToUnclassifiedPostingItem(long postingItemId, long detailBatchId) {
        //1. Validate parameters.
        PostingItem postingItem = getPostingItemById(postingItemId);
        if (postingItem == null){
            throw new IllegalArgumentException("Posting item with id=" + postingItemId + " was not found.");
        }
        else if (postingItem.getUnclassifiedCatalogItem() == null || postingItem.getDetailBatch() != null){
            throw new IllegalArgumentException("Posting item to attach detail batch should be unclassified and not having detail batch attached.");
        }

        DetailBatch detailBatch = detailBatchDAO.get(detailBatchId);
        if (detailBatch == null){
            throw new IllegalArgumentException("Detail batch with id=" + detailBatchId + " was not found.");
        }

        //2. Attach detail batch to posting item (with proper posting item initialization).
        postingItem.setDetailBatch(detailBatch);
        postingItemDAO.update(postingItem);

        //3. Update detail batch to conform unclassified item.
        String barCode = postingItem.getUnclassifiedCatalogItem().getBarCode();
        if (barCode != null && !barCode.equals(detailBatch.getBarCode())){
            detailBatch.setBarCode(barCode);
            detailBatchDAO.update(detailBatch);
        }
    }

    public PostingItem getPostingItemById(long postingItemId) {
        return postingItemDAO.get(postingItemId);
    }

    public void savePosting(Posting posting) {
        postingDAO.save(posting);
    }

    //========================= Posting items import helpers ============================

    /**
     * Enumerate list of columns for specified posting items identity type.
     * @param identifyType identity type to be used in import.
     * @return domain columns for this sidentity type.
     */
    public List<DomainColumn> getDomainColumnsForIdentifyType(PostingItemsIdentityType identifyType) {
        List<DomainColumn> domainColumns = new ArrayList<DomainColumn>();

        //1. Fields dependent on identity type.
        if (identifyType.equals(PostingItemsIdentityType.NAME_MISC_NOTICE)){
            domainColumns.add(new DomainColumn(DOMAIN_COLUMN_NAME, I18nSupport.message("posting.items.import.item.field.name"), true, true, " "));
            domainColumns.add(new DomainColumn(DOMAIN_COLUMN_MISC, I18nSupport.message("posting.items.import.item.field.misc"), false, true, " "));
            domainColumns.add(new DomainColumn(DOMAIN_COLUMN_NOTICE, I18nSupport.message("posting.items.import.item.field.notice"), false, true, " "));
        }
        else if (identifyType.equals(PostingItemsIdentityType.NOMENCLATURE_ARTICLE)){
            domainColumns.add(new DomainColumn(DOMAIN_COLUMN_NOMENCLATURE_ARTICLE, I18nSupport.message("posting.items.import.item.field.nomenclatureArticle"), true));
        }
        else if (identifyType.equals(PostingItemsIdentityType.BARCODE)){
            domainColumns.add(new DomainColumn(DOMAIN_COLUMN_BARCODE, I18nSupport.message("posting.items.import.item.field.barCode"), true));
        }
        else{
            throw new AssertionError("Forgot to provide domain columns for new identity type? Identity type:" + identifyType.name());
        }

        //2. Common fields (for all identity types).
        domainColumns.add(new DomainColumn(DOMAIN_COLUMN_COUNT, I18nSupport.message("posting.items.import.item.field.count"), true));
        domainColumns.add(new DomainColumn(DOMAIN_COLUMN_BUY_PRICE, I18nSupport.message("posting.items.import.item.field.buyPrice"), false));
        domainColumns.add(new DomainColumn(DOMAIN_COLUMN_POSTING_NOTICE, I18nSupport.message("posting.items.import.item.field.postingNotice"), false, true, " "));

        return domainColumns;
    }

    /**
     * Launch posting items import.
     * @param importConfiguration configuration of the import to be performed.
     * @param importFinishListener listener to be called when import will be finished.
     */
    public void performPostingItemsImport(PostingItemsImportConfiguration importConfiguration, DataImportFinishListener importFinishListener) {
        PostingTOForReport postingTO = getPostingForReport(importConfiguration.getPostingId());
        DataImportService dataImportService = SpringServiceContext.getInstance().getDataImportService();

        dataImportService.performDataImportInBackground(importConfiguration.getDataAdapterUid(),
                importConfiguration.getDataAdapterConfiguration(),
                new PostingItemsDataSaver(postingTO,
                        importConfiguration.getItemsIdentityType(),
                        importConfiguration.getCurrency(),
                        importConfiguration.getMeasureUnit(),
                        importConfiguration.getStoragePlace()),
                I18nSupport.message("posting.items.import.display.name.format", postingTO.getNumber()),
                importFinishListener
        );
    }

    //========================= Spring setters =================================
    public void setPostingDAO(PostingDAO postingsDAO) {
        this.postingDAO = postingsDAO;
    }

    public void setPostingItemDAO(PostingItemDAO postingItemsDAO) {
        this.postingItemDAO = postingItemsDAO;
    }

    public void setDetailBatchDAO(DetailBatchDAO detailBatchDAO) {
        this.detailBatchDAO = detailBatchDAO;
    }
}
