/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.baselayout;

/**
 * @author Valery Barysok, 10/18/11
 */

public interface ReportComponentListener {

    void componentOpened();

    void componentClosed();

    void componentShowing();

    void componentHidden();

    void componentActivated();

    void componentDeactivated();
}
