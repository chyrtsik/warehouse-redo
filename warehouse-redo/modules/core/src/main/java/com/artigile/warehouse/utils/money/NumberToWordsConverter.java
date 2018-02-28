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

import com.artigile.warehouse.domain.Gender;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Interface for converting number to words.
 *
 * @author Aliaksandr.Chyrtsik, 01.10.11
 */
public interface NumberToWordsConverter {

    String getIntegerAsWords(BigInteger number, Gender gender);

    /**
     * Calculates string representation of an integer number.
     * @param number number to be converted into words.
     * @return number in words.
     */
    String getIntegerPartAsWords(BigDecimal number);

    String getIntegerPartAsWords(BigDecimal number, Gender gender);

    /**
     * Calculates string representation of a fractal part of a number.
     * @param number number to be converted into words.
     * @param digits number of fractal digits to be converted.
     * @return fractal digits in words.
     */
    String getFractalPartAsWords(BigDecimal number, int digits);

    String getFractalPartAsWords(BigDecimal number, int digits, Gender gender);
}
