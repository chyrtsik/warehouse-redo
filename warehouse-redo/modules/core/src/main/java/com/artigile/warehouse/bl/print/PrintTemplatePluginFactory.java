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

/**
 * Factory for accessing all plugins available for printing.
 * @author Aliaksandr Chyrtsik
 * @since 09.05.13
 */
public interface PrintTemplatePluginFactory {
    /**
     * Enumerate all printing plugins available.
     * @return all plugins available.
     */
    PrintTemplatePlugin[] enumeratePlugins();

    /**
     * Find plugin supporting templates stored in specified file.
     * @param fileName file name to be used for plugin selection.
     * @return plugin supporting specified file or null.
     */
    PrintTemplatePlugin getPluginForFileName(String fileName);

    /**
     * Find plugin for given template instance.
     * @param templateInstance template instance to be printed.
     * @return plugin supporting given template instance.
     */
    PrintTemplatePlugin getPluginForTemplateInstance(PrintTemplateInstance templateInstance);
}