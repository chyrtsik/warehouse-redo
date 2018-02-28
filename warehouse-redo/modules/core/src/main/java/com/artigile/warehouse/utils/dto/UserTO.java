/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto;

import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;


/**
 * @author IoaN, Dec 10, 2008
 */
public class UserTO extends EqualsByIdImpl {

    private long id;

    private boolean predefined;

    private String login;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String nameOnProduct;

    private boolean simplifiedWorkplace;

    private boolean terminalAccess;

    public UserTO() {
    }

    //=================================== Operations =========================================
    public String getDisplayName(){
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

    //=============================== Getters and setters ====================================
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

    public boolean getTerminalAccess() {
        return terminalAccess;
    }

    public void setTerminalAccess(boolean terminalAccess) {
        this.terminalAccess = terminalAccess;
    }
}
