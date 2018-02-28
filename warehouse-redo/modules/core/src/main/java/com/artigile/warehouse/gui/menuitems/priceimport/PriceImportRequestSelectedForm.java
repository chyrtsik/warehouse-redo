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
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.date.DateUtils;
import com.artigile.warehouse.utils.dto.ContractorTO;
import com.artigile.warehouse.utils.dto.EmailConfigTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.springframework.mail.MailException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public class PriceImportRequestSelectedForm implements PropertiesForm {

    private JPanel contentPanel;
    private JTextArea messageText;
    private JTextField subject;


    private List<ContractorTO> contractors;


    /* Construction
    ------------------------------------------------------------------------------------------------------------------*/
    public PriceImportRequestSelectedForm(List<ContractorTO> contractors) {
        this.contractors = contractors;
        $$$setupUI$$$();
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    public String getTitle() {
        return I18nSupport.message("price.import.request.selected.properties.title");
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
        EmailConfigTO appEmailConfig = SpringServiceContext.getInstance().getEmailConfigService().getAppEmailConfig();
        if (appEmailConfig != null) {
            subject.setText(appEmailConfig.getPriceListRequestMessageSubject());
            subject.setEditable(false);
        }
        messageText.setText(PriceListRequestEmailService.buildMessageContent());
    }

    @Override
    public void validateData() throws DataValidationException {
        if (subject.isEditable()) {
            DataValidation.checkNotEmpty(subject);
        }
        DataValidation.checkNotEmpty(messageText);
    }

    @Override
    public void saveData() {
        boolean mailException = false;

        // Send requests
        try {
            if (PriceListRequestEmailService.requestPriceLists(contractors, messageText.getText())) {
                updateContractorPriceListRequest(contractors);
            }
        } catch (MailException e) {
            mailException = true;
        }

        // Operation status notifications
        if (mailException && contractors.size() == 1) {
            JOptionPane.showMessageDialog(contentPanel,
                    I18nSupport.message("price.import.fail.request.price.list.one.contractor"),
                    I18nSupport.message("error"),
                    JOptionPane.ERROR_MESSAGE);
        } else if (!mailException && contractors.size() == 1) {
            JOptionPane.showMessageDialog(contentPanel,
                    I18nSupport.message("price.import.success.request.price.list.one.contractor"),
                    I18nSupport.message("information"),
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (mailException && contractors.size() > 1) {
            JOptionPane.showMessageDialog(contentPanel,
                    I18nSupport.message("price.import.fail.request.price.list.many.contractors"),
                    I18nSupport.message("error"),
                    JOptionPane.ERROR_MESSAGE);
        } else if (!mailException && contractors.size() > 1) {
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


    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayoutManager(3, 1, new Insets(4, 2, 2, 2), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(500, 200), new Dimension(500, 200), null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("i18n/warehouse").getString("price.import.request.selected.message.text")));
        messageText = new JTextArea();
        panel1.add(messageText, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("price.import.request.selected.properties.subject"));
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        subject = new JTextField();
        subject.setEditable(true);
        subject.setEnabled(true);
        panel2.add(subject, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(200, -1), null, 0, false));
        final JSeparator separator1 = new JSeparator();
        contentPanel.add(separator1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
