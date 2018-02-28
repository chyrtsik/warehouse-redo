/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.dataimport.DataImport;
import com.artigile.warehouse.utils.dto.dataimport.DataImportTO;

/**
 * @author Aliaksandr.Chyrtsik, 03.11.11
 */
public final class DataImportTransformer {
    private DataImportTransformer(){
    }

    public static void update(DataImportTO dataImportTO, DataImport dataImport) {
        dataImportTO.setId(dataImport.getId());
        dataImportTO.setImportDate(dataImport.getImportDate());
        dataImportTO.setImportStatus(dataImport.getImportStatus());
        dataImportTO.setUser(UserTransformer.transformUser(dataImport.getUser()));
        dataImportTO.setDescription(dataImport.getDescription());
        dataImportTO.setAdapterUid(dataImport.getAdapterUid());
        dataImportTO.setAdapterConf(dataImport.getAdapterConf());
    }

    public static void update(DataImport dataImport, DataImportTO dataImportTO) {
        dataImport.setDescription(dataImportTO.getDescription());
        dataImport.setAdapterUid(dataImportTO.getAdapterUid());
        dataImport.setAdapterConf(dataImportTO.getAdapterConf());
    }
}
