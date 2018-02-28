/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.printing;



import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 25.11.2008
 */

/**
 * Holds settings of the printing template. Settings include:
 * <ul>
 *     <li>identifier of template type;</li>
 *     <li>fields available in template.</li>
 * </ul>
 */
@Entity
public class PrintTemplate {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Identifier of the form.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PrintTemplateType templateType;

    /**
     * Name of the printing template.
     */
    @Column(nullable = false, unique = true)
    private String name;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Mapping of the names of the fields that are available for printing.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "printTemplate")
    @OrderBy("reportField")
    private List<PrintTemplateFieldMapping> fieldsMapping = new ArrayList<PrintTemplateFieldMapping>();

    public PrintTemplate() {
    }

    public PrintTemplate(long id, PrintTemplateType templateType, String name) {
        this.id = id;
        this.templateType = templateType;
        this.name = name;
    }

    //========================== Getters and setters =========================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<PrintTemplateFieldMapping> getFieldsMapping() {
        return fieldsMapping;
    }

    public void setFieldsMapping(List<PrintTemplateFieldMapping> fieldsMapping) {
        this.fieldsMapping = new ArrayList<PrintTemplateFieldMapping>(fieldsMapping);
    }

    public PrintTemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(PrintTemplateType templateType) {
        this.templateType = templateType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVersion() {
        return version;
    }
}
