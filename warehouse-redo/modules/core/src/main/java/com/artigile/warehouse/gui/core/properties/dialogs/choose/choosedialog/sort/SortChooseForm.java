/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.choose.choosedialog.sort;

import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.dialogs.choose.choosedialog.ChooseForm;
import com.artigile.warehouse.gui.core.properties.dialogs.choose.choosedialog.DefaultModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Borisok V.V., 18.01.2009
 */
public class SortChooseForm extends ChooseForm {

    public SortChooseForm(String title, List<SortItem> sourceList, List<SortItem> destinationList) {
        this(title, sourceList, destinationList, new DefaultModel(sourceList), new DefaultSortModel(destinationList));
    }

    public SortChooseForm(String title, List<SortItem> sourceList, List<SortItem> destinationList, AbstractTableModel sourceModel, AbstractTableModel destinationModel) {
        super(title, sourceList, destinationList, sourceModel, destinationModel);
        init();
    }

    private void init() {
        getDestinationTable().setDefaultRenderer(SortOrder.class, new SortRenderer());
        getDestinationTable().setDefaultEditor(SortOrder.class, new SortEditor());
        TableColumn tc = getDestinationTable().getColumnModel().getColumn(DefaultSortModel.SORTORDERCOLUMN);
        tc.setMinWidth(16);
        tc.setMaxWidth(16);
    }

    /**
     * for test purpose only
     */
    public static void main(String args[]) {
        List<SortItem> sourceList = new ArrayList<SortItem>();
        sourceList.add(new SortItem("one", null));
        sourceList.add(new SortItem("two", null));
        sourceList.add(new SortItem("three", null));
        sourceList.add(new SortItem("four", null));
        sourceList.add(new SortItem("five", null));
        sourceList.add(new SortItem("six", null));
        sourceList.add(new SortItem("seven", null));
        sourceList.add(new SortItem("eight", null));
        sourceList.add(new SortItem("nine", null));
        sourceList.add(new SortItem("ten", null));

        List<SortItem> destinationList = new ArrayList<SortItem>();
        destinationList.add(new SortItem("eleven", null));
        destinationList.add(new SortItem("twelve", null));
        destinationList.add(new SortItem("thirteen", null));
        destinationList.add(new SortItem("fourteen", null));
        destinationList.add(new SortItem("fifteen", null));

        SortChooseForm prop = new SortChooseForm("sorting", sourceList, destinationList);
        PropertiesDialog dialog = new PropertiesDialog(prop);
        Rectangle rectangle = dialog.getBounds();
        rectangle.setLocation(300, 200);
        dialog.setBounds(rectangle);
        if (dialog.run()) {
        }
    }

}
