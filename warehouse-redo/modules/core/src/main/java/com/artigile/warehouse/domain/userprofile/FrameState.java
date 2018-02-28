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

/**
 * Contains information about frame state: user, unique frame id, frame border (top, left, width, height), maximized
 *
 * @author Borisok V.V., 13.09.2009
 */

@Entity
public class FrameState {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String frameId;

    @Column(name = "x")
    private Double left;

    @Column(name = "y")
    private Double top;

    private Double width;

    private Double height;

    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean maximized;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public FrameState() {
    }

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

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public boolean getMaximized() {
        return maximized;
    }

    public void setMaximized(boolean maximized) {
        this.maximized = maximized;
    }

    public long getVersion() {
        return version;
    }
}
