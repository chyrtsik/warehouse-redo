/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.print;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.util.files.StoredFileService;
import com.artigile.warehouse.dao.PrintTemplateDAO;
import com.artigile.warehouse.dao.PrintTemplateImagesDAO;
import com.artigile.warehouse.dao.PrintTemplateInstanceDAO;
import com.artigile.warehouse.domain.printing.*;
import com.artigile.warehouse.gui.core.print.PrintTemplateFieldsProvider;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;
import com.artigile.warehouse.utils.dto.template.PrintTemplateTO;
import com.artigile.warehouse.utils.dto.template.TemplateFieldMappingTO;
import com.artigile.warehouse.utils.transofmers.PrintTemplateTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

/**
 * @author Shyrik, 03.12.2008
 */
@Transactional(rollbackFor = BusinessException.class)
public class PrintTemplateService {
    private PrintTemplateDAO printTemplateDAO;
    private PrintTemplateInstanceDAO printTemplateInstanceDAO;
    private PrintTemplateImagesDAO printTemplateImagesDAO;

    private StoredFileService storedFileService;
    private PrintTemplatePluginFactory printTemplatePluginFactory;

    public PrintTemplateService() {
    }

    //============= Manipulations with printing ===========

    public List<PrintTemplateTO> getAllTemplates(){
        return PrintTemplateTransformer.transformTemplateList(printTemplateDAO.getAll());
    }

    /**
     * @return - List of all printing templates.
     */
    public List<PrintTemplateInstanceTO> getAllTemplateInstances() {
        return PrintTemplateTransformer.transformTemplateInstanceList(printTemplateInstanceDAO.getAll());
    }

    /**
     * Check if specified name of template instance will be unique.
     * @param templateInstanceId template instance id which name will be checked.
     * @param name name to be checked.
     * @return true if name is unique.
     */
    public boolean isUniqueTemplateInstanceName(long templateInstanceId, String name) {
        PrintTemplateInstance sameTemplateInstance = printTemplateInstanceDAO.findByName(name);
        return sameTemplateInstance == null || sameTemplateInstance.getId() == templateInstanceId;
    }

    /**
     * Save edited print template instance or create a new one.
     *
     * @param templateInstanceTO template to be saved.
     */
    public void saveTemplateInstance(PrintTemplateInstanceTO templateInstanceTO) throws BusinessException {
        PrintTemplateInstance templateInstance;
        if (templateInstanceTO.getId() == 0){
            //Create new template instance. When
            templateInstance = createTemplateInstance(templateInstanceTO.getNewTemplateFile());
        }
        else{
            //Update template instance.
            templateInstance = getTemplateInstanceById(templateInstanceTO.getId());
            if (templateInstanceTO.getNewTemplateFile() != null){
                if (differentTemplatePlugins(templateInstance.getTemplateFile().getName(), templateInstanceTO.getNewTemplateFile().getName())){
                    //With new file selected we need to change print engine (we are required recreate entity of different type).
                    printTemplateInstanceDAO.remove(templateInstance);
                    printTemplateInstanceDAO.flush();
                    templateInstance = createTemplateInstance(templateInstanceTO.getNewTemplateFile());
                    templateInstance.setId(templateInstanceTO.getId());
                }
            }
        }

        templateInstance.setPrintTemplate(printTemplateDAO.get(templateInstanceTO.getTemplate().getId()));
        templateInstance.setDefaultTemplate(templateInstanceTO.getDefaultTemplate());
        templateInstance.setName(templateInstanceTO.getName());
        templateInstance.setDescription(templateInstanceTO.getDescription());

        if (templateInstanceTO.getNewTemplateFile() != null){
            //Replace print template file (new template was specified by the user).
            templateInstance.setTemplateFile(storedFileService.storeFile(templateInstanceTO.getNewTemplateFile().getPath()));
        }

        templateInstance = prepareSave(templateInstance);
        printTemplateInstanceDAO.save(templateInstance);
        if (templateInstance.isDefaultTemplate()){
            printTemplateInstanceDAO.setNewDefaultTemplateInstance(templateInstance);
        }

        PrintTemplateTransformer.updatePrintTemplateInstance(templateInstanceTO, templateInstance);
    }

    private PrintTemplateInstance prepareSave(PrintTemplateInstance templateInstance) throws BusinessException {
        //Perform engine-dependent processing of print template that was just updated.
        PrintTemplatePlugin plugin = printTemplatePluginFactory.getPluginForFileName(templateInstance.getTemplateFile().getName());
        return plugin.prepareTemplateInstanceBeforeSave(templateInstance);
    }

    private boolean differentTemplatePlugins(String currentFileName, String newFileName) {
        PrintTemplatePlugin plugin = printTemplatePluginFactory.getPluginForFileName(currentFileName);
        return !plugin.isTemplateFileSupported(newFileName);
    }

    private PrintTemplateInstance createTemplateInstance(File templateFile) {
        PrintTemplatePlugin plugin = printTemplatePluginFactory.getPluginForFileName(templateFile.getName());
        if (plugin == null){
            throw new IllegalArgumentException("Cannot determine print engine of template file: " + templateFile.getName());
        }
        return plugin.createTemplateInstance();
    }

    /**
     * Gets list of template field mappings by the template id.
     *
     * @param templateId - the id of  the template.
     * @return - list of the template field mappings.
     */
    public List<TemplateFieldMappingTO> getTemplatedFieldMappingByTemplateId(long templateId) {
        List<PrintTemplateFieldMapping> loadedMappings = printTemplateDAO.get(templateId).getFieldsMapping();
        List<PrintTemplateFieldMapping> processedMappings = PrintTemplateFieldsProvider.preProcessFieldMapping(loadedMappings);
        return PrintTemplateTransformer.transformTemplateFieldMappingList(processedMappings);
    }

    /**
     * Gets printing template full data.
     * @param templateInstanceId - identifier of template instance to be loaded.
     * @return - detached printing template with full data loaded.
     */
    public PrintTemplateInstance getTemplateInstanceFullData(long templateInstanceId) {
        return printTemplateInstanceDAO.getFullTemplateInstanceData(templateInstanceId);
    }

    public PrintTemplateInstance getTemplateInstanceById(long templateInstanceId) {
        return printTemplateInstanceDAO.get(templateInstanceId);
    }

    public List<PrintTemplateInstanceTO> getTemplateInstancesByTemplateTypes(PrintTemplateType[] templateTypes) {
        return PrintTemplateTransformer.transformTemplateInstanceList(printTemplateInstanceDAO.findByTemplateTypes(templateTypes));
    }

    public void deleteTemplateInstance(long templateInstanceId) {
        printTemplateInstanceDAO.removeById(templateInstanceId);
    }

    public PrintTemplate getTemplateById(long templateId) {
        return printTemplateDAO.get(templateId);
    }

    //============================== Operations with images ==========================================
    public List<PrintTemplateImage> getAllTemplateImages() {
        return printTemplateImagesDAO.getAll();
    }

    public void saveImage(PrintTemplateImage image) {
        printTemplateImagesDAO.saveOrUpdate(image);
    }

    public void deleteImage(PrintTemplateImage image){
        printTemplateImagesDAO.remove(image);        
    }

    public boolean isUniqueImageName(String imageName, long imageId) {
        PrintTemplateImage sameImage = printTemplateImagesDAO.getImageByName(imageName);
        return sameImage == null || sameImage.getId() == imageId;        
    }

    //============== Spring setters =======================
    public void setPrintTemplateDAO(PrintTemplateDAO printTemplateDAO) {
        this.printTemplateDAO = printTemplateDAO;
    }

    public void setPrintTemplateInstanceDAO(PrintTemplateInstanceDAO printTemplateInstanceDAO) {
        this.printTemplateInstanceDAO = printTemplateInstanceDAO;
    }

    public void setPrintTemplateImagesDAO(PrintTemplateImagesDAO printTemplateImagesDAO) {
        this.printTemplateImagesDAO = printTemplateImagesDAO;
    }

    public void setStoredFileService(StoredFileService storedFileService) {
        this.storedFileService = storedFileService;
    }

    public void setPrintTemplatePluginFactory(PrintTemplatePluginFactory printTemplatePluginFactory) {
        this.printTemplatePluginFactory = printTemplatePluginFactory;
    }
}
