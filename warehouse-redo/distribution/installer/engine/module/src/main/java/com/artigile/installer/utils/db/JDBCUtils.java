/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.installer.utils.db;

import com.artigile.installer.utils.CommonUtils;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import org.netbeans.installer.utils.LogManager;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Common methods to access a MySQL database and executing queries using JDBC driver.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class JDBCUtils {

    /**
     * MYSQL JavaDatabaseConnectivity driver location.
     */
    private static final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";


    /**
     * Opens connection to the database.
     *
     * @param host MySQL server host
     * @param port MySQL server port
     * @param user MySQL user
     * @param password MySQL password
     * @param db Database name
     * @return New connection instance or null if connection haven't opened.
     */
    public static Connection connect(String host, int port, String user, String password, String db) {
        try {
            Class.forName(MYSQL_JDBC_DRIVER);
            String url = buildConnectionString(host, port, db);
            return (Connection) DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            LogManager.log(e);
        }
        return null;
    }

    /**
     * Build connection URL to the MySQL database.
     *
     * @param host MySQL server host
     * @param port MySQL server port
     * @param db Database name
     * @return URL to connect the MySQL database
     */
    public static String buildConnectionString(String host, int port, String db) {
        return (CommonUtils.isEmptyString(host) || CommonUtils.isEmptyString(db))
                ? null
                : new StringBuilder("jdbc:mysql://").append(host).append(":").append(port).append("/").append(db).toString();
    }

    public static boolean isOpenedConnection(Connection connection) {
        return connection != null && !connection.isClosed();
    }

    /**
     * Closes connection to the MySQL database.
     *
     * @param connection Existing connection to the MySQL database
     */
    public static void disconnect(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LogManager.log(e);
        }
    }

    /**
     * Creates SQL statement, using connection.
     *
     * @param connection Existing connection to the database.
     * @return New instance of SQL statement or null if statement haven't created.
     */
    public static Statement createStatement(Connection connection) {
        try {
            return (Statement) connection.createStatement();
        } catch (SQLException e) {
            LogManager.log(e);
        }
        return null;
    }

    /**
     * Executes SQL query for getting data from the database.
     *
     * @param statement SQL statement
     * @param query SQL query
     * @return Results of executing query.
     */
    public static ResultSet executeQuery(Statement statement, String query) {
        ResultSet resultSet = null;
        try {
            if (statement != null && !statement.isClosed() && !CommonUtils.isEmptyString(query)) {
                resultSet = (ResultSet) statement.executeQuery(query);
            }
        } catch (SQLException e) {
            LogManager.log(e);
        }
        return resultSet;
    }

    /**
     * Executes SQL query for updating database state.
     *
     * @param statement SQL statement
     * @param query SQL query
     * @return True - query have executed successfully, false - query have executed with errors.
     */
    public static boolean executeUpdate(Statement statement, String query) {
        boolean executionStatus = false;
        try {
            if (statement != null && !statement.isClosed() && !CommonUtils.isEmptyString(query)) {
                statement.executeUpdate(query);
                executionStatus = true;
            }
        } catch (SQLException e) {
            LogManager.log(e);
        }
        return executionStatus;
    }

    /**
     * @param resultSet Some result set
     * @return True - result set is not containing data, false - result set is containing data.
     */
    public static boolean isEmptyResultSet(ResultSet resultSet) {
        try {
            return resultSet == null || !resultSet.next();
        } catch (SQLException e) {
            LogManager.log(e);
        }
        return true;
    }
}
