/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi;

/**
 * Information about data adapter.
 *
 * @author Aliaksandr.Chyrtsik, 04.11.11
 */
public interface DataAdapterInfo {
    /**
     * @return unique identified of data adapter.
     */
    public String getAdapterUid();

    /**
     * @return user-friendly name of data adapter.
     */
    public String getAdapterName();

    /**
     * @return user-friendly description of data adapter.
     */
    public String getAdapterDescription();
}
