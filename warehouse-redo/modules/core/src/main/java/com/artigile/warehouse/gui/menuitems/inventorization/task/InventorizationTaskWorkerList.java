/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.inventorization.task;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.inventorization.task.InventorizationTaskService;
import com.artigile.warehouse.bl.warehouse.WarehouseFilter;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.FramePlugin;
import com.artigile.warehouse.gui.core.print.PrintFacade;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.menuitems.inventorization.task.printing.*;
import com.artigile.warehouse.gui.menuitems.warehouse.warehouselist.WarehouseSelectForm;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.inventorization.task.InventorizationTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.print.PrintException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Borisok V.V., 03.10.2009
 */
public class InventorizationTaskWorkerList extends FramePlugin {
    private JPanel contentPanel;
    private JTextField fieldWarehouse;
    private JTextField fieldWorker;
    private JButton changeWarehouse;
    private JPanel inventorizationTaskListPanel;
    private JButton beginProcessing;
    private JButton print;
    private JButton completeProcessing;

    private InventorizationTaskList inventorizationTasksList;
    private InventorizationTasksPrintOptions printOptions = new InventorizationTasksPrintOptions();
    private WarehouseTOForReport warehouse;
    private ActionListener printAction = null;
    private ActionListener beginInventorizationAction = null;
    private ActionListener completeInventorizationAction = null;

    public InventorizationTaskWorkerList() {
        warehouse = WareHouse.getUserSession().getUserWarehouse();
        initListeners();
        initReportHeader();
        initComplectingTasksList();
    }

    private UserTO getWorker() {
        return WareHouse.getUserSession().getUser();
    }

    private WarehouseTOForReport getWarehouse() {
        return warehouse;
    }

    private void initListeners() {
        printAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onPrint();
            }
        };
        beginInventorizationAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onStartInventorizationTasks();
            }
        };
        completeInventorizationAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCompleteInventorizationTasks();
            }
        };

        updateButtons();

        changeWarehouse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onChangeWarehouse();
            }
        });
    }

    private void disableButtons() {
        print.setEnabled(false);
        print.removeActionListener(printAction);
        beginProcessing.setEnabled(false);
        beginProcessing.removeActionListener(beginInventorizationAction);
        completeProcessing.setEnabled(false);
        completeProcessing.removeActionListener(completeInventorizationAction);
    }

    private void updateButtons() {
        AvailabilityStrategy inventorizationAvailability = new InventorizationTaskEditAvailability(getWorker(), getWarehouse());
        boolean canDoInventorization = inventorizationAvailability.isAvailable(null);

        print.setEnabled(canDoInventorization);
        beginProcessing.setEnabled(canDoInventorization);
        completeProcessing.setEnabled(canDoInventorization);

        if (canDoInventorization) {
            print.addActionListener(printAction);
            beginProcessing.addActionListener(beginInventorizationAction);
            completeProcessing.addActionListener(completeInventorizationAction);
        }
    }

    private InventorizationTaskService getInventorizationTaskService() {
        return SpringServiceContext.getInstance().getInventorizationTaskService();
    }

    private void onStartInventorizationTasks() {
        //Try to begin all unstarted inventorization tasks.
        List<InventorizationTaskTO> allTasks = inventorizationTasksList.getFilteredTasksList(InventorizationTasksFilterType.ALL);
        List<InventorizationTaskTO> tasksToBegin = new ArrayList<InventorizationTaskTO>();
        for (InventorizationTaskTO task : allTasks) {
            if (task.isNotProcessed()) {
                tasksToBegin.add(task);
            }
        }
        try {
            getInventorizationTaskService().beginInventorizationTasks(tasksToBegin);
        } catch (BusinessException e) {
            MessageDialogs.showWarning(contentPanel, e.getMessage());
        }
    }

    private void onCompleteInventorizationTasks() {
        //Try to complete all started inventorization tasks.
        List<InventorizationTaskTO> allTasks = inventorizationTasksList.getFilteredTasksList(InventorizationTasksFilterType.ALL);
        List<InventorizationTaskTO> tasksToComplete = new ArrayList<InventorizationTaskTO>();
        for (InventorizationTaskTO task : allTasks) {
            if (task.isInProcess()) {
                tasksToComplete.add(task);
            }
        }
        try {
            getInventorizationTaskService().completeInventorizationTasks(tasksToComplete);
        } catch (BusinessException e) {
            MessageDialogs.showWarning(contentPanel, e.getMessage());
        }
    }

    private void onPrint() {
        if (hasTasksForPrinting()) {
            doPrint();
        } else {
            MessageDialogs.showInfo(contentPanel, I18nSupport.message("inventorization.task.print.form.message.nothing.to.print"));
        }
    }

    private void onChangeWarehouse() {
        WarehouseFilter filter = new WarehouseFilter();
        filter.setComplectingUserId(WareHouse.getUserSession().getUser().getId());
        WarehouseSelectForm warehouseForm = new WarehouseSelectForm(filter);
        if (Dialogs.runProperties(warehouseForm)) {
            warehouse = warehouseForm.getWarehouse();
            fieldWarehouse.setText(warehouse.getName());
            disableButtons();
            inventorizationTasksList.setWarehouse(warehouse);
            inventorizationTasksList.refreshData();
            updateButtons();
        }
    }

    private boolean hasTasksForPrinting() {
        //Check, if there is any task for printing.
        List<InventorizationTaskTO> tasks = inventorizationTasksList.getFilteredTasksList(InventorizationTasksFilterType.ALL);
        return tasks.size() > 0;
    }

    private void doPrint() {
        //At first we let user to choose, what to print.
        InventorizationTasksPrintPropertiesForm prop = new InventorizationTasksPrintPropertiesForm(printOptions);
        if (Dialogs.runProperties(prop)) {
            try {
                //Print using selected print options.
                InventorizationTasksForPrinting tasksForPrinting = new InventorizationTasksForPrintingImpl(inventorizationTasksList, printOptions.getWhatToPrint());
                if (tasksForPrinting.getItems().size() == 0) {
                    //Nothing to print.
                    MessageDialogs.showInfo(contentPanel, I18nSupport.message("inventorization.task.print.form.message.nothing.to.print"));
                    return;
                }

                if (tasksForPrinting.getItems().size() > 0) {
                    PrintFacade.printDirect(tasksForPrinting, PrintTemplateType.TEMPLATE_INVENTORIZATION_TASKS_FOR_WORKER, getWarehouse().getUsualPrinter());

                    //Perform necessary post print tasks processing.
                    getInventorizationTaskService().postPrintTasks(tasksForPrinting.getItems());
                }
            } catch (PrintException e) {
                LoggingFacade.logError(this, e);
                MessageDialogs.showError(contentPanel, e);
            }
        }
    }

    private void initReportHeader() {
        fieldWarehouse.setText(getWarehouse().getName());
        fieldWorker.setText(getWorker().getDisplayName());
    }

    private void initComplectingTasksList() {
        inventorizationTasksList = new InventorizationTaskList(getWarehouse(), getWorker());
        inventorizationTaskListPanel.removeAll();
        inventorizationTaskListPanel.add((new TableReport(inventorizationTasksList, this)).getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("inventorization.task.worker.list.title", getWarehouse().getName());
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
        scrollPane1.setViewportView(panel1);
        inventorizationTaskListPanel = new JPanel();
        inventorizationTaskListPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(inventorizationTaskListPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        beginProcessing = new JButton();
        this.$$$loadButtonText$$$(beginProcessing, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.task.worker.list.command.inventorization.start"));
        panel3.add(beginProcessing, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        print = new JButton();
        this.$$$loadButtonText$$$(print, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.task.worker.list.command.print"));
        panel3.add(print, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        completeProcessing = new JButton();
        this.$$$loadButtonText$$$(completeProcessing, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.task.worker.list.command.inventorization.completed"));
        panel3.add(completeProcessing, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.task.worker.list.warehouse"));
        panel4.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldWarehouse = new JTextField();
        fieldWarehouse.setEditable(false);
        panel4.add(fieldWarehouse, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel4.add(spacer3, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.task.worker.list.woker"));
        panel4.add(label2, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldWorker = new JTextField();
        fieldWorker.setEditable(false);
        panel4.add(fieldWorker, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        changeWarehouse = new JButton();
        this.$$$loadButtonText$$$(changeWarehouse, ResourceBundle.getBundle("i18n/warehouse").getString("inventorization.task.worker.list.warehouse.chooser"));
        panel4.add(changeWarehouse, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }
}
