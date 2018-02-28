/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.print.jasper;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.print.PrintEngine;
import com.artigile.warehouse.bl.print.PrintTemplatePlugin;
import com.artigile.warehouse.bl.util.files.StoredFileService;
import com.artigile.warehouse.domain.printing.JasperPrintTemplateInstance;
import com.artigile.warehouse.domain.printing.PrintTemplateInstance;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

import java.io.*;

/**
 * Plugin proving support for Jasper Reports print templates.
 * @author Aliaksandr Chyrtsik
 * @since 09.05.13
 */
public class JasperPrintTemplatePlugin implements PrintTemplatePlugin {
    public static final String REPORT_FILE_EXTENSION = "jrxml";
    public static final String COMPILED_REPORT_FILE_EXTENSION = "jasper";

    private StoredFileService storedFileService;
    private PrintEngine printEngine;

    @Override
    public PrintTemplateInstance createTemplateInstance() {
        return new JasperPrintTemplateInstance();
    }

    @Override
    public PrintTemplateInstance prepareTemplateInstanceBeforeSave(PrintTemplateInstance templateInstance) throws BusinessException {
        JasperPrintTemplateInstance jasperTemplate = (JasperPrintTemplateInstance)templateInstance;

        //Compile report to save time each time report is generated.
        InputStream reportStream = storedFileService.getStoredFileDataStream(templateInstance.getTemplateFile().getId());
        try{
            ByteArrayOutputStream compiledReportOut = new ByteArrayOutputStream();
            JasperCompileManager.compileReportToStream(reportStream, new BufferedOutputStream(compiledReportOut));

            String reportFileName = templateInstance.getTemplateFile().getName();
            String compiledReportFileName = reportFileName.substring(0, reportFileName.length() - StringUtils.getFileExtension(reportFileName).length()) + COMPILED_REPORT_FILE_EXTENSION;

            ByteArrayInputStream compiledReportIn = new ByteArrayInputStream(compiledReportOut.toByteArray());
            jasperTemplate.setCompiledTemplate(storedFileService.storeFileFromStream(compiledReportFileName, compiledReportIn));
        } catch (JRException e) {
            LoggingFacade.logError(this, "Error compiling jasper report.", e);
            throw new BusinessException(I18nSupport.message("printing.error.compilingPrintTemplate", e.getLocalizedMessage()), e);
        } finally {
            if (reportStream != null){
                try {
                    reportStream.close();
                } catch (IOException e) {
                    LoggingFacade.logError(this, e);
                }
            }
        }

        return jasperTemplate;
    }

    @Override
    public boolean isTemplateFileSupported(String templateFileName) {
        return REPORT_FILE_EXTENSION.compareToIgnoreCase(StringUtils.getFileExtension(templateFileName)) == 0;
    }

    @Override
    public boolean isTemplateInstanceSupported(PrintTemplateInstance templateInstance) {
        return templateInstance instanceof JasperPrintTemplateInstance;
    }

    @Override
    public String getTemplateFileTypeDescription() {
        return I18nSupport.message("printing.template.fileFilter.jasper");
    }

    @Override
    public String getTemplateFileExtension() {
        return REPORT_FILE_EXTENSION;
    }

    @Override
    synchronized public PrintEngine getPrintEngine() {
        if (printEngine == null){
            printEngine = new JasperPrintEngine();
        }
        return printEngine;
    }

    public void setStoredFileService(StoredFileService storedFileService) {
        this.storedFileService = storedFileService;
    }
}
