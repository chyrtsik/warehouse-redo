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

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.dao.ContractorProductDAO;
import com.artigile.warehouse.domain.priceimport.ContractorProduct;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.priceimport.ContractorProductTOForReport;
import com.artigile.warehouse.utils.transofmers.ContractorProductTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author Valery.Barysok
 */
@Transactional(rollbackFor = BusinessException.class)
public class ContractorProductService {

    private ContractorProductDAO contractorProductDAO;

    public ContractorProductService() {}


    @SuppressWarnings("unchecked")
    public List<ContractorProductTOForReport> getListByFilter(ContractorProductFilter filter) {
        return checkFilterFilling(filter)
                ? ContractorProductTransformer.transformList(contractorProductDAO.getListByFilter(filter))
                : Collections.EMPTY_LIST;
    }

    public ContractorProduct getContractorProductById(long contractorProductId) {
        return contractorProductDAO.get(contractorProductId);
    }

    public void setContractorProductDAO(ContractorProductDAO contractorProductDAO) {
        this.contractorProductDAO = contractorProductDAO;
    }

    public void saveContractorProductWithFlush(ContractorProductTOForReport contractorProduct) {
        ContractorProduct persistentContractorProduct = contractorProductDAO.get(contractorProduct.getId());
        if (persistentContractorProduct == null) {
            persistentContractorProduct = new ContractorProduct();
        }

        ContractorProductTransformer.update(persistentContractorProduct, contractorProduct);
        // Name processing for ignoring special symbols
        persistentContractorProduct.setSimplifiedName(StringUtils.simplifyName(persistentContractorProduct.getName()));
        contractorProductDAO.save(persistentContractorProduct);
        contractorProductDAO.flush();
        contractorProductDAO.refresh(persistentContractorProduct);
        ContractorProductTransformer.update(contractorProduct, persistentContractorProduct);
    }

    public void deleteContractorProduct(long productId) {
        contractorProductDAO.removeById(productId);
    }

    /**
     * Marks the contractor's product with the given ID as selected by user.
     *
     * @param productId ID of the selected contractor's product
     */
    public void selectContractorProduct(long productId) {
        updateContractorProductSelection(productId, true);
    }

    /**
     * Removes mark of selection a product by user.
     *
     * @param productId ID of the contractor's product for deselection
     */
    public void deselectContractorProduct(long productId) {
        updateContractorProductSelection(productId, false);
    }

    private void updateContractorProductSelection(long productID, boolean select) {
        ContractorProduct contractorProduct = contractorProductDAO.get(productID);
        if (contractorProduct != null) {
            contractorProduct.setSelected(select);
            contractorProductDAO.update(contractorProduct);
        }
    }

    /**
     * @param filter Search filter
     * @return Count of records, found using the given filter
     */
    public long getContractorsProductsCount(ContractorProductFilter filter) {
        return checkFilterFilling(filter) ? contractorProductDAO.getCountByFilter(filter) : -1;
    }

    /**
     * @param filter Filter for checking
     * @return True - filter is valid, false - filter isn't valid
     */
    private boolean checkFilterFilling(ContractorProductFilter filter) {
        return (filter != null && (filter.getNameMask() != null || filter.getSelected() != null));
    }
}
