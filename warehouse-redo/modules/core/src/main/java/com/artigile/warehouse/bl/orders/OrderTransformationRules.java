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

import com.artigile.warehouse.bl.common.listeners.*;
import com.artigile.warehouse.domain.orders.Order;
import com.artigile.warehouse.domain.orders.OrderItem;
import com.artigile.warehouse.domain.orders.OrderProcessingInfo;
import com.artigile.warehouse.utils.dto.orders.OrderItemTO;
import com.artigile.warehouse.utils.dto.orders.OrderTO;
import com.artigile.warehouse.utils.dto.orders.OrderTOForReport;
import com.artigile.warehouse.utils.transofmers.OrdersTransformer;

/**
 * @author Shyrik, 27.03.2010
 */

/**
 * Rules of transformation for orders-related classes.
 */
public class OrderTransformationRules {
    public OrderTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getOrderToOrderTORule());
        notifier.registerTransformRule(getOrderToOrderTOForReportRule());
        notifier.registerTransformRule(getOrderProcessingInfoToOrderTOForReportRule());
        notifier.registerTransformRule(getOrderItemToOrderItemTORule());
        notifier.registerTransformRule(getOrderItemToOrderTORule());
    }

    private EntityTransformRule getOrderToOrderTORule() {
        //Rule for transformation from Order entity to OrderTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Order.class);
        rule.setTargetClass(OrderTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return OrdersTransformer.transform((Order)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getOrderToOrderTOForReportRule() {
        //Rule for transformation from Order entity to OrderTOForReport DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Order.class);
        rule.setTargetClass(OrderTOForReport.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return OrdersTransformer.transformForReport((Order)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getOrderProcessingInfoToOrderTOForReportRule() {
        //Rule for transformation from OrderProcessingInfo entity to OrderTOForReport DTO.
        OperationReductionEntityTransformRule rule = new OperationReductionEntityTransformRule();
        rule.setFromClass(OrderProcessingInfo.class);
        rule.setFromOperations(EntityOperation.ALL);
        rule.setTargetClass(OrderTOForReport.class);
        rule.setTargetOperation(EntityOperation.CHANGE);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return OrdersTransformer.transformForReport(((OrderProcessingInfo)entity).getOrder());
            }
        });
        return rule;
    }

    private EntityTransformRule getOrderItemToOrderItemTORule() {
        //Rule for transformation from OrderItem entity to OrderItemTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(OrderItem.class);
        rule.setTargetClass(OrderItemTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return OrdersTransformer.transformItem((OrderItem)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getOrderItemToOrderTORule() {
        //Rule for transformation from OrderItem entity to OrderTOForReport DTO.
        OperationReductionEntityTransformRule rule = new OperationReductionEntityTransformRule();
        rule.setFromClass(OrderItem.class);
        rule.setFromOperations(EntityOperation.ALL);
        rule.setTargetClass(OrderTOForReport.class);
        rule.setTargetOperation(EntityOperation.CHANGE);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                OrderItem orderItem = (OrderItem) entity;
                return OrdersTransformer.transformForReport(orderItem.getOrder());
            }
        });
        return rule;
    }
}
