/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.complecting;

import com.artigile.warehouse.bl.common.exceptions.BusinessException;
import com.artigile.warehouse.bl.complecting.ComplectingTaskFilter;
import com.artigile.warehouse.bl.complecting.UncomplectingTaskFilter;
import com.artigile.warehouse.bl.warehouse.WarehouseFilter;
import com.artigile.warehouse.domain.complecting.ComplectingTaskState;
import com.artigile.warehouse.domain.complecting.UncomplectingTaskState;
import com.artigile.warehouse.domain.printing.PrintTemplateType;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.plugin.FramePlugin;
import com.artigile.warehouse.gui.core.print.PrintFacade;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.report.command.availability.AvailabilityStrategy;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.menuitems.complecting.complectingTasks.ComplectingTaskEditAvailability;
import com.artigile.warehouse.gui.menuitems.complecting.complectingTasks.ComplectingTaskList;
import com.artigile.warehouse.gui.menuitems.complecting.printing.*;
import com.artigile.warehouse.gui.menuitems.complecting.uncomplectingTasks.UncomplectingTaskList;
import com.artigile.warehouse.gui.menuitems.warehouse.warehouselist.WarehouseSelectForm;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.complecting.ComplectingTaskTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.artigile.warehouse.utils.logging.LoggingFacade;
import com.artigile.warehouse.utils.properties.savers.ComplectingTaskWarehouseSaver;
import com.artigile.warehouse.utils.properties.savers.SplitPaneOrietationSaver;
import com.artigile.warehouse.utils.properties.savers.SplitPaneSaver;
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
 * @author Shyrik, 28.04.2009
 */

/**
 * The main window of the warehouse worder. Here he sees his tasks and reports about threi progress.
 */
public class ComplectingTaskWorkerList extends FramePlugin {
    private JPanel contentPanel;
    private JPanel complectingTaskListPanel;
    private JTextField fieldWarehouse;
    private JTextField fieldWorker;
    private JButton complect;
    private JButton print;
    private JPanel uncomplectingTaskListPanel;
    private JButton changeWarehouse;
    private JSplitPane splitPane;

    private ComplectingTaskList complectingTasksList;
    private UncomplectingTaskList uncomplectingTasksList;
    private ComplectingTasksPrintOptions printOptions = new ComplectingTasksPrintOptions();
    private WarehouseTOForReport warehouse;
    private ActionListener printAction = null;
    private ActionListener complectAction = null;

    public ComplectingTaskWorkerList() {
        warehouse = ComplectingTaskWarehouseSaver.restore(WareHouse.getUserSession().getUserWarehouse(), getFrameId());
        initListeners();
        initReportHeader();
        initComplectingTasksList();
        initUncomplectingTasksList();
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
        complectAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onBeginComplecting();
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
        complect.setEnabled(false);
        complect.removeActionListener(complectAction);
    }

    private void updateButtons() {
        AvailabilityStrategy complectingAvailability = new ComplectingTaskEditAvailability(getWorker(), getWarehouse());
        boolean canDoComplecting = complectingAvailability.isAvailable(null);

        print.setEnabled(canDoComplecting);
        complect.setEnabled(canDoComplecting);

        if (canDoComplecting) {
            print.addActionListener(printAction);
            complect.addActionListener(complectAction);
        }
    }

    private void onBeginComplecting() {
        //Try to begin complecting of all unstarted complecting tasks.
        List<ComplectingTaskTO> allTasks = complectingTasksList.getFilteredTasksList(ComplectingTasksToPrintFilterType.ALL, false);
        List<ComplectingTaskTO> tasksToBegin = new ArrayList<ComplectingTaskTO>();
        for (ComplectingTaskTO task : allTasks) {
            if (task.getState() == ComplectingTaskState.NOT_PROCESSED) {
                tasksToBegin.add(task);
            }
        }
        try {
            SpringServiceContext.getInstance().getComplectingTaskService().beginComplecting(tasksToBegin);
        } catch (BusinessException e) {
            MessageDialogs.showWarning(contentPanel, e.getMessage());
        }
    }

    private void onPrint() {
        if (hasTasksForPrinting()) {
            doPrint();
        } else {
            MessageDialogs.showInfo(contentPanel, I18nSupport.message("complectingTask.printForm.message.notningToPrint"));
        }
    }

    private void onChangeWarehouse() {
        WarehouseFilter filter = new WarehouseFilter();
        filter.setComplectingUserId(WareHouse.getUserSession().getUser().getId());
        WarehouseSelectForm warehouseForm = new WarehouseSelectForm(filter);
        if (Dialogs.runProperties(warehouseForm)) {
            warehouse = warehouseForm.getWarehouse();
            ComplectingTaskWarehouseSaver.store(warehouse, getFrameId());
            updateWarehouse();
            disableButtons();
            complectingTasksList.setWarehouse(warehouse);
            complectingTasksList.refreshData();
            uncomplectingTasksList.setWarehouse(warehouse);
            uncomplectingTasksList.refreshData();
            updateButtons();
        }
    }

    private boolean hasTasksForPrinting() {
        //Check, if there is any task for printing.
        List<ComplectingTaskTO> tasks = complectingTasksList.getFilteredTasksList(ComplectingTasksToPrintFilterType.ALL, false);
        return tasks.size() > 0;
    }

    private void doPrint() {
        //At first we let user to choose, what to print.
        ComplectingTasksPrintPropertiesForm prop = new ComplectingTasksPrintPropertiesForm(printOptions);
        if (Dialogs.runProperties(prop)) {
            try {
                //Print using selected print options.
                TasksForPrinting tasksForPrinting = new TasksForPrinting(complectingTasksList, printOptions.getWhatToPrint());
                StickersForPrinting stickersForPrinting = new StickersForPrinting(complectingTasksList, printOptions.getWhatToPrint());
                if (tasksForPrinting.getItems().size() == 0 && stickersForPrinting.getItems().size() == 0) {
                    //Nothing to print.
                    MessageDialogs.showInfo(contentPanel, I18nSupport.message("complectingTask.printForm.message.notningToPrint"));
                    return;
                }

                if (printOptions.isPrintPositions() && tasksForPrinting.getItems().size() > 0) {
                    PrintFacade.printDirect(tasksForPrinting, PrintTemplateType.TEMPLATE_COMPLECTING_TASKS_FOR_WORKER, getWarehouse().getUsualPrinter());

                    //Perform necessary post print tasks processing.
                    SpringServiceContext.getInstance().getComplectingTaskService().postPrintTasks(tasksForPrinting.getItems());
                }
                if (printOptions.isPrintStickers() && stickersForPrinting.getItems().size() > 0) {
                    if (printOptions.isPrintPositions()) {
                        //Remind user not to forget to change paper.
                        MessageDialogs.showInfo(contentPanel, I18nSupport.message("complectingTask.printForm.message.insertPaperForStickers"));
                    }

                    PrintFacade.printDirect(stickersForPrinting, PrintTemplateType.TEMPLATE_STICKER, getWarehouse().getStickerPrinter());

                    //Perform necessary post print tasks processing.
                    SpringServiceContext.getInstance().getComplectingTaskService().postPrintStickers(stickersForPrinting.getAllItems());
                }
            } catch (PrintException e) {
                LoggingFacade.logError(this, e);
                MessageDialogs.showError(contentPanel, e);
            }
        }
    }

    private void updateWarehouse() {
        WarehouseTOForReport warehouse = getWarehouse();
        if (warehouse != null) {
            fieldWarehouse.setText(warehouse.getName());
        }
    }

    private void initReportHeader() {
        updateWarehouse();
        fieldWorker.setText(getWorker().getDisplayName());
    }

    private void initComplectingTasksList() {
        ComplectingTaskFilter filter = new ComplectingTaskFilter();
        filter.setWarehouseId(getWarehouse().getId());
        filter.setStates(new ComplectingTaskState[]{ComplectingTaskState.NOT_PROCESSED, ComplectingTaskState.PROCESSING, ComplectingTaskState.PROCESSED});
        complectingTasksList = new ComplectingTaskList(filter, getWorker());
        complectingTaskListPanel.removeAll();
        complectingTaskListPanel.add((new TableReport(complectingTasksList, this)).getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    private void initUncomplectingTasksList() {
        UncomplectingTaskFilter filter = new UncomplectingTaskFilter();
        filter.setWarehouseId(getWarehouse().getId());
        filter.setStates(new UncomplectingTaskState[]{UncomplectingTaskState.NOT_PROCESSED, UncomplectingTaskState.PROCESSED});
        uncomplectingTasksList = new UncomplectingTaskList(filter, getWorker());
        uncomplectingTaskListPanel.removeAll();
        uncomplectingTaskListPanel.add((new TableReport(uncomplectingTasksList, this)).getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("complectingTask.worker.list.title", getWarehouse().getName());
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    protected void onFrameOpened() {
        super.onFrameOpened();

        SplitPaneSaver.restore(splitPane, getFrameId());
        SplitPaneOrietationSaver.restore(splitPane, ComplectingTaskWorkerList.class);
    }

    @Override
    protected void onFrameClosed() {
        SplitPaneSaver.store(splitPane, getFrameId());
        super.onFrameClosed();
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
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("complectingTask.worker.list.warehouse"));
        panel3.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldWarehouse = new JTextField();
        fieldWarehouse.setEditable(false);
        panel3.add(fieldWarehouse, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("complectingTask.worker.list.woker"));
        panel3.add(label2, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldWorker = new JTextField();
        fieldWorker.setEditable(false);
        panel3.add(fieldWorker, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        changeWarehouse = new JButton();
        this.$$$loadButtonText$$$(changeWarehouse, ResourceBundle.getBundle("i18n/warehouse").getString("complectingTask.worker.list.warehouse.chooser"));
        panel3.add(changeWarehouse, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        complect = new JButton();
        this.$$$loadButtonText$$$(complect, ResourceBundle.getBundle("i18n/warehouse").getString("complectingTask.worker.list.command.complect"));
        panel4.add(complect, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        print = new JButton();
        this.$$$loadButtonText$$$(print, ResourceBundle.getBundle("i18n/warehouse").getString("complectingTask.worker.list.command.print"));
        panel4.add(print, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel4.add(spacer3, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        splitPane = new JSplitPane();
        splitPane.setOrientation(0);
        panel1.add(splitPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 100), null, 0, false));
        complectingTaskListPanel = new JPanel();
        complectingTaskListPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane.setLeftComponent(complectingTaskListPanel);
        uncomplectingTaskListPanel = new JPanel();
        uncomplectingTaskListPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane.setRightComponent(uncomplectingTaskListPanel);
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
