/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.transofmers;

import com.artigile.warehouse.domain.dataimport.StoredFile;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.template.StoredFileTO;

/**
 * @author Aliaksandr Chyrtsik
 * @since 08.05.13
 */
public final class StoredFileTransformer {
    private StoredFileTransformer(){
    }

    public static StoredFileTO transform(StoredFile storedFile) {
        if (storedFile == null){
            return null;
        }
        StoredFileTO storedFileTO = new StoredFileTO();
        update(storedFileTO, storedFile);
        return storedFileTO;
    }

    private static void update(StoredFileTO storedFileTO, StoredFile storedFile) {
        storedFileTO.setId(storedFile.getId());
        storedFileTO.setName(storedFile.getName());
        storedFileTO.setStoreDate(storedFile.getStoreDate());
    }

    public static StoredFile transform(StoredFileTO storedFileTO) {
        return (storedFileTO == null) ? null : SpringServiceContext.getInstance().getStoredFileService().findStoredFile(storedFileTO.getId());
    }
}
