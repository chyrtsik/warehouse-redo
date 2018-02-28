/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao;

import com.artigile.warehouse.dao.generic.EntityDAO;
import com.artigile.warehouse.domain.dataimport.StoredFile;

import java.io.InputStream;

/**
 * @author Aliaksandr.Chyrtsik, 14.07.11
 */
public interface StoredFileDAO extends EntityDAO<StoredFile> {

    StoredFile storeFileStream(String fileName, InputStream inputStream);

    InputStream getStoredFileDataStream(long storedFileId);

}
