/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.dao.generic;

import com.artigile.warehouse.dao.generic.builder.QueryBuilder;
import com.artigile.warehouse.dao.generic.builder.SimpleQueryBuilder;
import com.artigile.warehouse.dao.generic.filter.*;
import com.artigile.warehouse.dao.generic.util.ReflectionUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;

import java.io.Serializable;
import java.util.*;

/**
 * @author IoaN, 22.11.2008
 */
@SuppressWarnings({"unchecked"})
public abstract class GenericEntityDAO<T> implements EntityDAO<T> {

    private SessionFactory sessionFactory;
    
    /**
     * Entity class.
     */
    private Class<? extends T> entityClass = null;

    /**
     * Constructor.
     */
    public GenericEntityDAO() {
        getEntityClass();
    }

    /**
     * Constructor.
     *
     * @param entityClass the entity class
     */
    public GenericEntityDAO(Class<? extends T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Loads entity by given key.
     *
     * @param primaryKey Primary key of entity
     * @return Entity, if it was found
     */
    @Override
    public T get(Serializable primaryKey) {
        return get(entityClass, primaryKey);
    }

    /**
     * Load all entities of this type.
     *
     * @return List of entities
     */
    @Override
    public List<T> getAll() {
        return getAll(entityClass);
    }

    @Override
    public List<T> getAllSorted(String sortedField, boolean isAsc) {
        return getSession().createCriteria(entityClass).addOrder(isAsc ? Order.asc(sortedField) : Order.desc(sortedField)).list();
    }

    /**
     * Gets entity class.
     *
     * @return the entity class
     */
    @SuppressWarnings("unchecked")
    protected <K extends T> Class<K> getEntityClass() {

        if (entityClass == null) {

            entityClass = (Class<K>) ReflectionUtils.getEntityType(this);
        }

        return (Class<K>) entityClass;
    }

    /**
     * Find object of specified class by primary key.
     *
     * @param entityClass Class of the entity
     * @param primaryKey  Primary key
     * @return Entity for given key
     */
    @Override
    public T get(Class<? extends T> entityClass, Serializable primaryKey) {
        return (T) getSession().get(entityClass, primaryKey);
    }

    /**
     * Find objects of specified class by primary keys.
     *
     * @param primaryKeys - list of Primary keys
     * @return Entity for given key
     */
    @Override
    public List<T> getByIds(List<Serializable> primaryKeys) {
        List<T> resultList = new ArrayList<T>();
        for (Serializable serializable : primaryKeys) {
            resultList.add((T) getSession().get(entityClass, serializable));
        }
        return resultList;
    }

    /**
     * Load all entities of specified type.
     *
     * @param entityClass Class of the entity
     * @return List of entities
     */
    @Override
    public List<T> getAll(Class<? extends T> entityClass) {
        return (List<T>) getSession().createCriteria(entityClass).list();
    }

    /**
     * Load all entities of specified type by specified filter.
     *
     * @param filter Filter instance
     * @param pager  Pager instance
     * @param sorter Sorter instance
     * @return List of entities
     */
    @Deprecated
    @Override
    public List<T> getByFilter(DataFilter filter,
                               PagingInfo pager, SortingInfo sorter) {

        List<DataFilter> filters = new ArrayList<DataFilter>();
        filters.add(filter);

        return getByFilters(filters, pager, sorter);
    }

    /**
     * Loads all entities of specified type by specified filters.
     *
     * @param filters Filter instances collection
     * @param pager   Pager instance
     * @param sorter  Sorter instance
     * @return List of entities
     */
    @SuppressWarnings({"HardCodedStringLiteral"})
    @Override
    public List<T> getByFilters(List<? extends DataFilter> filters, PagingInfo pager, SortingInfo sorter) {

        String sql = "from " + entityClass.getName();
        return findByFilters(sql, filters, pager, sorter);
    }

    /**
     * Loads all entities of specified type by specified filter.
     *
     * @param query  query string
     * @param filter Filter instances
     * @param pager  Pager instance
     * @param sorter Sorter instance
     * @return List of entities
     */
    @Deprecated
    public List<T> findByFilter(String query, DataFilter filter, PagingInfo pager,
                                SortingInfo sorter) {

        List<DataFilter> filters = new ArrayList<DataFilter>();
        filters.add(filter);

        return findByFilters(query, filters, pager, sorter);
    }

    /**
     * Loads all entities of specified type by specified filters.
     *
     * @param query   query string
     * @param filters Filter instances collection
     * @param pager   Pager instance
     * @param sorter  Sorter instance
     * @return List of entities
     */
    @SuppressWarnings("unchecked")
    public List<T> findByFilters(String query, List<? extends DataFilter> filters,
                                 PagingInfo pager, SortingInfo sorter) {

        QueryBuilder builder = getQueryBuilder(query, filters, sorter);
        Query hibernateQuery = createQuery(builder, filters);

        // Set page to fetch
        if (pager != null) {
            hibernateQuery.setFirstResult(pager.getStartIndex());
            hibernateQuery.setMaxResults(pager.getNumberOfRows());
        }
        List resultList;
        resultList = hibernateQuery.list();
        return resultList;
    }

    /**
     * Load all entities of specified type by specified filter.
     *
     * @param filter Filter instance
     * @return List of entities
     */
    @Deprecated
    @Override
    public List<T> getByFilter(DataFilter filter) {
        return getByFilter(filter, null, null);
    }

    /**
     * Load all entities of specified type by specified filters.
     *
     * @param filters Filter instance collection
     * @return List of entities
     */
    @Override
    public List<T> getByFilters(List<? extends DataFilter> filters) {
        return getByFilters(filters, null, null);
    }

    /**
     * Load all entities of specified type by specified filter.
     *
     * @param query  Query string
     * @param filter Filter instance
     * @return List of entities
     */
    @Deprecated
    public List<T> findByFilter(String query, DataFilter filter) {
        return findByFilter(query, filter, null, null);
    }

    /**
     * Load all entities of specified type by specified filters.
     *
     * @param query   Query string
     * @param filters Filter instance collection
     * @return List of entities
     */
    public List<T> findByFilters(String query,
                                 List<? extends DataFilter> filters) {
        return findByFilters(query, filters, null, null);
    }

    /**
     * Gets number of records that will be returned by query with specified
     * filter.
     *
     * @param filter the filter instance
     * @return the number of records
     */
    public Long getRecordsCount(DataFilter filter) {
        List<DataFilter> filters = new ArrayList<DataFilter>();
        filters.add(filter);

        return getRecordsCount(filters);
    }

    /**
     * Gets number of records that will be returned by query with specified
     * filter collection.
     *
     * @param filters the filter instance collection
     * @return the number of records
     */
    @SuppressWarnings({"HardCodedStringLiteral"})
    @Override
    public Long getRecordsCount(List<? extends DataFilter> filters) {
        QueryBuilder builder =
                getQueryBuilder("select count(*) from ", entityClass, filters, null);
        Query query = createQuery(builder, filters);
        return (Long) query.uniqueResult();
    }

    /**
     * Create Query using specific QueryBuilder and DataFilter.
     *
     * @param builder the QueryBuilder instance
     * @param filter  the DataFilter instance
     * @return the Query instance
     */
    protected Query createQuery(QueryBuilder builder, DataFilter filter) {

        List<DataFilter> filters = new ArrayList<DataFilter>();
        filters.add(filter);

        return createQuery(builder, filters);
    }

    /**
     * Create Query and set parameters using specific QueryBuilder and DataFilter's.
     *
     * @param builder the QueryBuilder instance
     * @param filters the DataFilter instance collection
     * @return the Query instance
     */
    @SuppressWarnings("unchecked")
    protected Query createQuery(QueryBuilder builder, List<? extends DataFilter> filters) {
        String builtQuery = builder.buildQuery();
        Query queryResult = getSession().createQuery(builtQuery);
        int counter = 1;
        if (filters != null) {
            for (DataFilter filter : filters) {
                if (filter != null) {
                    List<FilterCriterion> filterCriteriaList = filter.getCriteria();

                    String uniqueKey = filter.getClass().getSimpleName() + "_" + counter;
                    counter++;

                    setQueryParameters(queryResult, filterCriteriaList, uniqueKey);
                }
            }
        }

        return queryResult;
    }

    /**
     * Sets parameters to the query.
     *
     * @param query         The query.
     * @param criterionList List of criteria from filter.
     * @param uniqueKey     The unique filter id.
     */
    @SuppressWarnings("unchecked")
    private void setQueryParameters(Query query, List<FilterCriterion> criterionList,
                                    String uniqueKey) {
        // Inject parameters

        for (FilterCriterion aCriterionList : criterionList) {
            Object criteriaValue = aCriterionList.getValue();
            Expression operation = aCriterionList.getOperation();

            if (operation.isNullCkeck()) {
                //check for null does not require any parameters
            } else if (operation.isCollectionBased()) {
                Iterator parameters = ((Collection) criteriaValue).iterator();
                int collectionCounter = 1;
                while (parameters.hasNext()) {
                    query.setParameter(aCriterionList
                            .getQueryParameterName(uniqueKey, collectionCounter),
                            parameters.next());
                    collectionCounter++;
                }
            } else {
                Object value = aCriterionList.evaluateQueryParam(criteriaValue);
                query.setParameter(aCriterionList.getQueryParameterName(uniqueKey), value);
            }
        }
    }

    /**
     * Persist unsaved object.
     *
     * @param entity Entity to save
     */
    @Override
    public void persist(Object entity) {
        getSession().persist(entity);
    }

    /**
     * Saves unsaved object.
     *
     * @param entity Entity to save
     */
    @Override
    public long save(Object entity) {
        return (Long) getSession().save(entity);
    }

    /**
     * Merge detached object.
     *
     * @param entity the entity instance
     * @param <K>    the entity type
     * @return the merged entity instance
     */
    @Override
    public <K extends T> T merge(K entity) {
        T mergedObj = (T) getSession().merge(entity);
        getSession().flush();
        return mergedObj;

    }

    /**
     * Update detached object.
     *
     * @param entity Entity to update
     */
    @Override
    public void update(Object entity) {
        getSession().update(entity);
    }

    /**
     * Remove entity from the persistent store.
     *
     * @param entity Entity to remove
     */
    @Override
    public void remove(Object entity) {
        getSession().delete(entity);
    }

    /**
     * Remove entity by idfrom the persistent store.
     *
     * @param id the id of entity that should be removed.
     */
    @Override
    public void removeById(Serializable id) {
        getSession().delete(get(id));
    }

    /**
     * Gets a query builder for Entity.
     *
     * @param prefix      the HQL query prefix
     * @param entityClass the class of Entity
     * @param sortingInfo the sorting order type
     * @return the query builder for Entity
     */
    protected QueryBuilder getQueryBuilder(String prefix, Class<? extends T> entityClass, SortingInfo sortingInfo) {

        List<DataFilter> filters = new ArrayList<DataFilter>();

        return getQueryBuilder(prefix + entityClass.getName(), filters, sortingInfo);
    }

    /**
     * Gets a query builder for Entity.
     *
     * @param prefix      the HQL query prefix
     * @param entityClass the class of Entity
     * @param filters     the data filter collection
     * @param sortingInfo the sorting order type
     * @return the query builder for Entity
     */
    protected QueryBuilder getQueryBuilder(String prefix, Class<? extends T> entityClass,
                                           List<? extends DataFilter> filters, SortingInfo sortingInfo) {

        return getQueryBuilder(prefix + entityClass.getName(), filters, sortingInfo);
    }

    /**
     * Gets a query builder for Entity.
     *
     * @param query       the query string
     * @param filter      the data filter
     * @param sortingInfo the sorting order type
     * @return the query builder for Entity
     */
    protected QueryBuilder getQueryBuilder(String query, DataFilter filter,
                                           SortingInfo sortingInfo) {

        List<DataFilter> filters = new ArrayList<DataFilter>();
        filters.add(filter);

        return getQueryBuilder(query, filters, sortingInfo);
    }

    /**
     * Gets a query builder for Entity.
     *
     * @param query       the query string
     * @param filters     the data filter collection
     * @param sortingInfo the sorting order type
     * @return the query builder for Entity
     */
    protected QueryBuilder getQueryBuilder(String query, List<? extends DataFilter> filters,
                                           SortingInfo sortingInfo) {

        SimpleQueryBuilder builder = new SimpleQueryBuilder(query);

        // Add filters to builder
        builder.addFilters(filters);

        // Set sorter for builder
        if (sortingInfo != null) {
            Map<SortingInfo, String> sortExpressions = getSortExpressions();
            if (sortExpressions != null && sortExpressions.containsKey(sortingInfo)) {
                builder.setSortOrder(sortExpressions.get(sortingInfo));
            } else {
                builder.setSortOrder(sortingInfo);
            }
        }

        return builder;
    }

    /**
     * Get sorting expressions for a current DAO.
     *
     * @return Map with sorting expression like "field1 asc, field2 desc"
     */
    protected Map<SortingInfo, String> getSortExpressions() {
        return null;
    }

    @Override
    public void saveOrUpdate(T entity) {
        getSession().saveOrUpdate(entity);
        getSession().flush();
    }

    @Override
    public void flush() {
        getSession().flush();
    }

    @Override
    public void clear() {
        getSession().clear();
    }

    @Override
    public void refresh(Object entity) {
        getSession().refresh(entity);
    }
    
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
