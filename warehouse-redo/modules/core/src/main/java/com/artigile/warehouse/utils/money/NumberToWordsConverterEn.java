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

/**
 * @author Valery Barysok, 10/1/11
 */

public class NumberToWordsConverterEn extends AbstractNumberToWordsConverter {

    @Override
    protected NumberWords getNumberWords() {
        return NumberWordsLocale.NUMBER_WORDS_LOCALE_EN;
    }
}
