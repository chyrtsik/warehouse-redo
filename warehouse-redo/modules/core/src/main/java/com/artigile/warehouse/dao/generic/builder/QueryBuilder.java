/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao.generic.builder;



/**
 * The HQL query builder API definition.
 *
 * @author ihar
 * 
 * $Id$
 */
public interface QueryBuilder {
	
    /**
     * Builds a query string using HQL query language.
     *
     * @return the query string
     */
    String buildQuery();
}
