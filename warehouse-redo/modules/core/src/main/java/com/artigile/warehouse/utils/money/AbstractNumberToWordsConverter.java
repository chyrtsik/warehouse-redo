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
 * @author Valery Barysok, 10/2/11
 */

public abstract class AbstractNumberToWordsConverter implements NumberToWordsConverter {

    private static final BigInteger BI1000 = BigInteger.valueOf(1000);

    @Override
    public String getIntegerAsWords(BigInteger number, Gender gender) {
        if (number.equals(BigInteger.ZERO)) {
            return getNumberWords().getUnitWord(0, gender);
        }

        StringBuilder res = new StringBuilder();
        for (int pos = 0; number.compareTo(BigInteger.ZERO) > 0; ++pos) {
            BigInteger[] divideAndRemainder = number.divideAndRemainder(BI1000);
            int remainder = divideAndRemainder[1].intValue();
            String triad = triadToString(remainder, pos == 0 ? gender : getNumberWords().getUnitGender(pos), false);
            String unit = getNumberWords().getUnit(pos, remainder);
            StringBuilder sb = new StringBuilder();
            sb.append(triad);
            if (!triad.isEmpty() && !unit.isEmpty()) {
                sb.append(' ');
                sb.append(unit);
            }

            if (sb.length() > 0 && pos > 0 && res.length() > 0) {
                sb.append(' ');
            }
            res.insert(0, sb);
            number = divideAndRemainder[0];
        }
        return res.toString();
    }

    @Override
    public String getIntegerPartAsWords(BigDecimal number) {
        return getIntegerPartAsWords(number, Gender.MASCULINE);
    }

    @Override
    public String getIntegerPartAsWords(BigDecimal number, Gender gender) {
        return getIntegerAsWords(number.toBigInteger(), gender);
    }

    @Override
    public String getFractalPartAsWords(BigDecimal number, int digits) {
        return getFractalPartAsWords(number, digits, Gender.MASCULINE);
    }

    @Override
    public String getFractalPartAsWords(BigDecimal number, int digits, Gender gender) {
        BigDecimal remainder = number.remainder(BigDecimal.ONE);
        return getIntegerAsWords(remainder.movePointRight(digits).toBigInteger(), gender);
    }

    protected abstract NumberWords getNumberWords();

    protected String triadToString(int n, Gender gender, boolean acceptZero) {
        if (!acceptZero && n == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int digits3 = n % 1000;
        int digits2 = n % 100;
        int digits1 = n % 10;

        if (digits3 > 99) {
            sb.append(getNumberWords().getHundredWord(digits3 / 100));
        }

        if (digits2 > 10 && digits2 < 20) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(getNumberWords().getSecondDozenWord(digits1));
            return sb.toString();
        }

        if (digits2 > 9) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(getNumberWords().getDozenWord(digits2 / 10));
        }

        if (sb.length() == 0 || digits1 > 0) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(getNumberWords().getUnitWord(digits1, gender));
        }

        return sb.toString();
    }
}
