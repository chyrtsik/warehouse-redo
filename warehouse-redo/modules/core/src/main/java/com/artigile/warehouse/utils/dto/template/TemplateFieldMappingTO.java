/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.template;

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

/**
 * @author IoaN, Dec 28, 2008
 */

public class TemplateFieldMappingTO extends EqualsByIdImpl {
    private long id;

    private String reportField;

    private String objectField;

    public TemplateFieldMappingTO() {
    }

    public TemplateFieldMappingTO(long id, String reportField, String objectField) {
        this.id = id;
        this.reportField = reportField;
        this.objectField = objectField;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        this.objectField = objectField;
    }

    @Override
    public String toString() {
        return reportField;
    }
}
