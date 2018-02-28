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
 * Data import configuration (all setting and source data used for import).
 *
 * @author Aliaksandr.Chyrtsik, 14.07.11
 */
public interface DataAdapterConfiguration {
    /**
     * Data adapter is supposed to have it's own string configuration (in any format). <br/>
     * This method returns this custom configuration of data adapter.
     * @return configuration of data import adapter.
     */
    public String getConfigurationString();

    /**
     * Data import adapter mey be using an unlimited numbers of data stream.
     * This method should be used to get access to the streams. <br/>
     * Import adapter implementation and import configuration implementation should take care of how to
     * encode data stream identifiers. <br/>
     * For example, for imports which use files data streams will be files to be imported,
     * for http imports data streams will be content downloaded from a web-server.
     *
     * @param dataStreamId identifier or data stream to be opened and returned. Supposed to be valid data stream identifier.
     * @return data stream requested. <strong>Caller it responsible for closing this stream.</strong>
     */
    public InputStream getDataInputStream(String dataStreamId);

    /**
     * Saves configuration data to a persistent storage.
     *
     * @param configurationSaver saver to be used (persistent storage adapter).
     */
    void save(AdapterConfigurationSaver configurationSaver);

    /**
     * Loads configuration data from a persistent storage.
     *
     * @param configurationLoader loader to be used (persistent storage adapter).
     */
    void load(AdapterConfigurationLoader configurationLoader);
}
