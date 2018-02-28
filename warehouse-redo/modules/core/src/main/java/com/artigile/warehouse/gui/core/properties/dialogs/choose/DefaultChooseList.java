/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.choose;

import javax.swing.*;
import java.util.List;

/**
 * @author Borisok V.V., 17.01.2009
 */
public class DefaultChooseList extends AbstractChooseList {
    private List sourceList;
    private List destinationList;
    private ListSelectionModel sourceSelectionModel;
    private ListSelectionModel destinationSelectionModel;

    public DefaultChooseList(List sourceList, ListSelectionModel sourceSelectionModel,
                             List destinationList, ListSelectionModel destinationSelectionModel) {
        this.sourceList = sourceList;
        this.sourceSelectionModel = sourceSelectionModel;
        this.destinationList = destinationList;
        this.destinationSelectionModel = destinationSelectionModel;
    }

    @Override
    public List getSourceList() {
        return sourceList;
    }

    @Override
    public List getDestinationList() {
        return destinationList;
    }

    @Override
    public ListSelectionModel getSourceSelectionModel() {
        return sourceSelectionModel;
    }

    @Override
    public ListSelectionModel getDestinationSelectionModel() {
        return destinationSelectionModel;
    }
}
