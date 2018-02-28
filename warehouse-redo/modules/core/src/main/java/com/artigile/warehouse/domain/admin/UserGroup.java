/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.admin;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Group of users.
 * @author Shyrik, 01.12.2008
 */
@Entity
public class UserGroup {
    /**
     * Primary key.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * Indicator of the group predefined group.
     * This is Administrators group.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean predefined;

    /**
     * Name of group.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Description for group of users.
     */
    private String description;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Rights, that are assigned to the group.
     */
    @ManyToMany
    @JoinTable(inverseJoinColumns = @JoinColumn(name = "userPermission_id"))
    private List<UserPermission> permissions = new ArrayList<UserPermission>();


    //================= Constructors ==========================
    public UserGroup() {
    }

    //================= Operations ============================

    /**
     * Used to check, if group of users has specified right, that is specefied by right id.
     *
     * @param rightId - right id to check.
     * @return - true, if group has such right.
     */
    public boolean hasPermission(PermissionType rightId) {

        for (UserPermission permission : permissions) {
            if (permission.getRightType() == rightId) {
                return true;
            }
        }
        return false;
    }

    /**
     * Used to add new right to the user group.
     *
     * @param newPermission
     */
    public void addPermission(UserPermission newPermission) {
        if (!hasPermission(newPermission.getRightType())) {
            permissions.add(newPermission);
        }
    }

    /**
     * Copies data from the given group.
     * @param group
     */
    public void copyFrom(UserGroup group) {
        setPredefined(group.isPredefined());
        setName(group.getName());
        setDescription(group.getDescription());
    }

    //============== Getters and setters ======================

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isPredefined() {
        return predefined;
    }

    public void setPredefined(boolean predefined) {
        this.predefined = predefined;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getVersion() {
        return version;
    }

    public List<UserPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<UserPermission> permissions) {
        this.permissions = permissions;
    }
}
