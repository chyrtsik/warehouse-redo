/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.authentification;

import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.utils.xml.ServersInitializer;
import com.artigile.warehouse.utils.configuration.impl.Server;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Properties;

/**
 * This class is used to store connection with database not using Hibernate connection.
 * This class also updates mysql user's data for synchronization between warehouse.user and mysql.user tables.
 *
 * @author IoaN
 */

final public class MySqlAuthenticator {
    private MySqlAuthenticator(){
    }

    /**
     * MySql database username
     */
    private static String dbLogin = null;

    /**
     * MySql database password
     */
    private static String dbPassword = null;

    /**
     * If true that current user is system administrator.
     */
    private static boolean isSystemAdministrator;

    /**
     * Properties of database connection.
     */
    private static Properties connectionProperties = null;
    
    private static MySQLDatabaseUrl databaseUrl = null;

    /**
     * This suffix is appended to password that is stored as database password.
     */
    private static final String PASSWORD_SUFFIX = new String(new char[]{'h', '3', '6', 'd', 'f'}); 

    public static Properties getConnectionProperties() {
        if (connectionProperties == null) {
            connectionProperties = new Properties();
            try {
                Resource res = new ClassPathResource("config/database.properties");
                connectionProperties.load(res.getInputStream());
            } catch (IOException e) {
                String infoMessage = MessageFormat.format(
                    "The database properties file was not found, loading default values:\n{0}\n{1}",
                    "database.driver: com.mysql.jdbc.Driver",
                    "database.url: jdbc:mysql://localhost:3306/whclient?characterEncoding=utf-8"
                );
                LoggingFacade.logInfo(infoMessage);
                connectionProperties.setProperty("database.driver", "com.mysql.jdbc.Driver");
                connectionProperties.setProperty("database.url", "jdbc:mysql://localhost:3306/whclient?characterEncoding=utf-8");
            }
        }

        return connectionProperties;
    }

    public static MySQLDatabaseUrl getDatabaseUrl() {
        return getDatabaseUrl(false);
    }

    public static MySQLDatabaseUrl getDatabaseUrl(boolean force) {
        if (databaseUrl == null || force) {
            databaseUrl = new MySQLDatabaseUrl(getConnectionProperties().getProperty("database.url"));
        }

        return databaseUrl;
    }

    /**
     * Logs into database using Statement Object. This function called from login window. Before any spring dependency
     * injection were loaded and hibernate connection to database was not stored.
     *
     * @param login    user login that must also be a Mysql user login
     * @param password user password that must also be a Mysql user password.
     * @return true if connection was successfully stored and user login and user password were correct.
     */
    public static boolean loginIntoDb(String login, String password) {
        Connection conn = null;
        dbLogin = login;
        dbPassword = encodePassword(password);
        try {
            //1. Database authentication (if failed then exception would be thrown).
            //TODO: refactor this.
            conn = getConnection(dbLogin, dbPassword);

            //2. Application level authentication.
            if (!checkUserPassword(conn, login, password)) {
                LoggingFacade.logInfo("Cannot connect to database. Invalid user or password.");
                return false;
            }
        } catch (Exception e) {
            LoggingFacade.logError("Cannot connect to database.", e);
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LoggingFacade.logError("mysql connection was not correctly closed. Please check MySqlAdministrator.class loginIntoDb function.", e);
                }
            }
        }
        LoggingFacade.logInfo("Login to mysql database was successfully verified. Loading hibernate database connection.");
        return true;
    }

    public static String encodePassword(String password) {
        return StringUtils.getStringMD5(password + PASSWORD_SUFFIX);
    }

    private static boolean checkUserPassword(Connection conn, String login, String password) {
        String sqlQuery = "select predefined from user where login=? and password=?";
        String hashedPassword = encodePassword(password);

        boolean passwordAccepted = false;
        try {
            PreparedStatement statement = conn.prepareStatement(sqlQuery);

            statement.setString(1, login);
            statement.setString(2, hashedPassword);

            ResultSet result = statement.executeQuery();
            passwordAccepted = result.next();

            if (passwordAccepted){
                isSystemAdministrator = result.getBoolean(1);
            }
        } catch (SQLException e) {
            LoggingFacade.logError(e);
        }

        return passwordAccepted;
    }


    /**
     * Returns true if login creation was successful
     *
     * @param login user name to update password.
     * @param password new password ot be set (not encoded).
     * @return true if password was successfully updated.
     */
    public static boolean updatePassword(String login, String password) {
        boolean success = executeSql(getUpdatePasswordSql(login, encodePassword(password)), getFlushPrivilegesSql());
        if ( success && login.equals(dbLogin) ){
            dbPassword = encodePassword(password);
        }
        return success;
    }

    /**
     * creates new user.
     *
     * @param login    login of new user
     * @param password password
     * @return true if user created successfully.
     */
    public static boolean createNewUser(String login, String password) {
        return executeSql(getCreateUserSql(login, encodePassword(password)),
                getGrantWarehousePrivilegesSql(login),
                getGrantMySqlPrivilegesSql(login),
                getGrantReloadPrivilegeSql(login)
        );
    }

    /**
     * returns true if user login was successfully updated.
     *
     * @param prevLogin previous user login
     * @param newLogin    new user login
     * @return true if user login was successfully updated.
     */
    public static boolean updateUserLogin(String prevLogin, String newLogin) {
        boolean success = executeSql(updateUserSql(prevLogin, newLogin),
                getFlushPrivilegesSql());
        if (prevLogin.equals(dbLogin)) {
            dbLogin = newLogin;
        }
        return success;
    }

    /**
     * removes user from mysql database
     *
     * @param login user login that have to be removed
     * @return true if user was successfully removed, otherwise returns false.
     */
    public static boolean removeUser(String login) {
        return executeSql(getDropUserSql(login));
    }

    /**
     * returns warehouse database connection
     *
     * @return warehouse database connection
     * @throws ClassNotFoundException throws in case if not all required libs were load in class loader.
     * @throws SQLException           probably throws in case if sql was not properly built.
     */
    public static Connection getConnection(String login, String password) throws ClassNotFoundException, SQLException {
        Properties props = getConnectionProperties();
        String databaseClassName = props.getProperty("database.driver");
        Class.forName(databaseClassName);
        String dbUrl = props.getProperty("database.url");
        props.setProperty("user", login);
        props.setProperty("password", password);

        return DriverManager.getConnection(dbUrl, props);
    }

    /**
     * returns warehouse database connection
     *
     * @return warehouse database connection
     * @throws ClassNotFoundException throws in case if not all required libs were load in class loader.
     * @throws SQLException           probably throws in case if sql was not properly built.
     */
    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Properties props = getConnectionProperties();
        String databaseClassName = props.getProperty("database.driver");
        Class.forName(databaseClassName);
        String dbUrl = props.getProperty("database.url");

        return DriverManager.getConnection(dbUrl, props);
    }

    /**
     * Executes all the sqlScripts.
     *
     * @param sqlScripts scripts list
     * @return true if scripts where successfully executed. Otherwise returns false.
     */
    private static boolean executeSql(String... sqlScripts) {
        try {
            Connection connection = getConnection();
            for (String sqlScript : sqlScripts) {
                connection.createStatement().execute(sqlScript);
            }
            connection.close();
            return true;
        } catch (ClassNotFoundException e) {
            LoggingFacade.logError(e);
            return false;
        } catch (SQLException e) {
            LoggingFacade.logError(e);
            return false;
        }
    }

    /**
     * returns sql for granting privileges to user with login <b>login</b> to warehouse table.
     *
     * @param login user login to what the privileges will be granted.
     * @return sql for granting privileges to user with login <b>login</b> to warehouse table.
     */
    private static String getGrantWarehousePrivilegesSql(String login) {
        return getGrantPrivilegesSql(getDatabaseUrl().getDatabase(), login);
    }

    /**
     * returns sql for granting privileges to user with login <b>login</b> to mysql table.
     *
     * @param login user login to what the privileges will be granted.
     * @return sql for granting privileges to user with login <b>login</b> to mysql table.
     */
    private static String getGrantMySqlPrivilegesSql(String login) {
        return getGrantPrivilegesSql("mysql", login);
    }

    /**
     * Returns grant reload privilege for given user. Without this privilege user cannot change his password.
     * @param login
     * @return
     */
    private static String getGrantReloadPrivilegeSql(String login){
        return MessageFormat.format("grant reload on *.* to ''{0}'' with grant option", login);
    }

    /**
     * generic grant-to-table privileges script creator. Creates script for granting privileges to user with login
     * <b>login</b>  on table with name <b>database</b>.
     *
     * @param database the name of table on which grant should be added.
     * @param login     the user login to what the grant will be added.
     * @return script for granting privileges to user with login <b>login</b>  on table with name <b>database</b>.
     */
    private static String getGrantPrivilegesSql(String database, String login) {
        return MessageFormat.format("grant all privileges on {0}.* to ''{1}'' with grant option", database, login);
    }


    /**
     * Returns create mysql user script.
     *
     * @param login    new user login
     * @param password new user password
     * @return create mysql user script.
     */
    private static String getCreateUserSql(String login, String password) {
        return MessageFormat.format("create user ''{0}'' identified by ''{1}''", login, password);
    }

    /**
     * Returns script that updates current logged in user password.
     *
     * @param password new password for current logged in user.
     * @return script that updates current logged in user password.
     */
    private static String getUpdatePasswordSql(String login, String password) {
        return MessageFormat.format("set password for ''{0}'' = password (''{1}'')", login, password);
    }

    /**
     * Returns script that updates user and user login.
     *
     * @param login       login of user that have to be updated.
     * @param newLogin    new user login
     * @return script that updates user and user login.
     */
    private static String updateUserSql(String login, String newLogin) {
        return MessageFormat.format("rename user ''{0}'' to ''{1}''", login, newLogin);
    }

    /**
     * Returns script for dropping user with login <b>userLogin</b>
     *
     * @param userLogin user login that should be dropped.
     * @return script for dropping user with login <b>userLogin</b>
     */
    private static String getDropUserSql(String userLogin) {
        return MessageFormat.format("drop user ''{0}''", userLogin);
    }

    /**
     * Returns flush privileges script. Have to be called after mysql user name or password was changed.
     *
     * @return flush privileges script.
     */
    private static String getFlushPrivilegesSql() {
        return "flush privileges";
    }

    public static boolean isSystemAdministrator() {
        return isSystemAdministrator;
    }

    public static boolean updateConnectionProperties(String serverName) {
        Server databaseServer = ServersInitializer.getInstance().getServerByName(serverName);
        if (databaseServer != null) {
            Properties connectionProperties = MySqlAuthenticator.getConnectionProperties();
            for (Map.Entry<String, String> kv : databaseServer.getProperties().entrySet()) {
                connectionProperties.setProperty(kv.getKey(), kv.getValue());
            }
            MySqlAuthenticator.getDatabaseUrl(true);
            return true;
        }
        else {
            LoggingFacade.logError(MessageFormat.format("Cannot load server %s. Probably these is a mistake in config.", serverName));
            return false;
        }
    }
}