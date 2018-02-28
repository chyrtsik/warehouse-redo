/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.userprofile;

import com.artigile.warehouse.domain.admin.User;

/**
 * @author Borisok V.V., 17.02.2009
 */
public class ReportStateFilter {

    private User user;

    private String major;

    private String minor;

    public ReportStateFilter(User user, String major, String minor) {
        this.user = user;
        this.major = major;
        this.minor = minor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }
}
