/*
 * Copyright (c) 2007-2011 Artigile.
 * Software art development company.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Artigile. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Artigile software company.
 */

package com.artigile.warehouse.gui.menuitems.finance.currency;

import com.artigile.warehouse.domain.Gender;
import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.filtering.DataFiltering;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.utils.ModelFieldsLengths;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.CurrencyTO;
import com.artigile.warehouse.utils.dto.CurrencyWordTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * @author Shyrik, 08.08.2009
 */
public class CurrencyForm implements PropertiesForm {
    private JPanel contentPanel;
    private JTextField fieldName;
    private JTextField fieldSign;
    private JCheckBox fieldDefaultCurrency;
    private JTabbedPane tabbedPane1;
    private JTextField unitWord;
    private JTextField twoUnitsWord;
    private JTextField fiveUnitsWord;
    private JRadioButton masculine;
    private JRadioButton feminine;
    private JRadioButton neuter;
    private JRadioButton fractionalMasculine;
    private JRadioButton fractionalFeminine;
    private JRadioButton fractionalNeuter;
    private JTextField fractionalUnitWord;
    private JTextField fractionalTwoUnitsWord;
    private JTextField fractionalFiveUnitsWord;
    private JCheckBox hasFractionalPart;
    private JTextField fractionalPrecision;
    private JButton colorChooser;
    private JPanel currencyColor;

    CurrencyTO currency;
    boolean canEdit;

    public CurrencyForm(CurrencyTO currency, boolean canEdit) {
        this.currency = currency;
        this.canEdit = canEdit;

        // Limitation of text length in the fields
        DataFiltering.setTextLengthLimit(fieldName, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fieldSign, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(unitWord, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(twoUnitsWord, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fiveUnitsWord, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fractionalUnitWord, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fractionalTwoUnitsWord, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);
        DataFiltering.setTextLengthLimit(fractionalFiveUnitsWord, ModelFieldsLengths.DEFAULT_MAX_TEXT_LENGTH);

        initListeners();
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("currency.properties.title");
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
        fieldSign.setText(currency.getSign());
        fieldName.setText(currency.getName());
        fieldDefaultCurrency.setSelected(currency.getDefaultCurrency());
        currencyColor.setBackground(new Color(currency.getAssociatedColor()));

        CurrencyWordTO currencyWord = currency.getCurrencyWord();
        if (currencyWord != null) {
            unitWord.setText(currencyWord.getUnitWord());
            twoUnitsWord.setText(currencyWord.getTwoUnitsWord());
            fiveUnitsWord.setText(currencyWord.getFiveUnitsWord());
            switch (currencyWord.getGender()) {
                case MASCULINE:
                    masculine.setSelected(true);
                    break;
                case FEMININE:
                    feminine.setSelected(true);
                    break;
                case NEUTER:
                    neuter.setSelected(true);
                    break;
            }
            hasFractionalPart.setSelected(currencyWord.getFractionalPart());
            if (currencyWord.getFractionalPrecision() > 0) {
                fractionalPrecision.setText(String.valueOf(currencyWord.getFractionalPrecision()));
            }
            fractionalUnitWord.setText(currencyWord.getFractionalUnitWord());
            fractionalTwoUnitsWord.setText(currencyWord.getFractionalTwoUnitsWord());
            fractionalFiveUnitsWord.setText(currencyWord.getFractionalFiveUnitsWord());
            Gender fractionalGender = currencyWord.getFractionalGender();
            if (fractionalGender != null) {
                switch (fractionalGender) {
                    case MASCULINE:
                        fractionalMasculine.setSelected(true);
                        break;
                    case FEMININE:
                        fractionalFeminine.setSelected(true);
                        break;
                    case NEUTER:
                        fractionalNeuter.setSelected(true);
                        break;
                }
            }
        }
        enableFractionalPart(hasFractionalPart.isSelected());
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(fieldSign);
        if (!SpringServiceContext.getInstance().getCurrencyService().isUniqueCurrencySign(fieldSign.getText(), currency.getId())) {
            DataValidation.failRes(fieldSign, "currency.properties.not.unique.sign");
        }
        DataValidation.checkNotEmpty(fieldName);
        if (!SpringServiceContext.getInstance().getCurrencyService().isUniqueCurrencyName(fieldName.getText(), currency.getId())) {
            DataValidation.failRes(fieldName, "currency.properties.not.unique.name");
        }

        String fracPrec = fractionalPrecision.getText();
        if (hasFractionalPart.isSelected()) {
            DataValidation.checkIsNumber(fractionalPrecision);
            if (!fracPrec.isEmpty()) {
                DataValidation.checkPositiveValue(Integer.valueOf(fracPrec), fractionalPrecision);
            }
            DataValidation.checkNotEmpty(fractionalUnitWord);
            DataValidation.checkNotEmpty(fractionalTwoUnitsWord);
            DataValidation.checkNotEmpty(fractionalFiveUnitsWord);
        } else if (!fracPrec.isEmpty()) {
            DataValidation.checkIsNumber(fractionalPrecision);
            if (!fracPrec.isEmpty()) {
                DataValidation.checkPositiveValue(Integer.valueOf(fracPrec), fractionalPrecision);
            }
        }
    }

    @Override
    public void saveData() {
        currency.setSign(fieldSign.getText());
        currency.setName(fieldName.getText());
        currency.setDefaultCurrency(fieldDefaultCurrency.isSelected());
        currency.setAssociatedColor(currencyColor.getBackground().getRGB());

        CurrencyWordTO currencyWord = currency.getCurrencyWord();
        if (currencyWord == null) {
            currencyWord = new CurrencyWordTO();
            currency.setCurrencyWord(currencyWord);
        }

        currencyWord.setUnitWord(unitWord.getText());
        currencyWord.setTwoUnitsWord(twoUnitsWord.getText());
        currencyWord.setFiveUnitsWord(fiveUnitsWord.getText());

        if (masculine.isSelected()) {
            currencyWord.setGender(Gender.MASCULINE);
        } else if (feminine.isSelected()) {
            currencyWord.setGender(Gender.FEMININE);
        } else if (neuter.isSelected()) {
            currencyWord.setGender(Gender.NEUTER);
        }

        currencyWord.setFractionalPart(hasFractionalPart.isSelected());
        String fracPrec = fractionalPrecision.getText();
        currencyWord.setFractionalPrecision(fracPrec.isEmpty() ? 0 : Integer.valueOf(fracPrec));
        currencyWord.setFractionalUnitWord(fractionalUnitWord.getText());
        currencyWord.setFractionalTwoUnitsWord(fractionalTwoUnitsWord.getText());
        currencyWord.setFractionalFiveUnitsWord(fractionalFiveUnitsWord.getText());

        if (fractionalMasculine.isSelected()) {
            currencyWord.setFractionalGender(Gender.MASCULINE);
        } else if (fractionalFeminine.isSelected()) {
            currencyWord.setFractionalGender(Gender.FEMININE);
        } else if (fractionalNeuter.isSelected()) {
            currencyWord.setFractionalGender(Gender.NEUTER);
        }
    }

    private void enableFractionalPart(boolean enabled) {
        fractionalPrecision.setEnabled(enabled);
        fractionalMasculine.setEnabled(enabled);
        fractionalFeminine.setEnabled(enabled);
        fractionalNeuter.setEnabled(enabled);
        fractionalUnitWord.setEnabled(enabled);
        fractionalTwoUnitsWord.setEnabled(enabled);
        fractionalFiveUnitsWord.setEnabled(enabled);
    }

    /**
     * Opens dialog for choosing currency color.
     */
    private void chooseCurrencyColor() {
        Color selectedColor = JColorChooser.showDialog(
                contentPanel,
                I18nSupport.message("currency.properties.color.chooser.title"),
                currencyColor.getBackground());
        if (selectedColor != null) {
            currencyColor.setBackground(selectedColor);
        }
    }

    private void initListeners() {
        hasFractionalPart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableFractionalPart(hasFractionalPart.isSelected());
            }
        });

        colorChooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooseCurrencyColor();
            }
        });
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
        panel1.setLayout(new GridLayoutManager(4, 3, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane1.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("currency.properties.main.tab"), panel1);
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("currency.properties.sign"));
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldSign = new JTextField();
        panel1.add(fieldSign, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(130, 22), null, 0, false));
        fieldDefaultCurrency = new JCheckBox();
        this.$$$loadButtonText$$$(fieldDefaultCurrency, ResourceBundle.getBundle("i18n/warehouse").getString("currency.properties.default"));
        panel1.add(fieldDefaultCurrency, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldName = new JTextField();
        panel1.add(fieldName, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("currency.properties.name"));
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(130, 14), null, 0, false));
        final JLabel label3 = new JLabel();
        this.$$$loadLabelText$$$(label3, ResourceBundle.getBundle("i18n/warehouse").getString("currency.properties.color"));
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        currencyColor = new JPanel();
        currencyColor.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        currencyColor.setBackground(new Color(-1));
        panel2.add(currencyColor, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(50, 15), null, 0, false));
        currencyColor.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-16777216)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(-16777216)));
        colorChooser = new JButton();
        colorChooser.setEnabled(true);
        colorChooser.setText("...");
        panel2.add(colorChooser, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, new Dimension(50, -1), 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(5, 5, new Insets(5, 5, 5, 5), -1, -1));
        tabbedPane1.addTab(ResourceBundle.getBundle("i18n/warehouse").getString("currency.properties.word.tab"), panel3);
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(4, 5, new Insets(5, 5, 5, 5), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.integer.part")));
        feminine = new JRadioButton();
        this.$$$loadButtonText$$$(feminine, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.gender.feminine"));
        panel4.add(feminine, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        neuter = new JRadioButton();
        this.$$$loadButtonText$$$(neuter, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.gender.neuter"));
        panel4.add(neuter, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel4.add(spacer3, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.unit.word"));
        panel4.add(label4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.two.units.word"));
        panel4.add(label5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.five.units.word"));
        panel4.add(label6, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        unitWord = new JTextField();
        panel4.add(unitWord, new GridConstraints(1, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        twoUnitsWord = new JTextField();
        panel4.add(twoUnitsWord, new GridConstraints(2, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fiveUnitsWord = new JTextField();
        panel4.add(fiveUnitsWord, new GridConstraints(3, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        masculine = new JRadioButton();
        masculine.setSelected(true);
        this.$$$loadButtonText$$$(masculine, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.gender.masculine"));
        panel4.add(masculine, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(5, 5, new Insets(5, 5, 5, 5), -1, -1));
        panel3.add(panel5, new GridConstraints(2, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel5.setBorder(BorderFactory.createTitledBorder(ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.fractional.part")));
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.unit.word"));
        panel5.add(label7, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.two.units.word"));
        panel5.add(label8, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.five.units.word"));
        panel5.add(label9, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel5.add(spacer4, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        fractionalUnitWord = new JTextField();
        panel5.add(fractionalUnitWord, new GridConstraints(2, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fractionalTwoUnitsWord = new JTextField();
        panel5.add(fractionalTwoUnitsWord, new GridConstraints(3, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fractionalFiveUnitsWord = new JTextField();
        panel5.add(fractionalFiveUnitsWord, new GridConstraints(4, 1, 1, 4, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        fractionalPrecision = new JTextField();
        panel5.add(fractionalPrecision, new GridConstraints(0, 2, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label10 = new JLabel();
        this.$$$loadLabelText$$$(label10, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.fractional.precision"));
        panel5.add(label10, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fractionalMasculine = new JRadioButton();
        fractionalMasculine.setSelected(true);
        this.$$$loadButtonText$$$(fractionalMasculine, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.gender.masculine"));
        panel5.add(fractionalMasculine, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fractionalFeminine = new JRadioButton();
        this.$$$loadButtonText$$$(fractionalFeminine, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.gender.feminine"));
        panel5.add(fractionalFeminine, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fractionalNeuter = new JRadioButton();
        this.$$$loadButtonText$$$(fractionalNeuter, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.gender.neuter"));
        panel5.add(fractionalNeuter, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        hasFractionalPart = new JCheckBox();
        this.$$$loadButtonText$$$(hasFractionalPart, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.properties.has.fractional.part"));
        panel3.add(hasFractionalPart, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel6, new GridConstraints(3, 0, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 40), new Dimension(-1, 40), null, 0, false));
        final JLabel label11 = new JLabel();
        this.$$$loadLabelText$$$(label11, ResourceBundle.getBundle("i18n/warehouse").getString("currency.word.hint"));
        panel6.add(label11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, 1, 1, null, null, null, 0, false));
        label4.setLabelFor(unitWord);
        label5.setLabelFor(twoUnitsWord);
        label6.setLabelFor(fiveUnitsWord);
        label10.setLabelFor(fractionalPrecision);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(neuter);
        buttonGroup.add(neuter);
        buttonGroup.add(feminine);
        buttonGroup.add(masculine);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(fractionalFeminine);
        buttonGroup.add(fractionalFeminine);
        buttonGroup.add(fractionalMasculine);
        buttonGroup.add(fractionalNeuter);
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
