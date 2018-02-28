/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.style.predefined;

import com.artigile.warehouse.gui.core.report.style.Alignment;
import com.artigile.warehouse.gui.core.report.style.Style;
import com.artigile.warehouse.gui.core.report.style.StyleFactory;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class RightMiddleAlignStyleFactory implements StyleFactory {

    /**
     * Singleton instance
     */
    private static RightMiddleAlignStyleFactory instance;

    private static final Style rightCenterAlignStyle = new Style(null, null, Alignment.RIGHT, Alignment.CENTER);


    private RightMiddleAlignStyleFactory() { /* Silence is gold */ }

    public static RightMiddleAlignStyleFactory getInstance() {
        return instance == null ? new RightMiddleAlignStyleFactory() : instance;
    }


    @Override
    public Style getStyle(Object rowData) {
        return rightCenterAlignStyle;
    }
}
