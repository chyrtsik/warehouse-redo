/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.warehouse.warehouselist;

import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.bl.warehouse.WarehouseService;
import com.artigile.warehouse.gui.baselayout.WareHouse;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.init.ComboBoxFillOptions;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.browser.BrowseResult;
import com.artigile.warehouse.gui.core.properties.dialogs.choosedialog.ChooseDialogResult;
import com.artigile.warehouse.gui.core.report.controller.TreeReport;
import com.artigile.warehouse.gui.core.report.controller.TreeReportViewType;
import com.artigile.warehouse.gui.menuitems.contractors.ContractorsList;
import com.artigile.warehouse.gui.menuitems.warehouse.storageplace.StoragePlaceTreeReport;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.ContactTO;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.warehouse.StoragePlaceTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class WarehouseForm implements PropertiesForm {
    private JPanel contentPane;
    private JTextField textName;
    private JTextArea textNotice;
    private JPanel storagePlacePanel;
    private JTabbedPane tabbedPane;
    private JComboBox usualPrinter;
    private JComboBox stickerPrinter;
    private JList allowedUsersList;
    private JButton browseUsers;
    private JLabel allowedUsersLabel;
    private JTextField fieldOwner;
    private JButton browseOwner;
    private JComboBox fieldResponsible;
    private JButton clearOwner;
    private JTextField fieldAddress;

    private boolean canEdit;
    private boolean canEditUserList;
    private boolean canEditOther;
    private WarehouseTO warehouseTO;
    private List<UserTO> allowedUsers;
    private WarehouseTO tempWarehouse; //Copy of warehouseTO for editing groups.
    private ContractorTO selectedOwner;

    private WarehouseService warehouseService = SpringServiceContext.getInstance().getWarehouseService();

    public WarehouseForm(WarehouseTO warehouseTO, List<UserTO> allowedUsers, boolean canEdit, boolean canEditUserList, boolean canEditOther) {
        this.canEdit = canEdit;
        this.canEditUserList = canEditUserList;
        this.canEditOther = canEditOther;
        this.warehouseTO = warehouseTO;
        this.allowedUsers = allowedUsers == null ? new ArrayList<UserTO>() : allowedUsers;

        this.tempWarehouse = new WarehouseTO();
        this.tempWarehouse.copyFrom(warehouseTO);

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(textName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fieldAddress, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(textNotice, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        initPrinters();
        initListeners();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("warehouse.properties.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPane;
    }

    @Override
    public boolean canSaveData() {
        return canEdit;
    }

    @Override
    public void loadData() {
        textName.setText(warehouseTO.getName());
        fieldAddress.setText(warehouseTO.getAddress());
        textNotice.setText(warehouseTO.getNotice());
        setPrinter(usualPrinter, warehouseTO.getUsualPrinter());
        setPrinter(stickerPrinter, warehouseTO.getStickerPrinter());
        DataExchange.setListItems(allowedUsersList, makeUsersList(allowedUsers));
        TreeReport storagePlaceTreeReport = new TreeReport(new StoragePlaceTreeReport(tempWarehouse), TreeReportViewType.TREE_TABLE);
        storagePlacePanel.add(storagePlaceTreeReport.getReportComponent(), GridLayoutUtils.getGrowingAndFillingCellConstraints());

        //It is important to select owner first so that list of it's employees will be initialized.
        selectOwner(warehouseTO.getOwner());
        DataExchange.selectComboItem(fieldResponsible, warehouseTO.getResponsible());
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(textName);
        if (!warehouseService.isUniqueWarehouseName(textName.getText(), warehouseTO.getId())) {
            DataValidation.failRes(textName, "warehouse.properties.warehouse.already.exists");
        }
    }

    private void updateWarehouse(WarehouseTO warehouseTO, List<StoragePlaceTO> storagePlaces) {
        for (StoragePlaceTO storagePlaceTO : storagePlaces) {
            storagePlaceTO.setWarehouse(warehouseTO);

            List<StoragePlaceTO> sps = storagePlaceTO.getStoragePlaces();
            if (!sps.isEmpty()) {
                updateWarehouse(warehouseTO, sps);
            }
        }
    }

    @Override
    public void saveData() {
        warehouseTO.setName(textName.getText());
        warehouseTO.setAddress(fieldAddress.getText());
        warehouseTO.setOwner(selectedOwner);
        warehouseTO.setResponsible((ContactTO) DataExchange.getComboSelection(fieldResponsible));
        warehouseTO.setNotice(textNotice.getText());
        warehouseTO.setUsualPrinter((String) DataExchange.getComboSelection(usualPrinter));
        warehouseTO.setStickerPrinter((String) DataExchange.getComboSelection(stickerPrinter));
        List<StoragePlaceTO> storagePlaces = tempWarehouse.getStoragePlaces();
        updateWarehouse(warehouseTO, storagePlaces);
        warehouseTO.setStoragePlaces(storagePlaces);

        allowedUsers.clear();
        allowedUsers.addAll(getUsers(DataExchange.getListItems(this.allowedUsersList)));
    }

    //============================ Helpers ===================================

    private void selectOwner(ContractorTO owner) {
        if (selectedOwner == null || !selectedOwner.equals(owner)) {
            //When owner is changes we need to select responsible from scratch (different contractors have  different
            //employees).
            selectedOwner = owner;
            InitUtils.initContractorContactsCombo(fieldResponsible, selectedOwner,
                    new ComboBoxFillOptions().setAddNotSelectedItem(true).setSelectNotSelectedByDefault(true));
        }
        fieldOwner.setText(selectedOwner == null ? "" : selectedOwner.getName());
        clearOwner.setVisible(selectedOwner != null);
    }

    public List<UserTO> getAllowedUsers() {
        return allowedUsers;
    }

    private void initPrinters() {
        if (canEditPrinters()) {
            InitUtils.initPrintersCombo(usualPrinter, new ComboBoxFillOptions().setAddNotSelectedItem(true));
            InitUtils.initPrintersCombo(stickerPrinter, new ComboBoxFillOptions().setAddNotSelectedItem(true));
        }
    }

    private boolean canEditPrinters() {
        //User can edit printer, if he is logger at the editing warehouseTO.
        return canEditOther && WareHouse.getUserSession().getUserWarehouse().getId() == warehouseTO.getId();
    }

    private void setPrinter(JComboBox printersCombo, String printer) {
        if (!canEditPrinters()) {
            printersCombo.removeAllItems();
            printersCombo.addItem(new ListItem(printer == null ? "" : printer, printer));
        }
        printersCombo.setEnabled(canEditPrinters());
        DataExchange.selectComboItem(printersCombo, printer);
    }

    private List<UserTO> getUsers(List<ListItem> listItems) {
        List<UserTO> users = new ArrayList<UserTO>();
        for (ListItem item : listItems) {
            users.add((UserTO) item.getValue());
        }
        return users;
    }

    private List<ListItem> makeUsersList(List<UserTO> users) {
        List<ListItem> usersList = new ArrayList<ListItem>();
        for (UserTO user : users) {
            usersList.add(new ListItem(user.getDisplayName(), user));
        }
        return usersList;
    }

    //========================= Processing user input ===================================

    private void initListeners() {
        allowedUsersLabel.setEnabled(canEditUserList);
        browseUsers.setEnabled(canEditUserList);
        browseUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBrowseUsers();
            }
        });
        browseOwner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBrowseOwner();
            }
        });
        clearOwner.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectOwner(null);
            }
        });
    }

    private void onBrowseOwner() {
        //Show browser for choosing warehouse owner.
        BrowseResult result = Dialogs.runBrowser(new ContractorsList());
        if (result.isOk()) {
            selectOwner((ContractorTO) result.getSelectedItems().get(0));
        }
    }

    private void onBrowseUsers() {
        //Showing dialog for choosing users, thar are allowed to complect parcels from this warehouseTO.
        UserService userService = SpringServiceContext.getInstance().getUserService();
        List<ListItem> availableUsers = makeUsersList(userService.getAllUsers());
        List<ListItem> selectedUsers = DataExchange.getListItems(this.allowedUsersList);
        String title = I18nSupport.message("warehouse.properties.choose.users.title");

        ChooseDialogResult result = Dialogs.runChooseListDialog(title, availableUsers, selectedUsers);
        if (result.isOk()) {
            DataExchange.setListItems(this.allowedUsersList, result.getSelectedItems());
        }
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
        contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("warehouse.properties.tab.generalProperties"), panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("warehouse.properties.general")));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("warehouse.properties.name"));
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("warehouse.properties.notice"));
        panel2.add(label2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textName = new JTextField();
        panel2.add(textName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(350, -1), new Dimension(150, -1), null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 50), new Dimension(-1, 50), null, 0, false));
        textNotice = new JTextArea();
        textNotice.setLineWrap(true);
        textNotice.setWrapStyleWord(true);
        scrollPane1.setViewportView(textNotice);
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("warehouse.properties.owner"));
        panel2.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldOwner = new JTextField();
        fieldOwner.setEditable(false);
        panel3.add(fieldOwner, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        browseOwner = new JButton();
        browseOwner.setText("...");
        panel3.add(browseOwner, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clearOwner = new JButton();
        this.$$$loadButtonText$$$(clearOwner, ResourceBundle.getBundle("i18n/warehouse").getString("common.clear.button"));
        panel3.add(clearOwner, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("warehouse.properties.responsible"));
        panel2.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldResponsible = new JComboBox();
        panel2.add(fieldResponsible, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("warehouse.properties.address"));
        panel2.add(label5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldAddress = new JTextField();
        panel2.add(fieldAddress, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("warehouse.properties.printers")));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("warehouse.properties.usualPrinter"));
        panel4.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("warehouse.properties.stickerPrinter"));
        panel4.add(label7, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usualPrinter = new JComboBox();
        panel4.add(usualPrinter, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stickerPrinter = new JComboBox();
        panel4.add(stickerPrinter, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(""));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel5.add(scrollPane2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 150), null, null, 0, false));
        allowedUsersList = new JList();
        scrollPane2.setViewportView(allowedUsersList);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        browseUsers = new JButton();
        browseUsers.setText("...");
        panel6.add(browseUsers, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel6.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        allowedUsersLabel = new JLabel();
        this.$$$loadLabelText$$$(allowedUsersLabel, ResourceBundle.getBundle("i18n/warehouse").getString("warehouse.properties.mayComplectUsers"));
        panel6.add(allowedUsersLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("warehouse.properties.tab.storagePlaces"), panel7);
        panel7.setBorder(BorderFactory.createTitledBorder(""));
        storagePlacePanel = new JPanel();
        storagePlacePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(storagePlacePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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
        return contentPane;
    }
}
