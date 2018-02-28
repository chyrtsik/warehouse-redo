/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.finance;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.domain.finance.Payment;

/**
 * @author Shyrik, 13.03.2010
 */

/**
 * Adapter for PaymentEventListener interface.
 */
public class PaymentEventAdapter implements PaymentEventListener {
    @Override
    public void onPaymentPerformed(Payment payment) throws BusinessException {
    }
}
