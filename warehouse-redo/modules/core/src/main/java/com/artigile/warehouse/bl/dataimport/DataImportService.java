/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.dataimport;

import com.artigile.warehouse.adapter.spi.DataAdapterFactory;
import com.artigile.warehouse.adapter.spi.DataImportContext;
import com.artigile.warehouse.adapter.spi.DataSaver;
import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.common.exceptions.ItemNotExistsException;
import com.artigile.warehouse.bl.postings.PostingItemsDataSaver;
import com.artigile.warehouse.bl.util.async.AsynchronousTaskExecutor;
import com.artigile.warehouse.bl.util.files.StoredFileService;
import com.artigile.warehouse.dao.DataImportDAO;
import com.artigile.warehouse.domain.dataimport.DataImport;
import com.artigile.warehouse.domain.dataimport.ImportStatus;
import com.artigile.warehouse.domain.dataimport.StoredFile;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.openide.util.Lookup;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Service for working with data import (common logic used by all concrete data imports).
 *
 * @author Aliaksandr.Chyrtsik, 04.11.11
 */
@Transactional(rollbackFor = BusinessException.class)
public class DataImportService {
    private DataImportDAO dataImportDAO;

    private ImportFinishListenerWrapper finishListenerWrapper = new ImportFinishListenerWrapper();

    public DataImportService(){
        addDataImportListener(finishListenerWrapper);
    }

    /**
     * Initialize created data import with common field values.
     * @param dataImport (in|out) import to be initialized.
     */
    public void initializeImport(DataImport dataImport) {
        dataImport.setImportStatus(ImportStatus.NOT_COMPLETED);
        dataImport.setUser(SpringServiceContext.getInstance().getUserService().getUserById(WareHouse.getUserSession().getUser().getId())); //TODO: remove reference to the presentation tier.
    }

    /**
     * Perform new data import in background. All import data is created and saved automatically.
     *
     * @param adapterUid uid of adapter to be used.
     * @param adapterConfiguration adapter configuration.
     * @param dataSaver saver for imported data storing.
     * @param backgroundTaskName name of a tack to be shown in progress tracker.
     * @param importFinishListener listener to be called when import will be finished.
     */
    public void performDataImportInBackground(String adapterUid, String adapterConfiguration, PostingItemsDataSaver dataSaver,
                                              String backgroundTaskName, DataImportFinishListener importFinishListener) {
        //1. Create new instance of data import.
        DataImport dataImport = new DataImport();
        initializeImport(dataImport);
        dataImport.setAdapterUid(adapterUid);
        dataImport.setAdapterConf(adapterConfiguration);
        dataImportDAO.save(dataImport);
        dataImportDAO.flush();

        if (importFinishListener != null){
            //Register listener of import finish event.
            finishListenerWrapper.addListener(dataImport.getId(), importFinishListener);
        }

        //2. Launch data import.
        performDataImportInBackground(dataImport.getId(), dataSaver, backgroundTaskName);
    }

    /**
     * Perform data import as background task.
     *
     * @param dataImportId id of import to be performed (import data are expected to be already saved).
     * @param dataSaver saver to be used to store imported data.
     * @param backgroundTaskName name of a tack to be shown in progress tracker.
     */
    public void performDataImportInBackground(long dataImportId, DataSaver dataSaver, String backgroundTaskName) {
        performDataImportInBackground(dataImportId, null, dataSaver, backgroundTaskName);
    }

    /**
     * Perform data import as background task.
     *
     * @param dataImportId id of import to be performed (import data are expected to be already saved).
     * @param oldDataImportId id of previously performed data import to use it's data is input for new import.
     * @param dataSaver saver to be used to store imported data.
     * @param backgroundTaskName name of a tack to be shown in progress tracker.
     */
    public void performDataImportInBackground(long dataImportId, Long oldDataImportId, DataSaver dataSaver, String backgroundTaskName) {
        DataImportTask dataImportTask = new DataImportTask(dataImportDAO.get(dataImportId), dataSaver, oldDataImportId);
        AsynchronousTaskExecutor asynchronousTaskExecutor = SpringServiceContext.getInstance().getAsynchronousTaskExecutor();
        asynchronousTaskExecutor.executeTask(dataImportTask, backgroundTaskName);
    }

    /**
     * Updates configuration of the given import.
     *
     * @param dataImportId import id to update configuration.
     * @param configurationString new configuration string.
     * @throws BusinessException if cannot load data import.
     */
    public void updateImportConfiguration(long dataImportId, String configurationString) throws BusinessException{
        DataImport dataImport = dataImportDAO.get(dataImportId);
        if (dataImport == null){
            throw new ItemNotExistsException(I18nSupport.message("data.import.error.import.not.exists", dataImportId));
        }
        dataImport.setAdapterConf(configurationString);
        dataImportDAO.update(dataImport);
    }

    /**
     * Creates new stored file and attaches it to the data import.
     * @param dataImportId import id to attach the file.
     * @param fileName tile name (or stream name etc.).
     * @param inputStream stream with file data.
     * @return data of stored file that was created during storing operation.
     * @throws BusinessException if cannot load import or database error occurs.
     */
    public StoredFile attachFileToImport(long dataImportId, String fileName, InputStream inputStream) throws BusinessException {
        DataImport dataImport = dataImportDAO.get(dataImportId);
        if (dataImport == null){
            throw new ItemNotExistsException(I18nSupport.message("data.import.error.import.not.exists", dataImportId));
        }

        //1. Creating new stored file.
        StoredFileService storedFileService = SpringServiceContext.getInstance().getStoredFileService();
        StoredFile storedFile = storedFileService.storeFileFromStream(fileName, inputStream);

        //2. Attaching stored file to the price import.
        dataImport.getStoredFiles().add(storedFile);
        dataImportDAO.update(dataImport);

        return storedFile;
    }

    /**
     * Loads file (stream) attached to the import.
     * @param dataImportId identifier of the import which attachments to check.
     * @param fileId identifier of stored file to be loaded.
     * @return input stream to write the stored file data.
     * @throws BusinessException if file is not attached to import or database error occurs.
     */
    public InputStream loadAttachedFile(long dataImportId, long fileId) throws BusinessException {
        //1. Checking that import with given id has the file requested.
        DataImport dataImport = dataImportDAO.get(dataImportId);
        if (dataImport == null){
            throw new ItemNotExistsException(I18nSupport.message("data.import.error.import.not.exists", dataImportId));
        }

        StoredFile attachedFile = null;
        for (StoredFile file : dataImport.getStoredFiles()){
            if (file.getId() == fileId){
                attachedFile = file;
                break;
            }
        }
        if (attachedFile == null){
            throw new ItemNotExistsException(I18nSupport.message("data.import.error.attached.file.not.exists", dataImportId, fileId));
        }

        //2. Loading file data stream.
        StoredFileService storedFileService = SpringServiceContext.getInstance().getStoredFileService();
        return storedFileService.getStoredFileDataStream(fileId);
    }

    /**
     * Mark import as completed. Also calls of import listeners are performed here.
     *
     *
     * @param dataImportId id of import of to be marked as completed.
     * @param importContext context of the import.
     * @throws BusinessException when import completion cannot be performed.
     */
    public void completeImport(long dataImportId, DataImportContext importContext) throws BusinessException {
        //1. Call before complete listeners (to make changes required to complete this concrete import).
        fireBeforeCompleteImport(dataImportId, importContext);

        //2. Mark import as completed.
        DataImport dataImport = dataImportDAO.get(dataImportId);
        dataImport.setImportStatus(ImportStatus.COMPLETED);
        dataImportDAO.update(dataImport);

        //3. Call after complete listeners (to process results of completed import).
        fireAfterCompleteImport(dataImportId, importContext);
    }

    //============================= Import data adapter helpers ==================================

    @SuppressWarnings("unchecked")
    private Collection<DataAdapterFactory> factories = (Collection<DataAdapterFactory>) Lookup.getDefault().lookupAll(DataAdapterFactory.class);

    /**
     * Get all available import data adapter factories.
     * <strong>Hot adapter reloading IS NOT supported now.</strong>
     * Restart system to be able to use new data adapter.
     * @return list of all factories available.
     */
    public Collection<DataAdapterFactory> getAvailableDataAdapterFactories(){
        return factories;
    }

    /**
     * Returns data adapter factory responsible for creation of adapter with given uid.
     * @param adapterUid uid of data adapter to be looked up.
     * @return factory instance of null if no factory for such data adapter was found.
     */
    public DataAdapterFactory getDataAdapterFactoryByAdapterUid(String adapterUid) {
        for (DataAdapterFactory factory : getAvailableDataAdapterFactories()) {
            if (factory.getDataAdapterInfo().getAdapterUid().equals(adapterUid)) {
                return factory;
            }
        }
       return null;
    }

    /**
     * Get name of data adapter with given UID.
     * @param adapterUid uid of data adapter to be looked up.
     * @return data adapter name of error message if data adapter cannot be found.
     */
    public String getDataAdapterNameByAdapterUid(String adapterUid) {
        DataAdapterFactory factory = getDataAdapterFactoryByAdapterUid(adapterUid);
        if (factory != null){
            return factory.getDataAdapterInfo().getAdapterName();
        }
        else{
            return I18nSupport.message("data.import.error.adapter.not.found", adapterUid);
        }
    }

    //================================ Listeners support =================================

    private List<DataImportListener> listeners = new ArrayList<DataImportListener>();

    public void addDataImportListener(DataImportListener listener){
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    public void removeDataImportListener(DataImportListener listener){
        listeners.remove(listener);
    }

    private void fireBeforeCompleteImport(long dataImportId, DataImportContext importContext) throws BusinessException {
        for (DataImportListener listener : listeners){
            listener.onBeforeCompleteImport(dataImportId, importContext);
        }
    }

    private void fireAfterCompleteImport(long dataImportId, DataImportContext importContext) throws BusinessException {
        for (DataImportListener listener : listeners){
            listener.onAfterCompleteImport(dataImportId, importContext);
        }
    }


    //================================= Spring setters ===============================

    public void setDataImportDAO(DataImportDAO dataImportDAO) {
        this.dataImportDAO = dataImportDAO;
    }
}
