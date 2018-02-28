/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.filter;

import com.artigile.swingx.filter.comparator.ComparatorFactory;
import com.artigile.swingx.filter.comparator.FilterComparator;
import com.artigile.swingx.filter.impl.*;
import com.artigile.swingx.workarounds.FilterPipelineEx;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;
import org.jdesktop.swingx.decorator.ShuttleSorter;

import javax.swing.table.TableModel;
import java.util.regex.Pattern;

/**
 * @author Borisok V.V., 27.01.2009
 */
public class TableFilter implements FilterObserver {

    JXTable table = null;

    public TableFilter() {
    }

    public void setTable(JXTable table) {
        this.table = table;
    }

    public JXTable getTable() {
        return table;
    }

    /**
     * Try find index of PatternFilter with the columIndex equals to filter columIndex
     *
     * @param filters array of filter: sorting and filter types
     * @param filter  try find
     * @return -1 if not found else index at filters array
     */

    private int indexOf(Filter[] filters, Filter filter) {
        for (int i = 0; i < filters.length; ++i) {
            Filter f = filters[i];
            if (f instanceof PatternFilter || f instanceof BaseFilter) {
                if (f.getColumnIndex() == filter.getColumnIndex()) {
                    return i;
                }
            }
        }
        return -1;
    }

    private Filter[] update(Filter[] oldFilters, Filter filter) {
        if (filter instanceof PatternFilter) {
            PatternFilter patternFilter = (PatternFilter) filter;
            if (".*".equals(patternFilter.getPattern().pattern())) {
                return delete(oldFilters, filter);
            } else {
                return addOrReplace(oldFilters, filter);
            }
        } else if (filter instanceof BaseFilter) {
            return addOrReplace(oldFilters, filter);
        }

        return oldFilters;
    }

    private Filter[] addOrReplace(Filter[] oldFilters, Filter filter) {
        int replaceIndex = indexOf(oldFilters, filter);
        int count = oldFilters.length;
        if (replaceIndex == -1) {
            replaceIndex = count++;
        }

        Filter filters[] = new Filter[count];
        // TODO: it is so bad %(
        for (int i = 0; i < oldFilters.length; ++i) {
            Filter f = oldFilters[i];
            if (f instanceof PatternFilter) {
                PatternFilter oldFilter = (PatternFilter) f;
                Pattern p = oldFilter.getPattern();
                PatternFilter newFilter = new CustomPatternFilter(p.pattern(),
                        p.flags(),
                        oldFilter.getColumnIndex());
                filters[i] = newFilter;
            } else if (f instanceof ShuttleSorter) {
                ShuttleSorter oldSorter = (ShuttleSorter) f;
                ShuttleSorter newSorter = new ShuttleSorter(oldSorter.getColumnIndex(),
                        oldSorter.isAscending(),
                        oldSorter.getComparator());
                filters[i] = newSorter;
            } else if (f instanceof BaseFilter) {
                BaseFilter oldFilter = (BaseFilter) f;
                filters[i] = oldFilter.makeClone();
            } else {
                throw new IllegalArgumentException("Must be never throws");
            }

        }
        filters[replaceIndex] = filter;

        return filters;
    }

    private Filter[] delete(Filter[] oldFilters, Filter filter) {
        int deleteIndex = indexOf(oldFilters, filter);
        int count = oldFilters.length;
        if (deleteIndex != -1) {
            --count;
        }
        Filter filters[] = new Filter[count];
        // TODO: it is so bad %(
        for (int i = 0, j = 0; i < oldFilters.length; ++i) {
            Filter f = oldFilters[i];
            if (i == deleteIndex) {
                continue;
            }

            if (f instanceof PatternFilter) {
                PatternFilter oldFilter = (PatternFilter) f;
                Pattern p = oldFilter.getPattern();
                PatternFilter newFilter = new CustomPatternFilter(p.pattern(),
                        p.flags(),
                        oldFilter.getColumnIndex());
                filters[j++] = newFilter;
            } else if (f instanceof ShuttleSorter) {
                ShuttleSorter oldSorter = (ShuttleSorter) f;
                ShuttleSorter newSorter = new ShuttleSorter(oldSorter.getColumnIndex(),
                        oldSorter.isAscending(),
                        oldSorter.getComparator());
                filters[j++] = newSorter;
            } else if (f instanceof BaseFilter) {
                BaseFilter oldFilter = (BaseFilter) f;
                filters[j++] = oldFilter.makeClone();
            } else {
                throw new IllegalArgumentException("Must be never throws");
            }

        }

        return filters;
    }

    @Override
    public void filterUpdated(FilterObservable obs, Filter newValue) {
        if (table != null) {
            if (newValue instanceof EqualFilter
                    || newValue instanceof EqualGreaterFilter
                    || newValue instanceof EqualLessFilter
                    || newValue instanceof GreaterFilter
                    || newValue instanceof LessFilter) {
                BaseFilter filter = (BaseFilter) newValue;
                TableModel model = table.getModel();
                Class clazz = model.getColumnClass(filter.getColumnIndex());
                FilterComparator comparator = ComparatorFactory.createComparator(clazz);
                comparator.setFilterText(filter.text);
                filter.setComparator(comparator);
            }

            FilterPipeline pipeline = table.getFilters();
            if (pipeline instanceof FilterPipelineEx) {
                FilterPipelineEx pipelineEx = (FilterPipelineEx) pipeline;
                Filter filters[] = update(pipelineEx.getFilters(), newValue);
                FilterPipelineEx filterPipeline = new FilterPipelineEx(filters);
                table.setFilters(filterPipeline);
            }
        }
    }
}
