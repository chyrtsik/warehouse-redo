/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.adapter.spi.impl.configuration;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Valery Barysok, 6/22/11
 */

public class TableHeaderRelationshipUtil {

    private TableHeaderRelationshipUtil() {
    }

    public static List<ColumnRelationship> getColumnRelationships(RelationshipTableView relationshipTableView) {
        List<ColumnRelationship> result = new ArrayList<ColumnRelationship>();
        JTable table = relationshipTableView.getTable();
        TableModel tableModel = table.getModel();
        for (int i = 0; i < tableModel.getColumnCount(); ++i) {
            result.add(new ColumnRelationship(relationshipTableView.getRelationship(i), i));
        }

        return result;
    }

    public static void setColumnRelationships(RelationshipTableView relationshipTableView, List<ColumnRelationship> relationships) {
        for (ColumnRelationship columnConfig : relationships) {
            relationshipTableView.setRelationship(columnConfig.getColumnIndex(), columnConfig.getDomainColumn());
        }
    }
}
