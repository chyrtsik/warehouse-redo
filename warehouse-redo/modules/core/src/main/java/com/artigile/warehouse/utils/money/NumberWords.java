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

public interface NumberWords {

    /**
     *
     * @param n 0-9
     * @param gender
     * @return
     */
    String getUnitWord(int n, Gender gender);

    /**
     *
     * @param n 11-19
     * @return
     */
    String getSecondDozenWord(int n);

    /**
     *
     * @param n 10,20,30,40,50,60,70,80,90
     * @return
     */
    String getDozenWord(int n);

    /**
     *
     * @param n 100,200,300,400,500,600,700,800,900
     * @return
     */
    String getHundredWord(int n);

    Gender getUnitGender(int pos);

    String getUnit(int pos, int value);
}
