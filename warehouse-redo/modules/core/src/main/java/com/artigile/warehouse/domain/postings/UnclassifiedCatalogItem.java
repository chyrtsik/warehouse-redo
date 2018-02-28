package com.artigile.warehouse.domain.postings;

import com.artigile.warehouse.utils.ModelFieldsLengths;

import javax.persistence.*;

/**
 * Item that appeared in the system without being classified as one of detail batches.
 * These items are used at temporary storage of unclassified data and need to be replaced with detail batches
 * before documents containing these items can be processed.
 *
 * @author Aliaksandr.Chyrtsik, 24.11.12
 */
@Entity
@Table(name = "unclassified_catalog_item")
public class UnclassifiedCatalogItem {

    @Id
    @GeneratedValue
    private long id;

    /**
     * Bar code of this item.
     */
    @Column(length = ModelFieldsLengths.MAX_BAR_CODE_LENGTH)
    private String barCode;

    @Version
    @Column(name = "version", nullable = false)
    private long version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public long getVersion() {
        return version;
    }
}
