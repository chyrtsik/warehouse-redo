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

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Aliaksandr.Chyrtsik, 07.11.11
 */
public enum PostingItemsIdentityType {
    /**
     * Posting items are searched by nomenclature article.
     */
    NOMENCLATURE_ARTICLE(I18nSupport.message("posting.item.identity.type.nomenclatureArticle")),

    /**
     * Posting items are searched by barcode.
     */
    BARCODE(I18nSupport.message("posting.item.identity.type.barcode")),

    /**
     * Posting items are searched in price list by name, misc and notice fields.
     */
    NAME_MISC_NOTICE(I18nSupport.message("posting.item.identity.type.nameMiscNotice"));

    private String name;

    public String getName() {
        return name;
    }

    PostingItemsIdentityType(String name) {
        this.name = name;
    }
}
