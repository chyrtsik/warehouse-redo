/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.finance.Currency;
import com.artigile.warehouse.domain.finance.CurrencyWord;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.CurrencyWordTO;

/**
 * @author Valery Barysok, 10/2/11
 */

public final class CurrencyWordTransformer {

    private CurrencyWordTransformer() {
    }

    public static CurrencyWordTO transform(CurrencyWord currencyWord, CurrencyTO currencyTO) {
        if (currencyWord == null) {
            return null;
        }

        CurrencyWordTO currencyWordTO = new CurrencyWordTO();
        update(currencyWordTO, currencyWord, currencyTO);
        return currencyWordTO;
    }

    public static CurrencyWord transform(CurrencyWordTO currencyWordTO, Currency currency) {
        CurrencyWord currencyWord = null;
        if (currencyWordTO != null) {
            currencyWord = SpringServiceContext.getInstance().getCurrencyService().getCurrencyWordById(currencyWordTO.getId());
        }

        if (currencyWord == null) {
            currencyWord = new CurrencyWord();
            currencyWord.setCurrency(currency);
        }

        update(currencyWord, currencyWordTO, currency);
        return currencyWord;
    }

    public static void update(CurrencyWordTO currencyWordTO, CurrencyWord currencyWord, CurrencyTO currencyTO) {
        if (currencyWord == null) {
            return;
        }

        currencyWordTO.setId(currencyWord.getId());
        currencyWordTO.setCurrency(currencyTO);
        currencyWordTO.setUnitWord(currencyWord.getUnitWord());
        currencyWordTO.setTwoUnitsWord(currencyWord.getTwoUnitsWord());
        currencyWordTO.setFiveUnitsWord(currencyWord.getFiveUnitsWord());
        currencyWordTO.setGender(currencyWord.getGender());
        currencyWordTO.setFractionalPart(currencyWord.getFractionalPart());
        currencyWordTO.setFractionalPrecision(currencyWord.getFractionalPrecision());
        currencyWordTO.setFractionalUnitWord(currencyWord.getFractionalUnitWord());
        currencyWordTO.setFractionalTwoUnitsWord(currencyWord.getFractionalTwoUnitsWord());
        currencyWordTO.setFractionalFiveUnitsWord(currencyWord.getFractionalFiveUnitsWord());
        currencyWordTO.setFractionalGender(currencyWord.getFractionalGender());
    }

    public static void update(CurrencyWord currencyWord, CurrencyWordTO currencyWordTO, Currency currency) {
        if (currencyWordTO == null) {
            return;
        }

        currencyWord.setId(currencyWordTO.getId());
        currencyWord.setCurrency(currency);
        currencyWord.setUnitWord(currencyWordTO.getUnitWord());
        currencyWord.setTwoUnitsWord(currencyWordTO.getTwoUnitsWord());
        currencyWord.setFiveUnitsWord(currencyWordTO.getFiveUnitsWord());
        currencyWord.setGender(currencyWordTO.getGender());
        currencyWord.setFractionalPart(currencyWordTO.getFractionalPart());
        currencyWord.setFractionalPrecision(currencyWordTO.getFractionalPrecision());
        currencyWord.setFractionalUnitWord(currencyWordTO.getFractionalUnitWord());
        currencyWord.setFractionalTwoUnitsWord(currencyWordTO.getFractionalTwoUnitsWord());
        currencyWord.setFractionalFiveUnitsWord(currencyWordTO.getFractionalFiveUnitsWord());
        currencyWord.setFractionalGender(currencyWordTO.getFractionalGender());
    }
}
