/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.GenericEntityDAO;
import com.artigile.warehouse.domain.dataimport.StoredFile;
import org.hibernate.jdbc.Work;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Aliaksandr.Chyrtsik, 14.07.11
 */
public class StoredFileDAOImpl extends GenericEntityDAO<StoredFile> implements StoredFileDAO {
    @Override
    public StoredFile storeFileStream(String fileName, InputStream inputStream) {
        //1. Creating new database record about stored file.
        StoredFile storedFile = new StoredFile();
        storedFile.setName(fileName);
        save(storedFile);

        //2. Writing the file itself.
        getSession().doWork(new StoreFileWork(storedFile.getId(), inputStream));

        //3. Updating entity to reflect the actual value of timestamp.
        getSession().refresh(storedFile);

        return storedFile;
    }

    private class StoreFileWork implements Work {
        private long storedFileId;
        private InputStream inputStream;

        StoreFileWork(long storedFileId, InputStream inputStream) {
            this.storedFileId = storedFileId;
            this.inputStream = inputStream;
        }

        @Override
        public void execute(Connection connection) throws SQLException {
            String sqlStoreFile = "update StoredFile a set a.content=? where a.id=?";
            PreparedStatement ps = connection.prepareStatement(sqlStoreFile);
            ps.setBinaryStream(1, inputStream);
            ps.setLong(2, storedFileId);
            ps.execute();
            ps.close();
        }
    }

    @Override
    public InputStream getStoredFileDataStream(long storedFileId) {
        LoadFileWork loadWork = new LoadFileWork(storedFileId);
        getSession().doWork(loadWork);
        return loadWork.getLoadedInputStream();
    }

    private class LoadFileWork implements Work {
        private long storedFileId;

        private InputStream loadedInputStream;

        public LoadFileWork(long storedFileId) {
            this.storedFileId =storedFileId;
        }

        @Override
        public void execute(Connection connection) throws SQLException {
            String sqlLoadFile = "select a.content from StoredFile a where a.id=?";
            PreparedStatement ps = connection.prepareStatement(sqlLoadFile);
            ps.setLong(1, storedFileId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            loadedInputStream = rs.getBinaryStream(1);
            ps.close();
        }

        public InputStream getLoadedInputStream() {
            return loadedInputStream;
        }

        public void setLoadedInputStream(InputStream loadedInputStream) {
            this.loadedInputStream = loadedInputStream;
        }
    }
}
