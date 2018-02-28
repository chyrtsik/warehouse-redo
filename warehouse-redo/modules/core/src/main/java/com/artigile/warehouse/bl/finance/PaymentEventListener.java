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
 * Listener, that is used to catch payment events.
 */
public interface PaymentEventListener {
    /**
     * Called when new payment was performed.
     * @param payment new payment's data.
     * @throws BusinessException listener may throw this exception in order to rollback all operations
     * concerned with this payment (and payment itself).
     */
    void onPaymentPerformed(Payment payment) throws BusinessException;
}
