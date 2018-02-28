/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.configuration;

import com.artigile.warehouse.adapter.spi.impl.DomainColumn;

import java.util.List;

public final class ExcelDataAdapterConfigurationParserUtils {
    private ExcelDataAdapterConfigurationParserUtils(){
    }

    static DomainColumn getDomainColumnById(List<DomainColumn> domainColumns, String domainColumnId) {
        if (DomainColumn.NOT_DEFINED.getId().equals(domainColumnId)) {
            return DomainColumn.NOT_DEFINED;
        } else {
            for (DomainColumn domainColumn : domainColumns) {
                if (domainColumn.getId().equals(domainColumnId)) {
                    return domainColumn;
                }
            }
        }
        return null;
    }
}