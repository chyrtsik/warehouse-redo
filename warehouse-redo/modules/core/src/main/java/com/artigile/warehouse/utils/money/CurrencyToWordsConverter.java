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

import java.math.BigDecimal;

/**
 * Interface for converting currency to words.
 *
 * @author Aliaksandr.Chyrtsik, 01.10.11
 */
public interface CurrencyToWordsConverter {
    /**
     * Calculates proper currency words for given integer number.
     * @param number amount of money.
     * @param currency currency to be converted into words.
     * @return currency words for integer part of a number.
     */
    String getCurrencyForIntegerPart(BigDecimal number, CurrencyTO currency);

    /**
     * Calculates proper currency words for given fractal part of a given number.
     * @param number amount of money.
     * @param currency currency to be converted into words.
     * @return currency words for fractal part of a number.
     */
    String getCurrencyForFractalPart(BigDecimal number, CurrencyTO currency);
}
