/*
 * Copyright (c) 2007-2011 Artigile.
 * Software development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.nboptions.whview;

import com.artigile.warehouse.gui.core.splitter.SplitPaneManager;
import com.artigile.warehouse.gui.core.splitter.SplitPaneSettings;
import com.artigile.warehouse.gui.menuitems.configuration.preferences.SplitPaneOrientation;
import com.artigile.warehouse.gui.nboptions.OptionsPanel;
import com.artigile.warehouse.gui.utils.GridLayoutUtils;
import com.artigile.warehouse.utils.StringUtils;
import com.artigile.warehouse.utils.formatter.FormatUtils;
import com.artigile.warehouse.utils.preferences.SystemPreferences;
import com.artigile.warehouse.utils.preferences.SystemPreferencesUtils;
import com.artigile.warehouse.utils.properties.savers.SplitPaneOrietationSaver;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * @author Vadim Zverugo (vadim.zverugo@artigile.by)
 */
public final class WarehouseViewOptionsPanel extends OptionsPanel {

    /**
     * It used for showing in the example field
     */
    private static final double exampleNumber = 5555555.5555555;

    private JPanel rootPanel;
    private JTextField nfPattern;
    private JTextField nfDecimalSeparator;
    private JTextField nfGroupSeparator;
    private JTextField nfExample;
    private JLabel nfNotValid;
    private JPanel reportSplitterOrientation;
    private JPanel reportTitle;

    /**
     * Map that contains orientation preferences
     */
    private Map<SplitPaneOrientation, Class> splitPaneOrientationMap;


    /* Construction
    ------------------------------------------------------------------------------------------------------------------*/
    public WarehouseViewOptionsPanel() {
        this.splitPaneOrientationMap = new HashMap<SplitPaneOrientation, Class>();

        // Init components
        $$$setupUI$$$();
        // Add components on the panel of options
        // DON'T FORGET TO DO THIS!
        applyDefaultLayout();
        this.add($$$getRootComponent$$$());

        // Other initialization
        initListeners();
    }

    private void initListeners() {
        NumberFormatListener numberFormatListener = new NumberFormatListener();
        nfPattern.getDocument().addDocumentListener(numberFormatListener);
        nfDecimalSeparator.getDocument().addDocumentListener(numberFormatListener);
        nfGroupSeparator.getDocument().addDocumentListener(numberFormatListener);
    }


    /* Main methods
    ------------------------------------------------------------------------------------------------------------------*/
    @Override
    protected void load() {
        // Splitter orientation loading
        clearReportOrientationPanels();
        List<SplitPaneSettings> splitPaneSettingsList = SplitPaneManager.getInstance().getSplitPaneSettings();
        for (SplitPaneSettings settings : splitPaneSettingsList) {
            // Create UI
            SplitPaneOrientation splitPaneOrientation = new SplitPaneOrientation(SplitPaneOrietationSaver.getOrientation(settings.getClazz()));
            splitPaneOrientationMap.put(splitPaneOrientation, settings.getClazz());
            JPanel reportTitlePanel = new JPanel(new GridLayoutManager(1, 1));
            reportTitlePanel.add(new Label(settings.getTitle()), GridLayoutUtils.getCenteredCellConstraints());
            reportTitle.add(reportTitlePanel);
            reportSplitterOrientation.add(splitPaneOrientation);
        }

        // Number format loading
        String numberPattern = SystemPreferencesUtils.getSysOption(SystemPreferences.NUMBER_FORMAT_PATTERN);
        String numberDecimalSeparator = SystemPreferencesUtils.getSysOption(SystemPreferences.NUMBER_FORMAT_DECIMAL_SEPARATOR);
        String numberGroupSeparator = SystemPreferencesUtils.getSysOption(SystemPreferences.NUMBER_FORMAT_GROUP_SEPARATOR);
        nfPattern.setText(numberPattern != null ? numberPattern : FormatUtils.getDefaultPattern());
        nfDecimalSeparator.setText(numberDecimalSeparator != null
                ? numberDecimalSeparator : FormatUtils.getDefaultDecimalSeparator().toString());
        nfGroupSeparator.setText(numberGroupSeparator != null
                ? numberGroupSeparator : FormatUtils.getDefaultGroupSeparator().toString());

        updateExampleNumber();
    }

    private void clearReportOrientationPanels() {
        reportTitle.removeAll();
        reportSplitterOrientation.removeAll();
    }

    @Override
    protected void store() {
        // Splitter orientation saving
        for (Map.Entry<SplitPaneOrientation, Class> orientationPair : splitPaneOrientationMap.entrySet()) {
            SplitPaneOrietationSaver.store(orientationPair.getKey().getOrientation(), orientationPair.getValue());
        }

        // Number format saving
        if (validNumberFormat()) {
            SystemPreferencesUtils.setSysOption(SystemPreferences.NUMBER_FORMAT_PATTERN, nfPattern.getText());
            SystemPreferencesUtils.setSysOption(SystemPreferences.NUMBER_FORMAT_DECIMAL_SEPARATOR, nfDecimalSeparator.getText());
            SystemPreferencesUtils.setSysOption(SystemPreferences.NUMBER_FORMAT_GROUP_SEPARATOR, nfGroupSeparator.getText());
            // Apply number format
            SystemPreferencesUtils.applySysOption(SystemPreferences.NUMBER_FORMAT_PATTERN);
        }
    }

    @Override
    protected boolean valid() {
        return true;
    }

    private boolean validNumberFormat() {
        String pattern = nfPattern.getText();
        if (StringUtils.containsSymbols(pattern) && FormatUtils.isValidPattern(pattern)) {
            String decimalS = nfDecimalSeparator.getText();
            if (StringUtils.containsSymbols(decimalS) && decimalS.length() == 1) {
                String groupS = nfGroupSeparator.getText();
                if (groupS != null && groupS.length() == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Updates number in the example field.
     */
    private void updateExampleNumber() {
        if (validNumberFormat()) {
            String pattern = nfPattern.getText();
            Character decimalS = nfDecimalSeparator.getText().charAt(0);
            Character groupS = nfGroupSeparator.getText().charAt(0);
            nfExample.setText(FormatUtils.format(exampleNumber, pattern, decimalS, groupS));
            nfNotValid.setVisible(false);
        } else {
            nfExample.setText(null);
            nfNotValid.setVisible(true);
        }
    }

    private void createUIComponents() {
        reportTitle = new JPanel();
        reportTitle.setLayout(new BoxLayout(reportTitle, BoxLayout.PAGE_AXIS));
        reportSplitterOrientation = new JPanel();
        reportSplitterOrientation.setLayout(new BoxLayout(reportSplitterOrientation, BoxLayout.PAGE_AXIS));
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 4, 4, 0), -1, -1));
        rootPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("com/artigile/warehouse/gui/nboptions/whview/Bundle").getString("warehouse.options.view.splitter.orientation.title")));
        panel1.add(reportSplitterOrientation, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.add(reportTitle, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JSeparator separator1 = new JSeparator();
        separator1.setOrientation(1);
        panel1.add(separator1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        rootPanel.add(spacer1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(4, 2, new Insets(0, 4, 4, 0), -1, -1));
        rootPanel.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), ResourceBundle.getBundle("com/artigile/warehouse/gui/nboptions/whview/Bundle").getString("warehouse.options.view.format.number.title")));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("com/artigile/warehouse/gui/nboptions/whview/Bundle").getString("warehouse.options.view.format.number.pattern"));
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("com/artigile/warehouse/gui/nboptions/whview/Bundle").getString("warehouse.options.view.format.number.separator.decimal"));
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("com/artigile/warehouse/gui/nboptions/whview/Bundle").getString("warehouse.options.view.format.number.separator.group"));
        panel2.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("com/artigile/warehouse/gui/nboptions/whview/Bundle").getString("warehouse.options.view.format.number.separator.example"));
        panel2.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nfPattern = new JTextField();
        panel2.add(nfPattern, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), new Dimension(150, -1), 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nfDecimalSeparator = new JTextField();
        panel3.add(nfDecimalSeparator, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), new Dimension(30, -1), 0, false));
        final JLabel label5 = new JLabel();
        label5.setHorizontalAlignment(2);
        label5.setHorizontalTextPosition(11);
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("com/artigile/warehouse/gui/nboptions/whview/Bundle").getString("warehouse.options.view.format.number.separator.decimal.desc"));
        panel3.add(label5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nfGroupSeparator = new JTextField();
        panel4.add(nfGroupSeparator, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(30, -1), new Dimension(30, -1), 0, false));
        final JLabel label6 = new JLabel();
        label6.setHorizontalAlignment(2);
        label6.setHorizontalTextPosition(11);
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("com/artigile/warehouse/gui/nboptions/whview/Bundle").getString("warehouse.options.view.format.number.separator.group.desc"));
        panel4.add(label6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel5, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        nfExample = new JTextField();
        nfExample.setEditable(false);
        panel5.add(nfExample, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), new Dimension(150, -1), 0, false));
        nfNotValid = new JLabel();
        nfNotValid.setForeground(new Color(-52429));
        this.$$$loadLabelText$$$(nfNotValid, ResourceBundle.getBundle("com/artigile/warehouse/gui/nboptions/whview/Bundle").getString("warehouse.options.view.format.number.notValid"));
        panel5.add(nfNotValid, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
        return rootPanel;
    }

    /**
     * Action listener on changing number format fields.
     */
    private class NumberFormatListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateExampleNumber();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateExampleNumber();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateExampleNumber();
        }
    }
}
