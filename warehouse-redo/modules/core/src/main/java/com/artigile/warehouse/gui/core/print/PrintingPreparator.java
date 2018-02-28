/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.print;

/**
 * @author Shyrik, 29.01.2009
 */

/**
 * Implementation of this interface may repoform some operations to make given object valid for printing.
 */
public interface PrintingPreparator {
    /**
     * Performs operations for prepairing object for printing.
     * @param objectForPrinting - object to be printed.
     * @return - may be object, given as the parameter objectForPrinting, and may be other, for example,
     * full-data implementation of the object.
     */
    public Object prepareForPrinting(Object objectForPrinting);
}
