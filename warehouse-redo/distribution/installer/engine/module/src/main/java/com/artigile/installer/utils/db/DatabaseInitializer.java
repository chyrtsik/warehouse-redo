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
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import org.netbeans.installer.utils.LogManager;

import java.sql.SQLException;
import java.text.MessageFormat;

/**
 * Contains methods to initialize system database.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public abstract class DatabaseInitializer {

    /**
     * Special suffix that adds to the each password before encryption.
     * Attention! It's must be equals com.artigile.warehouse.utils.authentification.MySqlAuthenticator.PASSWORD_SUFFIX
     */
    private static final String PASSWORD_SUFFIX = "h36df";

    private static String selectUserQuery = "SELECT User FROM mysql.user WHERE User = ''{0}''";

    private static String createUserAndGrantPrivilegesQuery = "GRANT ALL PRIVILEGES ON *.* TO ''{0}''@''%'' IDENTIFIED BY ''{1}'' WITH GRANT OPTION";

    private static String encryptPasswordQuery = "SELECT MD5(''{0}'')";

    private static String showDatabasesQuery = "SHOW DATABASES";
    
    private static String createDatabaseQuery =  "CREATE DATABASE {0} DEFAULT CHARSET utf8";
    
    private static String createUserTableQuery = "CREATE TABLE {0}.User (\n" +
            "  id bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  email varchar(255) DEFAULT NULL,\n" +
            "  firstName varchar(255) DEFAULT NULL,\n" +
            "  lastName varchar(255) DEFAULT NULL,\n" +
            "  login varchar(16) NOT NULL,\n" +
            "  password varchar(255) DEFAULT NULL,\n" +
            "  predefined boolean NOT NULL DEFAULT FALSE,\n" +
            "  PRIMARY KEY (id),\n" +
            "  UNIQUE KEY login (login)\n" +
            ")";
    
    private static String insertUserQuery = "INSERT INTO {0}.User (login, password, predefined) VALUES (''{1}'', ''{2}'', 1)";
    

    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    /**
     * Creates new MySQL user.
     *
     * @see com.artigile.installer.wizard.components.panels.DBConfigPanel.DBConfigPanelSwingUI#saveInput()
     *
     * @param statement Active SQL statement.
     * @param username User name.
     * @param password User password.
     * @return True - user has been successfully created,
     *         false - user hasn't been created.
     */
    public static boolean createUser(Statement statement, String username, String password) {
        boolean userCreationResult = false;
        if (statement != null && !CommonUtils.isEmptyString(username) && !CommonUtils.isEmptyString(password)) {
            // Encrypt password using MD5
            password = encryptPassword(statement, password);

            // Prepare SQL queries
            String createUserAndGrantPrivilegesQ = MessageFormat.format(createUserAndGrantPrivilegesQuery, username, password);

            // Create MySQL user and grant privileges
            userCreationResult = JDBCUtils.executeUpdate(statement, createUserAndGrantPrivilegesQ);
        }
        return userCreationResult;
    }

    /**
     * Creates database with a table that contains users. Inserts in that table default user - administrator.
     *
     * @see com.artigile.installer.wizard.components.panels.DBConfigPanel.DBConfigPanelSwingUI#saveInput()
     *
     * @param statement Active SQL statement.
     * @param dbName Database name.
     * @param username User name.
     * @param password User password.
     * @return True - all queries have been executed successfully,
     *         false - one of the queries hasn't been executed.
     */
    public static boolean createDatabase(Statement statement, String dbName, String username, String password) {
        boolean databaseCreationResult = false;
        if (statement != null && !CommonUtils.isEmptyString(dbName) && !CommonUtils.isEmptyString(username)
                && !CommonUtils.isEmptyString(password)) {
            // Encrypt password using MD5
            password = encryptPassword(statement, password);

            // Prepare SQL queries
            String createDatabaseQ = MessageFormat.format(createDatabaseQuery, dbName);
            String createUserTableQ = MessageFormat.format(createUserTableQuery, dbName);
            String insertUserQ = MessageFormat.format(insertUserQuery, dbName, username, password);

            // Create database and table with a default user
            databaseCreationResult = JDBCUtils.executeUpdate(statement, createDatabaseQ)
                    && JDBCUtils.executeUpdate(statement, createUserTableQ)
                    && JDBCUtils.executeUpdate(statement, insertUserQ);
        }
        return databaseCreationResult;
    }

    /**
     * Encrypts the given password using MySQL function 'MD5()'.
     * Alternative: use java.security.MessageDigest
     *
     * @param statement Active SQL statement.
     * @param password Initial password for encryption.
     * @return Encrypted password.
     */
    private static String encryptPassword(Statement statement, String password) {
        String encryptPasswordQ = MessageFormat.format(encryptPasswordQuery, password + PASSWORD_SUFFIX);
        ResultSet encryptionResult = JDBCUtils.executeQuery(statement, encryptPasswordQ);
        try {
            encryptionResult.next();
            return encryptionResult.getString(1);
        } catch (Exception e) {
            LogManager.log(e);
            return null;
        }
    }

    /**
     * Checks duplication of the given user among MySQL users.
     *
     * @param statement Active SQL statement
     * @param username User name for checking
     * @return True - user with the given name has already existed,
     *         false - user with the given name hasn't been found.
     */
    public static boolean userDuplicated(Statement statement, String username) {
        String selectUserQ = MessageFormat.format(selectUserQuery, username);
        ResultSet mysqlUsers = JDBCUtils.executeQuery(statement, selectUserQ);
        return !JDBCUtils.isEmptyResultSet(mysqlUsers);
    }

    /**
     * Checks duplication of the given database among other MySQL databases.
     *
     * @param statement Active SQL statement
     * @param dbName Database name for checking
     * @return True - database with the given name has already existed,
     *         false - database with the given name hasn't been found.
     */
    public static boolean databaseDuplicated(Statement statement, String dbName) {
        ResultSet mysqlDatabases = JDBCUtils.executeQuery(statement, showDatabasesQuery);
        try {
            while (mysqlDatabases.next()) {
                if (dbName.equals(mysqlDatabases.getString(1))) {
                    return true;
                }
            }
        } catch (SQLException e) {
            LogManager.log(e);
            return true;
        }
        return false;
    }
}
