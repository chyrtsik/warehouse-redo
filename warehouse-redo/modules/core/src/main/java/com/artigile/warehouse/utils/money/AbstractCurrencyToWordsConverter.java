/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.money;

import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.CurrencyWordTO;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Valery Barysok, 10/4/11
 */

public abstract class AbstractCurrencyToWordsConverter implements CurrencyToWordsConverter {

    private static final BigInteger BI100 = BigInteger.valueOf(100L);
    private static final BigInteger BI10 = BigInteger.valueOf(10L);

    @Override
    public String getCurrencyForIntegerPart(BigDecimal number, CurrencyTO currency) {
        CurrencyWordTO currencyWord = currency.getCurrencyWord();
        String res = "";
        if (currencyWord != null) {
            BigInteger num = number.toBigInteger();
            int digits = num.remainder(BI100).intValue();
            int digit = num.remainder(BI10).intValue();
            if (11 <= digits && digits < 20) {
                res = currencyWord.getFiveUnitsWord();
            } else if (digit == 1) {
                res = currencyWord.getUnitWord();
            } else if (2 <= digit && digit <= 4) {
                res = currencyWord.getTwoUnitsWord();
            } else {
                res = currencyWord.getFiveUnitsWord();
            }
        }

        return res.isEmpty() ? currency.getSign() : res;
    }

    @Override
    public String getCurrencyForFractalPart(BigDecimal number, CurrencyTO currency) {
        CurrencyWordTO currencyWord = currency.getCurrencyWord();
        String res = "";
        if (currencyWord != null && currencyWord.getFractionalPart()) {
            BigDecimal remainder = number.remainder(BigDecimal.ONE);
            BigInteger num = remainder.movePointRight(currencyWord.getFractionalPrecision()).toBigInteger();
            int digits = num.remainder(BI100).intValue();
            int digit = num.remainder(BI10).intValue();
            if (11 <= digits && digits < 20) {
                res = currencyWord.getFractionalFiveUnitsWord();
            } else if (digit == 1) {
                res = currencyWord.getFractionalUnitWord();
            } else if (2 <= digit && digit <= 4) {
                res = currencyWord.getFractionalTwoUnitsWord();
            } else {
                res = currencyWord.getFractionalFiveUnitsWord();
            }
        }

        return res;
    }
}
