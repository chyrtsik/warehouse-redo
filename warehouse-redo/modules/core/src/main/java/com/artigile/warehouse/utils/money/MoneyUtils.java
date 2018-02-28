/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.money;

import com.artigile.warehouse.domain.Gender;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.CurrencyWordTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Utility class for money-related staff.
 *
 * @author Aliaksandr.Chyrtsik, 01.10.11
 */
public final class MoneyUtils {
    private MoneyUtils(){
    }

    /**
     * All supported locales of money utils (enum used as a factory).
     */
    public enum MoneyLocale {
        MONEY_LOCALE_EN(new NumberToWordsConverterEn(), new CurrencyToWordsConverterEn()),
        MONEY_LOCALE_RU(new NumberToWordsConverterRu(), new CurrencyToWordsConverterRu());

        private NumberToWordsConverter numberToWordsConverter;

        private CurrencyToWordsConverter currencyToWordsConverter;

        MoneyLocale(NumberToWordsConverter numberToWordsConverter, CurrencyToWordsConverter currencyToWordsConverter) {
            this.numberToWordsConverter = numberToWordsConverter;
            this.currencyToWordsConverter = currencyToWordsConverter;
        }

        public NumberToWordsConverter getNumberToWordsConverter() {
            return numberToWordsConverter;
        }

        public CurrencyToWordsConverter getCurrencyToWordsConverter() {
            return currencyToWordsConverter;
        }
    }

    public static String getIntegerNumberToWords(BigInteger number, Gender gender){
        return getMoneyLocale().getNumberToWordsConverter().getIntegerAsWords(number, gender);
    }

    public static String getMoneyAmountInWords(BigDecimal amount, long currencyId) {
        CurrencyTO currencyTO =  SpringServiceContext.getInstance().getCurrencyService().getCurrencyTOById(currencyId);
        if (currencyTO == null){
            throw new IllegalArgumentException("Invalid currency id: " + currencyId);
        }
        return getMoneyAmountInWords(amount, currencyTO);
    }

    /**
     * Returns string representation of the money amount using current locale.
     * @param amount amount of money to be converted into string.
     * @param currency currency for amount naming.
     * @return string representation of the amount given.
     */
    public static String getMoneyAmountInWords(BigDecimal amount, CurrencyTO currency) {
        return getMoneyAmountInWordsUsingLocale(amount, currency, getMoneyLocale());
    }

    private static MoneyLocale getMoneyLocale() {
        return MoneyLocale.valueOf(I18nSupport.message("money.locale"));
    }

    public static String getMoneyAmountInWordsUsingLocale(BigDecimal amount, CurrencyTO currency, MoneyLocale locale) {
        CurrencyWordTO currencyWord = currency.getCurrencyWord();

        String integerPart = locale.getNumberToWordsConverter().getIntegerPartAsWords(amount, currencyWord.getGender());
        String integerPartCurrency = locale.getCurrencyToWordsConverter().getCurrencyForIntegerPart(amount, currency);

        StringBuilder resultBuilder = new StringBuilder();
        if (!integerPart.isEmpty()) {
            resultBuilder.append(integerPart);
            if (!integerPartCurrency.isEmpty()) {
                resultBuilder.append(' ').append(integerPartCurrency);
            }
        }

        if (currencyWord.getFractionalPart()) {
            String fractalPart = locale.getNumberToWordsConverter().getFractalPartAsWords(amount, currencyWord.getFractionalPrecision(), currencyWord.getFractionalGender());
            String fractalPartCurrency = locale.getCurrencyToWordsConverter().getCurrencyForFractalPart(amount, currency);
            if (!fractalPart.isEmpty()) {
                if (resultBuilder.length() > 0) {
                    resultBuilder.append(' ');
                }
                resultBuilder.append(fractalPart);
                if (!fractalPartCurrency.isEmpty()) {
                    resultBuilder.append(' ').append(fractalPartCurrency);
                }
            }
        }
        return resultBuilder.toString();
    }
}
