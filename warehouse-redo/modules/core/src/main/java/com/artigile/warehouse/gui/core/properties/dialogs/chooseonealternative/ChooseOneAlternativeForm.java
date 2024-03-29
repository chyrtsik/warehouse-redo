/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.chooseonealternative;

import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Shyrik, 19.06.2009
 */

/**
 *
 */
public class ChooseOneAlternativeForm implements PropertiesForm {
    private JPanel contentPanel;
    private JPanel chooseListPanel;
    private JLabel fieldMessage;

    /**
     * Title, that will be shown it the dialog.
     */
    private String title;

    /**
     * Additional message for user.
     */
    private String message;

    /**
     * List of available alternatives.
     */
    private List<ListItem> chooseList = new ArrayList<ListItem>();

    /**
     * Temporary saved chosen alternative.
     */
    private ListItem tempChosenItem = null;

    /**
     * Chosen alternative.
     */
    private ListItem chosenItem = null;

    //============================ Choose dialog operations =====================================
    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Adds new alternative to the list of available alternatives.
     *
     * @param newChoise
     */
    public void addChoice(ListItem newChoise) {
        chooseList.add(newChoise);
    }

    /**
     * Retrieves alternative, has been chosen be the user.
     */
    public ListItem getChoice() {
        return chosenItem;
    }

    //================= Properties form implementation ==========================================
    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public boolean canSaveData() {
        return true;
    }

    @Override
    public void loadData() {
        //Message for user.
        fieldMessage.setText(message);
        
        //List of available values.
        ButtonGroup buttonGroup = new ButtonGroup();
        chooseListPanel.removeAll();
        chooseListPanel.setLayout(new GridLayoutManager(chooseList.size(), 1));
        for (int i = 0; i < chooseList.size(); i++) {
            ListItem item = chooseList.get(i);
            JRadioButton radio = new JRadioButton(item.getDisplayName(), item.equals(chosenItem));
            radio.addActionListener(new ChooseItemListener(item));

            GridConstraints constraints = GridLayoutUtils.getGrowingAndFillingCellConstraints();
            constraints.setRow(i);
            chooseListPanel.add(radio, constraints);

            buttonGroup.add(radio);
        }
    }

    @Override
    public void validateData() throws DataValidationException {
        if (tempChosenItem == null) {
            throw new DataValidationException(I18nSupport.message("validation.select.value"));
        }
    }

    @Override
    public void saveData() {
        chosenItem = tempChosenItem;
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
        contentPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldMessage = new JLabel();
        fieldMessage.setText("<message>");
        panel1.add(fieldMessage, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        chooseListPanel = new JPanel();
        chooseListPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(chooseListPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }

    /**
     * Used for reaction of choosing one of the alternatives.
     */
    private class ChooseItemListener implements ActionListener {
        private ListItem itemToBeChosen;

        public ChooseItemListener(ListItem item) {
            itemToBeChosen = item;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            tempChosenItem = itemToBeChosen;
        }
    }
}
