/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.details;

import com.artigile.warehouse.domain.printing.PrintTemplateInstance;
import com.artigile.warehouse.domain.sticker.StickerPrintParam;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents type of product. Type means large group of products with the same sets of parameters.
 * @author Shyrik, 14.12.2008
 */
@Entity
public class DetailType {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Sorting numbed of this detail type
     */
    private Integer sortNum;

    /**
     * Name of the detail type.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Description of the detail type.
     */
    private String description;

    /**
     * Fields of this type of detail.
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(inverseJoinColumns=@JoinColumn(name="detailField_id"))
    private List<DetailField> fields = new ArrayList<DetailField>();

    /**
     * Field type for the detail batch name.
     */
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private DetailField detailBatchNameField;

    /**
     * Field type for the detail batch misc field.
     */
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private DetailField detailBatchMiscField;

     /**
     * Field type for the detail batch misc field.
     */
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private DetailField detailBatchTypeField;

    /**
     * Fields of serial number of this type of detail.
     */
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "detailtype_serialnumberfield", inverseJoinColumns=@JoinColumn(name="detailField_id"))
    private List<DetailField> serialNumberFields = new ArrayList<DetailField>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "detailType")
    private List<StickerPrintParam> stickerPrintParams = new ArrayList<StickerPrintParam>();

    @Column(name = "print_serial_numbers", nullable = false, columnDefinition = "bit", length = 1)
    private boolean printSerialNumbers;

    @ManyToOne
    @JoinColumn(name = "print_template_instance_id")
    private PrintTemplateInstance printTemplateInstance;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public DetailType() {
    }

    public DetailType(long id) {
        this.id = id;
    }

    public List<DetailField> getSortedGroupingFields() {
        List<DetailField> groupingFields = new ArrayList<DetailField>(fields.size());
        for (DetailField field : fields){
            if (field.getCatalogGroupNum() != null){
                groupingFields.add(field);
            }
        }
        Collections.sort(groupingFields, DetailField.CATALOG_GROUPING_NUM_COMPARATOR);
        return groupingFields;
    }

    //======================= Getters and Setters ============================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
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

    public List<DetailField> getFields() {
        return fields;
    }

    public void setFields(List<DetailField> fields) {
        this.fields = fields;
    }

    public DetailField getDetailBatchNameField() {
        return detailBatchNameField;
    }

    public void setDetailBatchNameField(DetailField detailBatchNameField) {
        this.detailBatchNameField = detailBatchNameField;
    }

    public DetailField getDetailBatchMiscField() {
        return detailBatchMiscField;
    }

    public void setDetailBatchMiscField(DetailField detailBatchMiscField) {
        this.detailBatchMiscField = detailBatchMiscField;
    }

    public DetailField getDetailBatchTypeField() {
        return detailBatchTypeField;
    }

    public void setDetailBatchTypeField(DetailField detailBatchTypeField) {
        this.detailBatchTypeField = detailBatchTypeField;
    }

    public List<DetailField> getSerialNumberFields() {
        return serialNumberFields;
    }

    public void setSerialNumberFields(List<DetailField> serialNumberFields) {
        this.serialNumberFields = serialNumberFields;
    }

    public List<StickerPrintParam> getStickerPrintParams() {
        return stickerPrintParams;
    }

    public void setStickerPrintParams(List<StickerPrintParam> stickerPrintParams) {
        this.stickerPrintParams = stickerPrintParams;
    }

    public boolean getPrintSerialNumbers() {
        return printSerialNumbers;
    }

    public void setPrintSerialNumbers(boolean printSerialNumbers) {
        this.printSerialNumbers = printSerialNumbers;
    }

    public PrintTemplateInstance getPrintTemplateInstance() {
        return printTemplateInstance;
    }

    public void setPrintTemplateInstance(PrintTemplateInstance printTemplateInstance) {
        this.printTemplateInstance = printTemplateInstance;
    }
}
