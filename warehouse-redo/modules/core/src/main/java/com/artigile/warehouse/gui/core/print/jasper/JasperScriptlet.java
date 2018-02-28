/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.print.jasper;

import com.artigile.warehouse.domain.Gender;
import com.artigile.warehouse.utils.money.MoneyUtils;
import net.sf.jasperreports.engine.JRDefaultScriptlet;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Scriptlet helping to print document using Jasper Reports.
 * @author Aliaksandr Chyrtsik
 * @since 23.05.13
 */
public class JasperScriptlet extends JRDefaultScriptlet {
    /**
     * Get amount of money spelled as words in proper forms.
     * @param amount amount to get spelling of.
     * @param currencyId currency to use.
     * @return amount of money as words.
     */
    public String getMoneyAmountInWords(BigDecimal amount, long currencyId){
        return MoneyUtils.getMoneyAmountInWords(amount, currencyId);
    }

    /**
     * Get integer number spelled in words.
     * @param number number to spell.
     * @param gender gender of number to use.
     * @return number in words with given gender.
     */
    public String getIntegerNumberInWords(long number, String gender){
        return MoneyUtils.getIntegerNumberToWords(BigInteger.valueOf(number), Gender.valueOf(gender));
    }
}
