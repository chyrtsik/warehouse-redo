package com.artigile.warehouse.gui.menuitems.warehouse.warehousebatch;

import com.artigile.warehouse.gui.core.properties.PropertiesForm;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidation;
import com.artigile.warehouse.gui.core.properties.data.validation.DataValidationException;
import com.artigile.warehouse.gui.utils.UIComponentUtils;
import com.artigile.warehouse.utils.dto.warehouse.WarehouseBatchTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * @author Aliaksandr.Chyrtsik, 8/30/12
 */
public class ChangeWarehouseBatchCountForm implements PropertiesForm {
    private JPanel contentPanel;
    private JTextField fieldOriginalCount;
    private JTextField fieldRealCount;
    private JLabel countMeas;
    private JLabel countMeas1;

    private WarehouseBatchTO warehouseBatchTO;
    private long realCount;

    public ChangeWarehouseBatchCountForm(WarehouseBatchTO warehouseBatchTO) {
        this.warehouseBatchTO = warehouseBatchTO;

        fieldOriginalCount.setFocusable(false);
        UIComponentUtils.makeSelectingAllTextOnFocus(fieldRealCount);
    }

    @Override
    public String getTitle() {
        return I18nSupport.message("warehousebatch.changeCount.title");
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
        String countMeasUnit = warehouseBatchTO.getDetailBatch().getCountMeas().getSign();
        countMeas.setText(countMeasUnit);
        countMeas1.setText(countMeasUnit);
        fieldOriginalCount.setText(String.valueOf(warehouseBatchTO.getCount()));
    }

    @Override
    public void validateData() throws DataValidationException {
        DataValidation.checkNotEmpty(fieldRealCount);
        DataValidation.checkIsNumberLong(fieldRealCount.getText(), fieldRealCount);
        DataValidation.checkValueMinLong(Long.parseLong(fieldRealCount.getText()), fieldRealCount, 0);
    }

    @Override
    public void saveData() {
        realCount = Long.parseLong(fieldRealCount.getText());
    }

    public long getCountDiff() {
        return realCount - warehouseBatchTO.getCount();
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
        contentPanel.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        this.$$$loadLabelText$$$(label1, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.changeCount.originalCount"));
        contentPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldOriginalCount = new JTextField();
        fieldOriginalCount.setEditable(false);
        contentPanel.add(fieldOriginalCount, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), new Dimension(100, -1), 0, false));
        countMeas = new JLabel();
        countMeas.setText("<Meas>");
        contentPanel.add(countMeas, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        this.$$$loadLabelText$$$(label2, ResourceBundle.getBundle("i18n/warehouse").getString("warehousebatch.changeCount.realCount"));
        contentPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        fieldRealCount = new JTextField();
        contentPanel.add(fieldRealCount, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), new Dimension(100, -1), 0, false));
        countMeas1 = new JLabel();
        countMeas1.setText("<Meas>");
        contentPanel.add(countMeas1, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        label1.setLabelFor(fieldOriginalCount);
        label2.setLabelFor(fieldRealCount);
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
