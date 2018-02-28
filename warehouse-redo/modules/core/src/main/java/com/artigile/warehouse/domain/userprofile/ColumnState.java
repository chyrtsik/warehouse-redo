/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.userprofile;

import javax.persistence.*;

/**
 * @author Borisok V.V., 15.02.2009
 */
@Entity
public class ColumnState {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    ReportState reportState;

    /**
     * Identifier of a column.
     */
    @Column(nullable = false)
    private String identifier;

    /**
     * Visibility of this column.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean visible;

    /**
     * Filter text for this column.
     */
    private String filterText="";

    /**
     * Width of column.
     */
    private Integer width;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //================================ Constructors ======================================
    public ColumnState() {
    }

    //============================== Gettres an setters ==================================    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ReportState getReportState() {
        return reportState;
    }

    public void setReportState(ReportState reportState) {
        this.reportState = reportState;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean getVisible() {
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

    public boolean isVisible() {
        return visible;
    }
}
