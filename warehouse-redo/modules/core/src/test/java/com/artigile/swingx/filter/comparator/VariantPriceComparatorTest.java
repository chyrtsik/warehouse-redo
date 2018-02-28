/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.filter.comparator;

import com.artigile.swingx.filter.comparator.impl.VariantPriceComparator;
import com.artigile.warehouse.utils.custom.types.VariantPrice;
import junit.framework.TestCase;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class VariantPriceComparatorTest extends TestCase {

    private static VariantPriceComparator variantPriceComparator = new VariantPriceComparator();

    public void testValidation() {
        VariantPrice testVariantPrice = new VariantPrice(123.456);
        assertTrue(variantPriceComparator.validate(testVariantPrice));

        testVariantPrice.setValue("234.12");
        assertTrue(variantPriceComparator.validate(testVariantPrice));

        testVariantPrice.setValue(" < 332.32");
        assertTrue(variantPriceComparator.validate(testVariantPrice));

        testVariantPrice.setValue(">= 33");
        assertTrue(variantPriceComparator.validate(testVariantPrice));

        testVariantPrice.setValue("<33s");
        assertFalse(variantPriceComparator.validate(testVariantPrice));

        testVariantPrice.setValue("<33,33");
        assertTrue(variantPriceComparator.validate(testVariantPrice));

        testVariantPrice.setValue(">=<322.33");
        assertFalse(variantPriceComparator.validate(testVariantPrice));

        testVariantPrice.setValue("<33,33");
        assertTrue(variantPriceComparator.validate(testVariantPrice));

        testVariantPrice.setValue("<=33,3s");
        assertFalse(variantPriceComparator.validate(testVariantPrice));

        testVariantPrice.setValue("             >            123,45444");
        assertTrue(variantPriceComparator.validate(testVariantPrice));

        testVariantPrice.setValue(">123/45444");
        assertFalse(variantPriceComparator.validate(testVariantPrice));
    }
}
