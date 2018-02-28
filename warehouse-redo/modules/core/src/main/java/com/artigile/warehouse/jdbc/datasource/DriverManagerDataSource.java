package com.artigile.warehouse.jdbc.datasource;

import com.artigile.warehouse.utils.authentification.MySqlAuthenticator;

import java.util.Properties;

/**
 * @author Valery Barysok, 2013-03-24
 */
public class DriverManagerDataSource extends org.springframework.jdbc.datasource.DriverManagerDataSource {

    public DriverManagerDataSource() {
        Properties connectionProperties = MySqlAuthenticator.getConnectionProperties();
        setUsername(connectionProperties.getProperty("user"));
        setPassword(connectionProperties.getProperty("password"));
        setUrl(connectionProperties.getProperty("database.url"));
        setDriverClassName(connectionProperties.getProperty("database.driver"));
        setConnectionProperties(connectionProperties);
    }
}
