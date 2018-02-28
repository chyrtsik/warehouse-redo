/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.directory.MeasureUnit;
import com.artigile.warehouse.domain.postings.Posting;
import com.artigile.warehouse.domain.postings.PostingItem;
import com.artigile.warehouse.domain.postings.UnclassifiedCatalogItem;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.postings.PostingItemTO;
import com.artigile.warehouse.utils.dto.postings.PostingTO;
import com.artigile.warehouse.utils.dto.postings.PostingTOForReport;
import com.artigile.warehouse.utils.dto.postings.UnclassifiedCatalogItemTO;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.google.common.base.Joiner;

import java.text.MessageFormat;
import java.util.*;

/**
 * @author Shyrik, 02.02.2009
 */
public final class PostingsTransformer {
    private PostingsTransformer(){}

    public static List<PostingTOForReport> transformPostingList(List<Posting> postings) {
        List<PostingTOForReport> postingsTO = new ArrayList<PostingTOForReport>();
        for (Posting posting : postings){
            postingsTO.add(transformPostingForReport(posting));
        }
        return postingsTO;
    }

    public static PostingTOForReport transformPostingForReport(Posting posting) {
        if (posting == null){
            return null;
        }
        PostingTOForReport postingTO = new PostingTOForReport();
        transformPostingForReport(postingTO, posting);
        return postingTO;
    }

    public static void transformPostingForReport(PostingTOForReport postingTO, Posting posting) {
        postingTO.setId(posting.getId());
        postingTO.setNumber(posting.getNumber());
        postingTO.setState(posting.getState());
        postingTO.setCreatedUser(UserTransformer.transformUser(posting.getCreatedUser()));
        postingTO.setCreateDate(posting.getCreateDate());
        postingTO.setContractor(ContractorTransformer.transformContractor(posting.getContractor()));
        postingTO.setCurrency(CurrencyTransformer.transformCurrency(posting.getCurrency()));
        postingTO.setDefaultCurrency(CurrencyTransformer.transformCurrency(posting.getDefaultCurrency()));
        postingTO.setWarehouse(WarehouseTransformer.transformForReport(posting.getDefaultWarehouse()));
        postingTO.setDefaultStoragePlace(StoragePlaceTransformer.transformForReport(posting.getDefaultStoragePlace()));
        postingTO.setNotice(posting.getNotice());
        postingTO.setPurchase(PurchaseTransformer.transform(posting.getPurchase()));
        postingTO.setDeliveryNote(DeliveryNoteTransformer.transformForReport(posting.getDeliveryNote(), postingTO));
        postingTO.setTotalPrice(posting.getTotalPrice());
        Map<MeasureUnit, Long> totalItemsQuantityMap = new HashMap<>();
        for (PostingItem postingItem : posting.getItems()) {
            if (postingItem.getDetailBatch() != null && postingItem.getAmount() != null) {
                MeasureUnit measureUnit = postingItem.getDetailBatch().getCountMeas();
                if (!totalItemsQuantityMap.containsKey(measureUnit)) {
                    totalItemsQuantityMap.put(postingItem.getDetailBatch().getCountMeas(), 0L);
                }
                totalItemsQuantityMap.put(measureUnit, totalItemsQuantityMap.get(measureUnit) + postingItem.getAmount());
            } else {
                LoggingFacade.logInfo("Skipping measure unit calculation because detailBatch or amount is null");
            }
        }
        ArrayList<String> resultString = new ArrayList<>();
        for (Map.Entry<MeasureUnit, Long> measureUnitIntegerEntry : totalItemsQuantityMap.entrySet()) {
            resultString.add(MessageFormat.format("{1} {0}", measureUnitIntegerEntry.getKey().getName(), measureUnitIntegerEntry.getValue()));
        }
        postingTO.setTotalItemsQuantity(Joiner.on(", ").join(resultString));
    }

    public static void update(Posting posting, PostingTOForReport postingTO) {
        posting.setId(postingTO.getId());
        posting.setNumber(postingTO.getNumber());
        posting.setState(postingTO.getState());
        posting.setCreatedUser(UserTransformer.transformUser(postingTO.getCreatedUser()));
        posting.setCreateDate(postingTO.getCreateDate());
        posting.setContractor(ContractorTransformer.transformContractor(postingTO.getContractor()));
        posting.setCurrency(CurrencyTransformer.transformCurrency(postingTO.getCurrency()));
        posting.setDefaultCurrency(CurrencyTransformer.transformCurrency(postingTO.getDefaultCurrency()));
        posting.setDefaultWarehouse(WarehouseTransformer.transform(postingTO.getWarehouse()));
        posting.setDefaultStoragePlace(StoragePlaceTransformer.transform(postingTO.getDefaultStoragePlace()));
        posting.setNotice(postingTO.getNotice());
        posting.setPurchase(PurchaseTransformer.transform(postingTO.getPurchase()));
        posting.setDeliveryNote(DeliveryNoteTransformer.transform(postingTO.getDeliveryNote()));
    }

    public static Posting transform(PostingTOForReport postingTO){
        if (postingTO == null){
            return null;
        }
        Posting posting = SpringServiceContext.getInstance().getPostingsService().getPostingById(postingTO.getId());
        if (posting == null){
            posting = new Posting();
        }
        return posting;
    }

    public static PostingTO transform(Posting posting) {
        PostingTO postingTO = new PostingTO();
        transformPosting(postingTO, posting);
        return postingTO;
    }

    /**
     * @param postingTO - in, out.
     * @param posting - in.
     */
    public static void transformPosting(PostingTO postingTO, Posting posting){
        transformPostingForReport(postingTO, posting);
        List<PostingItemTO> itemsTO = new ArrayList<PostingItemTO>();
        for (PostingItem item : posting.getItems()){
            itemsTO.add(transformItem(item, postingTO));
        }
        postingTO.setItems(itemsTO);        
    }

    private static PostingItemTO transformItem(PostingItem item, PostingTOForReport postingTO) {
        PostingItemTO itemTO = new PostingItemTO();
        itemTO.setPosting(postingTO);
        updateItem(itemTO, item);
        return itemTO;
    }

    public static PostingItemTO transformItem(PostingItem item) {
        if (item == null){
            return null;
        }
        return transformItem(item, transformPostingForReport(item.getPosting()));
    }

    public static void updateItem(PostingItemTO itemTO, PostingItem item) {
        itemTO.setId(item.getId());
        itemTO.setNumber(item.getNumber());
        itemTO.setPrice(item.getPrice());
        itemTO.setCount(item.getAmount());
        itemTO.setDetailBatch(DetailBatchTransformer.batchTO(item.getDetailBatch()));
        itemTO.setUnclassifiedCatalogItem(transformUnclassifiedCatalogItem(item.getUnclassifiedCatalogItem()));
        itemTO.setOriginalCurrency(CurrencyTransformer.transformCurrency(item.getOriginalCurrency()));
        itemTO.setOriginalPrice(item.getOriginalPrice());
        itemTO.setSalePrice(item.getSalePrice());
        itemTO.setStoragePlace(StoragePlaceTransformer.transformForReport(item.getStoragePlace()));
        itemTO.setNotice(item.getNotice());
        itemTO.setShelfLifeDate(item.getShelfLifeDate());
    }

    public static void updateItem(PostingItem item, PostingItemTO itemTO) {
        item.setPosting(SpringServiceContext.getInstance().getPostingsService().getPostingById(itemTO.getPosting().getId()));
        item.setNumber(itemTO.getNumber());
        item.setPrice(itemTO.getPrice());
        item.setAmount(itemTO.getCount());
        item.setDetailBatch(DetailBatchTransformer.batch(itemTO.getDetailBatch()));
        item.setUnclassifiedCatalogItem(transformUnclassifiedCatalogItem(itemTO.getUnclassifiedCatalogItem()));
        item.setOriginalCurrency(CurrencyTransformer.transformCurrency(itemTO.getOriginalCurrency()));
        item.setOriginalPrice(itemTO.getOriginalPrice());
        item.setSalePrice(itemTO.getSalePrice());
        item.setStoragePlace(StoragePlaceTransformer.transform(itemTO.getStoragePlace()));
        item.setNotice(itemTO.getNotice());
        item.setShelfLifeDate(itemTO.getShelfLifeDate());
    }

    public static PostingItem transformItem(PostingItemTO itemTO) {
        if (itemTO == null){
            return null;
        }
        PostingItem item = SpringServiceContext.getInstance().getPostingsService().getPostingItemById(itemTO.getId());
        if (item == null){
            item = new PostingItem();
        }
        return item;
    }

    private static UnclassifiedCatalogItemTO transformUnclassifiedCatalogItem(UnclassifiedCatalogItem unclassifiedCatalogItem) {
        if (unclassifiedCatalogItem == null){
            return null;
        }
        UnclassifiedCatalogItemTO unclassifiedCatalogItemTO = new UnclassifiedCatalogItemTO();
        unclassifiedCatalogItemTO.setId(unclassifiedCatalogItem.getId());
        unclassifiedCatalogItemTO.setBarCode(unclassifiedCatalogItem.getBarCode());
        return unclassifiedCatalogItemTO;
    }

    private static UnclassifiedCatalogItem transformUnclassifiedCatalogItem(UnclassifiedCatalogItemTO unclassifiedCatalogItemTO) {
        if (unclassifiedCatalogItemTO == null) {
            return null;
        }

        UnclassifiedCatalogItem unclassifiedCatalogItem;
        if (unclassifiedCatalogItemTO.getId() == null) {
            unclassifiedCatalogItem = new UnclassifiedCatalogItem();
        }
        else{
            unclassifiedCatalogItem = SpringServiceContext.getInstance().getUnclassifiedCatalogItemService().findItemById(unclassifiedCatalogItemTO.getId());
            if (unclassifiedCatalogItem == null) {
                unclassifiedCatalogItem = new UnclassifiedCatalogItem();
            }
        }
        unclassifiedCatalogItem.setBarCode(unclassifiedCatalogItemTO.getBarCode());

        return unclassifiedCatalogItem;
    }
}
