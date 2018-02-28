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

import com.artigile.warehouse.adapter.spi.impl.DomainColumn;

import java.util.List;

/**
 * Factory for creating data adapter components.
 *
 * @author Valery Barysok, 6/7/11
 */

public interface DataAdapterFactory {

    /**
     * Retrieve information about data adapter managed by this factorty.
     * @return information about data adapter.
     */
    public DataAdapterInfo getDataAdapterInfo();

    /**
     * Creates new data adapter.
     * @return data adapter.
     */
    public DataAdapter createDataAdapter();

    /**
     * Creates new UI for managing data adapter.
     * @param domainColumns supported domain columns (these columns should be configured via UI being created).
     * @return data adapter UI.
     */
    public DataAdapterUI createDataAdapterUI(List<DomainColumn> domainColumns);

    /**
     * Creates new data adapter configuration and initializes it from given string.
     * @param configurationString string with configuration parameters.
     * @return configuration object to be used for import.
     */
    public DataAdapterConfiguration createDataAdapterConfiguration(String configurationString);
}
