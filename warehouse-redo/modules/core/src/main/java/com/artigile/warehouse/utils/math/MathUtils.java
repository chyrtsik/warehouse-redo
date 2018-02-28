/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.math;

/**
 * Mathematical utility methods.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class MathUtils {

    /**
     * Calculates price with discount by the given parameters.
     *
     * @param price Initial price (without discount)
     * @param discount Discount in percent
     * @return Price with discount
     */
    public static double calculateSimpleDiscountPrice(double price, double discount) {
        return price * (1 - discount / 100);
    }

    /**
     * Calculates prices with extra charge by the given parameters.
     *
     * @param price Initial price (without extra charge)
     * @param extraCharge Extra charge in percent
     * @return Price with extra charge
     */
    public static double calculateSimpleExtraChargePrice(double price, double extraCharge) {
        return price * (1 + extraCharge / 100);
    }
}
