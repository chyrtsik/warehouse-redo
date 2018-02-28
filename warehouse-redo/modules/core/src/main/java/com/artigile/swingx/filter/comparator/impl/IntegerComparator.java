/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */
package com.artigile.swingx.filter.comparator.impl;

import com.artigile.swingx.filter.comparator.NumberComparator;
import com.artigile.warehouse.utils.Utils;

/**
 * @author Valery Barysok, 19.12.2009
 */
public final class IntegerComparator extends NumberComparator<Integer> {

    @Override
    protected Integer parse(String text) {
        return Utils.getInteger(text);
    }
}
