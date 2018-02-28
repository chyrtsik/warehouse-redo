/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.warehouse.warehouselist;

import com.artigile.warehouse.bl.warehouse.WarehouseFilter;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.exchange.DataExchange;
import com.artigile.warehouse.gui.core.properties.data.init.InitUtils;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseTOForReport;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * @author IoaN
 */
public class WarehouseSelectForm implements PropertiesForm {
    private JPanel contentPane;
    private JComboBox warehouseCombo;
    private WarehouseFilter filter;

    public WarehouseSelectForm() {
        this(null);
    }

    public WarehouseSelectForm(WarehouseFilter filter) {
        this.filter = filter;
    }

    /**
     * Warehouse, where is the user stay now.
     */
    private WarehouseTOForReport warehouse;

    public WarehouseTOForReport getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(WarehouseTOForReport warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("choose.warehouse.title");
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
        InitUtils.initWarehousesCombo(warehouseCombo, filter, null);
        DataExchange.selectComboItem(warehouseCombo, warehouse);
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkSelected(warehouseCombo);
    }

    @Override
    public void saveData() {
        warehouse = (WarehouseTOForReport) DataExchange.getComboSelection(warehouseCombo);
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
        contentPane.setLayout(new FormLayout("fill:max(d;4px):noGrow,left:100dlu:grow", "center:d:grow"));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("login.warehouse"));
        CellConstraints cc = new CellConstraints();
        contentPane.add(label1, cc.xy(1, 1));
        warehouseCombo = new JComboBox();
        contentPane.add(warehouseCombo, cc.xy(2, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
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
        return contentPane;
    }
}