/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.util.collections;

import com.artigile.warehouse.bl.priceimport.ContractorProductFilter;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.custom.types.VariantPrice;
import com.artigile.warehouse.utils.dto.priceimport.ContractorProductTOForReport;
import com.artigile.warehouse.utils.logging.LoggingFacade;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class ContractorProductNavigableList implements NavigableCollection<ContractorProductTOForReport> {

    /**
     * Number of elements in one part output (default).
     * Used when output size of data isn't specified in the search filter.
     */
    private static final int DEFAULT_ELEMENTS_OUTPUT = 1000;

    /**
     * Search filter for getting required results.
     */
    private ContractorProductFilter filter;

    /**
     * Number of elements in one part output.
     */
    private int elementsOutput;

    /**
     * Number of all elements, found by the given filter.
     * Usually updates after each changing of the search filter.
     */
    private long elementsCount;

    /**
     * Container of loaded elements.
     * Every next part of loaded items is placed into this container.
     * It used for fast accessing to already loaded items.
     */
    private List<ContractorProductTOForReport> elements;

    /**
     * Special index that indicates current part of the output data
     */
    private int index;

    /**
     * Number of already loaded parts of the data
     */
    private int loaded;


    /* Constructor
    ------------------------------------------------------------------------------------------------------------------*/
    public ContractorProductNavigableList(ContractorProductFilter filter) {
        this.filter = filter;
        if (filter.getDataLimit().getMaxResults() == null) {
            filter.getDataLimit().setMaxResults(DEFAULT_ELEMENTS_OUTPUT);
        }
        this.elementsOutput = filter.getDataLimit().getMaxResults();
        this.elements = new ArrayList<ContractorProductTOForReport>();

        // Load first part of the data
        this.index = 1;
        this.loaded = 1;
        loadPart();

        // Get number of all elements found by this filter
        this.elementsCount = SpringServiceContext.getInstance()
                .getContractorProductService().getContractorsProductsCount(filter);
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void toPrev() {
        if (index > 1) {
            index--;
        }
    }

    @Override
    public boolean isBegin() {
        return index == 1;
    }

    @Override
    public void toNext() {
        if (index < maxIndex()) {
            index++;
        }
    }

    @Override
    public boolean isEnd() {
        return index == maxIndex();
    }

    @Override
    public List<ContractorProductTOForReport> getCurrentElements() {
        if (!elements.isEmpty()) {
            if (index > loaded) {
                // Load next required part of data
                filter.getDataLimit().setFirstResult(filter.getDataLimit().getFirstResult() + elementsOutput);
                loadPart();
                loaded++;
            }
            return elements.subList(from(), to());
        }
        return elements;
    }

    @Override
    public List<ContractorProductTOForReport> getAllElements() {
        return elements;
    }


    /* Util methods
    ------------------------------------------------------------------------------------------------------------------*/
    private void loadPart() {
        try {
            List<ContractorProductTOForReport> contractorsProducts = SpringServiceContext.getInstance()
                    .getContractorProductService().getListByFilter(filter);

            // Keep original values of some variables
            // TODO: It's bad to do here :( Think about it!
            for (ContractorProductTOForReport contractorProduct : contractorsProducts) {
                contractorProduct.setOriginalWholesalePrice(new VariantPrice(contractorProduct.getWholesalePrice().getPrice()));
                contractorProduct.setOriginalRetailPrice(new VariantPrice(contractorProduct.getRetailPrice().getPrice()));
                contractorProduct.setOriginalCurrency(contractorProduct.getCurrency());
            }
            elements.addAll(contractorsProducts);
        } catch (Exception e) {
            LoggingFacade.logError(StringUtils.buildString(this.getClass().getSimpleName(), ".loadPart - Failed loading data"), e);
        }
    }

    private int maxIndex() {
        if (elementsCount == 0) {
            return 1;
        }
        return (int) (elementsCount % elementsOutput == 0
                ? elementsCount / elementsOutput
                : elementsCount / elementsOutput + 1);
    }

    public int from() {
        return (index - 1) * elementsOutput;
    }

    public int to() {
        return (index * elementsOutput > elementsCount)
                ? elements.size()
                : index * elementsOutput;
    }

    public long getAllElementsCount() {
        return elementsCount;
    }
}
