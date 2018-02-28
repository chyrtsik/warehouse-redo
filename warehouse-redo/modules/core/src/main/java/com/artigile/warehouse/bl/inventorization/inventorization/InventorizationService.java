/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.inventorization.inventorization;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.exceptions.CannotEstablishLockException;
import com.artigile.warehouse.bl.common.exceptions.CannotPerformOperationException;
import com.artigile.warehouse.bl.common.exceptions.ItemNotExistsException;
import com.artigile.warehouse.bl.common.verifications.VerificationResult;
import com.artigile.warehouse.bl.common.verifications.Verifications;
import com.artigile.warehouse.bl.lock.LockGroupService;
import com.artigile.warehouse.dao.InventorizationDAO;
import com.artigile.warehouse.dao.InventorizationItemsDAO;
import com.artigile.warehouse.dao.WarehouseBatchDAO;
import com.artigile.warehouse.domain.details.DetailBatch;
import com.artigile.warehouse.domain.inventorization.Inventorization;
import com.artigile.warehouse.domain.inventorization.InventorizationItem;
import com.artigile.warehouse.domain.inventorization.InventorizationItemState;
import com.artigile.warehouse.domain.inventorization.InventorizationState;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationItemTO;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationTO;
import com.artigile.warehouse.utils.dto.inventorization.InventorizationTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.InventorizationItemTransformer;
import com.artigile.warehouse.utils.transofmers.InventorizationTransformer;
import com.artigile.warehouse.utils.transofmers.UserTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author Shyrik, 29.09.2009
 */
@Transactional(rollbackFor = BusinessException.class)
public class InventorizationService {
    
    private InventorizationDAO inventorizationDAO;

    private InventorizationItemsDAO inventorizationItemsDAO;

    private WarehouseBatchDAO warehouseBatchDAO;

    private LockGroupService lockGroupService;


    //=================================== Constructors and initialization =====================
    public InventorizationService() { /* Default constructor */ }

    //===================================== Listeners support ==========================
    private ArrayList<InventorizationChangeListener> listeners = new ArrayList<InventorizationChangeListener>();

    public void addListener(InventorizationChangeListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    private void fireInventorizationStateChanged(Inventorization inventorization, InventorizationState oldState, InventorizationState newState) throws BusinessException {
        for (InventorizationChangeListener listener : listeners) {
            listener.onInventorizationStateChanged(inventorization, oldState, newState);
        }
    }

    //=================================== Operations ========================================
    /**
     * Loads list of all inventorizations.
     * @return
     */
    public List<InventorizationTOForReport> getAllInventorizations() {
        return InventorizationTransformer.transformListForReport(inventorizationDAO.getAll());
    }

    /**
     * Loads inventorization DTO by id.
     * @param inventorizationId
     * @return
     */
    public InventorizationTOForReport getInventorization(long inventorizationId) {
        return InventorizationTransformer.transformForReport(inventorizationDAO.get(inventorizationId));
    }

    /**
     * Loads inventorization items DTOs by inventorization Id.
     * @param inventorizationId
     * @return
     */
    public List<InventorizationItemTO> getInventorizationItems(long inventorizationId) {
        Inventorization inventorization = inventorizationDAO.get(inventorizationId);
        if (inventorization == null){
            //Inventorization not found.
            return null;
        }
        else if (!inventorization.getState().equals(InventorizationState.NOT_PROCESSED)){
            //Inventorization already has persistent items.
            InventorizationTO inventorizationTO = InventorizationTransformer.transform(inventorization);
            return InventorizationItemTransformer.transformList(inventorization.getItems(), inventorizationTO);
        }
        else{
            //Inventorization hasn't persistent items yet. So, we need to select then manually.
            List<InventorizationItem> items = createInventorizationItemsList(inventorization);
            InventorizationTO inventorizationTO = InventorizationTransformer.transform(inventorization);
            return InventorizationItemTransformer.transformList(items, inventorizationTO);
        }
    }

    /**
     * Saves given inventorization.
     * @param inventorizationTO (in, out) -- inventorization data to be saved.
     * @return persistent inventorization, that has been inserted of updated.
     */
    public Inventorization save(InventorizationTOForReport inventorizationTO) {
        Inventorization persistentInventorization = InventorizationTransformer.transformFromReport(inventorizationTO);
        InventorizationTransformer.update(persistentInventorization, inventorizationTO);
        inventorizationDAO.save(persistentInventorization);
        InventorizationTransformer.update(inventorizationTO, persistentInventorization);
        return persistentInventorization;
    }

    /**
     * Generates new inventorization number, that seems to be unique.
     * @return
     */
    public long getNextAvailableInventorizationNumber() {
        return inventorizationDAO.getNextAvailableInventorizationNumber();
    }

    /**
     * Loads inventorization by given id.
     * @param inventorizationId
     * @return
     */
    public Inventorization getInventorizationById(long inventorizationId) {
        return inventorizationDAO.get(inventorizationId);
    }

    /**
     *
     * @param inventorizationId
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    public void deleteInventorization(long inventorizationId) throws BusinessException {
        Inventorization inventorization = inventorizationDAO.get(inventorizationId);
        if (!canDeleteInventorization(inventorization)){
            throw new BusinessException(I18nSupport.message("inventorization.error.cannot.delete.inventorization"));
        }
        inventorization.setState(InventorizationState.DELETED);
        inventorizationDAO.remove(inventorization);
    }

    private boolean canDeleteInventorization(Inventorization inventory) {
        return inventory != null && inventory.getState().equals(InventorizationState.NOT_PROCESSED);
    }

    public boolean canDeleteInventorization(long inventorizationId) {
        return canDeleteInventorization(inventorizationDAO.get(inventorizationId));
    }

    public void beginProcessingInventorization(InventorizationTOForReport inventorizationTO) throws BusinessException {
        Inventorization inventorization = inventorizationDAO.get(inventorizationTO.getId());
        if (inventorization == null){
            throw new ItemNotExistsException();
        }

        //1. Before beginning inventorization we should fix it's content.
        // We should create a limited number of inventorization items.
        createInventorizationItems(inventorization);

        //2. Makes inventorization 'in progress'.
        Verifications.ensureVerificationPasses(inventorization, new BeforeInventorizationBeginVerification());
        changeInventorizationState(inventorizationTO, InventorizationState.IN_PROCESS);
    }

    /**
     * Make inventorization 'closed'.
     * @param inventorization
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    public void closeInventorization(InventorizationTOForReport inventorization) throws BusinessException {
        Inventorization persistentInventorization = inventorizationDAO.get(inventorization.getId());

        //1. Verify, is inventorization ready to be closed.
        VerificationResult verificationResult = Verifications.performVerification(persistentInventorization, new BeforeInventorizationCompleteVerification());
        if (verificationResult.isFailed()){
            throw new BusinessException(verificationResult.getFailReason());
        }

        //2. Make inventorization completed.
        persistentInventorization.setCloseDate(Calendar.getInstance().getTime());
        persistentInventorization.setCloseUser(UserTransformer.transformUser(WareHouse.getUserSession().getUser()));  //TODO: eliminate this dependence on presentation tier.
        doChangeInventorizationState(persistentInventorization, InventorizationState.CLOSED);

        //3. Unlink inventorization items from warehouse batches to allow delete warehouse batches.
        for (InventorizationItem item : persistentInventorization.getItems()){
            item.setWarehouseBatch(null);                        
        }

        //4. Refresh inventorization representation to conform new state
        InventorizationTransformer.update(inventorization, persistentInventorization);
    }

    private void changeInventorizationState(InventorizationTOForReport inventorization, InventorizationState newState) throws BusinessException {
        //1. Changes inventorization state.
        Inventorization persistentInventorization = inventorizationDAO.get(inventorization.getId());
        doChangeInventorizationState(persistentInventorization, newState);

        //2. Refresh inventorization representation to conform new state.
        InventorizationTransformer.update(inventorization, persistentInventorization);
    }

    private void doChangeInventorizationState(Inventorization inventorization, InventorizationState newState) throws BusinessException {
        //1. Changes inventorization state.
        InventorizationState oldState = inventorization.getState();
        inventorization.setState(newState);
        inventorizationDAO.save(inventorization);

        //2. Lock warehouse batches for this inventorization.
        if (newState.equals(InventorizationState.IN_PROCESS)) {
            Set<Object> batchesToLock = new HashSet<Object>();
            for (InventorizationItem item : inventorization.getItems()) {
                batchesToLock.add(item.getWarehouseBatch());
            }
            try{
                lockGroupService.exclusiveLock(inventorization, batchesToLock);
            }
            catch (CannotEstablishLockException e){
                throw new CannotPerformOperationException(I18nSupport.message("inventorization.error.cannotLockWaresForInventorization"), e);
            }
        } else if (newState.equals(InventorizationState.CLOSED)) {
            lockGroupService.unlockGroupsOfOwner(inventorization);
        }

        //3. Notify about changing of inventorization state.
        fireInventorizationStateChanged(inventorization, oldState, newState);
    }

    /**
     * Checks, if given inventorization number will be unuque.
     * @param number inventorization number to be checked.
     * @param inventorizationId inventorization, which this number belongs to.
     * @return
     */
    public boolean isUniqueInventorizationNumber(long number, long inventorizationId) {
        Inventorization sameInventory = inventorizationDAO.getInventorizationByNumber(number);
        return sameInventory == null || sameInventory.getId() == inventorizationId;
    }

    /**
     * Adds new item to the inventorization.
     * @param newInventorizationItemTO
     */
    public void addItemToInventorization(InventorizationItemTO newInventorizationItemTO) {
        InventorizationItem newInventorizationItem = InventorizationItemTransformer.transform(newInventorizationItemTO);
        InventorizationItemTransformer.update(newInventorizationItem, newInventorizationItemTO);
        if (newInventorizationItem.getNumber() == 0) {
            newInventorizationItem.setNumber(inventorizationItemsDAO.getNextAvailableNumber(newInventorizationItem.getInventorization().getId()));
        }
        inventorizationItemsDAO.save(newInventorizationItem);
        InventorizationItemTransformer.update(newInventorizationItemTO, newInventorizationItem);
    }

    /**
     * Deletes item from the inventorization.
     * @param inventorizationItem
     */
    public void deleteItemFromInventorization(InventorizationItemTO inventorizationItem) {
        InventorizationItem persistentInventorizationItem = inventorizationItemsDAO.get(inventorizationItem.getId());

        if (persistentInventorizationItem != null){
            List<InventorizationItem> items = persistentInventorizationItem.getInventorization().getItems();

            //1. Deletes item from the inventorization.
            items.remove(persistentInventorizationItem);
            inventorizationItemsDAO.remove(persistentInventorizationItem);

            //2. Updating numbers of the items, next to the deleting item.
            for (int i=0; i<items.size(); i++ ){
                items.get(i).setNumber(i+1);
            }
        }
    }

    public InventorizationItem getInventorizationItemById(long inventorizationItemId) {
        return inventorizationItemsDAO.get(inventorizationItemId);
    }

    /**
     * Creates new inventorization.
     * @param newInventorizationTO
     */
    public void createInventorization(InventorizationTOForReport newInventorizationTO) {
        //Only saves new inventorization. Real items will be created when inventorization begins.
        save(newInventorizationTO);
    }

    /**
     * Creates items for given inventorization.
     * @param inventorization
     */
    private void createInventorizationItems(Inventorization inventorization) {
        List<InventorizationItem> items = createInventorizationItemsList(inventorization);
        for (InventorizationItem item : items){
            inventorizationItemsDAO.save(item);
        }
    }

    private List<InventorizationItem> createInventorizationItemsList(Inventorization inventorization) {
        List<InventorizationItem> items = new ArrayList<InventorizationItem>();
        long itemNumber = inventorizationItemsDAO.getNextAvailableNumber(inventorization.getId());

        for (StoragePlace storagePlace : inventorization.getStoragePlacesToCheck()) {
            List<WarehouseBatch> batchList = warehouseBatchDAO.getWarehouseBatchesByStoragePlace(storagePlace);
            for(WarehouseBatch batch : batchList) {
                DetailBatch detailBatch = batch.getDetailBatch();

                InventorizationItem item = new InventorizationItem();
                item.setDetailBatch(detailBatch);
                item.setStoragePlace(batch.getStoragePlace());
                item.setWarehouseBatch(batch);
                item.setNeededCount(batch.getAmount());
                item.setCountMeas(detailBatch.getCountMeas());
                item.setState(InventorizationItemState.getInitialState());
                item.setNumber(itemNumber);
                item.setInventorization(inventorization);

                items.add(item);
                itemNumber++;
            }
        }
        
        return items;
    }

    //========================= Spring setters ===========================
    public void setInventorizationDAO(InventorizationDAO inventorizationDAO) {
        this.inventorizationDAO = inventorizationDAO;
    }

    public void setInventorizationItemsDAO(InventorizationItemsDAO inventorizationItemsDAO) {
        this.inventorizationItemsDAO = inventorizationItemsDAO;
    }

    public void setWarehouseBatchDAO(WarehouseBatchDAO warehouseBatchDAO) {
        this.warehouseBatchDAO = warehouseBatchDAO;
    }

    public void setLockGroupService(LockGroupService lockGroupService) {
        this.lockGroupService = lockGroupService;
    }
}
