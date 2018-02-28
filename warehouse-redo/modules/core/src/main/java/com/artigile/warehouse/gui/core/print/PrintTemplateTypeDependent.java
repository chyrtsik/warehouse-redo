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

import com.artigile.warehouse.domain.printing.PrintTemplateType;

/**
 * Interface implemented by printable objects those content is dependent on selected print template type.
 * @author Aliaksandr Chyrtsik
 * @since 13.05.13
 */
public interface PrintTemplateTypeDependent {
    /**
     * Called to prepare printable object to support selected print template type.
     * @param templateType template type to be used.
     * @return object to be used for print instead of current one.
     */
    Object prepareForTemplateType(PrintTemplateType templateType);
}
