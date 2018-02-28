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

/**
 * @author Shyrik, 01.12.2008
 */

/**
 * Objects of this class represents user rights. Usially one unique right allows
 * user to perform one kind of operations.
 */
@Entity
public class UserPermission {
    /**
     * Primary key.
     */
    @Id
    @GeneratedValue
    private long id;

    /**
     * Identifief of the right, that is used in code.
     */
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, unique = true)
    private PermissionType rightType;

    /**
     * Name of the right.
     */
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    //================== Constructors =================================
    public UserPermission() {
    }

    public UserPermission(PermissionType rightType, String name) {
        this.rightType = rightType;
        this.name = name;
    }

    //================= Getters and setters ===========================
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PermissionType getRightType() {
        return rightType;
    }

    public void setRightType(PermissionType rightType) {
        this.rightType = rightType;
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
}
