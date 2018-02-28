/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.purchase;

import com.artigile.warehouse.domain.purchase.Purchase;

/**
 * @author: Vadim.Zverugo
 */
public class PurchaseChangeAdapter implements PurchaseChangeListener {

    /**
     * Calls when changes purchase state.
     * @param purchase Purchase which changed state.
     */
    @Override
    public void onPurchaseStateChanged(Purchase purchase) {

    }
}
