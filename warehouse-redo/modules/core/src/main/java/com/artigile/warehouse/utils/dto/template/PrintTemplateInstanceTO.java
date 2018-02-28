/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.template;

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

import java.io.File;

/**
 * @author IoaN, Dec 28, 2008
 */

public class PrintTemplateInstanceTO extends EqualsByIdImpl {

    private long id;

    private PrintTemplateTO template;

    private boolean defaultTemplate;

    private String name;

    private String description;

    private StoredFileTO templateFile;

    /**
     * When specified then new template file should be loaded into database instead of the current one.
     */
    private File newTemplateFile;

    public PrintTemplateInstanceTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PrintTemplateTO getTemplate() {
        return template;
    }

    public void setTemplate(PrintTemplateTO template) {
        this.template = template;
    }

    public boolean getDefaultTemplate() {
        return defaultTemplate;
    }

    public void setDefaultTemplate(boolean defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StoredFileTO getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(StoredFileTO templateFile) {
        this.templateFile = templateFile;
    }

    public File getNewTemplateFile() {
        return newTemplateFile;
    }

    public void setNewTemplateFile(File newTemplateFile) {
        this.newTemplateFile = newTemplateFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PrintTemplateInstanceTO that = (PrintTemplateInstanceTO) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
