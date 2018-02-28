/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.movement;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.exceptions.ItemNotExistsException;
import com.artigile.warehouse.bl.common.verifications.Verifications;
import com.artigile.warehouse.bl.warehouseBatch.WarehouseBatchService;
import com.artigile.warehouse.dao.MovementDAO;
import com.artigile.warehouse.dao.MovementItemDAO;
import com.artigile.warehouse.domain.DocumentTotals;
import com.artigile.warehouse.domain.complecting.ComplectingTask;
import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;
import com.artigile.warehouse.domain.movement.*;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.MiscUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.MeasureUnitTO;
import com.artigile.warehouse.utils.dto.movement.MovementItemTO;
import com.artigile.warehouse.utils.dto.movement.MovementTO;
import com.artigile.warehouse.utils.dto.movement.MovementTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.*;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Shyrik, 21.11.2009
 */
@Transactional(rollbackFor = BusinessException.class)
public class MovementService {
    private MovementDAO movementDAO;
    private MovementItemDAO movementItemDAO;

    //=================================== Constructors and initialization =====================
    public MovementService() {
    }

    //=============================== Listeners support =====================================
    private ArrayList<MovementChangeListener> listeners = new ArrayList<MovementChangeListener>();

    public void addListener(MovementChangeListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    public void removeListener(MovementChangeListener listener){
        listeners.remove(listener);
    }

    private void fireMovementStateChanged(Movement movement, MovementState oldState, MovementState newState) throws BusinessException {
        for (MovementChangeListener listener : listeners){
            listener.onMovementStateChanged(movement, oldState, newState);
        }
    }

    //=================================== Operations ========================================
    public MovementTOForReport getMovement(long movementId) {
        return MovementTransformer.transformForReport(movementDAO.get(movementId));
    }

    public MovementTO getMovementFullData(long movementId) {
        return MovementTransformer.transform(movementDAO.get(movementId));
    }

    /**
     * Loads list of all movements.
     * @return
     */
    public List<MovementTOForReport> getAllMovements() {
        return MovementTransformer.transformListForReport(movementDAO.getAll());
    }

    /**
     * Created new movement document in the database.
     * @param newMovementTO
     * @return
     */
    public MovementTOForReport create(MovementTOForReport newMovementTO) {
        Movement newMovement = new Movement();

        newMovement.setNumber(newMovementTO.getNumber());
        newMovement.setState(newMovementTO.getState());
        newMovement.setFromWarehouse(WarehouseTransformer.transform(newMovementTO.getFromWarehouse()));
        newMovement.setToWarehouse(WarehouseTransformer.transform(newMovementTO.getToWarehouse()));
        newMovement.setNotice(newMovementTO.getNotice());
        newMovement.setCreateDate(Calendar.getInstance().getTime());
        newMovement.setCreateUser(UserTransformer.transformUser(WareHouse.getUserSession().getUser()));

        movementDAO.save(newMovement);
        MovementTransformer.update(newMovementTO, newMovement);
        return newMovementTO;
    }

    /**
     * Saves given inventorization.
     * @param movementTO (in, out) -- movement data to be saved.
     */
    public void save(MovementTOForReport movementTO) {
        Movement persistentMovement = MovementTransformer.transformFromReport(movementTO);
        MovementTransformer.update(persistentMovement, movementTO);
        movementDAO.save(persistentMovement);
        MovementTransformer.update(movementTO, persistentMovement);
    }

    /**
     * Generates new movement number, that seems to be unique.
     * @return
     */
    public long getNextAvailableMovementNumber() {
        return movementDAO.getNextAvailableMovementNumber();
    }

    /**
     * Loads movement by given id.
     * @param movementId
     * @return
     */
    public Movement getMovementById(long movementId) {
        return movementDAO.get(movementId);
    }

    /**
     * Deleted
     * @param movementId
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    public void deleteMovement(long movementId) throws BusinessException {
        Movement movement = movementDAO.get(movementId);
        if (!canDeleteMovement(movement)){
            throw new BusinessException(I18nSupport.message("movement.error.cannot.delete.movement"));
        }

        //Deleting all ware reserving for movement items.
        WarehouseBatchService warehouseBatchService = SpringServiceContext.getInstance().getWarehouseBatchService();
        for (MovementItem item : movement.getItems()){
            warehouseBatchService.unreserve(item.getWarehouseBatch().getId(), item.getAmount());
        }

        movementDAO.remove(movement);
    }

    private boolean canDeleteMovement(Movement movement) {
        return movement != null &&
               movement.getState().equals(MovementState.CONSTRUCTION);
    }

    public boolean canDeleteMovement(long movementId) {
        return canDeleteMovement(movementDAO.get(movementId));
    }

    /**
     * Checks, if given ovement number will be unuque.
     * @param number movement number to be checked.
     * @param movementId movement, which this number belongs to.
     * @return
     */
    public boolean isUniqueMovementNumber(long number, long movementId) {
        Movement sameMovement = movementDAO.getMovementByNumber(number);
        return sameMovement == null || sameMovement.getId() == movementId;
    }

    /**
     * Begins processing of a movement.
     * @param movementId
     * @throws BusinessException
     */
    public void beginMovement(long movementId) throws BusinessException {
        Movement movement = movementDAO.get(movementId);
        if (movement == null){
            throw new ItemNotExistsException();
        }

        //1. Check, that movement is ready to begin.
        Verifications.ensureVerificationPasses(movement, new BeforeBeginMovementVerification());

        //2. Beginning movement.
        movement.setBeginDate(Calendar.getInstance().getTime());
        changeMovementState(movement, MovementState.COMPLECTING);
    }

    /**
     * Cancels processing of the movements.
     * @param movementId
     * @throws BusinessException
     */
    public void cancelMovement(long movementId) throws BusinessException {
        Movement movement = movementDAO.get(movementId);
        if (movement == null){
            throw new ItemNotExistsException();
        }

        //1. Check, that movement can be cancelled.
        Verifications.ensureVerificationPasses(movement, new BeforeCancelMovementVerification());

        //2. Cancelling movement.
        movement.setBeginDate(null);
        changeMovementState(movement, MovementState.CONSTRUCTION);
    }

    /**
     * Changes state of the movement.
     * @param movement movement to be changed.
     * @param newState new state of the movement.
     */
    private void changeMovementState(Movement movement, MovementState newState) throws BusinessException {
        //1. Change state of the movement.
        MovementState oldState = movement.getState();
        movement.setState(newState);

        //2. Notify listeners about changing of the movement's state.
        fireMovementStateChanged(movement, oldState, newState);
    }

    /**
     * Adds new item to movement.
     * @param movementItemTO
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    public void addItemToMovement(MovementItemTO movementItemTO) throws BusinessException {
        //1. Check, that new item can be placed into movement.
        Movement movement = movementDAO.get(movementItemTO.getMovement().getId());
        Verifications.ensureVerificationPasses(movement, new BeforeAddItemToMovementVerification());

        //2. Try to reserve ware to be moved.
        if ( movementItemTO.getCount() != null ){
            SpringServiceContext.getInstance().getWarehouseBatchService().reserve(movementItemTO.getWarehouseBatch().getId(), movementItemTO.getCount());
        }

        //3. Add new item to the movenent.
        MovementItem movementItem = new MovementItem();
        MovementItemTransformer.update(movementItem, movementItemTO);
        movementItem.setMovement(movement);
        movementItem.setState(MovementItemState.NOT_PROCESSED);
        movementItem.setNumber(getNextAvailableMovementItemNumber(movementItem.getMovement().getId()));
        movementItemDAO.save(movementItem);
        MovementItemTransformer.update(movementItemTO, movementItem);
    }

    /**
     * Removes item from movement.
     * @param movementItemId
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    public void removeItemFromMovement(long movementItemId) throws BusinessException {
        //1. At first we try to unreserve ware to be moved.
        MovementItem itemToDelete = movementItemDAO.get(movementItemId);
        if ( itemToDelete.getAmount() != null ){
            SpringServiceContext.getInstance().getWarehouseBatchService().unreserve(itemToDelete.getWarehouseBatch().getId(), itemToDelete.getAmount());
        }

        //2. Delete item from the movenent and update numbers of the other items.
        itemToDelete.getMovement().getItems().remove(itemToDelete);
        for ( MovementItem item : itemToDelete.getMovement().getItems() ){
            if (item.getNumber() > itemToDelete.getNumber()){
                item.setNumber(item.getNumber() - 1);
            }
        }
        movementDAO.save(itemToDelete.getMovement());
        movementItemDAO.remove(itemToDelete);
    }

    /**
     * Saves given list of movement items.
     * @param movementItemTOs
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    public void saveMovementItems(List<MovementItemTO> movementItemTOs) throws BusinessException {
        for (MovementItemTO itemTO : movementItemTOs){
            saveMovementItem(itemTO);
        }
    }

    /**
     * Saves changes in movement item.
     * @param movementItemTO
     * @throws com.artigile.warehouse.bl.common.exceptions.BusinessException
     */
    public void saveMovementItem(MovementItemTO movementItemTO) throws BusinessException {
        MovementItem movementItem = movementItemDAO.get(movementItemTO.getId());

        //1. Process changes in count of ware (reserve, unreserve wares).
        if (!MiscUtils.objectsEquals(movementItem.getAmount(), movementItemTO.getCount())){
            long countDiff;
            if (movementItem.getAmount() != null && movementItemTO.getCount() != null){
                countDiff = movementItemTO.getCount() - movementItem.getAmount();
            }
            else if (movementItemTO.getCount() == null){
                countDiff = -movementItem.getAmount();
            }
            else{
                countDiff = movementItemTO.getCount();
            }

            WarehouseBatchService warehouseBatchService = SpringServiceContext.getInstance().getWarehouseBatchService();
            if (countDiff > 0){
                //We need to reserve more wares.
                warehouseBatchService.reserve(movementItemTO.getWarehouseBatch().getId(), countDiff);
            }
            else{
                //We need to unreserve some of wares.
                warehouseBatchService.unreserve(movementItemTO.getWarehouseBatch().getId(), -countDiff);
            }
        }

        //2. Update movement item itself.
        MovementItemTransformer.update(movementItem, movementItemTO);
        movementItemDAO.save(movementItem);
    }

    public MovementItem getMovementItemById(long movementItemId) {
        return movementItemDAO.get(movementItemId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private long getNextAvailableMovementItemNumber(long movementId) {
        return movementItemDAO.getNextAvailableMovementItemNumber(movementDAO.get(movementId));
    }

    public MovementItemTO findSameMovementItem(MovementItemTO item) {
        return MovementItemTransformer.transform(movementItemDAO.findSameMovementItem(item.getMovement().getId(), item.getWarehouseBatch().getId()));
    }

    /**
     * Calculate totals for movement specified.
     * @param movementId identifier of movement to perform calculation.
     * @return totals of this movement.
     */
    public DocumentTotals getMovementTotals(long movementId) {
        Movement movement = getMovementById(movementId);
        if (movement == null){
            throw new IllegalArgumentException("Movement with id = " + movementId + " does not exist.");
        }

        Map<CurrencyTO, BigDecimal> prices = new HashMap<CurrencyTO, BigDecimal>();
        Map<MeasureUnitTO, BigDecimal> counts = new HashMap<MeasureUnitTO, BigDecimal>();

        for (MovementItem item : movement.getItems()){
            //Sum counts of items in movement.
            if (item.getAmount() != null && item.getAmount() > 0){
                MeasureUnitTO measureUnitTO = MeasureUnitTransformer.transform(item.getCountMeas());
                if (counts.containsKey(measureUnitTO)){
                    BigDecimal newCount = BigDecimal.valueOf(item.getAmount()).add(counts.get(measureUnitTO));
                    counts.put(measureUnitTO, newCount);
                }
                else{
                    counts.put(measureUnitTO, BigDecimal.valueOf(item.getAmount()));
                }

                //Sum prices of items in movement.
                BigDecimal cost = item.getItemCost();
                if (cost != null){
                    CurrencyTO currencyTO = CurrencyTransformer.transformCurrency(item.getDetailBatch().getCurrency());
                    if (prices.containsKey(currencyTO)){
                        BigDecimal newTotalPrice = cost.multiply(BigDecimal.valueOf(item.getAmount())).add(prices.get(currencyTO));
                        prices.put(currencyTO, newTotalPrice);
                    }
                    else{
                        prices.put(currencyTO, cost.multiply(BigDecimal.valueOf(item.getAmount())));
                    }
                }
            }
        }

        return new DocumentTotals(prices, counts);
    }

    /**
     * Calculating current state of the movement item (and saving it to database).
     * @param movementItem
     */
    public void recalculateMovementItemState(MovementItem movementItem) throws BusinessException {
        MovementItemState newState = translateComplectingTaskState(movementItem.getComplectingTask());
        MovementItemProcessingResult newResult = translateComplectingTaskProcessingResult(movementItem.getComplectingTask());
        
        if (!newState.equals(movementItem.getState()) || !MiscUtils.objectsEquals(movementItem.getProcessingResult(), newResult)){
            //State of the movement item changed.
            movementItem.setState(newState);
            movementItem.setProcessingResult(newResult);
            movementItemDAO.save(movementItem);

            //Movement state depends on state of it's items. So, we should notify movement about changing.
            recalculateMovementState(movementItem.getMovement());
        }
    }

    /**
     * Calculates and stores in database state of movement according to the state of it's items.
     * @param movement
     */
    private void recalculateMovementState(Movement movement) throws BusinessException {
        if (movement.getState().equals(MovementState.CONSTRUCTION)){
            //Only when movement is in processing, it's state is calculated.
            return;
        }
        else if (movement.getItems().size() == 0){
            //Only movement with items is 
            return;
        }

        //State of the movement is determined by the latest state of is't item.
        MovementItemState latestItemState = MovementItemState.POSTED;
        boolean hasProblem = false;
        for (MovementItem item : movement.getItems()){
            if (item.getState().isBefore(latestItemState)){
                latestItemState = item.getState();
            }
            if (item.getProcessingResult() != null && !item.getProcessingResult().equals(MovementItemProcessingResult.SUCCESS)){
                hasProblem = true;
            }
        }

        MovementState newMovementState = translateMovementItemState(latestItemState);
        MovementResult newMovementResult = makeMovementResult(newMovementState, hasProblem);

        if (!movement.getState().equals(newMovementState) || !MiscUtils.objectsEquals(movement.getResult(), newMovementResult)){
            movement.setState(newMovementState);
            movement.setResult(newMovementResult);

            if (movement.getState().equals(MovementState.COMPLETED)){
                //When movement has been ended, we should save date and time of this event.
                movement.setEndDate(Calendar.getInstance().getTime());
            }
            else{
                movement.setEndDate(null);
            }

            movementDAO.save(movement);
        }
    }

    /**
     * Translates the latest movement item state into appropriate movement item state.
     * @param latestItemState
     * @return
     */
    private MovementState translateMovementItemState(MovementItemState latestItemState) {
        if (latestItemState.equals(MovementItemState.NOT_PROCESSED) || latestItemState.equals(MovementItemState.COMPLECTING)){
            return MovementState.COMPLECTING;
        }
        else if (latestItemState.equals(MovementItemState.COMPLECTED)){
            return MovementState.COMPLECTED;
        }
        else if (latestItemState.equals(MovementItemState.READY_FOR_SHIPPING)){
            return MovementState.READY_FOR_SHIPPING;
        }
        else if (latestItemState.equals(MovementItemState.SHIPPING)){
            return MovementState.SHIPPING;
        }
        else if (latestItemState.equals(MovementItemState.SHIPPED)){
            return MovementState.SHIPPED;
        }
        else if (latestItemState.equals(MovementItemState.POSTING)){
            return MovementState.POSTING;
        }
        else if (latestItemState.equals(MovementItemState.POSTED)){
            return MovementState.COMPLETED;
        }
        throw new RuntimeException("MovementService.translateMovementItemState -- unsupported movement item state.");
    }

    /**
     * Makes movement result from information about movement state and existence of problems.
     * @param movementState
     * @param hasProblem
     * @return
     */
    private MovementResult makeMovementResult(MovementState movementState, boolean hasProblem) {
        if (hasProblem){
            return MovementResult.PROBLEM;
        }
        else{
            return movementState.equals(MovementState.COMPLETED) ? MovementResult.SUCCESS : null;
        }
    }

    /**
     * Translates complecting task state into movement item state.
     * @param complectingTask
     * @return
     */
    private MovementItemState translateComplectingTaskState(ComplectingTask complectingTask) {
        if (complectingTask == null || complectingTask.getState().equals(ComplectingTaskState.NOT_PROCESSED)){
            return MovementItemState.NOT_PROCESSED;
        }
        else if (complectingTask.getState().equals(ComplectingTaskState.PROCESSING)){
            return MovementItemState.COMPLECTING;
        }
        else if (complectingTask.getState().equals(ComplectingTaskState.PROCESSED)){
            if (complectingTask.getFoundCount().equals(complectingTask.getMovementItem().getAmount())){
                return MovementItemState.COMPLECTED;
            }
            else {
                //Movement item considered to be uncomplected while any problem with it exists.
                return MovementItemState.COMPLECTING;
            }
        }
        else if (complectingTask.getState().equals(ComplectingTaskState.READY_FOR_SHIPPING)){
            return MovementItemState.READY_FOR_SHIPPING;
        }
        else if (complectingTask.getState().equals(ComplectingTaskState.SHIPPED)){
            //Next states depends on delivery note document state.
            DeliveryNoteState deliveryNoteState = complectingTask.getChargeOffItem().getDeliveryNoteItem().getDeliveryNote().getState();
            if (deliveryNoteState.equals(DeliveryNoteState.SHIPPING_TO_WAREHOUSE)){
                return MovementItemState.SHIPPING;
            }
            else if (deliveryNoteState.equals(DeliveryNoteState.SHIPPED)){
                return MovementItemState.SHIPPED;
            }
            else if (deliveryNoteState.equals(DeliveryNoteState.POSTING)){
                return MovementItemState.POSTING;
            }
            else if (deliveryNoteState.equals(DeliveryNoteState.CLOSED)){
                return MovementItemState.POSTED;
            }
        }
        throw new RuntimeException("MovementService.translateComplectingTaskState -- cannot define movement item state.");
    }

    /**
     * Translates complecting task processing result into movement item processing result.
     * @param complectingTask
     * @return
     */
    private MovementItemProcessingResult translateComplectingTaskProcessingResult(ComplectingTask complectingTask) {
        if (complectingTask == null || !complectingTask.isProcessed()){
            return null;
        }
        else {
            if (complectingTask.getFoundCount().equals(complectingTask.getMovementItem().getAmount())){
                return MovementItemProcessingResult.SUCCESS;
            }
            else{
                return MovementItemProcessingResult.PROBLEM;
            }
        }
    }

    /**
     * Returns a list of movement items, that are linked with items of given delivery note.
     * @param deliveryNoteId
     * @return
     */
    public List<MovementItem> getMovementItemsForDeliveryNote(long deliveryNoteId) {
        return movementItemDAO.getMovementItemsForDeliveryNote(deliveryNoteId);
    }

    //========================= Spring setters ===========================
    public void setMovementDAO(MovementDAO movementDAO) {
        this.movementDAO = movementDAO;
    }

    public void setMovementItemDAO(MovementItemDAO movementItemDAO) {
        this.movementItemDAO = movementItemDAO;
    }
}
