/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.postings;

import java.util.Map;

/**
 * Result (error description) of posting item that was not imported.
 *
 * @author Aliaksandr.Chyrtsik, 07.11.11
 */
public class PostingItemImportError {
    private String errorMessage;
    private Map<String, String> dataRow;

    public PostingItemImportError(String errorMessage, Map<String, String> dataRow) {
        this.errorMessage = errorMessage;
        this.dataRow = dataRow;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Map<String, String> getDataRow() {
        return dataRow;
    }
}
