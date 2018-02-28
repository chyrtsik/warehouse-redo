/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.choosedialog;

import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 05.01.2009
 */

/**
 * Form for choosing multiple values from list of available values.
 */
public class ChooseProperties implements PropertiesForm {
    private JPanel contentPanel;
    private JButton selectAll;
    private JButton select;
    private JButton unselect;
    private JButton unselectAll;
    private JList available;
    private JList selected;
    private JButton moveUp;
    private JButton moveDown;

    private String title;
    private List<ListItem> availableItems; //List af all available items.
    private List<ListItem> initSelectedItems;  //Initial list of selected items.
    private List<ListItem> selectedItems; //Result list of selected items.

    public ChooseProperties(String title, List<ListItem> availableItems, List<ListItem> selectedItems) {
        this.title = title;
        this.initSelectedItems = selectedItems == null ? new ArrayList<ListItem>() : selectedItems;
        this.availableItems = availableItems == null ? new ArrayList<ListItem>() : availableItems;

        //Delete from available items, that are already selected.
        for (ListItem item : this.initSelectedItems) {
            if (this.availableItems.contains(item)) {
                this.availableItems.remove(item);
            }
        }

        initListeners();
    }

    public List<ListItem> getSelectedItems() {
        return selectedItems;
    }

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
        setListItems(available, availableItems);
        setListItems(selected, initSelectedItems);
    }

    @Override
    public void validateData() throws DataValidationException {
        //TODO: implement delegation to the external data validator here or implement options class, that
        //TODO: holds information about data validation needed.
    }

    @Override
    public void saveData() {
        selectedItems = getListItems(selected);
    }

    //============================== Helpers =====================================
    private void setListItems(JList list, List<ListItem> items) {
        DefaultListModel defaultListModel = new DefaultListModel();
        Object objs[] = items.toArray();
        for (Object obj : objs) {
            defaultListModel.addElement(obj);
        }
        list.setModel(defaultListModel);
    }

    private List<ListItem> getListItems(JList list) {
        List<ListItem> items = new ArrayList<ListItem>();
        ListModel listModel = list.getModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            items.add((ListItem) listModel.getElementAt(i));
        }
        return items;
    }

    //============================== User input processing =========================================
    private void initListeners() {
        moveUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onMoveUp();
            }
        });

        moveDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onMoveDown();
            }
        });

        selectAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSelectAll();
            }
        });

        select.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSelect();
            }
        });

        unselectAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onUnselectAll();
            }
        });

        unselect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onUnselect();
            }
        });

        available.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() % 2 == 0 && e.getButton() == MouseEvent.BUTTON1) {
                    onSelect();
                }
            }
        });

        selected.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() % 2 == 0 && e.getButton() == MouseEvent.BUTTON1) {
                    onUnselect();
                }
            }
        });
    }

    private void onMoveUp() {
        moveItems(selected, -1);
    }

    private void onMoveDown() {
        moveItems(selected, +1);
    }

    private void onUnselect() {
        moveSelectedItems(available, selected);
    }

    private void onUnselectAll() {
        moveAllItems(available, selected);
    }

    private void onSelect() {
        moveSelectedItems(selected, available);
    }

    private void onSelectAll() {
        moveAllItems(selected, available);
    }

    private void moveItem(int srcIndex, int dstIndex, DefaultListModel listModel, ListSelectionModel selectionModel) {
        if (selectionModel.isSelectedIndex(srcIndex)) {
            Object obj = listModel.getElementAt(srcIndex);
            listModel.set(srcIndex, listModel.getElementAt(dstIndex));
            listModel.set(dstIndex, obj);
            selectionModel.removeSelectionInterval(srcIndex, srcIndex);
            selectionModel.addSelectionInterval(dstIndex, dstIndex);
        }
    }

    private void moveItems(JList list, int delta) {
        ListSelectionModel selectionModel = list.getSelectionModel();
        ListModel sourceListModel = list.getModel();
        int minSel = selectionModel.getMinSelectionIndex();
        int maxSel = selectionModel.getMaxSelectionIndex();
        if (delta > 0 && maxSel + delta < sourceListModel.getSize() ||
            delta < 0 && 0 <= minSel + delta) {
            if (sourceListModel instanceof DefaultListModel) {
                DefaultListModel listModel = (DefaultListModel) sourceListModel;
                if (delta < 0) {
                    for (int i = minSel; i <= maxSel; ++i) {
                        moveItem(i, i + delta, listModel, selectionModel);
                    }
                } else {
                    for (int i = maxSel; minSel <= i; --i) {
                        moveItem(i, i + delta, listModel, selectionModel);
                    }
                }
            }
        }
    }

    private void moveAllItems(JList toList, JList fromList) {
        List<ListItem> availItems = getListItems(fromList);
        fromList.setListData(new ListItem[0]);

        List<ListItem> selItems = getListItems(toList);
        selItems.addAll(availItems);
        setListItems(toList, selItems);
    }

    private void moveSelectedItems(JList toList, JList fromList) {
        List<ListItem> itemsToMove = new ArrayList<ListItem>();
        Object[] selItems = fromList.getSelectedValues();
        for (Object selItem : selItems) {
            itemsToMove.add((ListItem) selItem);
        }

        List<ListItem> toItems = getListItems(toList);
        toItems.addAll(itemsToMove);
        setListItems(toList, toItems);

        List<ListItem> fromItems = getListItems(fromList);
        fromItems.removeAll(itemsToMove);
        setListItems(fromList, fromItems);
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
        contentPanel.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.available")));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(200, 300), new Dimension(200, -1), null, 0, false));
        available = new JList();
        scrollPane1.setViewportView(available);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.selected")));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel2.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(200, 300), new Dimension(200, -1), null, 0, false));
        selected = new JList();
        scrollPane2.setViewportView(selected);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        selectAll = new JButton();
        this.$$$loadButtonText$$$(selectAll, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.button.selectAll"));
        panel4.add(selectAll, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        select = new JButton();
        this.$$$loadButtonText$$$(select, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.button.select"));
        panel4.add(select, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        unselect = new JButton();
        this.$$$loadButtonText$$$(unselect, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.button.unselect"));
        panel4.add(unselect, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        unselectAll = new JButton();
        this.$$$loadButtonText$$$(unselectAll, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.button.unselectAll"));
        panel4.add(unselectAll, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        moveUp = new JButton();
        this.$$$loadButtonText$$$(moveUp, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.button.moveUp"));
        panel4.add(moveUp, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        moveDown = new JButton();
        this.$$$loadButtonText$$$(moveDown, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.button.moveDown"));
        panel4.add(moveDown, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel3.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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
