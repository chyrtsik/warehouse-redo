/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.configuration;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Interface of parser of dexcel data adapter configuration.
 *
 * @author Aliaksandr.Chyrtsik, 18.08.11
 */
public interface ExcelDataAdapterConfigurationParser {
    /**
     * @return identifier of file stream from the configuration data.
     * @throws AdapterConfigurationFormatException is configuration string has wrong format.
     */
    public String getFileStreamId() throws AdapterConfigurationFormatException;

    /**
     * @return configurations for all sheet columns stored in configuration.
     * @throws AdapterConfigurationFormatException is configuration string has wrong format.
     */
    public LinkedHashMap<String, List<ColumnRelationship>> getSheetsColumnsConfig() throws AdapterConfigurationFormatException;
}
