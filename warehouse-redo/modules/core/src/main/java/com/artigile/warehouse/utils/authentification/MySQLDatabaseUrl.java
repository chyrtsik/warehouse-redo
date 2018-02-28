package com.artigile.warehouse.utils.authentification;

/**
 *
 * @author Valery.Barysok
 */
public class MySQLDatabaseUrl {
    private String databaseUrl;
    private String host;
    private String port;
    private String database;
    
    public MySQLDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
        split();
    }

    public String getDatabase() {
        return database;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    private void split() {
        int endOfPrefix = "jdbc:mysql://".length();
        int endOfHost = databaseUrl.indexOf(':', endOfPrefix);
        host = databaseUrl.substring(endOfPrefix, endOfHost);
        int endOfPort = databaseUrl.indexOf('/', endOfHost);
        port = databaseUrl.substring(endOfHost+1, endOfPort);
        int endOfDatabase = databaseUrl.indexOf('?', endOfPort);
        database = endOfDatabase == -1 ? databaseUrl.substring(endOfPort+1) : 
                databaseUrl.substring(endOfPort+1, endOfDatabase);
    }
}
