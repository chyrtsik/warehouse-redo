/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi;

/**
 * Context of the import (holds all information required by the import to be performed).
 *
 * @author Valery Barysok, 6/7/11
 */

public interface DataImportContext {
    /**
     * @return should return configuration used for current import.
     */
    public DataAdapterConfiguration getConfiguration();

    /**
     * @return should return component used for imported data saving.
     */
    public DataSaver getDataSaver();

    /**
     * @return should return import progress listener.
     */
    public DataImportProgressListener getImportProgressListener();
}
