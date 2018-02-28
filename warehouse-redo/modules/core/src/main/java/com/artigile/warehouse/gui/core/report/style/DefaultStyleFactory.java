/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.report.style;

/**
 * @author Shyrik, 21.05.2009
 */

/**
 * Default style factory (no style decorations). SINGLETON.
 */
public class DefaultStyleFactory implements StyleFactory {
    private Style style = new Style();
    private static DefaultStyleFactory instance;

    @Override
    public Style getStyle(Object rowData) {
        return style;
    }

    private DefaultStyleFactory(){
    }

    public static StyleFactory getInstance(){
         if (instance == null){
             instance = new DefaultStyleFactory();
         }
        return instance;
    }
}
