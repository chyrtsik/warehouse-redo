/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain;

import com.artigile.warehouse.domain.admin.UserPermission;
import com.artigile.warehouse.gui.core.plugin.PluginType;

import javax.persistence.*;

/**
 * Represents item of the menu tree.
 *
 * @author Ihar, Nov 30, 2008
 */
@Entity
public class MenuItem {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Full name of the item (full name means path of the menu item in the menu items tree).
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Type of the plugin.
     */
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PluginType pluginType;

    /**
     * Class name to plugin.
     */
    @Column(nullable = false)
    private String pluginClassName;

    /**
     * Permission, that user must have to see this menu item.
     * If not set, there is no permission required.
     */
    @OneToOne
    private UserPermission viewPermission;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public MenuItem() {
    }

    public MenuItem(String name, PluginType pluginType, String pluginClassName, UserPermission viewPermission) {
        this.name = name;
        this.pluginType = pluginType;
        this.pluginClassName = pluginClassName;
        this.viewPermission = viewPermission;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PluginType getPluginType() {
        return pluginType;
    }

    public void setPluginType(PluginType pluginType) {
        this.pluginType = pluginType;
    }

    public String getPluginClassName() {
        return pluginClassName;
    }

    public void setPluginClassName(String pluginClassName) {
        this.pluginClassName = pluginClassName;
    }

    public UserPermission getViewPermission() {
        return viewPermission;
    }

    public void setViewPermission(UserPermission viewPermission) {
        this.viewPermission = viewPermission;
    }

    public long getVersion() {
        return version;
    }
}
