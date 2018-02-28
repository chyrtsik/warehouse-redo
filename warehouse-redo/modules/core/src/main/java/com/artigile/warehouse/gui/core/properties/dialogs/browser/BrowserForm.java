/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.core.properties.dialogs.browser;

import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.PropertiesOwner;
import com.artigile.warehouse.gui.core.properties.PropertiesOwnerWanting;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.report.command.CustomCommand;
import com.artigile.warehouse.gui.core.report.command.ReportCommandContext;
import com.artigile.warehouse.gui.core.report.command.availability.PredefinedCommandAvailability;
import com.artigile.warehouse.gui.core.report.command.naming.ResourceCommandNaming;
import com.artigile.warehouse.gui.core.report.controller.ReportDataSource;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.core.report.decorator.ReportCommandsDecorator;
import com.artigile.warehouse.gui.core.report.exceptions.ReportCommandException;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;

/**
 * @author Shyrik, 08.01.2009
 */
public class BrowserForm implements PropertiesForm, PropertiesOwnerWanting {
    private JPanel contentPanel;
    private JPanel listPanel;

    private ReportCommandsDecorator browserDataSource;
    private TableReport browserReport;
    private PropertiesOwner propertiesOwner;
    private Object selectedItem;

    public BrowserForm(ReportDataSource dataSource) {
        browserDataSource = new ReportCommandsDecorator(dataSource, new ChooseItemCommand());
    }

    //======================= PropertiesOwnerWanting ==========================

    @Override
    public void setPropertiesOwner(PropertiesOwner propertiesOwner) {
        this.propertiesOwner = propertiesOwner;
    }

    //=========================== PropertiesForm ==================================

    @Override
    public String getTitle() {
        return browserDataSource.getReportTitle();
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
        browserReport = new TableReport(browserDataSource);
        listPanel.add(browserReport.getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }

    @Override
    public void validateData() throws DataValidationException {
        if (getSelectedItem() == null) {
            DataValidation.fail(browserReport.getReportTable().getTable(), I18nSupport.message("validation.select.value"));
        }
    }

    @Override
    public void saveData() {
        //This method is called, when there is any selected item in the tableReport.
        //So, we know this item and needn't to perform extra saving.
    }

    //============================ Helpers ==========================================

    public Object getSelectedItem() {
        if (selectedItem == null) {
            return browserDataSource.getSelectedItem();
        } else {
            return selectedItem;
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
        listPanel = new JPanel();
        listPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(listPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(600, 450), null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }

    /**
     * Class for processing event of selecting item in the report by the user.
     */
    private class ChooseItemCommand extends CustomCommand {
        protected ChooseItemCommand() {
            super(new ResourceCommandNaming("browser.report.command.choose"), new PredefinedCommandAvailability(true));
        }

        public boolean doExecute(ReportCommandContext context) throws ReportCommandException {
            assert (context.getCurrentReportItem() != null);
            selectedItem = context.getCurrentReportItem();
            propertiesOwner.fireOK();
            return true;
        }
    }


}
