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

import java.awt.*;

/**
 * Interface to be implemented by any filter editor
 * used by the TableFilterHeader
 *
 * @author Borisok V.V., 24.01.2009
 */
public interface TableFilterEditor {

    /**
     * Provides the GUI component associated to the given editor
     */
    public Component getComponent();

    /**
     * Provides the FilterObservable associated to the given instance
     */
    public FilterObservable getFilterObservable();

    /**
     * <p>Performs an update of the filter.</p>
     *
     * <p>Each editor defines its own semantics for this operation,
     * but is always ensure that the current filter is propagated
     * to any observers.</p>
     */
    public void updateFilter();

    /**
     * Resets the filter. The exact semantics for this operation
     * depend on the editor itself, but is always ensured that
     * this operation will remove any filtering currently
     * performed by this editor.
     */
    public void resetFilter();
}
