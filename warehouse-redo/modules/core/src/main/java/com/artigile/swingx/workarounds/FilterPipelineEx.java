/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.workarounds;

import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;

/**
 * There is implemented behaviour of antipattern known as "Pavlik Morozov" ;)
 * But what to make. It is impossible to receive the necessary information correctly
 *
 * @author Borisok V.V., 18.01.2009
 */
public class FilterPipelineEx extends FilterPipeline {

    private final Filter[] filters;

    public FilterPipelineEx() {
        this(new Filter[] {});
    }

    /**
     * FilterPipeline class makes own copy of filters
     * We store filters which received from a client
     * @param inList
     */
    public FilterPipelineEx(Filter... inList) {
        super(inList);
        filters = inList;
    }

    public Filter[] getFilters() {
        return filters;
    }
}
