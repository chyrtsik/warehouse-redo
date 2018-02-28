package com.artigile.warehouse.bl.detail;

import com.artigile.warehouse.dao.MovementItemDAO;
import com.artigile.warehouse.dao.OrderItemsDAO;
import com.artigile.warehouse.domain.movement.Movement;
import com.artigile.warehouse.domain.movement.MovementItem;
import com.artigile.warehouse.domain.movement.MovementItemState;
import com.artigile.warehouse.domain.orders.Order;
import com.artigile.warehouse.domain.orders.OrderItem;
import com.artigile.warehouse.domain.orders.OrderItemState;
import com.artigile.warehouse.domain.orders.OrderSubItem;
import com.artigile.warehouse.domain.postings.PostingItem;
import com.artigile.warehouse.domain.warehouse.StoragePlace;
import com.artigile.warehouse.domain.warehouse.WarehouseBatch;
import com.artigile.warehouse.utils.dto.details.DetailBatchDocumentType;
import com.artigile.warehouse.utils.dto.details.DetailBatchReservesTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Valery Barysok, 2013-05-20
 */
@Transactional
public class DetailBatchReservesService {

    private OrderItemsDAO orderItemsDAO;

    private MovementItemDAO movementItemDAO;

    public List<DetailBatchReservesTO> getDetailBatchReserves(DetailBatchReservesFilter filter) {
        List<DetailBatchReservesTO> result = new ArrayList<DetailBatchReservesTO>();

        filter.setOrderItemStates(Arrays.asList(OrderItemState.NOT_PROCESSED, OrderItemState.PROCESSING,
                OrderItemState.PROCESSED, OrderItemState.READY_FOR_SHIPPING));

        filter.setMovementItemStates(Arrays.asList(MovementItemState.NOT_PROCESSED, MovementItemState.COMPLECTING,
                MovementItemState.COMPLECTED, MovementItemState.READY_FOR_SHIPPING));

        List<OrderSubItem> orderSubItems = orderItemsDAO.findByFilter(filter);
        for (OrderSubItem orderSubItem : orderSubItems) {
            OrderItem orderItem = orderSubItem.getOrderItem();
            DetailBatchReservesTO detailBatchReservesTO = new DetailBatchReservesTO();
            detailBatchReservesTO.setDocumentType(DetailBatchDocumentType.ORDER);
            Order order = orderItem.getOrder();
            detailBatchReservesTO.setNumber(order.getNumber());
            detailBatchReservesTO.setCreateDate(order.getCreateDate());
            detailBatchReservesTO.setContractorName(order.getContractor().getName());
            detailBatchReservesTO.setItemNumber(orderItem.getNumber());
            detailBatchReservesTO.setAmount(orderItem.getAmount());
            detailBatchReservesTO.setUserName(order.getCreatedUser().getDisplayName());
            StoragePlace storagePlace = orderSubItem.getStoragePlace();
            detailBatchReservesTO.setWarehouseName(storagePlace.getWarehouse().getName());
            detailBatchReservesTO.setStoragePlaceSign(storagePlace.getSign());
            WarehouseBatch warehouseBatch = orderSubItem.getWarehouseBatch();
            PostingItem postingItem = warehouseBatch != null ? warehouseBatch.getPostingItem() : null;
            detailBatchReservesTO.setBatchNo(postingItem != null ? postingItem.getId() : null);
            result.add(detailBatchReservesTO);
        }

        List<MovementItem> movementItems = movementItemDAO.findByFilter(filter);
        for (MovementItem movementItem : movementItems) {
            DetailBatchReservesTO detailBatchReservesTO = new DetailBatchReservesTO();
            detailBatchReservesTO.setDocumentType(DetailBatchDocumentType.MOVEMENT);
            Movement movement = movementItem.getMovement();
            detailBatchReservesTO.setNumber(movement.getNumber());
            detailBatchReservesTO.setCreateDate(movement.getCreateDate());
            detailBatchReservesTO.setItemNumber(movementItem.getNumber());
            detailBatchReservesTO.setAmount(movementItem.getAmount());
            detailBatchReservesTO.setUserName(movement.getCreateUser().getDisplayName());
            StoragePlace fromStoragePlace = movementItem.getFromStoragePlace();
            detailBatchReservesTO.setWarehouseName(fromStoragePlace.getWarehouse().getName());
            detailBatchReservesTO.setStoragePlaceSign(fromStoragePlace.getSign());
            WarehouseBatch warehouseBatch = movementItem.getWarehouseBatch();
            PostingItem postingItem = warehouseBatch != null ? warehouseBatch.getPostingItem() : null;
            detailBatchReservesTO.setBatchNo(postingItem != null ? postingItem.getId() : null);
            result.add(detailBatchReservesTO);
        }

        return result;
    }

    public void setOrderItemsDAO(OrderItemsDAO orderItemsDAO) {
        this.orderItemsDAO = orderItemsDAO;
    }

    public void setMovementItemDAO(MovementItemDAO movementItemDAO) {
        this.movementItemDAO = movementItemDAO;
    }
}
