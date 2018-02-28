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

import com.artigile.warehouse.adapter.spi.AdapterConfigurationLoader;
import com.artigile.warehouse.adapter.spi.AdapterConfigurationSaver;
import com.artigile.warehouse.adapter.spi.DataAdapterConfiguration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of import configuration for excel file based import.
 *  *
 * @author Aliaksandr.Chyrtsik, 15.07.11
 */
public class ExcelDataAdapterConfiguration implements DataAdapterConfiguration {

    /**
     * Text configuration.
     */
    private String configurationString;

    /**
     * Loader for accessing to file data stored in the persistent storage.
     */
    private AdapterConfigurationLoader configurationLoader;

    public ExcelDataAdapterConfiguration(String configurationString) {
        this.configurationString = configurationString;
    }

    @Override
    public String getConfigurationString() {
        return configurationString;
    }

    @Override
    public InputStream getDataInputStream(String dataStreamId) {
        if (configurationLoader == null){
            //Configuration is transient -- data stored in a disk file.
            String filePath = extractFileStreamId(configurationString);
            try {
                return new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            //Configuration is persistent -- data stored in the persistent srorage.
            return configurationLoader.loadDataInputStream(dataStreamId);
        }
    }

    @Override
    public void save(AdapterConfigurationSaver configurationSaver) {
        //1. Storing file to be imported.
        String fileStreamId = extractFileStreamId(configurationString);
        InputStream inputStream = getDataInputStream(fileStreamId);
        String newStreamId = configurationSaver.saveDataInputStream(inputStream);
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //2. Storing updated configuration (now it will be referencing to a persistent storage).
        String savedConfiguration = replaceFileStreamId(configurationString, newStreamId);
        configurationSaver.updateConfigurationString(savedConfiguration);
    }

    @Override
    public void load(AdapterConfigurationLoader configurationLoader) {
        //Just remember loader object. Wi will use it later.
        this.configurationLoader = configurationLoader;
    }

    //=================================== Helpers ========================================
    private String extractFileStreamId(String configurationString) {
        ExcelDataAdapterConfigurationData parsedConfiguration = ExcelDataAdapterConfigurationData.parse(configurationString);
        return parsedConfiguration.getFileStreamId();
    }

    private String replaceFileStreamId(String configurationString, String newStreamId) {
        ExcelDataAdapterConfigurationData parsedConfiguration = ExcelDataAdapterConfigurationData.parse(configurationString);
        parsedConfiguration.setFileStreamId(newStreamId);
        return parsedConfiguration.toString();
    }
}
