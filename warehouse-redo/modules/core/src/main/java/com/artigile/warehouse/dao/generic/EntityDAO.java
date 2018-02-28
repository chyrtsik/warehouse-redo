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

import com.artigile.warehouse.dao.generic.filter.DataFilter;
import com.artigile.warehouse.dao.generic.filter.PagingInfo;
import com.artigile.warehouse.dao.generic.filter.SortingInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author IoaN, 22.11.2008
 */
public interface EntityDAO<T> {

    /**
     * Find object by primary key.
     *
     * @param primaryKey Primary key
     * @return Entity for given key
     */
    T get(Serializable primaryKey);

    /**
     * Find object of specified class by primary key.
     *
     * @param entityClass Class of the entity
     * @param primaryKey  Primary key
     * @return Entity for given key
     */
    T get(Class<? extends T> entityClass, Serializable primaryKey);

    /**
     * Find objects of specified class by primary keys.
     *
     * @param primaryKeys - list of Primary keys
     * @return Entity for given key
     */
    public List<T> getByIds(List<Serializable> primaryKeys);

    /**
     * Load all entities of this type.
     *
     * @return List of entities
     */
    List<T> getAll();

    /**
     * Load all entities of this type, sorted by specified field.
     *
     * @param sortedField hte sorted field.
     * @param isAsc sorted will be ascending if {@code isAsc} true. othewise sorting will be descending.
     * @return List of entities
     */
    List<T> getAllSorted(String sortedField,boolean isAsc);

    /**
     * Load all entities of specified type.
     *
     * @param entityClass Class of the entity
     * @return List of entities
     */
    List<T> getAll(Class<? extends T> entityClass);

    /**
     * Load all entities of specified type by specified filter.
     *
     * @param filter Filter instance
     * @param pager  Pager instance
     * @param sorter Sorter instance
     * @return List of entities
     */
    List<T> getByFilter(DataFilter filter, PagingInfo pager, SortingInfo sorter);

    /**
     * Load all entities of specified type by specified filters.
     *
     * @param filters Filter instance collection
     * @param pager   Pager instance
     * @param sorter  Sorter instance
     * @return List of entities
     */
    List<T> getByFilters(List<? extends DataFilter> filters, PagingInfo pager, SortingInfo sorter);

    /**
     * Load all entities of specified type by specified filter.
     *
     * @param filter Filter instance
     * @return List of entities
     */
    List<T> getByFilter(DataFilter filter);

    /**
     * Load all entities of specified type by specified filter collection.
     *
     * @param filters Filter instance collection
     * @return List of entities
     */
    List<T> getByFilters(List<? extends DataFilter> filters);

    /**
     * Gets number of records that will be returned by query with specified filters.
     *
     * @param filters the filter instance collection
     * @return the number of records
     */
    Long getRecordsCount(List<? extends DataFilter> filters);

    /**
     * Persist unsaved object.
     *
     * @param entity Entity to save
     */
    void persist(Object entity);

    /**
     * Save unsaved object.
     *
     * @param entity Entity to save
     */
    long save(Object entity);

    /**
     * Update detached object.
     *
     * @param entity Entity to update
     */
    void update(Object entity);


    /**
     * Merge detached object.
     *
     * @param entity Entity to save
     * @param <K>    the Entity type
     * @return Merged entity.
     */
    <K extends T> T merge(K entity);

    /**
     * Remove entity from the persistent store.
     *
     * @param entity Entity to remove
     */
    void remove(Object entity);

    /**
     * Remove entity from the persistent store by it's id.
     *
     * @param primaryKey key of entity to be removed
     */
    void removeById(Serializable primaryKey);


    void saveOrUpdate(T entity);

    void flush();

    void clear();

    void refresh(Object entity);
}
