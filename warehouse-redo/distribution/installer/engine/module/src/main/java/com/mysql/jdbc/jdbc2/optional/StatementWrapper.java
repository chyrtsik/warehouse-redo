/*
 Copyright (C) 2002-2004 MySQL AB

 This program is free software; you can redistribute it and/or modify
 it under the terms of version 2 of the GNU General Public License as
 published by the Free Software Foundation.
 

 There are special exceptions to the terms and conditions of the GPL 
 as it is applied to this software. View the full text of the 
 exception exception in file EXCEPTIONS-CONNECTOR-J in the directory of this 
 software distribution.
   
 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
   
 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
      
 */
package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.SQLError;

import java.sql.*;


/**
 * Wraps statements so that errors can be reported correctly to 
 * ConnectionEventListeners.
 *
 * @author Mark Matthews
 * 
 * @version $Id: StatementWrapper.java,v 1.1.2.6 2004/12/15 21:30:45 mmatthew Exp $
 */
class StatementWrapper extends WrapperBase implements Statement {
    protected Statement wrappedStmt;
    protected ConnectionWrapper wrappedConn;

    protected StatementWrapper(ConnectionWrapper c, MysqlPooledConnection conn, Statement toWrap) {
        this.pooledConnection = conn;
        this.wrappedStmt = toWrap;
        this.wrappedConn = c;
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getConnection()
     */
    public Connection getConnection() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedConn;
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return null; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setCursorName(java.lang.String)
     */
    public void setCursorName(String name) throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                this.wrappedStmt.setCursorName(name);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setEscapeProcessing(boolean)
     */
    public void setEscapeProcessing(boolean enable) throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                this.wrappedStmt.setEscapeProcessing(enable);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setFetchDirection(int)
     */
    public void setFetchDirection(int direction) throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                this.wrappedStmt.setFetchDirection(direction);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getFetchDirection()
     */
    public int getFetchDirection() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.getFetchDirection();
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return ResultSet.FETCH_FORWARD; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setFetchSize(int)
     */
    public void setFetchSize(int rows) throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                this.wrappedStmt.setFetchSize(rows);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getFetchSize()
     */
    public int getFetchSize() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.getFetchSize();
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return 0; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getGeneratedKeys()
     */
    public ResultSet getGeneratedKeys() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.getGeneratedKeys();
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return null; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setMaxFieldSize(int)
     */
    public void setMaxFieldSize(int max) throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                this.wrappedStmt.setMaxFieldSize(max);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getMaxFieldSize()
     */
    public int getMaxFieldSize() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.getMaxFieldSize();
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return 0; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setMaxRows(int)
     */
    public void setMaxRows(int max) throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                this.wrappedStmt.setMaxRows(max);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getMaxRows()
     */
    public int getMaxRows() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.getMaxRows();
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return 0; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getMoreResults()
     */
    public boolean getMoreResults() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.getMoreResults();
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return false;
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getMoreResults(int)
     */
    public boolean getMoreResults(int current) throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.getMoreResults(current);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return false;
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#setQueryTimeout(int)
     */
    public void setQueryTimeout(int seconds) throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                this.wrappedStmt.setQueryTimeout(seconds);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getQueryTimeout()
     */
    public int getQueryTimeout() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.getQueryTimeout();
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return 0;
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getResultSet()
     */
    public ResultSet getResultSet() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
            	ResultSet rs = this.wrappedStmt.getResultSet();
            	((com.mysql.jdbc.ResultSet)rs).setStatement(this);
            	
                return rs;
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getResultSetConcurrency()
     */
    public int getResultSetConcurrency() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.getResultSetConcurrency();
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return 0;
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getResultSetHoldability()
     */
    public int getResultSetHoldability() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.getResultSetHoldability();
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return Statement.CLOSE_CURRENT_RESULT;
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getResultSetType()
     */
    public int getResultSetType() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.getResultSetType();
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return ResultSet.TYPE_FORWARD_ONLY;
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getUpdateCount()
     */
    public int getUpdateCount() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.getUpdateCount();
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return -1;
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#getWarnings()
     */
    public SQLWarning getWarnings() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.getWarnings();
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#addBatch(java.lang.String)
     */
    public void addBatch(String sql) throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                this.wrappedStmt.addBatch(sql);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#cancel()
     */
    public void cancel() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                this.wrappedStmt.cancel();
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#clearBatch()
     */
    public void clearBatch() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                this.wrappedStmt.clearBatch();
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#clearWarnings()
     */
    public void clearWarnings() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                this.wrappedStmt.clearWarnings();
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#close()
     */
    public void close() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                this.wrappedStmt.close();
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        } finally {
            this.wrappedStmt = null;
            this.pooledConnection = null;
        }
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#execute(java.lang.String, int)
     */
    public boolean execute(String sql, int autoGeneratedKeys)
        throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.execute(sql, autoGeneratedKeys);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return false; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#execute(java.lang.String, int[])
     */
    public boolean execute(String sql, int[] columnIndexes)
        throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.execute(sql, columnIndexes);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return false; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#execute(java.lang.String, java.lang.String[])
     */
    public boolean execute(String sql, String[] columnNames)
        throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.execute(sql, columnNames);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return false; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#execute(java.lang.String)
     */
    public boolean execute(String sql) throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.execute(sql);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return false; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#executeBatch()
     */
    public int[] executeBatch() throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.executeBatch();
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return null; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#executeQuery(java.lang.String)
     */
    public ResultSet executeQuery(String sql) throws SQLException {
        try {
            if (this.wrappedStmt != null) {
            	ResultSet rs = this.wrappedStmt.executeQuery(sql);
            	((com.mysql.jdbc.ResultSet)rs).setStatement(this);
                return rs;
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return null; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#executeUpdate(java.lang.String, int)
     */
    public int executeUpdate(String sql, int autoGeneratedKeys)
        throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.executeUpdate(sql, autoGeneratedKeys);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return -1; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#executeUpdate(java.lang.String, int[])
     */
    public int executeUpdate(String sql, int[] columnIndexes)
        throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.executeUpdate(sql, columnIndexes);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return -1; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#executeUpdate(java.lang.String, java.lang.String[])
     */
    public int executeUpdate(String sql, String[] columnNames)
        throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.executeUpdate(sql, columnNames);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return -1; // we actually never get here, but the compiler can't figure 

        // that out
    }

    /* (non-Javadoc)
     * @see java.sql.Statement#executeUpdate(java.lang.String)
     */
    public int executeUpdate(String sql) throws SQLException {
        try {
            if (this.wrappedStmt != null) {
                return this.wrappedStmt.executeUpdate(sql);
            } else {
                throw new SQLException("Statement already closed",
                    SQLError.SQL_STATE_ILLEGAL_ARGUMENT);
            }
        } catch (SQLException sqlEx) {
            checkAndFireConnectionError(sqlEx);
        }

        return -1; // we actually never get here, but the compiler can't figure 

        // that out
    }

    public boolean isClosed() throws SQLException {
        return false;
    }

    public void setPoolable(boolean poolable) throws SQLException {
    }

    public boolean isPoolable() throws SQLException {
        return false;
    }

    public void closeOnCompletion() throws SQLException {
    }

    public boolean isCloseOnCompletion() throws SQLException {
        return false;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
