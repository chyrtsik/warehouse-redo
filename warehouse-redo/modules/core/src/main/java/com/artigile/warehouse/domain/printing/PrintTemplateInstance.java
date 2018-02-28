/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.printing;

import com.artigile.warehouse.domain.dataimport.StoredFile;

import javax.persistence.*;

/**
 * Instance of print template. Contains concrete print layout used for printing.
 *
 * @author Aliaksandr.Chyrtsik, 9/11/12
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "printEngine", discriminatorType = DiscriminatorType.STRING)
public abstract class PrintTemplateInstance {

    @Id
    @GeneratedValue
    private long id;

    @JoinColumn
    @ManyToOne(optional = false)
    private PrintTemplate printTemplate;

    /**
     * If true then template is used as default (this values should be selected in UI by default).
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean defaultTemplate;

    /**
     * Name of the printing template.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Description of the printing template.
     */
    private String description;

    /**
     * Report definition file provided by the user.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private StoredFile templateFile;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PrintTemplate getPrintTemplate() {
        return printTemplate;
    }

    public void setPrintTemplate(PrintTemplate printTemplate) {
        this.printTemplate = printTemplate;
    }

    public boolean isDefaultTemplate() {
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

    public StoredFile getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(StoredFile templateFile) {
        this.templateFile = templateFile;
    }

    public long getVersion() {
        return version;
    }
}
