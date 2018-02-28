package com.artigile.warehouse.utils.dto.postings;

import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.i18n.I18nSupport;

import java.text.MessageFormat;

/**
 * @author Aliaksandr.Chyrtsik, 24.11.12
 */
public class UnclassifiedCatalogItemTO {
    private Long id;
    private String barCode;

    private String displayName;

    //============================ Calculated getters ========================================

    /**
     * @return name of unclassified item to display depending on data present.
     */
    public String getDisplayName(){
        if (displayName == null){
            String name;
            if (!StringUtils.isStringNullOrEmpty(barCode)){
                name = I18nSupport.message("unclassified.catalog.item.barcode", barCode);
            }
            else{
                name = I18nSupport.message("unclassified.catalog.item.no.name");
            }
            displayName = MessageFormat.format("{0} ({1})", name, I18nSupport.message("unclassified.catalog.item.need.mapping.to.detail.batch"));
        }
        return displayName;
    }

    //=============================== Getters and setters ====================================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
        this.displayName = null;
    }

    public boolean isSameItem(UnclassifiedCatalogItemTO unclassifiedCatalogItem) {
        return id != null && unclassifiedCatalogItem.id != null && id.equals(unclassifiedCatalogItem.id);
    }
}
