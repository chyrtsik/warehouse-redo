/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.utils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Shyrik, 06.12.2008
 */

/**
 * Simple implementation of File filter for file choosing dialogs.
 */
public class SimpleFilter extends FileFilter {
    private String description;
    private String[] extensions;

    public SimpleFilter(String extension, String description) {
        this.description = description;
        this.extensions = new String[]{"." + extension.toLowerCase()};
    }

    public SimpleFilter(String []extensions, String description) {
        this.description = description;
        this.extensions = extensions;
    }

    public String getDescription() {
        return description;
    }

    public boolean accept(File file) {
        if (file == null) {
            return false;
        } else if (file.isDirectory()) {
            return true;
        }
        String fileLower = file.getName().toLowerCase();
        for (String ext : extensions){
            if (fileLower.endsWith(ext)){
                return true;
            }
        }
        return false;
    }
}
