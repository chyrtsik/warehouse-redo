/*
 * Copyright (c) 2007-2013 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.admin.groups;

import com.artigile.warehouse.bl.admin.UserGroupService;
import com.artigile.warehouse.bl.admin.UserService;
import com.artigile.warehouse.domain.admin.UserGroup;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.exchange.ListItem;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.MessageDialogs;
import com.artigile.warehouse.gui.core.properties.dialogs.choosedialog.ChooseDialogResult;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.UserGroupTO;
import com.artigile.warehouse.utils.dto.UserPermissionTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 07.12.2008
 */
public class UserGroupForm implements PropertiesForm {

    private JPanel contentPanel;

    private JTextField textName;

    private JTextField textDescription;

    private JList permissionJList;

    private JButton choosePermissions;


    private UserGroupTO group;

    private boolean canEdit = false;

    /**
     * Service to save group with particular permissions.
     */
    private UserGroupService userGroupService = SpringServiceContext.getInstance().getUserGroupService();

    public UserGroupForm(UserGroupTO group, boolean canEdit) {
        this.group = group;
        this.canEdit = canEdit;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(textName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(textDescription, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        initListeners();
    }

    //===================== PropertiesForm implementation ================

    @Override
    public String getTitle() {
        return I18nSupport.message("user.groups.properties.title");
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
        textName.setText(group.getName());
        textDescription.setText(group.getDescription());
        DataExchange.setListItems(permissionJList, makePermissionsList(userGroupService.getPermissionByGroupId(group.getId())));
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(textName);
        //Group name must be unique
        UserGroup sameGroup = userGroupService.getGroupByName(textName.getText());
        if (sameGroup != null && sameGroup.getId() != group.getId()) {
            DataValidation.failRes(textName, "user.groups.properties.group.already.exists");
        }

        //Group may not have any permissions, by we should notice user about it.
        if (permissionJList.getModel().getSize() == 0) {
            String title = I18nSupport.message("user.groups.properties.groupWithoutPermissions.title");
            String message = I18nSupport.message("user.groups.properties.groupWithoutPermissions.message");
            if (!MessageDialogs.showConfirm(getContentPanel(), title, message)) {
                DataValidation.failSilent();
            }
        }

        //Administrators group should have rights to administer users/groups.
        if (group.isPredefined()) {
            List<UserPermissionTO> adminPermissions = userGroupService.getAdminPermissions();
            List<UserPermissionTO> selectedPermissions = getPermissions();
            if (!selectedPermissions.containsAll(adminPermissions)) {
                StringBuilder messageBuilder = new StringBuilder(I18nSupport.message("user.groups.properties.adminGroupShouldBeAbleToEditUsers.message"));
                for (UserPermissionTO adminPermission : adminPermissions) {
                    messageBuilder.append("\n").append(adminPermission.getName());
                }
                DataValidation.fail(messageBuilder.toString());
            }
        }
    }

    @Override
    public void saveData() {
        group.setName(textName.getText());
        group.setDescription(textDescription.getText());
    }

    //===========================  Helpers  ===============================

    private List<ListItem> makePermissionsList(List<UserPermissionTO> permissions) {
        List<ListItem> permissionsList = new ArrayList<ListItem>();
        for (UserPermissionTO permission : permissions) {
            permissionsList.add(new ListItem(permission.getName(), permission));
        }
        Collections.sort(permissionsList);
        return permissionsList;
    }

    public List<UserPermissionTO> getPermissions() {
        return DataExchange.getListItemsValues(permissionJList);
    }

    private void initListeners() {
        choosePermissions.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                onBrowsePermissions();
            }
        });
    }

    private void onBrowsePermissions() {
        //Choose a list of group's permissions.
        UserService userService = SpringServiceContext.getInstance().getUserService();
        List<ListItem> availablePermissions = makePermissionsList(userService.getAllPermissions());
        List<ListItem> selectedPermissions = DataExchange.getListItems(permissionJList);
        String title = I18nSupport.message("user.groups.properties.choosePermissions.title");

        ChooseDialogResult result = Dialogs.runChooseListDialog(title, availablePermissions, selectedPermissions);
        if (result.isOk()) {
            DataExchange.setListItems(permissionJList, result.getSelectedItems());
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
        contentPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        textDescription = new JTextField();
        panel1.add(textDescription, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("user.groups.properties.description"));
        panel1.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textName = new JTextField();
        panel1.add(textName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("user.groups.properties.name"));
        panel1.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        choosePermissions = new JButton();
        choosePermissions.setText("...");
        panel2.add(choosePermissions, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(450, 300), new Dimension(400, 200), null, 0, false));
        permissionJList = new JList();
        permissionJList.setSelectionMode(0);
        scrollPane1.setViewportView(permissionJList);
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("user.groups.properties.rights"));
        panel2.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }
}
