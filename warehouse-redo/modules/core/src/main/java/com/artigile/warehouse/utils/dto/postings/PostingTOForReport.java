/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.postings;

import com.artigile.warehouse.bl.postings.PostingService;
import com.artigile.warehouse.domain.postings.PostingState;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;
import com.artigile.warehouse.utils.dto.deliveryNote.DeliveryNoteTOForReport;
import com.artigile.warehouse.utils.dto.purchase.PurchaseTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTOForReport;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Shyrik, 02.02.2009
 */
public class PostingTOForReport extends EqualsByIdImpl {
    private long id;

    private PostingState state;

    private UserTO createdUser;

    private Long number;

    private Date createDate;

    private ContractorTO contractor;

    private CurrencyTO currency;

    private BigDecimal totalPrice;

    private CurrencyTO defaultCurrency;

    private WarehouseTOForReport warehouse;

    private StoragePlaceTOForReport defaultStoragePlace;

    private String notice;

    private PurchaseTOForReport purchase;

    private DeliveryNoteTOForReport deliveryNote;

    private String totalItemsQuantity;

    //======================== Constructors ==========================================
    public PostingTOForReport(){
    }

    public PostingTOForReport(boolean init){
        if (init){
            init();
        }
    }

    public void init(){
        setState(PostingState.getInitialState());
        setCreateDate(new Date());
        setCreatedUser(WareHouse.getUserSession().getUser());

        PostingService postingsService = SpringServiceContext.getInstance().getPostingsService();
        setNumber(postingsService.getNextAvailablePostingNumber());
    }

    public boolean equals(Object obj){
        if (obj instanceof PostingTOForReport){
            PostingTOForReport postingObj = (PostingTOForReport)obj;
            return getId() == postingObj.getId();
        }
        return super.equals(obj);
    }

    /**
     * Init posting item properties according to the purchase.
     * @param purchase
     */
    public void initForPurchase(PurchaseTOForReport purchase) {
        setPurchase(purchase);
        setContractor(purchase.getContractor());
        setCurrency(purchase.getCurrency());
        setDefaultCurrency(purchase.getCurrency());
        setNotice(purchase.getNotice());
    }

    public void initForDeliveryNote(DeliveryNoteTOForReport deliveryNote) {
        setDeliveryNote(deliveryNote);
        setWarehouse(deliveryNote.getDestinationWarehouse());
        setCurrency(deliveryNote.getCurrency());
        setDefaultCurrency(deliveryNote.getCurrency());
        setNotice(deliveryNote.getNotice());        
    }

    //===================== Calculated getters =======================================
    public boolean canBeDeleted() {
        //Posting can be deleted only in construction state
        return state.equals(PostingState.CONSTRUCTION);
    }

    public boolean isNew() {
        return id == 0;
    }

    /**
     * Returns posting type according to current posting fields values.
     * @return
     */
    public PostingType getPostingType() {
        if (getPurchase() != null){
            return PostingType.FROM_PURCHASE;
        }
        else if (getDeliveryNote() != null){
            return PostingType.FROM_DELIVERY_NOTE;
        }
        else{
            return PostingType.SIMPLE;
        }
    }

    /**
     * If true, then posting type can be changed.
     * @return
     */
    public boolean canEditPostingType(){
        return isNew() && getPurchase() == null && getDeliveryNote() == null;
    }

    /**
     * If true, then posting source (purchase, delivery note, etc. may be changed).
     * @return
     */
    public boolean canEditPostingSource(){
        return isNew();
    }

    /**
     * @return true is posting is being constructed from other document (some edition restrictions apply then).
     */
    public boolean isPostingFromDocument() {
        return getPurchase() != null || getDeliveryNote() != null;
    }

    public boolean isAddingNewPostingItemsAllowed() {
        return getState().equals(PostingState.CONSTRUCTION) &&
            getPurchase() == null && getDeliveryNote() == null;
    }

    public boolean isPostingCompletionAllowed() {
        return getState().equals(PostingState.CONSTRUCTION);
    }

    //========================= Getters and setters ===================================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PostingState getState() {
        return state;
    }

    public void setState(PostingState state) {
        this.state = state;
    }

    public UserTO getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(UserTO createdUser) {
        this.createdUser = createdUser;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getCreateDateAsText(){
        return StringUtils.getDateFormat().format(getCreateDate());
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public ContractorTO getContractor() {
        return contractor;
    }

    public void setContractor(ContractorTO contractor) {
        this.contractor = contractor;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public CurrencyTO getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(CurrencyTO defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public WarehouseTOForReport getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseTOForReport warehouse) {
        this.warehouse = warehouse;
    }

    public StoragePlaceTOForReport getDefaultStoragePlace() {
        return defaultStoragePlace;
    }

    public void setDefaultStoragePlace(StoragePlaceTOForReport defaultStoragePlace) {
        this.defaultStoragePlace = defaultStoragePlace;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public PurchaseTOForReport getPurchase() {
        return purchase;
    }

    public void setPurchase(PurchaseTOForReport purchase) {
        this.purchase = purchase;
    }

    public DeliveryNoteTOForReport getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(DeliveryNoteTOForReport deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public String getTotalItemsQuantity() {
        return totalItemsQuantity;
    }

    public void setTotalItemsQuantity(String totalItemsQuantity) {
        this.totalItemsQuantity = totalItemsQuantity;
    }
}
