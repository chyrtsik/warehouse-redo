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

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
/**
 * Holds a number of utility methods for building queries.
 * 
 * @author ihar
 *  
 * Date: 04.06.2008 15:30:58
 * 
 * $Id$
 *
 */
public final class QueryBuilderUtils {
	/**
	 * AND operator.
	 * */
	private static final String AND_OPERATOR = "AND";
	
	/**
	 * OR operator.
	 * */
	private static final String OR_OPERATOR = "OR";
	
	/**
	 * Default constructor.
	 * */
	private QueryBuilderUtils() {
		
	}

	/**
	 * Applies specified operator to the queries.
	 * 
	 * @param operator 
	 * 			The operator.
	 * @param queries
	 * 			List of queries.
	 */
	private static String applyOperator(String operator, Collection<String> queries) {
		// 1. Get active (not empty) queries
		List<String> activeQueries = new ArrayList<String>();
		for (String query : queries) {
			if (!StringUtils.isEmpty(query)) {
				activeQueries.add(query);
			}
		}
		
		// 2. If there are no active queries than quit.
		if (activeQueries.size() == 0) {
			return "";
		}
				
		// 3. Combine queries into one
		StringBuffer result = new StringBuffer();
		if (activeQueries.size() == 1) {
			result.append(activeQueries.get(0));
		} else if (activeQueries.size() > 1) {
			result.append("(");
			final int count = activeQueries.size() - 1;
			for (int i = 0; i < count; i++) {
				result.append(activeQueries.get(i));
				result.append(" ");
				result.append(operator);
				result.append(" ");
			}
			result.append(activeQueries.get(activeQueries.size() - 1));
			result.append(")");
		}		
		return result.toString();
	}
	
	/**
	 * Combines a number of query segments into one using AND.
	 * 
	 * @param queries
	 * 			Query segments.
	 * @return query
	 * */
	public static String AND(String...queries) {
		return applyOperator(AND_OPERATOR, Arrays.asList(queries));
	}
	
	/**
	 * Combines a number of query segments into one using AND.
	 * 
	 * @param queries
	 * 			Query segments.
	 * @return query
	 * */
	public static String AND(Collection<String> queries) {
		return applyOperator(AND_OPERATOR, queries);
	}

	/**
	 * Combines a number of query segments into one using OR.
	 * 
	 * @param queries
	 * 			Query segments.
	 * @return query
	 * */
	public static String OR(String...queries) {
		return applyOperator(OR_OPERATOR, Arrays.asList(queries));
	}
	
	/**
	 * Combines a number of query segments into one using OR.
	 * 
	 * @param queries
	 * 			Query segments.
	 * @return query
	 * */
	public static String OR(Collection<String> queries) {
		return applyOperator(OR_OPERATOR, queries);
	}
}
