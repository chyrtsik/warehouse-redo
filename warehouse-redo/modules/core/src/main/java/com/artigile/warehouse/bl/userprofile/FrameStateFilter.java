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
 * Filter for loading state of internal frame
 *
 * @see com.artigile.warehouse.domain.userprofile.FrameState
 *
 * @author Borisok V.V., 13.09.2009
 */

public class FrameStateFilter {

    private User user;

    private String frameId;

    public FrameStateFilter(User user, String frameId) {
        this.user = user;
        this.frameId = frameId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFrameId() {
        return frameId;
    }

    public void setFrameId(String frameId) {
        this.frameId = frameId;
    }
}
