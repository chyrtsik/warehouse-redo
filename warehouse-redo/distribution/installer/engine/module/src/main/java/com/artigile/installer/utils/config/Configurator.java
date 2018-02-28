/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.installer.utils.config;

import com.artigile.installer.utils.CommonUtils;
import com.artigile.installer.utils.FileUtils;

import java.util.Properties;

/**
 * Configures the Warehouse Client and the MySQL Server after installation.
 *
 * @author vadim.zverugo (vadim.zverugo@artigile.by)
 */
public class Configurator {

    /**
     * Required constants
     */
    private static final String WAREHOUSE_CONFIG_TEMPLATE_PATH = "com/artigile/installer/utils/config/resources.xtemplate";
    private static final String WAREHOUSE_CONFIG_SERVER_ADDRESS_REPLACE = "<server_address>";
    private static final String WAREHOUSE_CONFIG_SERVER_PORT_REPLACE = "<server_port>";
    private static final String WAREHOUSE_CONFIG_DATABASE_NAME_REPLACE = "<db_name>";
    private static final String MYSQL_CONFIG_ADDITIONAL_PROPERTIES_PATH = "com/artigile/installer/utils/config/mysql_additional.properties";

    /**
     * Singleton instance of the Configurator
     */
    private static Configurator instance;

    /**
     * Common utils to work with files and directories
     */
    private static FileUtils fileSystemWorker = new FileUtils();

    /**
     * The Warehouse Client installation path
     */
    private String warehouseDestinationPath;

    /**
     * The MySQL Server installation path
     */
    private String mysqlDestinationPath;


    private Configurator() {
        // Silence is gold
    }

    public static synchronized Configurator getInstance() {
        if (instance == null) {
            instance = new Configurator();
        }
        return instance;
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * Creates resources.xml with the given attributes in the Warehouse Client installation directory.
     * Invokes after database creation.
     *
     * @see this#WAREHOUSE_CONFIG_TEMPLATE_PATH - configuration file template.
     * @see com.artigile.installer.wizard.components.panels.DBConfigPanel.DBConfigPanelSwingUI#saveInput()
     *
     * @param dbServerHost MySQL server address.
     * @param dbServerPort MySQL server port.
     * @param dbName Database name.
     */
    public void configWarehouse(String dbServerHost, int dbServerPort, String dbName) {
        if (!CommonUtils.isEmptyString(warehouseDestinationPath)) {
            // Load template of the resources.xml
            String resourcesXML = fileSystemWorker.loadClasspathFileContent(WAREHOUSE_CONFIG_TEMPLATE_PATH);

            if (!CommonUtils.isEmptyString(resourcesXML)) {
                // Apply required parameters
                resourcesXML = resourcesXML.replaceAll(WAREHOUSE_CONFIG_SERVER_ADDRESS_REPLACE, dbServerHost)
                        .replaceAll(WAREHOUSE_CONFIG_SERVER_PORT_REPLACE, String.valueOf(dbServerPort))
                        .replaceAll(WAREHOUSE_CONFIG_DATABASE_NAME_REPLACE, dbName);

                // Make config directory and save resources.xml
                if (fileSystemWorker.makeDirectory(warehouseDestinationPath + "\\config")) {
                    fileSystemWorker.storeFile(warehouseDestinationPath + "\\config\\resources.xml", resourcesXML);
                }
            }
        }
    }

    /**
     * Adds special parameters to the MySQL configuration file (my.ini).
     * Invokes after installing MySQL Server.
     *
     * @see this#MYSQL_CONFIG_ADDITIONAL_PROPERTIES_PATH - additional properties.
     */
    public void configMySQL() {
        if (!CommonUtils.isEmptyString(mysqlDestinationPath)) {
            // Load special parameters
            Properties mysqlAdditionalProperties = CommonUtils.loadPropertiesFromStream(
                    fileSystemWorker.loadClasspathFileStream(MYSQL_CONFIG_ADDITIONAL_PROPERTIES_PATH));
            
            if (!mysqlAdditionalProperties.isEmpty()) {
                String mysqlMyINIFilePath = mysqlDestinationPath + "\\my.ini";
                if (fileSystemWorker.fileExists(mysqlMyINIFilePath)) {
                    // Load my.ini file
                    String myINI = fileSystemWorker.loadFileContent(mysqlMyINIFilePath);
                    StringBuilder mysqlAdditionalPropertiesBuilder = new StringBuilder();

                    // Add special parameters to the end of the my.ini file
                    for (Object key : mysqlAdditionalProperties.keySet()) {
                        String mysqlAdditionalPropertyKey = (String) key;
                        if (!myINI.contains(mysqlAdditionalPropertyKey)) {
                            String mysqlAdditionalPropertyValue = mysqlAdditionalProperties.getProperty(mysqlAdditionalPropertyKey);
                            mysqlAdditionalPropertiesBuilder.append("\n").append(mysqlAdditionalPropertyKey)
                                    .append("=").append(mysqlAdditionalPropertyValue);
                        }
                    }
                    fileSystemWorker.appendToFileContent(mysqlMyINIFilePath, mysqlAdditionalPropertiesBuilder.toString());
                }
            }
        }
    }


    /* Setters
    ------------------------------------------------------------------------------------------------------------------*/
    public void setWarehouseDestinationPath(String warehouseDestinationPath) {
        this.warehouseDestinationPath = warehouseDestinationPath;
    }

    public void setMysqlDestinationPath(String mysqlDestinationPath) {
        this.mysqlDestinationPath = mysqlDestinationPath;
    }
}
