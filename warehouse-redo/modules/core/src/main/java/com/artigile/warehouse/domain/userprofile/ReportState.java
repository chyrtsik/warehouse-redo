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

import com.artigile.warehouse.domain.admin.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 15.02.2009
 */
@Entity
public class ReportState {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private String reportMajor;

    @Column(nullable = false)
    private String reportMinor;

    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean filterVisible;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reportState")
    private List<ColumnState> columnStates = new ArrayList<ColumnState>();

    //================================ Constructors ==========================================
    public ReportState() {
    }

    //=============================== Getters and setters ====================================    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getReportMajor() {
        return reportMajor;
    }

    public void setReportMajor(String reportMajor) {
        this.reportMajor = reportMajor;
    }

    public String getReportMinor() {
        return reportMinor;
    }

    public void setReportMinor(String reportMinor) {
        this.reportMinor = reportMinor;
    }

    public boolean getFilterVisible() {
        return filterVisible;
    }

    public void setFilterVisible(boolean filterVisible) {
        this.filterVisible = filterVisible;
    }

    public long getVersion() {
        return version;
    }

    public List<ColumnState> getColumnStates() {
        return columnStates;
    }

    public void setColumnStates(List<ColumnState> columnStates) {
        this.columnStates = columnStates;
    }
}
