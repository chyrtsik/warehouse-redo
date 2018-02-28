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

import com.artigile.warehouse.bl.priceimport.PriceListRequestEmailService;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.menuitems.priceimport.importing.PriceImportContractorList;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.date.DateUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.priceimport.ContractorPriceImportTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.springframework.mail.MailException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class PriceImportRequestAllForm implements PropertiesForm {

    private JPanel contentPanel;
    private JCheckBox importDateFromOneToTwoWeeks;
    private JCheckBox importDateLessWeek;
    private JCheckBox importDateMoreTwoWeeks;
    private JPanel contractorsPanel;
    private JCheckBox requestForPastTwoDaysNotSend;
    private JTextArea messageText;

    /**
     * Report table with contractors
     */
    PriceImportContractorList priceImportContractorList;


    /* Construction
    ------------------------------------------------------------------------------------------------------------------*/
    public PriceImportRequestAllForm() {
        $$$setupUI$$$();
        initListeners();
        initComponents();
    }

    private void initListeners() {
        IntervalItemListener intervalItemListener = new IntervalItemListener();
        importDateMoreTwoWeeks.addItemListener(intervalItemListener);
        importDateFromOneToTwoWeeks.addItemListener(intervalItemListener);
        importDateLessWeek.addItemListener(intervalItemListener);
        requestForPastTwoDaysNotSend.addItemListener(intervalItemListener);
    }

    private void initComponents() {
        this.priceImportContractorList = new PriceImportContractorList();
        // Set default values
        priceImportContractorList.setPriceListRequestForPastTwoDays(true);
        priceImportContractorList.setImportDateMoreTwoWeeks(true);
        priceImportContractorList.setImportDateFromOneToTwoWeeks(true);

        // Init table with contractors
        contractorsPanel.removeAll();
        contractorsPanel.add((new TableReport(priceImportContractorList)).getContentPanel(),
                GridLayoutUtils.getGrowingAndFillingCellConstraints());
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public String getTitle() {
        return I18nSupport.message("price.import.request.all.properties.title");
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
        messageText.setText(PriceListRequestEmailService.buildMessageContent());
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(messageText);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void saveData() {
        boolean mailException = false;
        List<ContractorPriceImportTO> priceImports = priceImportContractorList.getReportData();
        List<ContractorTO> contractorsRecipients = new ArrayList<ContractorTO>();
        for (ContractorPriceImportTO priceImport : priceImports) {
            // E-mail address is required
            if (!StringUtils.isStringNullOrEmpty(priceImport.getContractor().getEmail())) {
                contractorsRecipients.add(priceImport.getContractor());
            }
        }

        // Send requests
        try {
            if (PriceListRequestEmailService.requestPriceLists(contractorsRecipients, messageText.getText())) {
                updateContractorPriceListRequest(contractorsRecipients);
            }
        } catch (MailException e) {
            mailException = true;
        }

        // Operation status notifications
        if (mailException && priceImports.size() == 1) {
            JOptionPane.showMessageDialog(contentPanel,
                    I18nSupport.message("price.import.fail.request.price.list.one.contractor"),
                    I18nSupport.message("error"),
                    JOptionPane.ERROR_MESSAGE);
        } else if (!mailException && priceImports.size() == 1) {
            JOptionPane.showMessageDialog(contentPanel,
                    I18nSupport.message("price.import.success.request.price.list.one.contractor"),
                    I18nSupport.message("information"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
        else if (mailException && priceImports.size() > 1) {
            JOptionPane.showMessageDialog(contentPanel,
                    I18nSupport.message("price.import.fail.request.price.list.many.contractors"),
                    I18nSupport.message("warning"),
                    JOptionPane.ERROR_MESSAGE);
        } else if (!mailException && priceImports.size() > 1) {
            JOptionPane.showMessageDialog(contentPanel,
                    I18nSupport.message("price.import.success.request.price.list.many.contractors"),
                    I18nSupport.message("information"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateContractorPriceListRequest(List<ContractorTO> contractors) {
        List<Long> contractorIDs = new ArrayList<Long>();
        for (ContractorTO contractor : contractors) {
            contractorIDs.add(contractor.getId());
        }
        SpringServiceContext.getInstance().getContractorService().updatePriceListRequestDatetime(contractorIDs, DateUtils.now());
    }

    private void onChangeIntervalItemState() {
        priceImportContractorList.setImportDateMoreTwoWeeks(importDateMoreTwoWeeks.isSelected());
        priceImportContractorList.setImportDateFromOneToTwoWeeks(importDateFromOneToTwoWeeks.isSelected());
        priceImportContractorList.setImportDateLessOneWeek(importDateLessWeek.isSelected());
        priceImportContractorList.setPriceListRequestForPastTwoDays(requestForPastTwoDaysNotSend.isSelected());

        priceImportContractorList.refreshData();
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
        contentPanel.setLayout(new GridLayoutManager(4, 1, new Insets(2, 2, 2, 2), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("i18n/warehouse").getString("price.import.request.all.send.request")));
        importDateFromOneToTwoWeeks = new JCheckBox();
        importDateFromOneToTwoWeeks.setSelected(true);
        this.$$$loadButtonText$$$(importDateFromOneToTwoWeeks, ResourceBundle.getBundle("i18n/warehouse").getString("price.import.request.all.from.one.to.two.weeks"));
        panel1.add(importDateFromOneToTwoWeeks, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        importDateLessWeek = new JCheckBox();
        this.$$$loadButtonText$$$(importDateLessWeek, ResourceBundle.getBundle("i18n/warehouse").getString("price.import.request.all.less.one.week"));
        panel1.add(importDateLessWeek, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        importDateMoreTwoWeeks = new JCheckBox();
        importDateMoreTwoWeeks.setSelected(true);
        this.$$$loadButtonText$$$(importDateMoreTwoWeeks, ResourceBundle.getBundle("i18n/warehouse").getString("price.import.request.all.more.two.weeks"));
        panel1.add(importDateMoreTwoWeeks, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        requestForPastTwoDaysNotSend = new JCheckBox();
        requestForPastTwoDaysNotSend.setSelected(true);
        this.$$$loadButtonText$$$(requestForPastTwoDaysNotSend, ResourceBundle.getBundle("i18n/warehouse").getString("price.import.request.all.not.send"));
        panel1.add(requestForPastTwoDaysNotSend, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        contractorsPanel = new JPanel();
        contractorsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(contractorsPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(500, 300), new Dimension(500, 300), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel2, new GridConstraints(2, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 150), new Dimension(-1, 150), 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("i18n/warehouse").getString("price.import.request.all.message.text")));
        messageText = new JTextArea();
        panel2.add(messageText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
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


    /* Local listeners
    ------------------------------------------------------------------------------------------------------------------*/
    private class IntervalItemListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            onChangeIntervalItemState();
        }
    }
}
