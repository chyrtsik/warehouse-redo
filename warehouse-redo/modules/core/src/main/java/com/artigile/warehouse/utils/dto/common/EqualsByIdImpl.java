/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.utils.dto.common;

/**
 * @author Shyrik, 11.03.2009
 */

/**
 * Implementation of comparation objects by their id field.
 * <P><STRONG>Important:</STRONG> zero (0) values are treated as null values and
 * two objects with id = 0 are not considered to be equal. You should provide your out mechanism
 * for comparison objects with id = 0. For example, by name of by other similar field.   
 */
public abstract class EqualsByIdImpl {
    public abstract long getId();

    public boolean equals(Object obj){
        if (super.equals(obj)){
            return true;
        }
        else if (obj instanceof EqualsByIdImpl){
            EqualsByIdImpl idObject = (EqualsByIdImpl)obj;
            long firstId = getId();
            long secondId = idObject.getId();
            return !(firstId == 0 || secondId == 0) && firstId == secondId;
        }
        return false;
    }
}
