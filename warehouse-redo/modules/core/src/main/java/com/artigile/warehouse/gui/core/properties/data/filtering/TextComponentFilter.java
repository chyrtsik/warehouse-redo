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

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * @author: Vadim.Zverugo 05.10.2010
 */

/**
 * Filtering of text in the text component
 */
public abstract class TextComponentFilter extends PlainDocument {

    /**
     * Will be override in the time of creation new developer filter.
     * @param text
     * @return
     */
    public abstract String filter(String text);

    @Override
    public final void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offs, filter(str), a);
    }
}
