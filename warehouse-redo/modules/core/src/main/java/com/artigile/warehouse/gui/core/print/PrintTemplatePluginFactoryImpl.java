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

import com.artigile.warehouse.bl.print.PrintTemplatePlugin;
import com.artigile.warehouse.bl.print.PrintTemplatePluginFactory;
import com.artigile.warehouse.domain.printing.PrintTemplateInstance;

/**
 * @author Aliaksandr Chyrtsik
 * @since 09.05.13
 */
public class PrintTemplatePluginFactoryImpl implements PrintTemplatePluginFactory {
    private PrintTemplatePlugin[] availablePlugins;

    public PrintTemplatePluginFactoryImpl(PrintTemplatePlugin[] availablePlugins){
        this.availablePlugins = availablePlugins;
    }

    @Override
    public PrintTemplatePlugin[] enumeratePlugins() {
        return availablePlugins;
    }

    @Override
    public PrintTemplatePlugin getPluginForFileName(String fileName) {
        for (PrintTemplatePlugin plugin : availablePlugins){
            if (plugin.isTemplateFileSupported(fileName)){
                return plugin;
            }
        }
        return null;
    }

    @Override
    public PrintTemplatePlugin getPluginForTemplateInstance(PrintTemplateInstance templateInstance) {
        for (PrintTemplatePlugin plugin : availablePlugins){
            if (plugin.isTemplateInstanceSupported(templateInstance)){
                return plugin;
            }
        }
        return null;
    }
}
