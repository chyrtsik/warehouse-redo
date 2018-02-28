/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.admin.users;

import com.artigile.warehouse.bl.admin.UserGroupService;
import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.bl.warehouse.WarehouseService;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.choosedialog.ChooseDialogResult;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.UserGroupTO;
import com.artigile.warehouse.utils.dto.UserTO;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * @author IoaN, 03.12.2008
 */
public class UserForm implements PropertiesForm {

    private JPanel contentPanel;

    private JTextField login;

    private JTextField firstName;

    private JTextField lastName;

    private JTextField email;

    private JList userGroups;

    private JPasswordField passField;

    private JPasswordField passConfirmField;

    private JButton browseUserGroups;

    private JButton browseWarehouses;

    private JList userWarehouses;

    private JTabbedPane tabbedPane1;

    private JLabel passLabel;

    private JLabel passConfirmLabel;

    private JCheckBox simplifiedWorkplace;

    private JTextField nameOnProduct;

    private UserService userService = SpringServiceContext.getInstance().getUserService();

    private UserTO user;

    private boolean canEdit;

    private boolean password;

    /**
     * @param user    - the user that will be edited
     * @param canEdit - flag that shows if the user can be updated.
     */
    public UserForm(UserTO user, boolean canEdit) {
        this(user, canEdit, true);
    }

    /**
     * @param user     - the user that will be edited
     * @param canEdit  - flag that shows if the user can be updated.
     * @param password - need to process password property
     */
    public UserForm(UserTO user, boolean canEdit, boolean password) {
        this.user = user;
        this.canEdit = canEdit;
        this.password = password;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(login, ModelFieldsLengths.USER_LOGIN_MAX_LENGTH);
        DataFiltering.setTextLengthLimit(firstName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(lastName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(email, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(passField, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(passConfirmField, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        initPassword();
        initListeners();
    }

    private void initPassword() {
        passLabel.setVisible(password);
        passField.setVisible(password);
        passConfirmLabel.setVisible(password);
        passConfirmField.setVisible(password);
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("user.properties.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public boolean canSaveData() {
        return canEdit;
    }

    @Override
    public void loadData() {
        firstName.setText(user.getFirstName());
        email.setText(user.getEmail());
        lastName.setText(user.getLastName());
        login.setText(user.getLogin());
        nameOnProduct.setText(user.getNameOnProduct());
        simplifiedWorkplace.setSelected(user.getSimplifiedWorkplace());
        DataExchange.setListItems(userGroups, makeGroupsList(userService.getGroupsByUserId(user.getId())));
        DataExchange.setListItems(userWarehouses, makeWarehousesList(userService.getUserMayComplectWarehouses(user.getId())));
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(login);
        //Searching user with such name
        UserTO sameUser = userService.getUserByLogin(login.getText());
        if (sameUser != null && sameUser.getId() != user.getId()) {
            DataValidation.failRes(login, "user.properties.user.already.exists");
        } else if (!userService.isValidUserLogin(login.getText())) {
            DataValidation.failRes(login, "user.validation.bad.login");
        }

        DataValidation.checkNotEmpty(firstName);
        DataValidation.checkNotEmpty(lastName);

        String nameOnProductText = nameOnProduct.getText();
        if (StringUtils.hasValue(nameOnProductText)) {
            UserTO userTO = userService.getUserByNameOnProduct(nameOnProductText);
            if (userTO != null && userTO.getId() != user.getId()) {
                DataValidation.failRes(nameOnProduct, "user.properties.user.name.on.product.already.exists");
            }
        }

        if (password) {
            DataValidation.checkNotEmpty(String.valueOf(passField.getPassword()), passField);
            if (!userService.isValidUserPassword(String.valueOf(passField.getPassword()))) {
                DataValidation.failRes(passField, "user.validation.bad.password");
            }

            DataValidation.checkNotEmpty(String.valueOf(passConfirmField.getPassword()), passConfirmField);
            DataValidation.checkEquals(String.valueOf(passField.getPassword()), String.valueOf(passConfirmField.getPassword()), passConfirmField);
        }

        if (userGroups.getModel().getSize() == 0) {
            DataValidation.failRes("user.validation.shouldBeInGroup");
        }
    }

    @Override
    public void saveData() {
        user.setEmail(email.getText());
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setNameOnProduct(nameOnProduct.getText());
        user.setSimplifiedWorkplace(simplifiedWorkplace.isSelected());
        user.setLogin(login.getText());
        if (password && passField.getPassword().length > 0) {
            user.setPassword(String.valueOf(passField.getPassword()));
        }
    }

    //=============================== Helpers ======================================

    public Set<UserGroupTO> getGroups() {
        return getGroupsList(DataExchange.getListItems(userGroups));
    }

    private Set<UserGroupTO> getGroupsList(List<ListItem> listItems) {
        Set<UserGroupTO> groups = new HashSet<UserGroupTO>();
        for (ListItem item : listItems) {
            groups.add((UserGroupTO) item.getValue());
        }
        return groups;
    }

    public List<WarehouseTOForReport> getWarehouses() {
        return getWarehousesList(DataExchange.getListItems(userWarehouses));
    }

    private List<WarehouseTOForReport> getWarehousesList(List<ListItem> listItems) {
        List<WarehouseTOForReport> warehouses = new ArrayList<WarehouseTOForReport>();
        for (ListItem item : listItems) {
            warehouses.add((WarehouseTOForReport) item.getValue());
        }
        return warehouses;
    }

    private List<ListItem> makeGroupsList(Set<UserGroupTO> groups) {
        List<ListItem> groupsList = new ArrayList<ListItem>();
        for (UserGroupTO group : groups) {
            groupsList.add(new ListItem(group.getName(), group));
        }
        return groupsList;
    }

    private List<ListItem> makeWarehousesList(Collection<WarehouseTOForReport> warehouses) {
        List<ListItem> warehousesList = new ArrayList<ListItem>();
        for (WarehouseTOForReport warehouse : warehouses) {
            warehousesList.add(new ListItem(warehouse.getName(), warehouse));
        }
        return warehousesList;
    }

    //===================== User input processing ==================================

    private void initListeners() {
        browseUserGroups.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onBrowseUserGroups();
            }
        });
        browseWarehouses.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                onBrowseWarehouses();
            }
        });
    }

    private void onBrowseUserGroups() {
        //Showing dialog for choosing user groups.
        UserGroupService userGroupService = SpringServiceContext.getInstance().getUserGroupService();
        List<ListItem> availableUserGroups = makeGroupsList(userGroupService.getAllGroups());
        List<ListItem> selectedUserGroups = DataExchange.getListItems(userGroups);
        String title = I18nSupport.message("user.properties.choose.groups.title");

        ChooseDialogResult result = Dialogs.runChooseListDialog(title, availableUserGroups, selectedUserGroups);
        if (result.isOk()) {
            DataExchange.setListItems(userGroups, result.getSelectedItems());
        }
    }

    private void onBrowseWarehouses() {
        //Showing dialog for choosing warehouses.
        WarehouseService warehouseService = SpringServiceContext.getInstance().getWarehouseService();
        List<ListItem> availableWarehouses = makeWarehousesList(warehouseService.getAllForReport());
        List<ListItem> selectedWarehouses = DataExchange.getListItems(userWarehouses);
        String title = I18nSupport.message("user.properties.choose.warehouses.title");

        ChooseDialogResult result = Dialogs.runChooseListDialog(title, availableWarehouses, selectedWarehouses);
        if (result.isOk()) {
            DataExchange.setListItems(userWarehouses, result.getSelectedItems());
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
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1 = new JTabbedPane();
        contentPanel.add(tabbedPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane1.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("user.properties.tab.general"), panel1);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("user.login.information")));
        login = new JTextField();
        panel2.add(login, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("user.login"));
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passLabel = new JLabel();
        this.$$$loadLabelText$$$(passLabel, ResourceBundle.getBundle("i18n/warehouse").getString("user.password"));
        panel2.add(passLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passField = new JPasswordField();
        panel2.add(passField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passConfirmField = new JPasswordField();
        panel2.add(passConfirmField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passConfirmLabel = new JLabel();
        this.$$$loadLabelText$$$(passConfirmLabel, ResourceBundle.getBundle("i18n/warehouse").getString("user.passwordConfirm"));
        panel2.add(passConfirmLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel3.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("user.personal.information")));
        firstName = new JTextField();
        panel3.add(firstName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("user.first.name"));
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("user.last.name"));
        panel3.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lastName = new JTextField();
        panel3.add(lastName, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("user.mail"));
        panel3.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        email = new JTextField();
        panel3.add(email, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("user.name.on.product"));
        panel3.add(label5, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nameOnProduct = new JTextField();
        panel3.add(nameOnProduct, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        simplifiedWorkplace = new JCheckBox();
        this.$$$loadButtonText$$$(simplifiedWorkplace, ResourceBundle.getBundle("i18n/warehouse").getString("user.simplified.workplace"));
        panel3.add(simplifiedWorkplace, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("user.properties.tab.groups"), panel4);
        panel4.setBorder(BorderFactory.createTitledBorder(""));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(400, 250), null, null, 0, false));
        userGroups = new JList();
        userGroups.setSelectionMode(0);
        scrollPane1.setViewportView(userGroups);
        browseUserGroups = new JButton();
        browseUserGroups.setText("...");
        browseUserGroups.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("user.properties.groups.tooltip"));
        panel4.add(browseUserGroups, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("user.properties.groups"));
        panel4.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane1.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("user.properties.tab.warehouses"), panel5);
        panel5.setBorder(BorderFactory.createTitledBorder(""));
        final Spacer spacer3 = new Spacer();
        panel5.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        browseWarehouses = new JButton();
        browseWarehouses.setText("...");
        browseWarehouses.setToolTipText(ResourceBundle.getBundle("i18n/warehouse").getString("user.properties.warehouses.tooltip"));
        panel5.add(browseWarehouses, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel5.add(scrollPane2, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        userWarehouses = new JList();
        scrollPane2.setViewportView(userWarehouses);
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("user.properties.warehouses"));
        panel5.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passLabel.setLabelFor(passField);
        passConfirmLabel.setLabelFor(passConfirmField);
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
