/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi;

import java.io.InputStream;

/**
 * Implementation should provide mechanism for loading of configuration data from a
 * persistent storage (database, file and so on).
 *
 * @author Aliaksandr.Chyrtsik, 14.07.11
 */
public interface AdapterConfigurationLoader {
    /**
     * Used to load stream data from the storage.
     *
     * @param streamId identified of stream to be returned.
     * @return stream with given id. <strong>Caller is responsible for closing the stream.</strong>
     */
    public InputStream loadDataInputStream(String streamId);
}
