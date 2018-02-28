/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.priceimport;

import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.jetbrains.annotations.PropertyKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration with all supported contractor product columns
 * (used in contractor price import).
 *
 * @author Aliaksandr.Chyrtsik, 05.11.11
 */
public enum ContractorProductDomainColumnType {
    NAME("NAME", "price.import.relationship.column.name", true, true, " "),
    DESCRIPTION("DESCRIPTION", "price.import.relationship.column.description", false, true, " "),
    YEAR("YEAR", "price.import.relationship.column.year", false, false, ""),
    QUANTITY("QUANTITY", "price.import.relationship.column.quantity", true, false, ""),
    WHOLESALE_PRICE("WHOLESALE_PRICE", "price.import.relationship.column.wholesale.price", false, false, ""),
    RETAIL_PRICE("RETAIL_PRICE", "price.import.relationship.column.retail.price", false, false, ""),
    PACK("PACK", "price.import.relationship.column.pack", false, false, ""),
    PRODUCT_TYPE("PRODUCT_TYPE", "price.import.relationship.column.product.type", false, true, " ");

    /**
     * Data import domain column information related to this enum value.
     */
    private DomainColumn domainColumn;

    ContractorProductDomainColumnType(String id, @PropertyKey(resourceBundle = "i18n.warehouse") String nameRes,
                                      boolean required, boolean multiple, String multipleDelimiter) {
        this.domainColumn = new DomainColumn(id, I18nSupport.message(nameRes), required, multiple, multipleDelimiter);
    }

    public DomainColumn getDomainColumn() {
        return domainColumn;
    }

    public static List<DomainColumn> enumerateDomainColumns(){
        List<DomainColumn> columns = new ArrayList<DomainColumn>(values().length);
        for (ContractorProductDomainColumnType enumValue : values()){
            columns.add(enumValue.getDomainColumn());
        }
        return columns;
    }
}
