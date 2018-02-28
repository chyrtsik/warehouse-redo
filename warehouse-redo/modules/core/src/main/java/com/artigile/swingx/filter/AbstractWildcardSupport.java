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

public abstract class AbstractWildcardSupport implements WildcardSupport {

    public String convert(String s) {
        // if it doesn't have the two special characters we support, we don't need to use regular expression.
        char zeroOrMoreQuantifier = getZeroOrMoreQuantifier();
        char zeroOrOneQuantifier = getZeroOrOneQuantifier();
        char oneOrMoreQuantifier = getOneOrMoreQuantifier();

        StringBuilder buffer = new StringBuilder();
        int length = s.length();
        for (int i = 0; i < length; i++) {
            char prevChar = i > 0 ? s.charAt(i-1) : 0;
            char currentChar = s.charAt(i);
            char nextChar = i < length-1 ? s.charAt(i + 1) : 0;
            if (zeroOrOneQuantifier != 0 && currentChar == zeroOrOneQuantifier && prevChar != '\\') {
                buffer.append(".");
            }
            else if (zeroOrMoreQuantifier != 0 && currentChar == zeroOrMoreQuantifier && prevChar != '\\') {
                buffer.append(".*");
            }
            else if (oneOrMoreQuantifier != 0 && currentChar == oneOrMoreQuantifier && prevChar != '\\') {
                buffer.append(".+");
            }
            else if (currentChar == '\\'){
                if (zeroOrOneQuantifier != 0 && nextChar == zeroOrOneQuantifier ||
                    zeroOrMoreQuantifier != 0 && nextChar == zeroOrMoreQuantifier ||
                    oneOrMoreQuantifier != 0 && nextChar == oneOrMoreQuantifier)
                {
                    //Skip backslash. Wildcard character is not replaced by regex sequence.
                    //If wildcard character is a special regex character it will be escapes in the next iteration.
                }
                else{
                    //Escape backslash.
                    buffer.append("\\\\");
                }
            }
            else if ("(){}[].^$*?+|".indexOf(currentChar) != -1) {
                // escape all other regex special characters
                buffer.append('\\');
                buffer.append(currentChar);
            }
            else {
                buffer.append(currentChar);
            }
        }

        return buffer.toString();
    }
}
