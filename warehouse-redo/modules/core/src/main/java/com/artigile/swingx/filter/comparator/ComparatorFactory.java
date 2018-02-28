/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */
package com.artigile.swingx.filter.comparator;

import com.artigile.swingx.filter.comparator.impl.*;
import com.artigile.warehouse.utils.custom.types.VariantPrice;
import com.artigile.warehouse.utils.custom.types.VariantQuantity;

import java.math.BigDecimal;

/**
 * @author Valery Barysok, 19.12.2009
 */
public class ComparatorFactory {

    private ComparatorFactory() {
    }

    public static FilterComparator createComparator(Class clazz) {
        if (clazz == Byte.class || clazz == byte.class) {
            return new ByteComparator();
        } else if (clazz == Short.class || clazz == short.class) {
            return new ShortComparator();
        } else if (clazz == Integer.class || clazz == int.class) {
            return new IntegerComparator();
        } else if (clazz == Long.class || clazz == long.class) {
            return new LongComparator();
        } else if (clazz == Float.class || clazz == float.class) {
            return new FloatComparator();
        } else if (clazz == Double.class || clazz == double.class) {
            return new DoubleComparator();
        } else if (clazz == BigDecimal.class) {
            return new BigDecimalComparator();
        } else if (clazz == VariantQuantity.class) {
            return new VariantQuantityComparator();
        } else if (clazz == VariantPrice.class) {
            return new VariantPriceComparator();
        }

        return new StringComparator();
    }
}
