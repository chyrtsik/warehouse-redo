package com.artigile.warehouse.bl.detail;

import com.artigile.warehouse.domain.movement.MovementItemState;
import com.artigile.warehouse.domain.orders.OrderItemState;

import java.util.List;

/**
 * @author Valery Barysok, 2013-05-19
 */
public class DetailBatchReservesFilter {

    private Long detailBatchId;

    private Long storagePlaceId;

    private List<OrderItemState> orderItemStates;

    private List<MovementItemState> movementItemStates;

    public DetailBatchReservesFilter(Long detailBatchId) {
        this(detailBatchId, null);
    }

    public DetailBatchReservesFilter(Long detailBatchId, Long storagePlaceId) {
        this.detailBatchId = detailBatchId;
        this.storagePlaceId = storagePlaceId;
    }

    public Long getDetailBatchId() {
        return detailBatchId;
    }

    public Long getStoragePlaceId() {
        return storagePlaceId;
    }

    public List<OrderItemState> getOrderItemStates() {
        return orderItemStates;
    }

    public void setOrderItemStates(List<OrderItemState> orderItemStates) {
        this.orderItemStates = orderItemStates;
    }

    public List<MovementItemState> getMovementItemStates() {
        return movementItemStates;
    }

    public void setMovementItemStates(List<MovementItemState> movementItemStates) {
        this.movementItemStates = movementItemStates;
    }
}
