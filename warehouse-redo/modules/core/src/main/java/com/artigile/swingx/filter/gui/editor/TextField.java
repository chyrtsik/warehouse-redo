/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.swingx.filter.gui.editor;

import com.artigile.swingx.filter.FilterFactory;
import com.artigile.swingx.filter.FilterObservable;
import com.artigile.swingx.filter.FilterObserver;
import com.artigile.swingx.filter.parser.FilterParser;
import com.artigile.swingx.filter.parser.SearchItem;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import org.jdesktop.swingx.decorator.Filter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * @author Borisok V.V., 24.01.2009
 */
public class TextField implements FilterObservable {

    /**
     * The associated text field
     */
    private JTextField editor;

    /**
     * The associated column
     */
    private int column;

    /**
     * Associated observers
     */
    private List<FilterObserver> observers = new ArrayList<FilterObserver>();

    public TextField(JTextField editor, int column) {
        this.column = column;
        this.editor = editor;
        this.editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            private void changed() {
                updateFilter(getEditor().getText());
            }
        });
    }

    protected JTextField getEditor() {
        return editor;
    }

    protected int getColumn() {
        return column;
    }

    @Override
    public void addFilterObserver(FilterObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeFilterObserver(FilterObserver observer) {
        observers.remove(observer);
    }

    public void updateFilter(String text) {
        try {
            SearchItem searchItem = FilterParser.parse(text);
            Filter filter = FilterFactory.createFilter(searchItem, getColumn());
            fireObservers(filter);
        } catch (PatternSyntaxException ex) {
            // Supress warnings about wrong using of meta characters.
            LoggingFacade.logError(ex);
        }
    }

    protected void fireObservers(Filter filter) {
        for (FilterObserver observer : observers) {
            observer.filterUpdated(this, filter);
        }
    }

    public void resetFilter() {
        updateFilter("");
    }
}
