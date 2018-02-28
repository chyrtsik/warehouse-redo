/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain;


import com.artigile.warehouse.utils.dto.common.EqualsByIdImpl;

import javax.persistence.*;

/**
 * Entity, which describes e-mail configuration of the application.
 *
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
@Entity
@Table(name = "email_config")
public class EmailConfig extends EqualsByIdImpl {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Mail server host
     */
    @Column(nullable = false)
    private String serverHost;

    /**
     * Mail server port
     */
    private int serverPort;

    /**
     * Username for the account at the mail host
     */
    @Column(nullable = false)
    private String accountUsername;

    /**
     * Password for the account at the mail host
     */
    @Column(nullable = false)
    private String accountPassword;

    /**
     * Subject of message for request price list
     */
    private String priceListRequestMessageSubject;

    /**
     * Subject of message for purchase from selected positions
     */
    private String selectedPositionsPurchaseMessageSubject;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

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

    public long getVersion() {
        return version;
    }
}
