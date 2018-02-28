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

public interface WildcardSupport {
    /**
     * Gets the quantifier that indicates there is zero or one of the preceding element. Usually '?', the question mark is used for this quantifier.
     * For example, colou?r matches both "color" and "colour".
     *
     * @return the quantifier that indicates there is zero or one of the preceding element.
     */
    char getZeroOrOneQuantifier();

    /**
     * Gets the quantifier that indicates there is zero or more of the preceding element. Usually '*', the asterisk is used for this quantifier.
     * For example, ab*c matches "ac", "abc", "abbc", "abbbc", and so on.
     *
     * @return the quantifier that indicates there is zero or more of the preceding element.
     */
    char getZeroOrMoreQuantifier();

    /**
     * Gets the quantifier that indicates there is one or more of the preceding element. Usually '+', the plus sign is used for this quantifier.
     * For example, ab+c matches "abc", "abbc", "abbbc", and so on, but not "ac".
     *
     * @return the quantifier that indicates there is one or more of the preceding element.
     */
    char getOneOrMoreQuantifier();

    /**
     * Converts a string with wildcards to a regular express that is compatible with {@link java.util.regex.Pattern}.
     * If the string has no wildcard, the same string will be returned.
     *
     * @param s a string with wildcards.
     * @return a regular express that is compatible with {@link java.util.regex.Pattern}.
     */
    String convert(String s);
}
