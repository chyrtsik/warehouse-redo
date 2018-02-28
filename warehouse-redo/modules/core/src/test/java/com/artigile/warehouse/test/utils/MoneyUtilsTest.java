/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.test.utils;

import com.artigile.warehouse.domain.Gender;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.CurrencyWordTO;
import com.artigile.warehouse.utils.money.MoneyUtils;
import com.artigile.warehouse.utils.money.NumberToWordsConverter;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Tests for money-related utilities.
 * @author Aliaksandr.Chyrtsik, 01.10.11
 */
public class MoneyUtilsTest {

    @Test
    public void testNumberToWordsEn() {
        MoneyUtils.MoneyLocale moneyLocale = MoneyUtils.MoneyLocale.MONEY_LOCALE_EN;
        NumberToWordsConverter numberToWordsConverter = moneyLocale.getNumberToWordsConverter();

        Assert.assertEquals("one", numberToWordsConverter.getIntegerAsWords(new BigInteger("1"), Gender.MASCULINE));

        Assert.assertEquals("one", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("1.1")));
        Assert.assertEquals("two", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("2.345")));
        Assert.assertEquals("three", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("3.224")));
        Assert.assertEquals("four", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("4.5564")));
        Assert.assertEquals("five", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("5.4623")));
        Assert.assertEquals("six", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("6.65654")));
        Assert.assertEquals("seven", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("7.5654")));
        Assert.assertEquals("eight", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("8.564")));
        Assert.assertEquals("nine", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("9.56554")));
        Assert.assertEquals("ten", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("10.64")));
        Assert.assertEquals("eleven", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("11.433")));
        Assert.assertEquals("twelve", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("12.6544")));
        Assert.assertEquals("thirteen", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("13.654")));
        Assert.assertEquals("fourteen", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("14.6767")));
        Assert.assertEquals("fifteen", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("15.7544")));
        Assert.assertEquals("sixteen", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("16.6545")));
        Assert.assertEquals("seventeen", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("17.6754")));
        Assert.assertEquals("eighteen", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("18.6544")));
        Assert.assertEquals("nineteen", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("19.6544")));
        Assert.assertEquals("twenty", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("20.5433")));
        Assert.assertEquals("thirty", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("30.443")));
        Assert.assertEquals("fourty", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("40.543")));
        Assert.assertEquals("fifty", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("50.434")));
        Assert.assertEquals("sixty", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("60.173")));
        Assert.assertEquals("seventy", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("70.6733")));
        Assert.assertEquals("eighty", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("80.78434")));
        Assert.assertEquals("ninety", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("90.56")));
        Assert.assertEquals("one hundred", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("100.008")));
        Assert.assertEquals("one hundred thousand", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("100000.00004")));
        Assert.assertEquals("one million", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("1000000.5432")));
        Assert.assertEquals("ten million", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("10000000.678")));
        Assert.assertEquals("one thousand one hundred twenty three", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("1123.333")));
        Assert.assertEquals("five hundred sixty two million two hundred eighty one thousand nine hundred fifty eight", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("562281958.555")));
        Assert.assertEquals("five hundred sixty two million nine hundred fifty eight", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("562000958.666")));
    }

    @Test
    public void testNumberToWordsRu() {
        MoneyUtils.MoneyLocale moneyLocale = MoneyUtils.MoneyLocale.MONEY_LOCALE_RU;
        NumberToWordsConverter numberToWordsConverter = moneyLocale.getNumberToWordsConverter();

        Assert.assertEquals("один", numberToWordsConverter.getIntegerAsWords(new BigInteger("1"), Gender.MASCULINE));
        Assert.assertEquals("одна", numberToWordsConverter.getIntegerAsWords(new BigInteger("1"), Gender.FEMININE));
        Assert.assertEquals("одно", numberToWordsConverter.getIntegerAsWords(new BigInteger("1"), Gender.NEUTER));
        Assert.assertEquals("два", numberToWordsConverter.getIntegerAsWords(new BigInteger("2"), Gender.MASCULINE));
        Assert.assertEquals("две", numberToWordsConverter.getIntegerAsWords(new BigInteger("2"), Gender.FEMININE));
        Assert.assertEquals("два", numberToWordsConverter.getIntegerAsWords(new BigInteger("2"), Gender.NEUTER));

        Assert.assertEquals("один", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("1.1")));
        Assert.assertEquals("два", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("2.345")));
        Assert.assertEquals("три", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("3.224")));
        Assert.assertEquals("четыре", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("4.5564")));
        Assert.assertEquals("пять", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("5.4623")));
        Assert.assertEquals("шесть", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("6.65654")));
        Assert.assertEquals("семь", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("7.5654")));
        Assert.assertEquals("восемь", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("8.564")));
        Assert.assertEquals("девять", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("9.56554")));
        Assert.assertEquals("десять", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("10.64")));
        Assert.assertEquals("одиннадцать", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("11.433")));
        Assert.assertEquals("двенадцать", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("12.6544")));
        Assert.assertEquals("тринадцать", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("13.654")));
        Assert.assertEquals("четырнадцать", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("14.6767")));
        Assert.assertEquals("пятнадцать", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("15.7544")));
        Assert.assertEquals("шестнадцать", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("16.6545")));
        Assert.assertEquals("семнадцать", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("17.6754")));
        Assert.assertEquals("восемнадцать", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("18.6544")));
        Assert.assertEquals("девятнадцать", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("19.6544")));
        Assert.assertEquals("двадцать", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("20.5433")));
        Assert.assertEquals("тридцать", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("30.443")));
        Assert.assertEquals("сорок", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("40.543")));
        Assert.assertEquals("пятьдесят", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("50.434")));
        Assert.assertEquals("шестьдесят", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("60.173")));
        Assert.assertEquals("семьдесят", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("70.6733")));
        Assert.assertEquals("восемьдесят", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("80.78434")));
        Assert.assertEquals("девяносто", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("90.56")));
        Assert.assertEquals("сто", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("100.008")));
        Assert.assertEquals("сто тысяч", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("100000.00004")));
        Assert.assertEquals("один миллион", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("1000000.5432")));
        Assert.assertEquals("десять миллионов", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("10000000.678")));
        Assert.assertEquals("одна тысяча сто двадцать три", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("1123.333")));
        Assert.assertEquals("пятьсот шестьдесят два миллиона двести восемьдесят одна тысяча девятьсот пятьдесят восемь", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("562281958.555")));
        Assert.assertEquals("пятьсот шестьдесят два миллиона девятьсот пятьдесят восемь", numberToWordsConverter.getIntegerPartAsWords(new BigDecimal("562000958.666")));
    }

    @Test
    public void testMoneyToWordsConversionRu(){
        MoneyUtils.MoneyLocale moneyLocale = MoneyUtils.MoneyLocale.MONEY_LOCALE_RU;
        CurrencyTO currency = new CurrencyTO();
        CurrencyWordTO currencyWord = new CurrencyWordTO();
        currencyWord.setGender(Gender.MASCULINE);
        currencyWord.setUnitWord("рубль");
        currencyWord.setTwoUnitsWord("рубля");
        currencyWord.setFiveUnitsWord("рублей");
        currency.setCurrencyWord(currencyWord);
        currency.setSign("RUB");

        Assert.assertEquals("один рубль", MoneyUtils.getMoneyAmountInWordsUsingLocale(BigDecimal.valueOf(1), currency, moneyLocale));
        Assert.assertEquals("два рубля", MoneyUtils.getMoneyAmountInWordsUsingLocale(BigDecimal.valueOf(2), currency, moneyLocale));
        Assert.assertEquals("три рубля", MoneyUtils.getMoneyAmountInWordsUsingLocale(BigDecimal.valueOf(3), currency, moneyLocale));
        Assert.assertEquals("четыре рубля", MoneyUtils.getMoneyAmountInWordsUsingLocale(BigDecimal.valueOf(4), currency, moneyLocale));
        Assert.assertEquals("пять рублей", MoneyUtils.getMoneyAmountInWordsUsingLocale(BigDecimal.valueOf(5), currency, moneyLocale));
        Assert.assertEquals("шесть рублей", MoneyUtils.getMoneyAmountInWordsUsingLocale(BigDecimal.valueOf(6), currency, moneyLocale));
        Assert.assertEquals("шесть рублей", MoneyUtils.getMoneyAmountInWordsUsingLocale(BigDecimal.valueOf(6), currency, moneyLocale));
        Assert.assertEquals("тринадцать рублей", MoneyUtils.getMoneyAmountInWordsUsingLocale(BigDecimal.valueOf(13), currency, moneyLocale));

        currencyWord.setFractionalPart(true);
        currencyWord.setFractionalPrecision(2);
        currencyWord.setFractionalGender(Gender.FEMININE);
        currencyWord.setFractionalUnitWord("копейка");
        currencyWord.setFractionalTwoUnitsWord("копейки");
        currencyWord.setFractionalFiveUnitsWord("копеек");

        Assert.assertEquals("ноль рублей одна копейка", MoneyUtils.getMoneyAmountInWordsUsingLocale(BigDecimal.valueOf(0.01), currency, moneyLocale));
        Assert.assertEquals("три рубля двенадцать копеек", MoneyUtils.getMoneyAmountInWordsUsingLocale(BigDecimal.valueOf(3.12234532), currency, moneyLocale));
    }

    private String amountToString(double amount, CurrencyTO currency, MoneyUtils.MoneyLocale moneyLocale){
        return MoneyUtils.getMoneyAmountInWordsUsingLocale(BigDecimal.valueOf(amount), currency, moneyLocale);
    }
}
