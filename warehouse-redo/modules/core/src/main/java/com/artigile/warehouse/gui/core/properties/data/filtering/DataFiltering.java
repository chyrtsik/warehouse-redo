/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.data.filtering;

import javax.swing.text.JTextComponent;

/**
 * @author Vadim.Zverugo, 02.10.2010
 */

/**
 *  Limitation length of value in the fields and data filtering. 
 */
public class DataFiltering {
    /**
     * Set maximal length of text in the text component.
     * @param textComponent
     * @param maxLength
     * @see JTextComponent
     */
    public static void setTextLengthLimit(JTextComponent textComponent, int maxLength) {
        setTextFilter(textComponent, new DefaultTextMaxLength(maxLength));
    }

    /**
     * Set filter for text component.
     * @param textComponent
     * @param textComponentFilter
     * @see JTextComponent
     */
    public static void setTextFilter(JTextComponent textComponent, TextComponentFilter textComponentFilter) {
        textComponent.setDocument(textComponentFilter);
    }
}
