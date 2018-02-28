/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.orders;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.deliveryNote.DeliveryNoteChangeAdapter;
import com.artigile.warehouse.bl.deliveryNote.DeliveryNoteService;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNote;
import com.artigile.warehouse.domain.deliveryNote.DeliveryNoteState;
import com.artigile.warehouse.domain.orders.OrderSubItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Shyrik, 13.03.2010
 */

/**
 * Synchronizes changes made in delivery notes with appropriate orders.
 */
@Transactional(rollbackFor = BusinessException.class)
public class OrderWithDeliveryNoteSynchronizer extends DeliveryNoteChangeAdapter {
    private DeliveryNoteService deliveryNoteService;
    private OrderService orderService;

    //======================== Construction and initialization =============================

    public OrderWithDeliveryNoteSynchronizer(){
    }

    public void initialize(){
        deliveryNoteService.addListener(this);
    }

    //======================== DeliveryNoteListener implementation =========================

    @Override
    public void onDeliveryNoteStateChanged(DeliveryNote deliveryNote, DeliveryNoteState oldState, DeliveryNoteState newState) throws BusinessException {
        processDeliveryNoteChangeEvent(deliveryNote);
    }

    @Override
    public void onDeliveryNoteCreated(DeliveryNote deliveryNote) throws BusinessException {
        processDeliveryNoteChangeEvent(deliveryNote);
    }

    private void processDeliveryNoteChangeEvent(DeliveryNote deliveryNote) throws BusinessException {
        //Performs recalculating of state of order sub items, that are linked with given delivery note.
        List<OrderSubItem> subItems = orderService.getOrderSubItemsForDeliveryNote(deliveryNote.getId());
        for (OrderSubItem subItem : subItems){
            orderService.recalculateOrderSubItemState(subItem);
        }
    }

    //========================== Spring setters ============================================

    public void setDeliveryNoteService(DeliveryNoteService deliveryNoteService) {
        this.deliveryNoteService = deliveryNoteService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
