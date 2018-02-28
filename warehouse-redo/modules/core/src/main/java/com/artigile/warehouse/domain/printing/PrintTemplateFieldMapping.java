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

import com.artigile.warehouse.utils.StringUtils;

import javax.persistence.*;

/**
 * @author Shyrik, 25.11.2008
 */

/**
 * Holds information about mapping user defined field's name to the
 * field's name of the printing template.
 * Mapping may be one of two types:
 *  - mapping of the printable object field (such ar Order etc.);
 *  - mapping of the image (one of the images, stored in the database specially for printing).
 */
@Entity
public class PrintTemplateFieldMapping {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Print template, to which this field mapping belongs to.
     */
    @ManyToOne(optional = false)
    private PrintTemplate printTemplate;

    /**
     * Field name, that can appear in report template.
     */
    @Column(nullable = false)
    private String reportField;

    /**
     * Printable object property name.
     */
    private String objectField;

    /**
     * Image, which this field mapping is represents.
     */
    @ManyToOne
    private PrintTemplateImage image;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public PrintTemplateFieldMapping() {
    }

    public PrintTemplateFieldMapping(String objectField, String reportField) {
        this.objectField = objectField;
        this.reportField = reportField;
    }

    public PrintTemplateFieldMapping(PrintTemplateImage image, String reportField) {
        this.image = image;
        this.reportField = reportField;
    }

    //========================= Manipulators =========================================
    public boolean isImageField(){
        return image != null;
    }

    public boolean isObjectField(){
        return StringUtils.hasValue(objectField);
    }

    //======================= Getters and setters =====================================
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

    public String getReportField() {
        return reportField;
    }

    public void setReportField(String reportField) {
        this.reportField = reportField;
    }

    public String getObjectField() {
        return objectField;
    }

    public void setObjectField(String objectField) {
        assert(image == null);
        this.objectField = objectField;
    }

    public PrintTemplateImage getImage() {
        return image;
    }

    public void setImage(PrintTemplateImage image) {
        assert(objectField == null);
        this.image = image;
    }

    public long getVersion() {
        return version;
    }
}
