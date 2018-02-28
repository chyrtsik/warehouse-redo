/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.marketProposals;

import com.artigile.warehouse.bl.detail.DetailBatchService;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.core.report.controller.TableReport;
import com.artigile.warehouse.gui.menuitems.contractors.ContractorRatingList;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author: Vadim.Zverugo
 */

/**
 * Information about contractors proposals of some detail batch.
 */
public class MarketProposalsForm implements PropertiesForm {
    private JCheckBox checkRating1;
    private JCheckBox checkRating2;
    private JCheckBox checkRating3;
    private JCheckBox checkRating4;
    private JCheckBox checkRating5;
    private JCheckBox checkRatingUnknown;
    private JPanel contentPanel;
    private JPanel marketProposalsPanel;
    private JTextField fieldName;
    private JTextField fieldType;
    private JTextField fieldMisc;
    private JTextField fieldSalePrice;
    private JLabel fieldSalePriceCurrencyLabel;
    private JTextField fieldBuyPrice;
    private JLabel fieldBuyPriceCurrencyLabel;

    /**
     * Lists with ids of entities, which will be use as criteries for filter. 
     */
    private List<Long> detailBatchIds;
    private List<Long> contractorIds;
    private List<Long> currencyIds;
    private List<Long> measureUnitIds;

    /**
     * Object of table model with market proposals data.
     */
    MarketProposalsList marketProposalsList;

    public MarketProposalsForm(List<Long> detailBatchIds,
                               List<Long> contractorIds,
                               List<Long> currencyIds,
                               List<Long> measureUnitIds) {

        this.detailBatchIds = detailBatchIds;
        this.contractorIds = contractorIds;
        this.currencyIds = currencyIds;
        this.measureUnitIds = measureUnitIds;

        fieldName.setEditable(false);
        fieldType.setEditable(false);
        fieldMisc.setEditable(false);
        fieldBuyPrice.setEditable(false);
        fieldSalePrice.setEditable(false);

        /**
         * Initialize table model. Load market proposals data from Warehouse Global application by some filter.
         */
        this.marketProposalsList = new MarketProposalsList(detailBatchIds, contractorIds, currencyIds, measureUnitIds);
        /**
         * Default selected contractor rating is 1.
         */
        marketProposalsList.addMarketProposalsByContractorRating(ContractorRatingList.RATING_1.getContractorRating());

        initMarketProposalsList(marketProposalsPanel);
        initListenersForCheckRating();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("market.proposals.properties.panel.ourPriceList");
    }

    @Override
    public JPanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public boolean canSaveData() {
        return false;
    }

    @Override
    public void loadData() {
        DetailBatchService detailBatchService = SpringServiceContext.getInstance().getDetailBatchesService();
        /**
         * Single selection table model, therefore criteria lists will be contain one item. 
         */
        DetailBatchTO detailBatchTO = detailBatchService.getBatch(detailBatchIds.get(0));
        
        fieldName.setText(detailBatchTO.getName());
        fieldType.setText(detailBatchTO.getType());
        fieldMisc.setText(detailBatchTO.getMisc());
        fieldBuyPrice.setText(StringUtils.formatNumber(detailBatchTO.getBuyPrice()));
        fieldBuyPriceCurrencyLabel.setText(detailBatchTO.getCurrency().getSign());
        fieldSalePrice.setText(StringUtils.formatNumber(detailBatchTO.getSellPrice()));
        fieldSalePriceCurrencyLabel.setText(detailBatchTO.getCurrency().getSign());

        checkRating1.setSelected(true);
    }

    @Override
    public void validateData() throws DataValidationException { /* not use */ }

    @Override
    public void saveData() { /* not use */ }

    /**
     * Initialization of table with contractors proposals data.
     *
     * @param container - there is a place of table with contractors proposals data
     */
    private void initMarketProposalsList(JComponent container) {
        container.removeAll();
        container.add(new TableReport(marketProposalsList).getContentPanel(), GridLayoutUtils.getGrowingAndFillingCellConstraints());
        container.revalidate();
    }

    /**
     * Initialization of listeners for processing changes of rating filter.
     */
    private void initListenersForCheckRating() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox check = (JCheckBox) e.getSource();

                /**
                 * If selected rating '1'.
                 */
                if (check.getText().equals(ContractorRatingList.RATING_1.getContractorRating().toString())) {
                    if (check.isSelected()) {
                        marketProposalsList.addMarketProposalsByContractorRating(ContractorRatingList.RATING_1.getContractorRating());
                    } else if (!check.isSelected()) {
                        marketProposalsList.delMarketProposalByContractorRating(ContractorRatingList.RATING_1.getContractorRating());
                    }
                }

                /**
                 * If selected rating '2'.
                 */
                else if (check.getText().equals(ContractorRatingList.RATING_2.getContractorRating().toString())) {
                    if (check.isSelected()) {
                        marketProposalsList.addMarketProposalsByContractorRating(ContractorRatingList.RATING_2.getContractorRating());
                    } else if (!check.isSelected()) {
                        marketProposalsList.delMarketProposalByContractorRating(ContractorRatingList.RATING_2.getContractorRating());
                    }
                }

                /**
                 * If selected rating '3'.
                 */
                else if (check.getText().equals(ContractorRatingList.RATING_3.getContractorRating().toString())) {
                    if (check.isSelected()) {
                        marketProposalsList.addMarketProposalsByContractorRating(ContractorRatingList.RATING_3.getContractorRating());
                    } else if (!check.isSelected()) {
                        marketProposalsList.delMarketProposalByContractorRating(ContractorRatingList.RATING_3.getContractorRating());
                    }
                }

                /**
                 * If selected rating '4'.
                 */
                else if (check.getText().equals(ContractorRatingList.RATING_4.getContractorRating().toString())) {
                    if (check.isSelected()) {
                        marketProposalsList.addMarketProposalsByContractorRating(ContractorRatingList.RATING_4.getContractorRating());
                    } else if (!check.isSelected()) {
                        marketProposalsList.delMarketProposalByContractorRating(ContractorRatingList.RATING_4.getContractorRating());
                    }
                }

                /**
                 * If selected rating '5'.
                 */
                else if (check.getText().equals(ContractorRatingList.RATING_5.getContractorRating().toString())) {
                    if (check.isSelected()) {
                        marketProposalsList.addMarketProposalsByContractorRating(ContractorRatingList.RATING_5.getContractorRating());
                    } else if (!check.isSelected()) {
                        marketProposalsList.delMarketProposalByContractorRating(ContractorRatingList.RATING_5.getContractorRating());
                    }
                }

                /**
                 * If selected 'Without rating'.
                 */
                else if (check.getText().equals(I18nSupport.message("market.proposals.properties.checkBox.withoutRating"))) {
                    if (check.isSelected()) {
                        marketProposalsList.addMarketProposalsByContractorRating(ContractorRatingList.RATING_UNKNOWN.getContractorRating());
                    } else if (!check.isSelected()) {
                        marketProposalsList.delMarketProposalByContractorRating(ContractorRatingList.RATING_UNKNOWN.getContractorRating());
                    }
                }

                marketProposalsList.refreshData();
            }
        };

        checkRating1.addActionListener(actionListener);
        checkRating2.addActionListener(actionListener);
        checkRating3.addActionListener(actionListener);
        checkRating4.addActionListener(actionListener);
        checkRating5.addActionListener(actionListener);
        checkRatingUnknown.addActionListener(actionListener);
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
        contentPanel.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 3, new Insets(0, 5, 5, 5), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("i18n/warehouse").getString("market.proposals.properties.panel.ourPriceList"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font(panel2.getFont().getName(), panel2.getFont().getStyle(), panel2.getFont().getSize()), new Color(-16777216)));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("market.proposals.properties.detailName"));
        panel3.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldName = new JTextField();
        panel3.add(fieldName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("market.proposals.properties.detailType"));
        panel3.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldType = new JTextField();
        panel3.add(fieldType, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("market.proposals.properties.detailMisc"));
        panel3.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldMisc = new JTextField();
        panel3.add(fieldMisc, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 11, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("market.proposals.properties.buyPrice"));
        panel4.add(label4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldBuyPrice = new JTextField();
        panel4.add(fieldBuyPrice, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("market.proposals.properties.salePrice"));
        panel4.add(label5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldSalePrice = new JTextField();
        panel4.add(fieldSalePrice, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fieldBuyPriceCurrencyLabel = new JLabel();
        fieldBuyPriceCurrencyLabel.setText("<Currency>");
        panel4.add(fieldBuyPriceCurrencyLabel, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldSalePriceCurrencyLabel = new JLabel();
        fieldSalePriceCurrencyLabel.setText("<Currency>");
        panel4.add(fieldSalePriceCurrencyLabel, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel4.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel4.add(spacer2, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel4.add(spacer3, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel4.add(spacer4, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel4.add(spacer5, new GridConstraints(0, 7, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel4.add(spacer6, new GridConstraints(0, 8, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel4.add(spacer7, new GridConstraints(0, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel4.add(spacer8, new GridConstraints(0, 10, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 7, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("market.proposals.properties.label.filteringByRating"));
        panel5.add(label6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkRating1 = new JCheckBox();
        checkRating1.setText("1");
        panel5.add(checkRating1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkRating2 = new JCheckBox();
        checkRating2.setText("2");
        panel5.add(checkRating2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkRating3 = new JCheckBox();
        checkRating3.setText("3");
        panel5.add(checkRating3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkRating4 = new JCheckBox();
        checkRating4.setText("4");
        panel5.add(checkRating4, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkRating5 = new JCheckBox();
        checkRating5.setText("5");
        panel5.add(checkRating5, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        checkRatingUnknown = new JCheckBox();
        this.$$$loadButtonText$$$(checkRatingUnknown, ResourceBundle.getBundle("i18n/warehouse").getString("market.proposals.properties.checkBox.withoutRating"));
        panel5.add(checkRatingUnknown, new GridConstraints(0, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        marketProposalsPanel = new JPanel();
        marketProposalsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        contentPanel.add(marketProposalsPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 250), null, null, 0, false));
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
