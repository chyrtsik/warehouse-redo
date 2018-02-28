/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao.generic.filter.annotations;




import com.artigile.warehouse.dao.generic.filter.Expression;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Criterion annotation definition.
 *
 * @author ihar
 *         <p/>
 *         $Id$
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Criterion {

    /**
     * Criterion name.
     */
    String name() default "";

    /**
     * Column name.
     */
    String column() default "";

    /**
     * Expression type.
     */
    Expression expression();
}
