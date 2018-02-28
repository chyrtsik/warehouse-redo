/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao.generic.filter;


import com.artigile.warehouse.dao.generic.builder.QueryBuilderUtils;
import com.artigile.warehouse.dao.generic.filter.annotations.Criteria;
import com.artigile.warehouse.dao.generic.filter.annotations.Criterion;

import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.*;


/**
 * Base class for DataFilter implementations.
 *
 * @author ihar
 *         <p/>
 *         $Id$
 */
public abstract class AbstractDataFilter
        implements DataFilter {

    /**
     * Collection of criteria.
     */
    @Transient
    private transient Map<String, FilterCriterion> criteria;

    /**
     * Unique key. Used for building a query to define unique parameter names.
     */
    private String uniqueKey;

    /**
     * Gets collection of criteria for filter.
     *
     * @return the collection of criteria
     */
    public List<FilterCriterion> getCriteria() {
        initializeCriteriaMap();
        Collection<FilterCriterion> criterions = criteria.values();
        return new ArrayList<FilterCriterion>(criterions);
    }

    /**
     * Initializes criteria map.
     */
    private void initializeCriteriaMap() {
        criteria = getCriteriaByReflection();
        criteria = removeInactive(criteria);
    }

    /**
     * Gets initialized criteria map.
     */
    private Map<String, FilterCriterion> getCriteriaMap() {
        initializeCriteriaMap();
        return criteria;
    }

    /**
     * Removes inactive criteria from the map.
     *
     * @param criteria criteria map.
     */
    private Map<String, FilterCriterion> removeInactive(Map<String, FilterCriterion> criteria) {
        Map<String, FilterCriterion> result = new HashMap<String, FilterCriterion>();
        Set<String> keys = criteria.keySet();
        for (String key : keys) {
            FilterCriterion criterion = criteria.get(key);
            //allow null value only in null-check (IS_NULL, IS_NOT_NULL) operations
            if (criterion != null
                    && (criterion.getOperation().isNullCkeck() || criterion.getValue() != null)) {
                result.put(key, criterion);
            }
        }
        return result;
    }

    /**
     * Gets collection of criteria by reflection.
     *
     * @return the collection of criteria
     * @throws IllegalAccessException if method fails
     */
    private Map<String, FilterCriterion> getCriteriaByReflection() {
        try {
            Map<String, FilterCriterion> result = new HashMap<String, FilterCriterion>();
            Class<?> clazz = this.getClass();
            do {
                result.putAll(extractFields(clazz));
                clazz = clazz.getSuperclass();
            } while (clazz != null);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Can't get Criteria by reflection.", e);
        }
    }


    /**
     * Extracts annotated fields of the class to criteria.
     */
    private Map<String, FilterCriterion> extractFields(Class<?> clazz)
            throws IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        Map<String, FilterCriterion> result = new HashMap<String, FilterCriterion>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Criteria.class)) {
                Criteria annotation = field.getAnnotation(Criteria.class);
                Criterion[] criteria = annotation.value();
                for (Criterion criterion : criteria) {
                    extractFilterCriterion(criterion, field, result);
                }
            } else if (field.isAnnotationPresent(Criterion.class)) {
                Criterion criterion = field.getAnnotation(Criterion.class);
                extractFilterCriterion(criterion, field, result);
            }
        }
        return result;
    }

    /**
     * Extracts filter criterion and puts it into collection.
     */
    private void extractFilterCriterion(Criterion criterion, Field field, Map<String,
            FilterCriterion> result) throws IllegalAccessException {

        FilterCriterion filterCriterion = createFilterCriterion(criterion, field);

        String name = filterCriterion.getName();
        if (result.containsKey(name)) {
            throw new IllegalStateException("Invalid Criterion annotation definition." +
                    "No duplicate criterion names allowed.");
        }

        result.put(name, filterCriterion);
    }

    /**
     * Creates filter criterion.
     */
    private FilterCriterion createFilterCriterion(Criterion criterion, Field field)
            throws IllegalAccessException {
        if (!isAnnotationValid(field, criterion)) {
            throw new IllegalStateException("Invalid Criterion annotation definition.");
        }

        String column = criterion.column();

        if (column.trim().length() == 0) {
            column = field.getName();
        }

        String name = criterion.name();

        if (name.trim().length() == 0) {
            name = field.getName();
        }

        field.setAccessible(true);
        return new FilterCriterion(name, column, criterion.expression(), field.get(this));
    }

    /**
     * Validate Criterion definition.
     *
     * @param field      the field instance
     * @param annotation the Criterion annotation instance
     * @return {@code true} if Criterion definition is valid, in other case
     *         returns {@code false}
     */
    private boolean isAnnotationValid(Field field, Criterion annotation) {
        boolean result = true;
        boolean isCollection = Collection.class.isAssignableFrom(field.getType());
        Expression expression = annotation.expression();

        switch (expression) {
            case LIKE:
            case CONTAINS:
            case STARTS_WITH:
            case ENDS_WITH:
                result = String.class.equals(field.getType());
                break;
            case IN:
            case NOT_IN:
                result = isCollection;
                break;
            case EQ:
            case NOT_EQ:
            case GE:
            case GT:
            case LE:
            case LT:
            case IS_NULL:
            case IS_NOT_NULL:
                result = !isCollection;
                break;
            default:
                throw new IllegalStateException("Illegal Criterion expression.");
        }

        return result;
    }

    /**
     * Converts current filter to string, using criteria. Format (for each
     * criterion): ${criterion.name}=${criterion.value};
     *
     * @return String representation.
     */
    @SuppressWarnings("unchecked")
    @Override
    public String toString() {
        List<FilterCriterion> criteria = new ArrayList(getCriteriaByReflection().values());
        StringBuilder result = new StringBuilder();

        for (FilterCriterion criterion : criteria) {
            result.append(criterion.getColumn());
            result.append("=");
            result.append(criterion.getValue());
            result.append(";");
        }
        return result.toString();
    }


    /**
     * Retrieves query segment for criterion.
     *
     * @param criteriaName Name of the criteria.
     * @return query segment
     */
    protected String getQuery(String criteriaName) {
        String result = null;
        FilterCriterion criterion = getCriteriaMap().get(criteriaName);
        if (criterion != null) {
            result = criterion.buildQuerySegment(uniqueKey);
        }
        return result;
    }

    /**
     * Default implementation. Combines queries for all criteria in the filter
     * into one using AND.
     *
     * @return query segment
     */
    public String buildQueryCondition() {
        List<FilterCriterion> criteria = getCriteria();
        List<String> criteriaQueries = new ArrayList<String>();
        for (FilterCriterion criterion : criteria) {
            criteriaQueries.add(criterion.buildQuerySegment(uniqueKey));
        }
        return AND(criteriaQueries);
    }

    /**
     * Gets a value of 'uniqueKey' property.
     *
     * @return the value of 'uniqueKey' property
     */
    public String getUniqueKey() {
        return uniqueKey;
    }

    /**
     * Sets a new value for 'uniqueKey' property.
     *
     * @param uniqueKey the new value for 'uniqueKey' property
     */
    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    /**
     * Combines a number of query segments into one using AND.
     *
     * @param queries Query segments.
     * @return query
     */
    protected String AND(Collection<String> queries) {
        return QueryBuilderUtils.AND(queries);
    }

    /**
     * Combines a number of query segments into one using AND.
     *
     * @param queries Query segments.
     * @return query
     */
    protected String AND(String... queries) {
        return QueryBuilderUtils.AND(queries);
    }

    /**
     * Combines a number of query segments into one using AND.
     *
     * @param queries Query segments.
     * @return query
     */
    protected String OR(Collection<String> queries) {
        return QueryBuilderUtils.OR(queries);
    }

    /**
     * Combines a number of query segments into one using AND.
     *
     * @param queries Query segments.
     * @return query
     */
	protected String OR(String...queries) {
		return QueryBuilderUtils.OR(queries);
	}
}
