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

import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 16.02.2009
 */
public class ReportStateTO extends EqualsByIdImpl {
    private long id;

    private UserTO user;

    private String reportMajor;

    private String reportMinor;

    private boolean filterVisible;

    private List<ColumnStateTO> columnStates = new ArrayList<ColumnStateTO>();

    public ReportStateTO() {
    }

    public ReportStateTO(long id) {
        this.id = id;
    }

    public ReportStateTO(long id, UserTO user, String reportMajor, String reportMinor, List<ColumnStateTO> columnStates, boolean filterVisible) {
        this.id = id;
        this.user = user;
        this.reportMajor = reportMajor;
        this.reportMinor = reportMinor;
        this.columnStates = columnStates;
        this.filterVisible = filterVisible;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserTO getUser() {
        return user;
    }

    public void setUser(UserTO user) {
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

    public List<ColumnStateTO> getColumnStates() {
        return columnStates;
    }

    public void setColumnStates(List<ColumnStateTO> columnStates) {
        this.columnStates = columnStates;
    }

    public boolean getFilterVisible() {
        return filterVisible;
    }

    public void setFilterVisible(boolean filterVisible) {
        this.filterVisible = filterVisible;
    }
}
