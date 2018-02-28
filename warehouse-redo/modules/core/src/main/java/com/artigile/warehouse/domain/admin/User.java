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

import com.artigile.warehouse.domain.warehouse.Warehouse;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents entity User.
 *
 * @author Ihar, 29.11.2008
 */
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Indicator of the predefined user.
     */
    @Column(nullable = false, columnDefinition = "bit", length = 1)
    private boolean predefined;

    @Column(nullable = false, unique = true, length = ModelFieldsLengths.USER_LOGIN_MAX_LENGTH)
    private String login;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    @Column(name = "name_on_product")
    private String nameOnProduct;

    @Column(name = "simplified_workplace", nullable = false, columnDefinition = "bit", length = 1)
    private boolean simplifiedWorkplace;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    /**
     * Groups, to which user belongs to.
     */
    @ManyToMany
    @JoinTable(inverseJoinColumns=@JoinColumn(name="userGroup_id"))
    private Set<UserGroup> groups = new HashSet<UserGroup>();

    /**
     * Warehouses, from which user is allowed to collect parcels.
     */
    @ManyToMany
    @OrderBy("name")
    @JoinTable(inverseJoinColumns=@JoinColumn(name="warehouse_id"))
    private Set<Warehouse> warehouses = new HashSet<Warehouse>();

    //================= Constructors ===========================
    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    //=========================== Calculated getters =========================
    public String getDisplayName() {
        StringBuilder displayName = new StringBuilder();
        boolean ws = false;
        if (getFirstName() != null) {
            displayName.append(getFirstName());
            ws = true;
        }
        if (getLastName() != null) {
            if (ws) {
                displayName.append(' ');
            }
            displayName.append(getLastName());
        }
        if (displayName.length() == 0){
            displayName.append(getLogin());
        }
        return displayName.toString();
    }

    //================   Operations  ===========================

    /**
     * Used to check, if user has specified right, that is specefied by right id.
     *
     * @param rightId - right id to check.
     * @return - true, if user has right.
     */
    public boolean hasRight(PermissionType rightId) {
        for (UserGroup group : getGroups()) {
            if (group.hasPermission(rightId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks, if user can complect given warehouse
     * @return
     */
    public boolean canComplectWarehouse(long warehouseId) {
        for (Warehouse warehouse : getWarehouses()){
            if (warehouse.getId() == warehouseId){
                return true;
            }
        }
        return false;
    }

    /**
     * Used to add add user to the new group.
     * @param newGroup
     */
    public void addToGroups(UserGroup newGroup) {
        groups.add(newGroup);
    }

    /**
     * Adds user to the users, those are allowed to complect given warehouse.
     * @param warehouse
     */
    public void addToWarehouse(Warehouse warehouse) {
        warehouses.add(warehouse);
    }

    public void deleteFromWarehouse(Warehouse warehouse){
        warehouses.remove(warehouse);
    }

    //==================Setters and getters=====================
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNameOnProduct() {
        return nameOnProduct;
    }

    public void setNameOnProduct(String nameOnProduct) {
        this.nameOnProduct = nameOnProduct;
    }

    public boolean getSimplifiedWorkplace() {
        return simplifiedWorkplace;
    }

    public void setSimplifiedWorkplace(boolean simplifiedWorkplace) {
        this.simplifiedWorkplace = simplifiedWorkplace;
    }

    public long getVersion() {
        return version;
    }

    public Set<UserGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<UserGroup> groups) {
        this.groups = groups;
    }

    public Set<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(Set<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }
}
