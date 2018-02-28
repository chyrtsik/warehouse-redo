/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.purchase.items;

import com.artigile.warehouse.bl.finance.CurrencyExchangeService;
import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.needs.WareNeedItemTO;
import com.artigile.warehouse.utils.dto.purchase.PurchaseItemTO;

import java.awt.*;
import java.math.BigDecimal;

/**
 * @author Valery Barysok, 21.07.2010
 */
public class PurchaseItemStyleFactory implements StyleFactory {

    /**
     * This style is applied if purchase price is higher then max planned price for ware need item.
     */
    private Style tooHighPriceStyle = new Style(new Color(255, 200, 200));

    private CurrencyExchangeService exchangeService = SpringServiceContext.getInstance().getCurencyExchangeService();

    @Override
    public Style getStyle(Object rowData) {
        PurchaseItemTO purchaseItem = (PurchaseItemTO) rowData;
        WareNeedItemTO wareNeedItem = purchaseItem.getWareNeedItem();

        if (wareNeedItem.getMaxPrice() == null){
            //No max price set for ware need item. This is normal case.
            return null;
        }

        long wareNeedItemCurrencyId = wareNeedItem.getCurrency().getId();
        long purchaseItemCurrencyId = purchaseItem.getPurchase().getCurrency().getId();
        if (wareNeedItemCurrencyId == purchaseItemCurrencyId) {
            if (purchaseItem.getWareNeedItem().getMaxPrice().compareTo(purchaseItem.getPrice()) < 0) {
                return tooHighPriceStyle;
            }
        } else {
            //We should perform currency conversion before price comparison.
            BigDecimal maxPrice = exchangeService.convert(purchaseItemCurrencyId, wareNeedItemCurrencyId, wareNeedItem.getMaxPrice());
            if (maxPrice.compareTo(purchaseItem.getPrice()) < 0) {
                return tooHighPriceStyle;
            }
        }
        
        return null;
    }
}
