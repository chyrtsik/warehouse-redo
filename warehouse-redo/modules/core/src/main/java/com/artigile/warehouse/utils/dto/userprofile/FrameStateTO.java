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

import java.awt.*;

/**
 * State of frame
 *
 * @see com.artigile.warehouse.domain.userprofile.FrameState
 *
 * @author Borisok V.V., 13.09.2009
 */
public class FrameStateTO {
    private long id;

    private UserTO user;

    private String frameId;

    private Double left;

    private Double top;

    private Double witdh;

    private Double height;

    private boolean maximazed;

    public FrameStateTO() {
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

    public String getFrameId() {
        return frameId;
    }

    public void setFrameId(String frameId) {
        this.frameId = frameId;
    }

    public Double getLeft() {
        return left;
    }

    public void setLeft(Double left) {
        this.left = left;
    }

    public Double getTop() {
        return top;
    }

    public void setTop(Double top) {
        this.top = top;
    }

    public Double getWitdh() {
        return witdh;
    }

    public void setWitdh(Double witdh) {
        this.witdh = witdh;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public void setBounds(Rectangle rect) {
        left = rect.getX();
        top = rect.getY();
        witdh = rect.getWidth();
        height = rect.getHeight();
    }

    public boolean getMaximazed() {
        return maximazed;
    }

    public void setMaximazed(boolean maximazed) {
        this.maximazed = maximazed;
    }
}
