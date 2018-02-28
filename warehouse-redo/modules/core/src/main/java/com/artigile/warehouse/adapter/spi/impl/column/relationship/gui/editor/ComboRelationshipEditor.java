/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.column.relationship.gui.editor;

import com.artigile.warehouse.adapter.spi.impl.DomainColumn;
import com.artigile.warehouse.adapter.spi.impl.column.relationship.RelationshipObservable;
import com.artigile.warehouse.adapter.spi.impl.column.relationship.TableRelationshipEditor;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Valery Barysok, 6/20/11
 */

public class ComboRelationshipEditor extends JComboBox implements TableRelationshipEditor {

    private ComboField editor = null;

    public ComboRelationshipEditor(int filterPosition, java.util.List<DomainColumn> domainColumns) {
        super(createItemsList(domainColumns));
        editor = new ComboField(this, filterPosition);
    }

    private static ListItem[] createItemsList(List<DomainColumn> domainColumns) {
        ListItem items[] = new ListItem[domainColumns.size() + 1];
        items[0] = new ListItem(DomainColumn.NOT_DEFINED.getName(), DomainColumn.NOT_DEFINED);
        for (int i=0; i<domainColumns.size(); i++){
            DomainColumn domainColumn = domainColumns.get(i);
            items[i+1] = new ListItem(domainColumn.getName(), domainColumn);
        }
        return items;
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public RelationshipObservable getRelationshipObservable() {
        return editor;
    }

    @Override
    public void updateRelationship() {
        editor.updateRelationship(getRelationshipColumn());
    }

    @Override
    public void resetRelationship() {
        editor.resetRelationship();
    }

    public DomainColumn getRelationshipColumn() {
        ListItem selectedItem = (ListItem) editor.getEditor().getSelectedItem();
        return (DomainColumn)selectedItem.getValue();
    }

    public void setRelationshipColumn(DomainColumn domainColumn) {
        ComboBoxModel model = editor.getEditor().getModel();
        for (int i = 0; i < model.getSize(); ++i) {
            ListItem item = (ListItem) model.getElementAt(i);
            if (item.getValue().equals(domainColumn)) {
                editor.getEditor().setSelectedIndex(i);
                break;
            }
        }
    }
}
