/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.choose.choosedialog;

import com.artigile.warehouse.gui.core.properties.PropertiesDialog;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.choose.ChooseEvent;
import com.artigile.warehouse.gui.core.properties.dialogs.choose.ChooseListener;
import com.artigile.warehouse.gui.core.properties.dialogs.choose.DefaultChooseList;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChooseForm implements PropertiesForm {
    private JPanel contentPane;
    private JXButton selectAllButton;
    private JXButton selectButton;
    private JXButton unselectButton;
    private JXButton unselectAllButton;
    private JXButton upwardTopButton;
    private JXButton upwardButton;
    private JXButton downwardButton;
    private JXButton downwardBottomButton;
    private JXTable sourceTable;
    private JXTable destinationTable;
    private String title;

    private List<? extends ListItem> sourceList;
    private List<? extends ListItem> destinationList;
    private DefaultChooseList chooseList;
    private AbstractTableModel sourceModel;
    private AbstractTableModel destinationModel;

    public ChooseForm(String title, List<? extends ListItem> sourceList, List<? extends ListItem> destinationList) {
        this(title, sourceList, destinationList, new DefaultModel(sourceList), new DefaultModel(destinationList));
    }

    public ChooseForm(String title, List<? extends ListItem> sourceList, List<? extends ListItem> destinationList, AbstractTableModel sourceModel, AbstractTableModel destinationModel) {
        this.title = title;
        this.sourceList = sourceList;
        this.destinationList = destinationList;
        this.sourceModel = sourceModel;
        this.destinationModel = destinationModel;

        init();
    }

    private void init() {
        getSourceTable().setTableHeader(null);
        getSourceTable().setModel(sourceModel);

        getDestinationTable().setTableHeader(null);
        getDestinationTable().setModel(destinationModel);

        chooseList = new DefaultChooseList(sourceList, getSourceTable().getSelectionModel(),
                destinationList, getDestinationTable().getSelectionModel());
        initListeners();
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public JPanel getContentPanel() {
        return contentPane;
    }

    @Override
    public boolean canSaveData() {
        return true;
    }

    @Override
    public void loadData() {
    }

    @Override
    public void validateData() throws DataValidationException {
        if (destinationList.isEmpty()) {
            DataValidation.fail(getDestinationTable(), I18nSupport.message("validation.empty.list"));
        }
    }

    @Override
    public void saveData() {
    }

    private void initListeners() {
        chooseList.addChooseListener(new ChooseListener() {
            @Override
            public void sourceChanged(ChooseEvent event) {
                sourceModel.fireTableDataChanged();
            }

            @Override
            public void destinationChanged(ChooseEvent event) {
                destinationModel.fireTableDataChanged();
            }

            @Override
            public void sourceOrderChanged(ChooseEvent event) {
                getSourceTable().invalidate();
                getSourceTable().repaint();
            }

            @Override
            public void destinationOrderChanged(ChooseEvent event) {
                getDestinationTable().invalidate();
                getDestinationTable().repaint();
            }
        });

        upwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseList.upward();
            }
        });
        upwardTopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseList.upwardTop();
            }
        });
        downwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseList.downward();
            }
        });
        downwardBottomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseList.downwardBottom();
            }
        });
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseList.select();
            }
        });
        selectAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseList.selectAll();
            }
        });
        unselectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseList.unselect();
            }
        });
        unselectAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseList.unselectAll();
            }
        });
        getSourceTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() % 2 == 0 && e.getButton() == MouseEvent.BUTTON1) {
                    chooseList.select();
                }
            }
        });
        getDestinationTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() % 2 == 0 && e.getButton() == MouseEvent.BUTTON1) {
                    chooseList.unselect();
                }
            }
        });
    }

    /**
     * for test purpose only
     */

    public static void main(String args[]) {
        List<ListItem> sourceList = new ArrayList<ListItem>();
        sourceList.add(new ListItem("one", null));
        sourceList.add(new ListItem("two", null));
        sourceList.add(new ListItem("three", null));
        sourceList.add(new ListItem("four", null));
        sourceList.add(new ListItem("five", null));
        sourceList.add(new ListItem("six", null));
        sourceList.add(new ListItem("seven", null));
        sourceList.add(new ListItem("eight", null));
        sourceList.add(new ListItem("nine", null));
        sourceList.add(new ListItem("ten", null));

        List<ListItem> destinationList = new ArrayList<ListItem>();
        destinationList.add(new ListItem("eleven", null));
        destinationList.add(new ListItem("twelve", null));
        destinationList.add(new ListItem("thirteen", null));
        destinationList.add(new ListItem("fourteen", null));
        destinationList.add(new ListItem("fifteen", null));

        ChooseForm prop = new ChooseForm("choosing", sourceList, destinationList);
        PropertiesDialog dialog = new PropertiesDialog(prop);
        Rectangle rectangle = dialog.getBounds();
        rectangle.setLocation(300, 200);
        dialog.setBounds(rectangle);
        if (dialog.run()) {
        }
    }

    public JXTable getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(JXTable sourceTable) {
        this.sourceTable = sourceTable;
    }

    public JXTable getDestinationTable() {
        return destinationTable;
    }

    public void setDestinationTable(JXTable destinationTable) {
        this.destinationTable = destinationTable;
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
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JXPanel jXPanel1 = new JXPanel();
        jXPanel1.setLayout(new GridLayoutManager(1, 6, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(jXPanel1, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        selectAllButton = new JXButton();
        this.$$$loadButtonText$$$(selectAllButton, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.choosedialog.button.selectAll"));
        jXPanel1.add(selectAllButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        selectButton = new JXButton();
        this.$$$loadButtonText$$$(selectButton, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.choosedialog.button.select"));
        jXPanel1.add(selectButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        unselectButton = new JXButton();
        this.$$$loadButtonText$$$(unselectButton, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.choosedialog.button.unselect"));
        jXPanel1.add(unselectButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        jXPanel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        jXPanel1.add(spacer2, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        unselectAllButton = new JXButton();
        this.$$$loadButtonText$$$(unselectAllButton, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.choosedialog.button.unselectAll"));
        jXPanel1.add(unselectAllButton, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JXPanel jXPanel2 = new JXPanel();
        jXPanel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(jXPanel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        jXPanel2.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.choosedialog.sourceTitle")));
        final JScrollPane scrollPane1 = new JScrollPane();
        jXPanel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(200, 250), new Dimension(200, -1), null, 0, false));
        sourceTable = new JXTable();
        sourceTable.setAutoResizeMode(2);
        sourceTable.setEditable(true);
        sourceTable.setHorizontalScrollEnabled(false);
        sourceTable.setShowHorizontalLines(false);
        sourceTable.setShowVerticalLines(false);
        sourceTable.setSortable(true);
        sourceTable.setVisibleRowCount(10);
        sourceTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        scrollPane1.setViewportView(sourceTable);
        final JXPanel jXPanel3 = new JXPanel();
        jXPanel3.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(jXPanel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        upwardTopButton = new JXButton();
        this.$$$loadButtonText$$$(upwardTopButton, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.choosedialog.button.upwardTop"));
        jXPanel3.add(upwardTopButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        upwardButton = new JXButton();
        this.$$$loadButtonText$$$(upwardButton, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.choosedialog.button.upward"));
        jXPanel3.add(upwardButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        downwardButton = new JXButton();
        this.$$$loadButtonText$$$(downwardButton, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.choosedialog.button.downward"));
        jXPanel3.add(downwardButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        downwardBottomButton = new JXButton();
        this.$$$loadButtonText$$$(downwardBottomButton, ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.choosedialog.button.downwardBottom"));
        jXPanel3.add(downwardBottomButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        jXPanel3.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        jXPanel3.add(spacer4, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JXPanel jXPanel4 = new JXPanel();
        jXPanel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(jXPanel4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        jXPanel4.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("dialogs.choose.choosedialog.destinationTitle")));
        final JScrollPane scrollPane2 = new JScrollPane();
        jXPanel4.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(200, 250), new Dimension(200, -1), null, 0, false));
        destinationTable = new JXTable();
        destinationTable.setShowHorizontalLines(false);
        destinationTable.setShowVerticalLines(false);
        destinationTable.setVisibleRowCount(10);
        destinationTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        scrollPane2.setViewportView(destinationTable);
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
        return contentPane;
    }
}
