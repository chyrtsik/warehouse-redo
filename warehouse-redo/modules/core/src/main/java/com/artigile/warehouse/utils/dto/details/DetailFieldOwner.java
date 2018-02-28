/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.details;

/**
 * Interface of the field's owner. Provides support for calculating and refreshing and so on and
 * separates field from it's concrete owner class, that gives us an ability to use field in defferent
 * ways, but not only as field of the detail model.
 * @author Shyrik, 18.01.2009
 */

public interface DetailFieldOwner {
    /**
     * Called, when field wants to notify it's owner about changing of it's value.
     * @param sender - field, which generates such notification.
     */
    public void onValueChanged(DetailFieldValueTO sender);

    /**
     * Called to check, if the value of the field if unique.
     * @param field - field, which value need to be checked.
     * @param value - checked value.
     * @return
     */
    public boolean isUniqueFieldValue(DetailFieldValueTO field, String value);

    /**
     * Implementation of this method mus provide a way to calculate field value from the given template.
     * @param template - template for the value.
     * @return
     */
    public String calculateTemplate(String template);
}
