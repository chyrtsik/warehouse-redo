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

import com.artigile.warehouse.adapter.spi.DataImportContext;
import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.dataimport.DataImportListener;
import com.artigile.warehouse.bl.dataimport.DataImportService;
import com.artigile.warehouse.dao.ContractorPriceImportDAO;
import com.artigile.warehouse.domain.priceimport.ContractorPriceImport;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.transofmers.ContractorPriceImportTransformer;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service for working with contractor price imports.
 *
 * @author Valery.Barysok
 */
@Transactional(rollbackFor = BusinessException.class)
public class ContractorPriceImportService {

    private ContractorPriceImportDAO priceImportDAO;

    private DataImportService dataImportService;

    public ContractorPriceImportService() {
    }

    public void initialize(){
        //Register import listener to synchronize results of price list import with related data import.
        dataImportService.addDataImportListener(new DataImportListener() {
            @Override
            public void onBeforeCompleteImport(long dataImportId, DataImportContext importContext) throws BusinessException {
                ContractorPriceImport priceImport = priceImportDAO.get(dataImportId);
                if (priceImport != null) {
                    //When contractor price import is completed store total count of items imported.
                    ContractorProductDataSaver dataSaver = (ContractorProductDataSaver) importContext.getDataSaver();
                    priceImport.setItemCount(dataSaver.getImportedProductsCount());
                    priceImport.setIgnoredItemCount(dataSaver.getIgnoredProductsCount());
                }
            }

            @Override
            public void onAfterCompleteImport(long dataImportId, DataImportContext importContext) throws BusinessException {
                ContractorPriceImport priceImport = priceImportDAO.get(dataImportId);
                if (priceImport != null) {
                    //Deleting old contractor products data order import is done.
                    priceImportDAO.removePrevContractorProducts(dataImportId);
                }
            }
        });
    }

    public List<ContractorPriceImportTO> getListByFilter(PriceImportFilter filter) {
        if (filter != null) {
            return ContractorPriceImportTransformer.transformList(priceImportDAO.getListByFilter(filter));
        } else {
            return ContractorPriceImportTransformer.transformList(priceImportDAO.getAll());
        }
    }

    public void savePriceImportWithFlush(ContractorPriceImportTO priceImport) {
        ContractorPriceImport persistentPriceImport = priceImportDAO.get(priceImport.getId());
        if (persistentPriceImport == null) {
            //Initialize new price list import.
            persistentPriceImport = new ContractorPriceImport();
            dataImportService.initializeImport(persistentPriceImport);
        }

        ContractorPriceImportTransformer.update(persistentPriceImport, priceImport);
        priceImportDAO.save(persistentPriceImport);
        priceImportDAO.flush();
        priceImportDAO.refresh(persistentPriceImport);
        ContractorPriceImportTransformer.update(priceImport, persistentPriceImport);
    }

    public void updatePriceImport(ContractorPriceImportTO priceImportTO) {
        ContractorPriceImport priceImport = priceImportDAO.get(priceImportTO.getId());
        ContractorPriceImportTransformer.update(priceImport, priceImportTO);
        priceImportDAO.update(priceImport);
    }

    public void deletePriceImport(ContractorPriceImportTO priceImport) throws BusinessException {
        priceImportDAO.remove(priceImportDAO.get(priceImport.getId()));
    }

    /**
     * Deleted all previously imported products for the contractor specified for this import.
     * @param priceImport import which contractor's older data should be deleted.
     */
    public void deletePrevContractorProducts(ContractorPriceImportTO priceImport) {
        priceImportDAO.removePrevContractorProducts(priceImport.getId());
    }

    /**
     * Performs new price list import.
     * @param priceImportTO price list import data (properties of new import).
     */
    public void performPriceListImport(ContractorPriceImportTO priceImportTO){
        savePriceImportWithFlush(priceImportTO);
        dataImportService.performDataImportInBackground(
                priceImportTO.getId(),
                new ContractorProductDataSaver(priceImportTO),
                I18nSupport.message("price.import.display.name.format", priceImportTO.getContractor().getName())
        );
    }

    /**
     * Performs rollback of the contractor price list to the data specified in the given import.
     * @param priceImportToBeUsed price list import to be used to replace current contractor price list.
     */
    public void rollbackPriceListToImport(ContractorPriceImportTO priceImportToBeUsed){
        //To perform rolling back action we just create and launch new import using data imported
        //before as a source for new import.
        ContractorPriceImportTO newPriceImport = priceImportToBeUsed.createNewAsCopy();
        newPriceImport.setDescription(I18nSupport.message("price.import.rollback.description", StringUtils.formatDateTime(priceImportToBeUsed.getImportDate())));
        savePriceImportWithFlush(newPriceImport);
        dataImportService.performDataImportInBackground(
                newPriceImport.getId(), priceImportToBeUsed.getId(),
                new ContractorProductDataSaver(newPriceImport),
                I18nSupport.message("price.import.display.name.format", newPriceImport.getContractor().getName())
        );
    }

    public ContractorPriceImport getById(long priceImportId) {
        return priceImportDAO.get(priceImportId);
    }

    /**
     * @param priceImportList List of imports
     * @return List, grouped by contractors with last import for each
     */
    public List<ContractorPriceImportTO> filterLastContractorPriceImports(List<ContractorPriceImportTO> priceImportList) {
        Map<Long, ContractorPriceImportTO> contractorPriceImportMap = new LinkedHashMap<Long, ContractorPriceImportTO>();
        for (ContractorPriceImportTO priceImport : priceImportList) {
            Long currentContractorID = priceImport.getContractor().getId();
            if (!contractorPriceImportMap.containsKey(currentContractorID)) {
                contractorPriceImportMap.put(currentContractorID, priceImport);
            } else {
                Date currentImportDatetime = priceImport.getImportDate();
                Date addedImportDatetime = contractorPriceImportMap.get(currentContractorID).getImportDate();
                if (currentImportDatetime.after(addedImportDatetime)) {
                    contractorPriceImportMap.put(currentContractorID, priceImport);
                }
            }
        }
        return new ArrayList<ContractorPriceImportTO>(contractorPriceImportMap.values());
    }

    /**
     * @return All imports, grouped by contractors
     */
    public List<ContractorPriceImport> getImportsGroupedByContractors() {
        return priceImportDAO.getListGroupedByContractorID();
    }


    //=============================== Spring setters ================================
    public void setPriceImportDAO(ContractorPriceImportDAO priceImportDAO) {
        this.priceImportDAO = priceImportDAO;
    }

    public void setDataImportService(DataImportService dataImportService) {
        this.dataImportService = dataImportService;
    }
}
