/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.mail;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public enum EmailTemplate {
    
    PRICE_LIST_REQUEST_RU("PriceListRequestRU.vm"),

    SELECTED_POSITIONS_PURCHASE_RU("SelectedPositionsPurchaseRU.vm");


    EmailTemplate(String templateFilename) {
        this.templateFilename = templateFilename;
    }

    private String templateFilename;


    public String getTemplateFilename() {
        return templateFilename;
    }
}
