/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.custom.types;

import java.text.MessageFormat;

/**
 * @author Shyrik, 28.04.2009
 */

/**
 * This class is used for correct showing and sorting composit numbers in different lists.
 * It uses given mask to represent number, sub number, sub sub number, etc. When sorting, this
 * class compares itself fo another composine numbers according to given order of numbers and subnumbers.
 *
 * For example: composite numbers, that contatins from 2 numbers and mask "{0} [ {1} ]" will be shown
 * as "1 [ 10 ]", if major number is 1 and minor number is 10.
 *
 * <P><STRONG>It's import, that numbers in the mask are treated as follows</STRONG>
 * <UL>
 *   <LI>{0} - the most major number
 *   <LI>{1}, {2}, ... - the minor numbers. Number with greater value is consideren to be less major. 
 * </UL>
 * 
 * <P>When this number is beeing compared to another composite number, it compares more major numbers first.
 */
public class CompositeNumber implements Comparable {
    /**
     * Displayed view of the number.
     */
    private String displayValue;

    /**
     * Values of the composite number.
     */
    private Long[] values;

    public CompositeNumber(String mask, Long[] values) {
        this.displayValue = MessageFormat.format(mask, (Object[])values);
        this.values = values;
    }

    public String toString(){
        return displayValue;
    }

    @Override
    public int compareTo(Object obj) {
        if (obj == null){
            return -1;
        }

        CompositeNumber second = (CompositeNumber)obj;
        int minCount = Math.min(values.length, second.values.length);
        for (int i=0; i<minCount; i++){
            if (values[i] < second.values[i]){
                return -1;
            }
            else if (values[i] > second.values[i]){
                return 1;
            }
        }

        if (values.length < second.values.length){
            return -1;
        }
        else if (values.length > second.values.length){
            return 1;
        }

        return 0;
    }
}
