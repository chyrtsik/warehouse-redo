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

import com.artigile.warehouse.domain.printing.PrintTemplateInstance;

import javax.print.PrintException;
import javax.print.PrintService;

/**
 * Engine used for document printing.
 *
 * @author Aliaksandr.Chyrtsik, 9/11/12
 */
public interface PrintEngine {

    boolean printDefault(Object objectForPrinting, PrintTemplateInstance templateInstance) throws PrintException;

    /**
     * This method tries to silently print object from the domain model with given printer.
     *
     * @param objectForPrinting object being printed.
     * @param templateInstance template to be used.
     * @param printService target printer.
     * @return true is print has been scheduled successfully.
     * @throws PrintException
     */
    boolean printDirect(Object objectForPrinting, PrintTemplateInstance templateInstance, PrintService printService) throws PrintException;

    /**
     * Printing object from application's domain model with showing dialogs for choosing the printer.
     * @param objectForPrinting object being printed.
     * @param templateInstance template to be used.
     * @return true is print has been scheduled successfully.
     * @throws PrintException
     */
    boolean print(Object objectForPrinting, PrintTemplateInstance templateInstance) throws PrintException;

    /**
     * Print preview of an object from then applications' domain model
     * @param objectForPrinting object being printed.
     * @param templateInstance template type to be used.
     * @throws PrintException
     */
    void printPreview(Object objectForPrinting, PrintTemplateInstance templateInstance) throws PrintException;
}
