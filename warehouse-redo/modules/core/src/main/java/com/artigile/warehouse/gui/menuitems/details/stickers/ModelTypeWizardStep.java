package com.artigile.warehouse.gui.menuitems.details.stickers;

import com.artigile.warehouse.bl.detail.DetailTypeService;
import com.artigile.warehouse.bl.sticker.StickerPrintParamService;
import com.artigile.warehouse.domain.details.DetailFieldType;
import com.artigile.warehouse.gui.core.wizard.Wizard;
import com.artigile.warehouse.gui.core.wizard.WizardStep;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldValueTO;
import com.artigile.warehouse.utils.dto.details.DetailTypeTO;
import com.artigile.warehouse.utils.dto.sticker.StickerPrintParamTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

/**
 * @author Valery Barysok, 2013-07-31
 */
public class ModelTypeWizardStep implements WizardStep {

    private static final Dimension CAPTION_PREF_SIZE = new Dimension(-1, 50);

    private static final Dimension PREF_SIZE = new Dimension(-1, 50);

    private Wizard wizard;

    @Override
    public Wizard getWizard() {
        return wizard;
    }

    @Override
    public String getName() {
        if (wizard != null) {
            StickerPrintWizardContext wizardContext = (StickerPrintWizardContext) wizard.getWizardContext();
            return I18nSupport.message("stickers.print.wizard.select.detail.type", wizardContext.getDetailTypeTO() != null ?
                    wizardContext.getDetailTypeTO().getName() : I18nSupport.message("stickers.print.wizard.select.detail.type.not.selected"));
        }
        else {
            return "";
        }
    }

    @Override
    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    @Override
    public void updatePanel(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new VerticalLayout());

        String option = I18nSupport.message("stickers.print.wizard.step.caption", I18nSupport.message("stickers.print.wizard.step.caption.detail.type"));
        JXLabel detailFieldName = new JXLabel(option, SwingConstants.CENTER);
        detailFieldName.setPreferredSize(CAPTION_PREF_SIZE);
        panel.add(detailFieldName, SwingConstants.CENTER);

        JPanel options = new JXPanel();
        showOptions(options);
        panel.add(options);

        panel.revalidate();
        panel.repaint();
    }

    private void showOptions(JPanel panel) {
        panel.setLayout(new VerticalLayout());
        final SpringServiceContext serviceContext = SpringServiceContext.getInstance();
        final DetailTypeService detailTypesService = serviceContext.getDetailTypesService();
        List<DetailTypeTO> detailTypes = detailTypesService.getAllDetailTypesFull();
        for (final DetailTypeTO detailType : detailTypes) {
            JXButton button = new JXButton(detailType.getName());
            button.setPreferredSize(PREF_SIZE);
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    StickerPrintParamService stickerPrintParamService = serviceContext.getStickerPrintParamService();
                    updateWizardSteps(detailType, stickerPrintParamService.getStickerPrintParamsBy(detailType.getId()));
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
    public void clearFieldValue() {
        ((StickerPrintWizardContext) wizard.getWizardContext()).setDetailTypeTO(null);
    }

    @Override
    public Dimension getPreferredSize() {
        return PREF_SIZE;
    }

    private void updateWizardSteps(DetailTypeTO detailType, List<StickerPrintParamTO> stickerPrintParams) {
        StickerPrintWizardContext wizardContext = (StickerPrintWizardContext) wizard.getWizardContext();
        DetailTypeTO detailTypeTO = wizardContext.getDetailTypeTO();
        if (!(detailTypeTO == detailType || detailTypeTO != null && detailTypeTO.equals(detailType))) {
            wizardContext.setDetailTypeTO(detailType);
            updateSerialFieldValues(wizardContext, detailType);
            wizard.setCurrentStep(this);
            List<WizardStep> steps = wizard.getSteps();
            for (WizardStep step : steps) {
                if (step != this) {
                    wizard.removeStep(step);
                }
            }
            for (StickerPrintParamTO stickerPrintParam : stickerPrintParams) {
                DetailFieldTO detailField = stickerPrintParam.getDetailField();
                if (detailField != null) {
                    wizard.addStep(new DetailFieldWizardStep(detailField));
                } else {
                    wizard.addStep(new SerialDetailFieldWizardStep(stickerPrintParam.getSerialDetailField()));
                }
            }
            wizard.addStep(new DetailBatchWizardStep());
            wizard.addStep(new StickerPrintWizardStep());
        }
    }

    private void updateSerialFieldValues(StickerPrintWizardContext wizardContext, DetailTypeTO detailTypeTO) {
        Map<DetailFieldTO,String> serialFieldValues = wizardContext.getSerialFieldValues();
        serialFieldValues.clear();
        List<DetailFieldTO> serialNumberFields = detailTypeTO.getSerialNumberFields();
        for (DetailFieldTO detailField : serialNumberFields) {
            DetailFieldType type = detailField.getType();
            if (!type.isEditableByUser()) {
                DetailFieldValueTO detailFieldValueTO = new DetailFieldValueTO(detailField, null);
                if (detailFieldValueTO.isCalculated()) {
                    serialFieldValues.put(detailField, detailFieldValueTO.getValue());
                }
            }
        }
    }
}
