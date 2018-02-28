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
import com.artigile.warehouse.domain.printing.JasperPrintTemplateInstance;
import com.artigile.warehouse.domain.printing.PrintTemplateInstance;
import com.artigile.warehouse.gui.core.print.PrintTemplateFieldsProvider;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.view.JasperViewer;

import javax.print.PrintException;
import javax.print.PrintService;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.io.InputStream;

/**
 * Jasper Reports printing engine.
 * @author Aliaksandr Chyrtsik
 * @since 12.05.13
 */
public class JasperPrintEngine implements PrintEngine {

    @Override
    public boolean printDefault(Object objectForPrinting, PrintTemplateInstance templateInstance) throws PrintException {
        try {
            JasperPrint print = preparePrint(objectForPrinting, templateInstance);
            return JasperPrintManager.printReport(print, false);
        } catch (JRException e) {
            LoggingFacade.logError(this, "Error printing Jasper report.", e);
            throw new PrintException(I18nSupport.message("printing.error.duringPrint", e.getLocalizedMessage()), e);
        }
    }

    @Override
    public boolean printDirect(Object objectForPrinting, PrintTemplateInstance templateInstance, PrintService printService) throws PrintException {
        try {
            JasperPrint print = preparePrint(objectForPrinting, templateInstance);
            JRPrintServiceExporter exporter = new JRPrintServiceExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);

            exporter.exportReport();
            return true;
        } catch (JRException e) {
            LoggingFacade.logError(this, "Error printing Jasper report.", e);
            throw new PrintException(I18nSupport.message("printing.error.duringPrint", e.getLocalizedMessage()), e);
        }
    }

    @Override
    public boolean print(Object objectForPrinting, PrintTemplateInstance templateInstance) throws PrintException {
        try {
            JasperPrint print = preparePrint(objectForPrinting, templateInstance);
            return JasperPrintManager.printReport(print, true);
        } catch (JRException e) {
            LoggingFacade.logError(this, "Error printing Jasper report.", e);
            throw new PrintException(I18nSupport.message("printing.error.duringPrint", e.getLocalizedMessage()), e);
        }
    }

    @Override
    public void printPreview(Object objectForPrinting, PrintTemplateInstance templateInstance) throws PrintException {
        try {
            JasperPrint print = preparePrint(objectForPrinting, templateInstance);
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setTitle(I18nSupport.message("printing.preview.title"));
            viewer.setVisible(true);
        } catch (JRException e) {
            LoggingFacade.logError(this, "Error printing Jasper report.", e);
            throw new PrintException(I18nSupport.message("printing.error.duringPrint", e.getLocalizedMessage()), e);
        }
    }

    private JasperPrint preparePrint(Object objectForPrinting, PrintTemplateInstance templateInstance) throws JRException, PrintException {
        JasperPrintTemplateInstance jasperTemplate = (JasperPrintTemplateInstance)templateInstance;
        InputStream reportStream = null;
        try {
            reportStream = SpringServiceContext.getInstance().getStoredFileService().getStoredFileDataStream(jasperTemplate.getCompiledTemplate().getId());
        } catch (BusinessException e) {
            LoggingFacade.logError(this, "Error loading compiled Jasper report from database.", e);
            throw new PrintException(I18nSupport.message("printing.error.duringPrint", e.getLocalizedMessage()), e);
        }
        finally {
            if (reportStream != null){
                try {
                    reportStream.close();
                } catch (IOException e) {
                    LoggingFacade.logError(this, e);
                }
            }
        }
        return JasperFillManager.fillReport(reportStream, null, createDataSource(objectForPrinting, templateInstance));
    }

    private JRDataSource createDataSource(Object objectForPrinting, PrintTemplateInstance templateInstance) {
        //Making a table model for printable object.
        PrintTemplateFieldsProvider provider = new PrintTemplateFieldsProvider(objectForPrinting, templateInstance.getPrintTemplate().getFieldsMapping());
        DefaultTableModel table = new DefaultTableModel(provider.getReportFields().toArray(), provider.getItemsCount());

        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getColumnCount(); col++) {
                table.setValueAt(provider.getFieldValue(col, row), row, col);
            }
        }

        return new JRTableModelDataSource(table);
    }
}
