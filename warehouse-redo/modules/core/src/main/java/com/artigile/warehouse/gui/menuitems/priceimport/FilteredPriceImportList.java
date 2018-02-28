/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.priceimport;

import com.artigile.warehouse.gui.core.plugin.FramePlugin;
import com.artigile.warehouse.gui.core.properties.dialogs.Dialogs;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.menuitems.priceimport.importing.PriceImportList;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.EmailConfigTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class FilteredPriceImportList extends FramePlugin {

    private JPanel contentPanel;
    private JPanel priceImports;
    private JCheckBox lastImports;
    private JButton requestAllPriceLists;


    private PriceImportList priceImportList;


    /* Construction
    ------------------------------------------------------------------------------------------------------------------*/
    public FilteredPriceImportList() {
        $$$setupUI$$$();

        initComponents();
        initPriceImports();
        initListeners();
    }

    private void initComponents() {
        EmailConfigTO appEmailConfig = SpringServiceContext.getInstance().getEmailConfigService().getAppEmailConfig();
        requestAllPriceLists.setEnabled(appEmailConfig != null && appEmailConfig.isConfigured());
    }

    private void initPriceImports() {
        priceImportList = new PriceImportList();
        priceImports.removeAll();
        priceImports.add(new TableReport(priceImportList, this).getContentPanel(),
                GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    private void initListeners() {
        lastImports.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                onChangeLastImportsState();
            }
        });

        requestAllPriceLists.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRequestAllPriceLists();
            }
        });
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public String getTitle() {
        return I18nSupport.message("price.import.list.title");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    private void onChangeLastImportsState() {
        priceImportList.showOnlyLastImports(lastImports.isSelected());
        priceImportList.refreshData();
    }

    private void onRequestAllPriceLists() {
        Dialogs.runProperties(new PriceImportRequestAllForm());
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
        contentPanel.setLayout(new GridLayoutManager(2, 1, new Insets(2, 2, 2, 2), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(2, 0, 2, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        lastImports = new JCheckBox();
        lastImports.setSelected(true);
        this.$$$loadButtonText$$$(lastImports, ResourceBundle.getBundle("i18n/warehouse").getString("price.import.list.lastImports"));
        panel1.add(lastImports, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        requestAllPriceLists = new JButton();
        this.$$$loadButtonText$$$(requestAllPriceLists, ResourceBundle.getBundle("i18n/warehouse").getString("price.import.list.requestAllPriceLists"));
        panel1.add(requestAllPriceLists, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        priceImports = new JPanel();
        priceImports.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(priceImports, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
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