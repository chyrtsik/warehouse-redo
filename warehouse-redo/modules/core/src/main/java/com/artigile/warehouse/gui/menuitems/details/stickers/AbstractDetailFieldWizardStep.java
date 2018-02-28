package com.artigile.warehouse.gui.menuitems.details.stickers;

import com.artigile.warehouse.domain.details.DetailFieldType;
import com.artigile.warehouse.gui.core.wizard.Wizard;
import com.artigile.warehouse.gui.core.wizard.WizardStep;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * @author Valery Barysok, 2013-10-13
 */
public abstract class AbstractDetailFieldWizardStep implements WizardStep {

    private static final Dimension CAPTION_PREF_SIZE = new Dimension(-1, 50);

    private static final Dimension PREF_SIZE = new Dimension(-1, 50);

    private DetailFieldTO detailFieldTO;

    private Wizard wizard;

    public AbstractDetailFieldWizardStep(DetailFieldTO detailFieldTO) {
        this.detailFieldTO = detailFieldTO;
    }

    @Override
    public Wizard getWizard() {
        return wizard;
    }

    @Override
    public String getName() {
        Map<DetailFieldTO, String> fieldValues = getFieldValues();
        String value = fieldValues.containsKey(detailFieldTO) ?
                DetailFieldValueTO.getDisplayValue(fieldValues.get(detailFieldTO), detailFieldTO.getType(), I18nSupport.message("stickers.print.wizard.select.detail.field.value.not.specified"), true)
                : I18nSupport.message("stickers.print.wizard.select.detail.field.value.not.selected");
        return I18nSupport.message("stickers.print.wizard.select.detail.field.value", detailFieldTO.getName(), value);
    }

    @Override
    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    public DetailFieldTO getDetailFieldTO() {
        return detailFieldTO;
    }

    @Override
    public void updatePanel(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new VerticalLayout());
        String option = I18nSupport.message("stickers.print.wizard.step.caption", detailFieldTO.getName());
        JXLabel detailFieldName = new JXLabel(option, SwingConstants.CENTER);
        detailFieldName.setPreferredSize(CAPTION_PREF_SIZE);
        panel.add(detailFieldName, SwingConstants.CENTER);
        JPanel options = new JXPanel();
        panel.add(options);
        final StickerPrintWizardContext wizardContext = (StickerPrintWizardContext) getWizard().getWizardContext();
        final DetailFieldTO detailFieldTO = getDetailFieldTO();
        java.util.List<String> availableFieldValues = getAvailableDetailFieldValues(wizardContext.getDetailTypeTO().getId(), detailFieldTO, wizardContext.getFieldValues(), wizardContext.getSerialFieldValues());
        showOptions(options, availableFieldValues, detailFieldTO.getType());

        panel.revalidate();
        panel.repaint();
    }

    protected void showOptions(JPanel panel, java.util.List<String> availableFieldValues, DetailFieldType detailFieldType) {
        panel.setLayout(new GridLayout(5, 0));

        for (final String value : availableFieldValues) {
            JXButton button = new JXButton(DetailFieldValueTO.getDisplayValue(value, detailFieldType, I18nSupport.message("stickers.print.wizard.select.detail.field.value.not.specified"), true));
            button.setPreferredSize(new Dimension(150, 50));
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    getFieldValues().put(detailFieldTO, value);
                    Wizard wizard = getWizard();
                    if (wizard.canForward()) {
                        wizard.forward();
                    }
                    wizard.fireWizardListeners();
                }
            });
            panel.add(button);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return PREF_SIZE;
    }

    protected abstract java.util.List<String> getAvailableDetailFieldValues(long detailTypeId, DetailFieldTO detailField, Map<DetailFieldTO, String> fieldValues, Map<DetailFieldTO, String> serialFieldValues);

    protected abstract Map<DetailFieldTO, String> getFieldValues();
}
