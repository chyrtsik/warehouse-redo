/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.print;

/**
 * Interface implemented by printable object that required initialization. Print cannot be continues if
 * initialization failed.
 * @author Aliaksandr Chyrtsik
 * @since 14.05.13
 */
public interface PrintTemplateRequiringInitialization {
    /**
     * Called by printing framework to initialize printable object.
     * @return result of initialization. Print cannot proceed if initialization returned false.
     */
    boolean initialize();
}
