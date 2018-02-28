/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.bl.common.listeners;

/**
 * @author Shyrik, 10.03.2009
 */

/**
 * Interface of transformer entity into another (may be DTO) representation.
 */
public interface EntityTransformer {
    /**
     * Should transform entity into corresponding object (DTO, for example), or return entity
     * inself, it needn't to be transformed.
     * @param entity - entity to be transformed.
     * @return
     */
    Object transform(Object entity);
}
