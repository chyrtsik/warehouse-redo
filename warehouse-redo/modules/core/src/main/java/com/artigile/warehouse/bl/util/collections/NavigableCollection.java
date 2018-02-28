/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.util.collections;

import java.util.Collection;

/**
 * Interface for navigable collections.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public interface NavigableCollection<T> {

    /**
     * Goes to the previous part of elements.
     */
    void toPrev();

    boolean isBegin();

    /**
     * Goes to the next part of elements.
     */
    void toNext();

    boolean isEnd();

    /**
     * @return Current part of elements
     */
    Collection<T> getCurrentElements();

    /**
     * @return All elements
     */
    Collection<T> getAllElements();
}
