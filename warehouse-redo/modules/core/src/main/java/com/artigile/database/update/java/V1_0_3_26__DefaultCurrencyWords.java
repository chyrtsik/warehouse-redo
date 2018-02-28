/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.database.update.java;

import com.artigile.warehouse.domain.Gender;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.googlecode.flyway.core.migration.java.JavaMigration;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Valery Barysok, 10/3/11
 */

public class V1_0_3_26__DefaultCurrencyWords implements JavaMigration {

    private static final String sqlCurrencyWordInsert = "insert into CurrencyWord " +
            "(currency_id, unitWord, twoUnitsWord, fiveUnitsWord, gender," +
            " fractionalPart, fractionalPrecision, fractionalUnitWord, fractionalTwoUnitsWord," +
            " fractionalFiveUnitsWord, fractionalGender)" +
            " select a.id, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? from Currency a where a.sign=? and not exists (select 1 from CurrencyWord b where b.currency_id=a.id)";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.batchUpdate(sqlCurrencyWordInsert, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                CurrencyWord currencyWord = CURRENCY_WORDS[i];
                ps.setString(1, currencyWord.getUnitWord());
                ps.setString(2, currencyWord.getTwoUnitsWord());
                ps.setString(3, currencyWord.getFiveUnitsWord());
                ps.setString(4, currencyWord.getGender().name());
                ps.setBoolean(5, currencyWord.getFractionalPart());
                ps.setInt(6, currencyWord.getFractionalPrecision());
                ps.setString(7, currencyWord.getFractionalUnitWord());
                ps.setString(8, currencyWord.getFractionalTwoUnitsWord());
                ps.setString(9, currencyWord.getFractionalFiveUnitsWord());
                ps.setString(10, currencyWord.getFractionalGender().name());
                ps.setString(11, currencyWord.getCurrencySign());
            }

            @Override
            public int getBatchSize() {
                return CURRENCY_WORDS.length;
            }
        });
    }

    private static final CurrencyWord[] CURRENCY_WORDS = {
            new CurrencyWord(I18nSupport.message("currency.word.rur.unitWord"),
                    I18nSupport.message("currency.word.rur.twoUnitWords"),
                    I18nSupport.message("currency.word.rur.fiveUnitWords"),
                    Gender.MASCULINE,
                    true,
                    2,
                    I18nSupport.message("currency.word.rur.fractionalUnitWord"),
                    I18nSupport.message("currency.word.rur.fractionalTwoUnitWords"),
                    I18nSupport.message("currency.word.rur.fractionalFiveUnitWords"),
                    Gender.FEMININE,
                    I18nSupport.message("currency.rur.sign")),
            new CurrencyWord(I18nSupport.message("currency.word.br.unitWord"),
                    I18nSupport.message("currency.word.br.twoUnitWords"),
                    I18nSupport.message("currency.word.br.fiveUnitWords"),
                    Gender.MASCULINE,
                    false,
                    2,
                    I18nSupport.message("currency.word.br.fractionalUnitWord"),
                    I18nSupport.message("currency.word.br.fractionalTwoUnitWords"),
                    I18nSupport.message("currency.word.br.fractionalFiveUnitWords"),
                    Gender.FEMININE,
                    I18nSupport.message("currency.br.sign")),
            new CurrencyWord(I18nSupport.message("currency.word.usd.unitWord"),
                    I18nSupport.message("currency.word.usd.twoUnitWords"),
                    I18nSupport.message("currency.word.usd.fiveUnitWords"),
                    Gender.MASCULINE,
                    true,
                    2,
                    I18nSupport.message("currency.word.usd.fractionalUnitWord"),
                    I18nSupport.message("currency.word.usd.fractionalTwoUnitWords"),
                    I18nSupport.message("currency.word.usd.fractionalFiveUnitWords"),
                    Gender.MASCULINE,
                    I18nSupport.message("currency.usd.sign")),
    };

    private static class CurrencyWord {
        private String unitWord;
        private String twoUnitsWord;
        private String fiveUnitsWord;
        private Gender gender;
        private boolean fractionalPart;
        private int fractionalPrecision;
        private String fractionalUnitWord;
        private String fractionalTwoUnitsWord;
        private String fractionalFiveUnitsWord;
        private Gender fractionalGender;
        private String currencySign;

        private CurrencyWord(String unitWord, String twoUnitsWord, String fiveUnitsWord, Gender gender,
                             boolean fractionalPart, int fractionalPrecision, String fractionalUnitWord,
                             String fractionalTwoUnitsWord, String fractionalFiveUnitsWord, Gender fractionalGender,
                             String currencySign) {
            this.unitWord = unitWord;
            this.twoUnitsWord = twoUnitsWord;
            this.fiveUnitsWord = fiveUnitsWord;
            this.gender = gender;
            this.fractionalPart = fractionalPart;
            this.fractionalPrecision = fractionalPrecision;
            this.fractionalUnitWord = fractionalUnitWord;
            this.fractionalTwoUnitsWord = fractionalTwoUnitsWord;
            this.fractionalFiveUnitsWord = fractionalFiveUnitsWord;
            this.fractionalGender = fractionalGender;
            this.currencySign = currencySign;
        }

        public String getUnitWord() {
            return unitWord;
        }

        public String getTwoUnitsWord() {
            return twoUnitsWord;
        }

        public String getFiveUnitsWord() {
            return fiveUnitsWord;
        }

        public Gender getGender() {
            return gender;
        }

        public boolean getFractionalPart() {
            return fractionalPart;
        }

        public int getFractionalPrecision() {
            return fractionalPrecision;
        }

        public String getFractionalUnitWord() {
            return fractionalUnitWord;
        }

        public String getFractionalTwoUnitsWord() {
            return fractionalTwoUnitsWord;
        }

        public String getFractionalFiveUnitsWord() {
            return fractionalFiveUnitsWord;
        }

        public Gender getFractionalGender() {
            return fractionalGender;
        }

        public String getCurrencySign() {
            return currencySign;
        }
    }
}
