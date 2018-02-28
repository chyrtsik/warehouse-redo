/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto;

import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class EmailConfigTO extends EqualsByIdImpl {

    private long id;

    private String serverHost;

    private int serverPort;

    private String accountUsername;

    private String accountPassword;

    private String priceListRequestMessageSubject;

    private String selectedPositionsPurchaseMessageSubject;


    /* Utils methods
    ------------------------------------------------------------------------------------------------------------------*/
    public boolean isConfigured() {
        return !StringUtils.isStringNullOrEmpty(serverHost)
                && serverPort != 0
                && !StringUtils.isStringNullOrEmpty(accountUsername)
                && !StringUtils.isStringNullOrEmpty(accountPassword)
                && !StringUtils.isStringNullOrEmpty(priceListRequestMessageSubject)
                && !StringUtils.isStringNullOrEmpty(selectedPositionsPurchaseMessageSubject);
    }


    /* Setters and getters
    ------------------------------------------------------------------------------------------------------------------*/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getAccountUsername() {
        return accountUsername;
    }

    public void setAccountUsername(String accountUsername) {
        this.accountUsername = accountUsername;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public String getPriceListRequestMessageSubject() {
        return priceListRequestMessageSubject;
    }

    public void setPriceListRequestMessageSubject(String priceListRequestMessageSubject) {
        this.priceListRequestMessageSubject = priceListRequestMessageSubject;
    }

    public String getSelectedPositionsPurchaseMessageSubject() {
        return selectedPositionsPurchaseMessageSubject;
    }

    public void setSelectedPositionsPurchaseMessageSubject(String selectedPositionsPurchaseMessageSubject) {
        this.selectedPositionsPurchaseMessageSubject = selectedPositionsPurchaseMessageSubject;
    }
}
