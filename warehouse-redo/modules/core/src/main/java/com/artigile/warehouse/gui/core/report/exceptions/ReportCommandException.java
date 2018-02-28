/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.exceptions;

/**
 * @author Shyrik, 20.12.2008
 */

/**
 * Base class for all exception, that take place during executing any report commands.
 */
public class ReportCommandException extends Exception {
    public ReportCommandException(String message){
        super(message);
    }

    public ReportCommandException(Exception cause){
        super(cause.getMessage(), cause);
    }
}
