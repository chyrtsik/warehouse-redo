package com.artigile.warehouse.utils.dto.details;

import com.artigile.warehouse.utils.i18n.I18nSupport;

/**
 * @author Valery Barysok, 2013-05-20
 */
public enum DetailBatchDocumentType {

    ORDER(I18nSupport.message("detail.batch.reserves.list.document.order")),

    MOVEMENT(I18nSupport.message("detail.batch.reserves.list.document.movement"));

    private String name;

    private DetailBatchDocumentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
