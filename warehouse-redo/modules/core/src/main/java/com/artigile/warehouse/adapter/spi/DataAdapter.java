/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi;

/**
 * Interface of data adapter. For different sources of data different implementations should be provided.
 * But interface should remain the same.
 *
 * @author Valery Barysok, 6/7/11
 */

public interface DataAdapter {
    /**
     * Performs import of data. All data related to the import is held in the import context.
     * @param context all information about import to be performed.
     */
    public void importData(DataImportContext context);
}
