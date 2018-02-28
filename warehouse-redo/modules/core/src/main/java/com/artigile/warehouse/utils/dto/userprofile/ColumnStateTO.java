/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.userprofile;

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

/**
 * @author Borisok V.V., 16.02.2009
 */
public class ColumnStateTO extends EqualsByIdImpl {
    private long id;

    ReportStateTO reportState;

    private String identifier;

    private String filterText;

    private boolean visible;

    private Integer width;

    public ColumnStateTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ReportStateTO getReportState() {
        return reportState;
    }

    public void setReportState(ReportStateTO reportState) {
        this.reportState = reportState;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }
}
