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

import com.artigile.swingx.filter.BaseFilter;
import com.artigile.warehouse.gui.core.properties.dialogs.choose.choosedialog.sort.SortItem;
import org.jdesktop.swingx.decorator.*;

import javax.swing.SortOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Valery Barysok, 26.01.2010
 */
public class FilterUtils {

    private FilterUtils() {
    }

    public static Filter[] clone(Filter[] filters) {
        Filter result[] = new Filter[filters.length];

        for (int i = 0; i < filters.length; ++i) {
            Filter filter = filters[i];
            if (filter instanceof ShuttleSorter) {
                ShuttleSorter oldSorter = (ShuttleSorter) filter;
                ShuttleSorter newSorter = new ShuttleSorter(oldSorter.getColumnIndex(),
                        oldSorter.isAscending(),
                        oldSorter.getComparator());
                result[i] = newSorter;
            } else if (filter instanceof PatternFilter) {
                PatternFilter oldFilter = (PatternFilter) filter;
                Pattern p = oldFilter.getPattern();
                PatternFilter newFilter = new PatternFilter(p.pattern(),
                        p.flags(),
                        oldFilter.getColumnIndex());
                result[i] = newFilter;
            } else if (filter instanceof BaseFilter) {
                BaseFilter oldFilter = (BaseFilter) filter;
                filters[i] = oldFilter.makeClone();
            } else {
                throw new IllegalArgumentException("Must be never throws");
            }
        }

        return result;
    }

    public static List<Filter> cloneSorters(Filter[] filters) {
        List<Filter> list = new ArrayList();
        for (int i = 0; i < filters.length; ++i) {
            Filter filter = filters[i];
            if (filter instanceof ShuttleSorter) {
                ShuttleSorter oldSorter = (ShuttleSorter) filter;
                ShuttleSorter newSorter = new ShuttleSorter(oldSorter.getColumnIndex(),
                        oldSorter.isAscending(),
                        oldSorter.getComparator());
                list.add(newSorter);
            }
        }

        return list;
    }

    public static List<Filter> cloneFilters(Filter[] filters) {
        List<Filter> list = new ArrayList();
        for (int i = 0; i < filters.length; ++i) {
            Filter filter = filters[i];
            if (filter instanceof PatternFilter) {
                PatternFilter oldFilter = (PatternFilter) filter;
                Pattern p = oldFilter.getPattern();
                PatternFilter newFilter = new PatternFilter(p.pattern(),
                        p.flags(),
                        oldFilter.getColumnIndex());
                list.add(newFilter);
            } else if (filter instanceof BaseFilter) {
                BaseFilter oldFilter = (BaseFilter) filter;
                list.add(oldFilter.makeClone());
            }
        }

        return list;
    }

    public static void sortByColumns(JXTableEx table, List<SortItem> destinationList) {
        if (!destinationList.isEmpty()) {
            List<Filter> filters = new ArrayList<Filter>();

            FilterPipeline filterPipeline = table.getFilters();
            if (filterPipeline instanceof FilterPipelineEx) {
                FilterPipelineEx pipelineEx = (FilterPipelineEx) filterPipeline;
                filters.addAll(FilterUtils.cloneFilters(pipelineEx.getFilters()));

                for (int i = 1; i < destinationList.size(); ++i) {
                    SortItem sortItem = destinationList.get(i);
                    Integer index = (Integer) sortItem.getValue();
                    filters.add(new ShuttleSorter(index, sortItem.getSortOrder().equals(SortOrder.ASCENDING)));
                }

                Filter flts[] = new Filter[filters.size()];
                flts = filters.toArray(flts);
                FilterPipelineEx pipeline = new FilterPipelineEx(flts);
                table.setFilters(pipeline);

                SortItem item = destinationList.get(0);
                SortKey sortKey = new SortKey(convertSortOrder(item.getSortOrder()), (Integer) item.getValue());
                pipeline.getSortController().setSortKeys(Collections.singletonList(sortKey));
            }
        }
    }

    public static org.jdesktop.swingx.decorator.SortOrder convertSortOrder(SortOrder sortOrder) {
        if (sortOrder.equals(SortOrder.ASCENDING)) {
            return org.jdesktop.swingx.decorator.SortOrder.ASCENDING;
        } else if (sortOrder.equals(SortOrder.DESCENDING)) {
            return org.jdesktop.swingx.decorator.SortOrder.DESCENDING;
        } else {
            return org.jdesktop.swingx.decorator.SortOrder.UNSORTED;
        }
    }
}
