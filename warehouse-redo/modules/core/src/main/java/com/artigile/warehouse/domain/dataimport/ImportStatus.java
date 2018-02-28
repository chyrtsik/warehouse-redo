/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.domain.dataimport;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Valery Barysok, 7/9/11
 */

public enum ImportStatus {
    NOT_COMPLETED(I18nSupport.message("price.import.status.not.completed")), COMPLETED(I18nSupport.message("price.import.status.completed"));

    private String name;

    private ImportStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ImportStatus getInitialState() {
        return NOT_COMPLETED;
    }
}
