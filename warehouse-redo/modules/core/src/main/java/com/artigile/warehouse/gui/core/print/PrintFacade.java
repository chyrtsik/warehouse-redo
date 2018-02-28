/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.print;

import com.artigile.warehouse.bl.print.PrintEngine;
import com.artigile.warehouse.bl.print.PrintTemplateService;
import com.artigile.warehouse.domain.printing.PrintTemplateInstance;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import javax.print.PrintException;
import javax.print.PrintService;
import java.awt.print.PrinterJob;

/**
 * Facade for printing.
 * @author Aliaksandr Chyrtsik
 * @since 12.05.13
 */
public final class PrintFacade {

    private PrintFacade() {
    }

    /**
     * This method tries to silently print object.
     * @param objectForPrinting object being printed.
     * @param templateInstanceId template to be used.
     * @return true is print has been scheduled successfully.
     * @throws javax.print.PrintException
     */
    public static boolean printDefault(Object objectForPrinting, Long templateInstanceId) throws PrintException {
        if (templateInstanceId == null) {
            return false;
        }
        PrintTemplateInstance templateInstance = loadTemplateInstance(templateInstanceId);
        objectForPrinting = constructPrintableObject(objectForPrinting, templateInstance);
        if (!initializePrintableObject(objectForPrinting)) {
            return false;
        }

        return getEngineForTemplate(templateInstance).printDefault(objectForPrinting, templateInstance);
    }

    /**
     * This method tries to silently print object from the domain model with given printer.
     * @param objectForPrinting object being printed.
     * @param templateType template to be used.
     * @param printerName name of the target printer.
     * @return true is print has been scheduled successfully.
     * @throws javax.print.PrintException
     */
    public static boolean printDirect(Object objectForPrinting, PrintTemplateType templateType, String printerName) throws PrintException {
        //Determine print engine to use for printing.
        Long templateInstanceId = getTemplateInstanceId(templateType);
        if (templateInstanceId == null){
            return false;
        }
        PrintTemplateInstance templateInstance = loadTemplateInstance(templateInstanceId);

        objectForPrinting = constructPrintableObject(objectForPrinting, templateInstance);
        if (!initializePrintableObject(objectForPrinting)){
            return false;
        }

        PrintEngine printEngine = getEngineForTemplate(templateInstance);

        //Try to found printer with given name.
        PrintService printService = null;
        if (printerName != null && !printerName.isEmpty()){
            PrintService[] services = PrinterJob.lookupPrintServices();
            for (PrintService service : services){
                if (service.getName().equals(printerName)){
                    printService = service;
                    break;
                }
            }
        }

        if (printService != null){
            //Print with given printer.
            return printEngine.printDirect(objectForPrinting, templateInstance, printService);
        }
        else{
            //Print with choosing a printer.
            return printEngine.print(objectForPrinting, templateInstance);
        }
    }

    /**
     * Printing object from application's domain model with showing dialogs for choosing the printer.
     * @param objectForPrinting object being printed.
     * @param templateTypes templates that can be used. If more than one templates available user is asked to choose one.
     * @return true is print has been scheduled successfully.
     * @throws PrintException
     */
    public static boolean print(Object objectForPrinting, PrintTemplateType... templateTypes) throws PrintException{
        Long templateInstanceId = getTemplateInstanceId(templateTypes);
        if (templateInstanceId == null){
            return false;
        }
        PrintTemplateInstance templateInstance = loadTemplateInstance(templateInstanceId);

        objectForPrinting = constructPrintableObject(objectForPrinting, templateInstance);
        if (!initializePrintableObject(objectForPrinting)){
            return false;
        }

        PrintEngine printEngine = getEngineForTemplate(templateInstance);
        return printEngine.print(objectForPrinting, templateInstance);
    }

    /**
     * Print preview of an object from then applications' domain model
     * @param objectForPrinting object being printed.
     * @param templateTypes template types to be used. If more than one template is available user is asked to choose one.
     * @throws PrintException
     */
    public static void printPreview(Object objectForPrinting, PrintTemplateType... templateTypes) throws PrintException{
        Long templateInstanceId = getTemplateInstanceId(templateTypes);
        if (templateInstanceId == null){
            return;
        }
        PrintTemplateInstance templateInstance = loadTemplateInstance(templateInstanceId);

        objectForPrinting = constructPrintableObject(objectForPrinting, templateInstance);
        if (!initializePrintableObject(objectForPrinting)){
            return;
        }

        PrintEngine printEngine = getEngineForTemplate(templateInstance);
        printEngine.printPreview(objectForPrinting, templateInstance);
    }

    /**
     * Determine print template instance to be used for printing this document. Will force user to
     * select instance to use if there are more than one template instances available.
     * @param templateTypes template types to lookup.
     * @return template instance for given template type.
     * @throws PrintException if template instance cannot be loaded.
     */
    private static Long getTemplateInstanceId(PrintTemplateType ... templateTypes) throws PrintException {
        PrintTemplateService printTemplateService = SpringServiceContext.getInstance().getPrintTemplateService();
        java.util.List<PrintTemplateInstanceTO> templateInstances = printTemplateService.getTemplateInstancesByTemplateTypes(templateTypes);
        if (templateInstances.isEmpty()){
            //Error loading template.
            throw new PrintException(I18nSupport.message("printing.error.cannotLoadTemplate"));
        }
        else if (templateInstances.size() == 1){
            //No conflict -- the only template will be default one.
            return templateInstances.get(0).getId();
        }
        else{
            //We need user to select which template to use.
            PrintTemplateInstanceSelectForm selectTemplateForm = new PrintTemplateInstanceSelectForm(templateInstances);
            if (Dialogs.runProperties(selectTemplateForm)){
                return selectTemplateForm.getSelectedTemplateInstance().getId();
            }
            else{
                return null;
            }
        }
    }

    private static boolean initializePrintableObject(Object objectForPrinting) {
        if (objectForPrinting instanceof PrintTemplateRequiringInitialization){
            PrintTemplateRequiringInitialization init = (PrintTemplateRequiringInitialization)objectForPrinting;
            return init.initialize();
        }
        else{
            return true;
        }
    }

    private static Object constructPrintableObject(Object objectForPrinting, PrintTemplateInstance templateInstance) {
        if (objectForPrinting instanceof PrintTemplateTypeDependent){
            PrintTemplateTypeDependent printTemplateTypeDependent = (PrintTemplateTypeDependent) objectForPrinting;
            objectForPrinting = printTemplateTypeDependent.prepareForTemplateType(templateInstance.getPrintTemplate().getTemplateType());
        }
        return objectForPrinting;
    }

    private static PrintEngine getEngineForTemplate(PrintTemplateInstance templateInstance) {
        return SpringServiceContext.getInstance().getPrintTemplatePluginFactory().getPluginForTemplateInstance(templateInstance).getPrintEngine();
    }

    private static PrintTemplateInstance loadTemplateInstance(Long templateInstanceId) throws PrintException {
        PrintTemplateInstance templateInstance = SpringServiceContext.getInstance().getPrintTemplateService().getTemplateInstanceFullData(templateInstanceId);
        if (templateInstance == null) {
            throw new PrintException(I18nSupport.message("printing.error.cannotLoadTemplate"));
        }
        else if (templateInstance.getTemplateFile() == null) {
            throw new PrintException(I18nSupport.message("printing.error.templateDataNotSet"));
        }
        return templateInstance;
    }
}
