/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.util.files;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.dao.StoredFileDAO;
import com.artigile.warehouse.domain.dataimport.StoredFile;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;

/**
 * Service for working with files stored in the database.
 *
 * @author Aliaksandr.Chyrtsik, 14.07.11
 */
@Transactional(rollbackFor = BusinessException.class)
public class StoredFileService {

    private StoredFileDAO storedFileDAO;

    public void setStoredFileDAO(StoredFileDAO storedFileDAO) {
        this.storedFileDAO = storedFileDAO;
    }

    /**
     * Stored new file into database.
     * @param filePath path of the file to be stored in the database.
     * @return entity representing stored file.
     * @throws BusinessException if cannot open file of database error occurs.
     */
    public StoredFile storeFile(String filePath) throws BusinessException {
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(filePath);
            return storeFileFromStream(new File(filePath).getName(), fileInputStream);
        }
        catch (FileNotFoundException e) {
            LoggingFacade.logError(this, e);
            throw new BusinessException(e.getLocalizedMessage(), e);
        }
        catch (HibernateException e){
            LoggingFacade.logError(this, e);
            throw new BusinessException(I18nSupport.message("stored.file.database.saving.error"), e);
        }
        finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    LoggingFacade.logError(this, e);
                }
            }
        }
    }

    /**
     * Stored new stream into database as stored file.
     * @param fileName name of a file or stream data source.
     * @param inputStream stream with data to be saved.
     * @return stored file data.
     * @throws BusinessException if database error occurs.
     */
    public StoredFile storeFileFromStream(String fileName, InputStream inputStream) throws BusinessException {
        try{
            return storedFileDAO.storeFileStream(fileName, inputStream);
        }
        catch (HibernateException e){
            LoggingFacade.logError(this, e);
            throw new BusinessException(I18nSupport.message("stored.file.database.saving.error"), e);
        }
    }

    /**
     * Load stored file by id.
     * @param storedFileId identifier to be looked up for.
     * @return found stored file of null of nothing was found.
     */
    public StoredFile findStoredFile(long storedFileId){
        return storedFileDAO.get(storedFileId);
    }

    /**
     * Returns input stream for loading file data from the database.
     *
     * @param storedFileId id of stored file to be loaded.
     * @return input stream for file loading. <strong>Caller if responsible for clising this stream.</strong>
     * @throws BusinessException if file cannot be found or cannot be loaded.
     */
    public InputStream getStoredFileDataStream(long storedFileId) throws BusinessException{
        try{
            return storedFileDAO.getStoredFileDataStream(storedFileId);
        }
        catch (HibernateException e){
            LoggingFacade.logError(this, e);
            throw new BusinessException(I18nSupport.message("stored.file.database.loading.error"), e);
        }
    }
}
