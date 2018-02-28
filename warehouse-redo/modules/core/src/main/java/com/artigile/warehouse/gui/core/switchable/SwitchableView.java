/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.switchable;

import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;

/**
 * View with support of multiple variants of data showing.
 * Components for each variant are created lazily (only when such view is requested).
 *
 * @author Aliaksandr.Chyrtsik, 26.10.11
 */
public class SwitchableView {
    private JPanel contentPanel;
    private JTabbedPane tabbedViewSwitcher;

    public SwitchableView(List<SwitchableViewItem> viewItems, int selectedViewIndex) {
        //Initialize switchable view. Order of steps is important.
        //1. Form list of views (without creating of views components).
        for (SwitchableViewItem viewItem : viewItems) {
            tabbedViewSwitcher.add(new SwitchableViewItemWrapper(viewItem), GridLayoutUtils.getGrowingAndFillingCellConstraints());
        }

        //2. Initialize selection (only selected view is created).
        if (selectedViewIndex >= 0 && selectedViewIndex < viewItems.size()) {
            tabbedViewSwitcher.setSelectedIndex(selectedViewIndex);
        }
        onViewSelected();

        //3. Setup listener to process next changes of view selection.
        tabbedViewSwitcher.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                onViewSelected();
            }
        });
    }

    private void onViewSelected() {
        //Initialize selected view (load content if not loaded).
        ((SwitchableViewItemWrapper) tabbedViewSwitcher.getSelectedComponent()).initialize();
    }

    public Component getContentPanel() {
        return contentPanel;
    }

    public int getSelectedViewIndex() {
        return tabbedViewSwitcher.getSelectedIndex();
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
        tabbedViewSwitcher = new JTabbedPane();
        contentPanel.add(tabbedViewSwitcher, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }

    /**
     * Wrapper panel for implementations of view lazy creation.
     * Views are created only when requested.
     */
    private class SwitchableViewItemWrapper extends JPanel {
        private boolean viewCreated;
        private SwitchableViewItem viewItem;

        private SwitchableViewItemWrapper(SwitchableViewItem viewItem) {
            super(new GridLayoutManager(1, 1));
            this.viewItem = viewItem;
            setName(viewItem.getName());
        }

        public void initialize() {
            if (!viewCreated) {
                viewCreated = true;
                add(viewItem.getCreateViewComponent(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
            }
        }
    }
}