/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.view;

import java.util.EventListener;
import java.util.EventObject;

/**
 * @author Borisok V.V., 27.12.2008
 */
public interface TableReportViewListener extends EventListener {
    public void refresh(EventObject eventObject);
}
