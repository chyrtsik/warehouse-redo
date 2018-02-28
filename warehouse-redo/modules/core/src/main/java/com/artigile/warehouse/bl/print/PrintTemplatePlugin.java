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
import com.artigile.warehouse.domain.printing.PrintTemplateInstance;

/**
 * Plugin for printing template with each own printing engine. This interface is implemented for each new
 * printing (or reporting) engine being added to the system.
 * @author Aliaksandr Chyrtsik
 * @since 09.05.13
 */
public interface PrintTemplatePlugin {
    /**
     * Create instance of print template that is able to store all data related to underlying print engine.
     * @return new instance of print template.
     */
    PrintTemplateInstance createTemplateInstance();

    /**
     * Prepare print template instance before storing. Underlying printing engine is supposed to generate all
     * addition structures needed for template to work properly.
     * @param templateInstance template instance being saved.
     * @return changed (and possibly wrapped) template instance.
     * @throws  BusinessException when report cannot be prepared.
     */
    PrintTemplateInstance prepareTemplateInstanceBeforeSave(PrintTemplateInstance templateInstance) throws BusinessException;

    /**
     * Determine if this template file is supported by underlying printing engine.
     * @param templateFileName file name with printing template.
     * @return true if file is supported.
     */
    boolean isTemplateFileSupported(String templateFileName);

    /**
     * Determine if this template instance is supported by underlying printing engine.
     * @param templateInstance instance of printing template.
     * @return true if template instance is supported.
     */
    boolean isTemplateInstanceSupported(PrintTemplateInstance templateInstance);

    /**
     * @return user friendly description of files containing printing template of this plugin.
     */
    String getTemplateFileTypeDescription();

    /**
     * @return file extension of this plugin.
     */
    String getTemplateFileExtension();

    /**
     * @return print engine to print templates provided by this plugin.
     */
    PrintEngine getPrintEngine();
}
