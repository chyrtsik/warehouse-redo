/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.detail;

import com.artigile.warehouse.utils.dto.details.DetailBatchOperationTO;

import java.util.Date;
import java.util.List;

/**
 * @author Aliaksandr.Chyrtsik, 03.10.11
 */
public interface DetailBatchHistoryService {
    List<DetailBatchOperationTO> getDetailBatchHistoryForReport(Date periodStart, Date periodEnd, DetailBatchHistoryFilter filter);
}
