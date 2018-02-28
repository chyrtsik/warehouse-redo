package com.artigile.warehouse.bl.postings;

import com.artigile.warehouse.dao.UnclassifiedCatalogItemDAO;
import com.artigile.warehouse.domain.postings.UnclassifiedCatalogItem;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for working with unclassified canalog items.
 *
 * @author Aliaksandr.Chyrtsik, 24.11.12
 */
@Transactional(readOnly = true)
public class UnclassifiedCatalogItemService {

    private UnclassifiedCatalogItemDAO unclassifiedCatalogItemDAO;

    public UnclassifiedCatalogItem findItemById(Long id) {
        return unclassifiedCatalogItemDAO.get(id);
    }

    //============================== Spring setters ===============================================
    public void setUnclassifiedCatalogItemDAO(UnclassifiedCatalogItemDAO unclassifiedCatalogItemDAO) {
        this.unclassifiedCatalogItemDAO = unclassifiedCatalogItemDAO;
    }
}
