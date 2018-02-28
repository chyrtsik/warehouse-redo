/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.filter;

/**
 * @author Valery Barysok, 7/10/11
 */

public class DefaultWildcardSupport extends AbstractWildcardSupport {
    @Override
    public char getZeroOrOneQuantifier() {
        return '?';
    }

    @Override
    public char getZeroOrMoreQuantifier() {
        return '*';
    }

    @Override
    public char getOneOrMoreQuantifier() {
        return '+';
    }
}
