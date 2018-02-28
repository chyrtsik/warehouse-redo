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
 * Implementation should provide mechanism for saving of configuration data to a
 * persistent storage (database, file and so on).
 *
 * @author Aliaksandr.Chyrtsik, 14.07.11
 */
public interface AdapterConfigurationSaver {
    /**
     * Should save data of the stream specified and return saved stream id.
     * @param inputStream stream to be saved. This id should be valid for loading the stream back.
     * @return stream id.
     */
    public String saveDataInputStream(InputStream inputStream);

    /**
     * Should update configuration string. Used because configuration string may changed during saving.
     *
     * @param configurationString changed configuration string.
     */
    public void updateConfigurationString(String configurationString);

}
