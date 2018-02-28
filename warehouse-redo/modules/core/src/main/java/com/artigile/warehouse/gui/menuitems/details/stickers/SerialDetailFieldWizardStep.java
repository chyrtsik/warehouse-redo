package com.artigile.warehouse.gui.menuitems.details.stickers;

import com.artigile.warehouse.domain.details.DetailFieldType;
import com.artigile.warehouse.gui.core.wizard.Wizard;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.jdesktop.swingx.JXButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Valery Barysok, 2013-10-13
 */
public class SerialDetailFieldWizardStep extends AbstractDetailFieldWizardStep {

    private JTextField countInPackaging;

    public SerialDetailFieldWizardStep(DetailFieldTO detailFieldTO) {
        super(detailFieldTO);
    }

    private void createKeyboard(JPanel panel) {
        GridLayout grid = new GridLayout(2, 1);
        grid.setVgap(5);
        JPanel fields = new JPanel(grid);
        fields.setOpaque(false);
        countInPackaging = new JTextField();
        fields.add(countInPackaging);

        GridLayout buttonGrid = new GridLayout(4, 3);
        JPanel buttons = new JPanel(buttonGrid);
        for (int i = 0; i <= 9; ++i) {
            if (i == 9) {
                JXButton del = new JXButton(I18nSupport.message("keyboard.delete"));
                del.addActionListener(new DelPadActionListener(countInPackaging));
                buttons.add(del);
            }
            int num = (i + 1) % 10;
            JXButton button = new JXButton("" + num);
            button.addActionListener(new NumberPadActionListener(countInPackaging, num));
            buttons.add(button);
            if (i == 9) {
                JXButton enter = new JXButton(I18nSupport.message("keyboard.enter"));
                enter.addActionListener(new EnterPadActionListener(countInPackaging));
                buttons.add(enter);
            }
        }

        fields.add(buttons);
        panel.add(fields);
    }

    @Override
    protected void showOptions(JPanel panel, List<String> availableFieldValues, DetailFieldType detailFieldType) {
        if (getDetailFieldTO().getType().equals(DetailFieldType.COUNT_IN_PACKAGING)) {
            panel.setLayout(new GridLayout(4, 0));
            createKeyboard(panel);

            for (final String value : availableFieldValues) {
                JXButton button = new JXButton(DetailFieldValueTO.getDisplayValue(value, detailFieldType, I18nSupport.message("stickers.print.wizard.select.detail.field.value.not.specified"), true));
                button.setPreferredSize(new Dimension(150, 50));
                button.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        getFieldValues().put(getDetailFieldTO(), value);
                        Wizard wizard = getWizard();
                        if (wizard.canForward()) {
                            wizard.forward();
                        }
                        wizard.fireWizardListeners();
                    }
                });
                panel.add(button);
            }
        } else {
            super.showOptions(panel, availableFieldValues, detailFieldType);
        }
    }

    @Override
    protected List<String> getAvailableDetailFieldValues(long detailTypeId, DetailFieldTO detailField, Map<DetailFieldTO, String> fieldValues, Map<DetailFieldTO, String> serialFieldValues) {
        switch (detailField.getType()) {
            case BOOLEAN: return getBooleanOptions(detailField);
            case COUNT_IN_PACKAGING: return getCountInPackagingOptions(detailTypeId, detailField, fieldValues, serialFieldValues);
            case ENUM: return getEnumOptions(detailField);
            default: return Collections.emptyList();
        }
    }

    @Override
    protected Map<DetailFieldTO, String> getFieldValues() {
        return ((StickerPrintWizardContext) getWizard().getWizardContext()).getSerialFieldValues();
    }

    private List<String> getBooleanOptions(DetailFieldTO detailField) {
        List<String> result = new ArrayList<String>();
        if (!detailField.getMandatory()) {
            result.add(null);
        }
        result.add(I18nSupport.message("detail.field.value.yes"));
        result.add(I18nSupport.message("detail.field.value.no"));
        return result;
    }

    private List<String> getCountInPackagingOptions(long detailTypeId, DetailFieldTO detailField, Map<DetailFieldTO, String> fieldValues, Map<DetailFieldTO, String> serialFieldValues) {
        return SpringServiceContext.getInstance().getDetailSerialNumberService().getLastUsedValues(detailTypeId, detailField, fieldValues, serialFieldValues, 10);
    }

    private List<String> getEnumOptions(DetailFieldTO detailField) {
        List<String> result = new ArrayList<String>(detailField.getEnumValues());
        if (!detailField.getMandatory()) {
            result.add(0, null);
        }
        return result;
    }

    @Override
    public void clearFieldValue() {
        ((StickerPrintWizardContext) getWizard().getWizardContext()).getSerialFieldValues().remove(getDetailFieldTO());
    }

    private class NumberPadActionListener implements ActionListener {

        private JTextField countInPackaging;

        private int num;

        private NumberPadActionListener(JTextField countInPackaging, int num) {
            this.countInPackaging = countInPackaging;
            this.num = num;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            countInPackaging.setText(countInPackaging.getText() + num);
        }
    }

    private class DelPadActionListener implements ActionListener {

        private JTextField countInPackaging;

        private DelPadActionListener(JTextField countInPackaging) {
            this.countInPackaging = countInPackaging;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String text = countInPackaging.getText();
            if (text.length() > 0) {
                countInPackaging.setText(text.substring(0, text.length() - 1));
            }
        }
    }

    private class EnterPadActionListener implements ActionListener {

        private JTextField countInPackaging;

        private EnterPadActionListener(JTextField countInPackaging) {
            this.countInPackaging = countInPackaging;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String text = countInPackaging.getText();
            if (!text.isEmpty()) {
                getFieldValues().put(getDetailFieldTO(), text);
                Wizard wizard = getWizard();
                if (wizard.canForward()) {
                    wizard.forward();
                }
                wizard.fireWizardListeners();
            }
        }
    }
}
