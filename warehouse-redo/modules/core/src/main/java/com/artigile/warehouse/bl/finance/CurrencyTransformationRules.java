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

import com.artigile.warehouse.bl.common.listeners.EntityTransformRule;
import com.artigile.warehouse.bl.common.listeners.EntityTransformer;
import com.artigile.warehouse.bl.common.listeners.GlobalDataChangeNotifier;
import com.artigile.warehouse.bl.common.listeners.OperationSavingEntityTransformRule;
import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.domain.finance.ExchangeRate;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.ExchangeRateTO;
import com.artigile.warehouse.utils.transofmers.CurrencyTransformer;

/**
 * @author Shyrik, 01.04.2010
 */

/**
 * Rules of transformation for Currency-related classes.
 */
public class CurrencyTransformationRules {
    public CurrencyTransformationRules(GlobalDataChangeNotifier notifier) {
        notifier.registerTransformRule(getCurrencyToCurrencyTORule());
        notifier.registerTransformRule(getExchangeRateToExchangeRateTORule());
    }

    private EntityTransformRule getCurrencyToCurrencyTORule() {
        //Rule for transformation from Currency entity to CurrencyTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(Currency.class);
        rule.setTargetClass(CurrencyTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return CurrencyTransformer.transformCurrency((Currency)entity);
            }
        });
        return rule;
    }

    private EntityTransformRule getExchangeRateToExchangeRateTORule() {
        //Rule for transformation from ExchangeRate entity to ExchangeRateTO DTO.
        OperationSavingEntityTransformRule rule = new OperationSavingEntityTransformRule();
        rule.setFromClass(ExchangeRate.class);
        rule.setTargetClass(ExchangeRateTO.class);
        rule.setEntityTransformer(new EntityTransformer(){
            @Override
            public Object transform(Object entity) {
                return CurrencyTransformer.transformExchangeRate((ExchangeRate)entity);
            }
        });
        return rule;
    }
}


