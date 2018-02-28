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

/**
 * @author Valery Barysok, 10/2/11
 */

public enum NumberWordsLocale implements NumberWords {
    NUMBER_WORDS_LOCALE_EN {
        private final String[][] unitWords = {
            {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"}
        };

        private String[] secondDozenWords = {
                "", "eleven", "twelve", "thirteen", "fourteen", "fifteen",
                "sixteen", "seventeen", "eighteen", "nineteen"
        };

        private String[] dozenWords = {
                "", "ten", "twenty", "thirty", "fourty", "fifty", "sixty",
                "seventy", "eighty", "ninety"
        };

        private final String[] hundredWords = {
                "", "one hundred", "two hundred", "three hundred", "four hundred", "five hundred",
                "six hundred", "seven hundred", "eight hundred", "nine hundred"
        };

        private final String[] forms = {
                "", "thousand", "million", "billion", "trillion", "trilliard"
        };

        @Override
        public String getUnitWord(int n, Gender gender) {
            return unitWords[0][n];
        }

        @Override
        public String getSecondDozenWord(int n) {
            return secondDozenWords[n];
        }

        @Override
        public String getDozenWord(int n) {
            return dozenWords[n];
        }

        @Override
        public String getHundredWord(int n) {
            return hundredWords[n];
        }

        @Override
        public Gender getUnitGender(int pos) {
            return Gender.MASCULINE;
        }

        @Override
        public String getUnit(int pos, int value) {
            return forms[pos];
        }
    }, NUMBER_WORDS_LOCALE_RU {
        private final String[][] unitWords = {
            {"ноль", "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"},
            {"ноль", "одна", "две", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"},
            {"ноль", "одно", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"},
        };

        private String[] secondDozenWords = {
                "", "одиннадцать", "двенадцать", "тринадцать", "четырнадцать", "пятнадцать",
                "шестнадцать", "семнадцать", "восемнадцать", "девятнадцать", "двадцать"
        };

        private String[] dozenWords = {
                "", "десять", "двадцать", "тридцать", "сорок", "пятьдесят",
                "шестьдесят", "семьдесят", "восемьдесят", "девяносто"
        };

        private final String[] hundredWords = {
                "", "сто", "двести", "триста", "четыреста", "пятьсот",
                "шестьсот", "семьсот", "восемьсот", "девятьсот"
        };

        private final String[][] forms = {
            {"", "", "", "0"},
            {"тысяча", "тысячи", "тысяч", "FEMININE"},
            {"миллион", "миллиона", "миллионов", "MASCULINE"},
            {"миллиард", "миллиарда", "миллиардов", "MASCULINE"},
            {"триллион", "триллиона", "триллионов", "MASCULINE"},
            {"триллиард", "триллиарда", "триллиардов", "MASCULINE"},
        };

        @Override
        public String getUnitWord(int n, Gender gender) {
            int pos = gender == Gender.MASCULINE ? 0 : gender == Gender.FEMININE ? 1 : 2;
            return unitWords[pos][n];
        }

        @Override
        public String getSecondDozenWord(int n) {
            return secondDozenWords[n];
        }

        @Override
        public String getDozenWord(int n) {
            return dozenWords[n];
        }

        @Override
        public String getHundredWord(int n) {
            return hundredWords[n];
        }

        @Override
        public Gender getUnitGender(int pos) {
            return Gender.valueOf(forms[pos][3]);
        }

        @Override
        public String getUnit(int pos, int value) {
            int digits = value % 100;
            if (digits > 4 && digits < 21) {
                return forms[pos][2];
            }

            int digit = value % 10;
            if (digit == 1) {
                return forms[pos][0];
            } else if (2 <= digit && digit <= 4) {
                return forms[pos][1];
            }

            return forms[pos][2];
        }
    };
}
