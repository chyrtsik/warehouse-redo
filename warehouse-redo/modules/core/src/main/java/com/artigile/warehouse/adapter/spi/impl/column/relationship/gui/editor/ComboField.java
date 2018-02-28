/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.column.relationship.gui.editor;

import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import com.artigile.warehouse.adapter.spi.impl.column.relationship.RelationshipObservable;
import com.artigile.warehouse.adapter.spi.impl.column.relationship.RelationshipObserver;
import com.artigile.warehouse.adapter.spi.impl.configuration.ColumnRelationship;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Valery Barysok, 6/20/11
 */

public class ComboField implements RelationshipObservable {

    private JComboBox editor;

    private int column;


    private List<RelationshipObserver> observers = new ArrayList<RelationshipObserver>();

    public ComboField(JComboBox editor, int column) {
        this.column = column;
        this.editor = editor;
        this.editor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ListItem selectedItem = (ListItem) getEditor().getSelectedItem();
                updateRelationship((DomainColumn)selectedItem.getValue());
            }
        });
    }

    protected JComboBox getEditor() {
        return editor;
    }

    protected int getColumn() {
        return column;
    }

    @Override
    public void addRelationshipObserver(RelationshipObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeRelationshipObserver(RelationshipObserver observer) {
        observers.remove(observer);
    }

    protected void fireObservers(ColumnRelationship relationship) {
        for (RelationshipObserver observer : observers) {
            observer.relationshipUpdated(this, relationship);
        }
    }

    public void updateRelationship(DomainColumn domainColumn) {
        fireObservers(new ColumnRelationship(domainColumn, getColumn()));
    }

    public void resetRelationship() {
        updateRelationship(DomainColumn.NOT_DEFINED);
    }
}
