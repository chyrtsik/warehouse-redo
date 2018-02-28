/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * Information about one column of data to be imported. Used to configure data import and to
 * parse import results.
 *
 * @author Valery Barysok, 6/21/11
 */
public class DomainColumn {
    /**
     * "Not defined" predefined values of domain column.
     */
    public static final DomainColumn NOT_DEFINED = new DomainColumn("NOT_DEFINED", I18nSupport.message("data.import.relationship.column.not.defined"), false);

    /**
     * Identifier of domain column. Should be unique.
     */
    private String id;

    /**
     * User-friendly name of domain column.
     */
    private String name;

    /**
     * If true then column is required to be mapped to the data imported.
     * May be used in import configuration (to let user map this column) and in import parsing.
     */
    private boolean required;

    /**
     * If true then column values are joined from one or more source data columns.
     */
    private boolean multiple;

    /**
     * Delimiter to be used for multiple values join (values are joined as val1 + <delimiter> + val2 + ...).
     */
    private String multipleDelimiter;

    public DomainColumn(String id, String name, boolean required) {
        this(id, name, required, false, "");
    }

    public DomainColumn(String id, String name, boolean required, boolean multiple, String multipleDelimiter) {
        this.id = id;
        this.name = name;
        this.required = required;
        this.multiple = multiple;
        this.multipleDelimiter = multipleDelimiter;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DomainColumn) {
            DomainColumn o = (DomainColumn) obj;
            return getId().equals(o.getId());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public String getMultipleDelimiter() {
        return multipleDelimiter;
    }
}
