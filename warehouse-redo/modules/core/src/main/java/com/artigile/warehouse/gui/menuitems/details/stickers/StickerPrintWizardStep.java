package com.artigile.warehouse.gui.menuitems.details.stickers;

import com.artigile.warehouse.bl.detail.DetailSerialNumberService;
import com.artigile.warehouse.gui.core.print.PrintFacade;
import com.artigile.warehouse.gui.core.wizard.Wizard;
import com.artigile.warehouse.gui.core.wizard.WizardStep;
import com.artigile.warehouse.gui.menuitems.details.batches.DetailBatchesPrintableObject;
import com.artigile.warehouse.gui.menuitems.details.serialnumbers.SerialNumbersForPrinting;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.*;
import com.artigile.warehouse.utils.dto.template.PrintTemplateInstanceTO;
import com.artigile.warehouse.utils.i18n.I18nSupport;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.VerticalLayout;

import javax.print.PrintException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Valery Barysok, 2013-08-12
 */
public class StickerPrintWizardStep implements WizardStep {

    private static final Dimension CAPTION_PREF_SIZE = new Dimension(-1, 100);

    private static final Dimension PREF_SIZE = new Dimension(-1, 50);

    private Wizard wizard;

    @Override
    public String getName() {
        return I18nSupport.message("stickers.print.wizard.title");
    }

    @Override
    public Wizard getWizard() {
        return wizard;
    }

    @Override
    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }

    @Override
    public void updatePanel(JPanel panel) {
        panel.removeAll();
        panel.setLayout(new VerticalLayout());

        String option = I18nSupport.message("stickers.print.wizard.step.caption",
                StickerPrintWizardUtils.getDisplayName(((StickerPrintWizardContext) wizard.getWizardContext()).getDetailBatchTO(), true));
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

        JXButton button = new JXButton(I18nSupport.message("stickers.print.wizard.title"));
        button.setPreferredSize(PREF_SIZE);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doPrintStickers();
            }
        });
        panel.add(button);
    }

    @Override
    public void clearFieldValue() {
        // to do nothing
    }

    @Override
    public Dimension getPreferredSize() {
        return PREF_SIZE;
    }

    private void doPrintStickers() {
        final StickerPrintWizardContext wizardContext = (StickerPrintWizardContext) getWizard().getWizardContext();
        DetailTypeTO detailTypeTO = wizardContext.getDetailTypeTO();
        DetailBatchTO detailBatchTO = wizardContext.getDetailBatchTO();
        if (detailTypeTO.getPrintSerialNumbers()) {
            Map<DetailFieldTO, String> serialFieldValues = wizardContext.getSerialFieldValues();
            DetailSerialNumberService detailSerialNumberService = SpringServiceContext.getInstance().getDetailSerialNumberService();
            List<DetailSerialNumberTO> detailSerialNumbers = detailSerialNumberService.getDetailSerialNumbers(detailBatchTO.getId(), serialFieldValues);
            int size = detailSerialNumbers.size();
            if (size == 0) {
                DetailSerialNumberTO detailSerialNumberTO = createDetailSerialNumberTO(detailTypeTO, detailBatchTO, serialFieldValues);
                detailSerialNumberService.saveSerialNumber(detailSerialNumberTO);
                printSerialNumber(detailSerialNumberTO, detailTypeTO);
            }
            else if (size == 1) {
                printSerialNumber(detailSerialNumbers.get(0), detailTypeTO);
            }
            else {
                // TODO:
            }
        }
        else {
            printDetailBatch(detailBatchTO, detailTypeTO);
        }
    }

    private void printSerialNumber(DetailSerialNumberTO detailSerialNumber, DetailTypeTO detailType) {
        try {
            PrintTemplateInstanceTO printTemplateInstance = detailType.getPrintTemplateInstance();
            if (printTemplateInstance != null) {
                PrintFacade.printDefault(new SerialNumbersForPrinting(Arrays.asList(detailSerialNumber)), printTemplateInstance.getId());
            }
        } catch (PrintException e) {
            e.printStackTrace();
        }
    }

    private void printDetailBatch(DetailBatchTO detailBatch, DetailTypeTO detailType) {
        try {
            PrintTemplateInstanceTO printTemplateInstance = detailType.getPrintTemplateInstance();
            if (printTemplateInstance != null) {
                PrintFacade.printDefault(new DetailBatchesPrintableObject(Arrays.asList(detailBatch)), printTemplateInstance.getId());
            }
        } catch (PrintException e) {
            e.printStackTrace();
        }
    }

    private DetailSerialNumberTO createDetailSerialNumberTO(DetailTypeTO detailTypeTO, DetailBatchTO detailBatchTO, Map<DetailFieldTO, String> serialFieldValues) {
        DetailSerialNumberTO detailSerialNumberTO = new DetailSerialNumberTO();
        detailSerialNumberTO.setDetail(detailBatchTO);
        List<DetailFieldValueTO> fields = new ArrayList<DetailFieldValueTO>();
        for (DetailFieldTO field : detailTypeTO.getSerialNumberFieldsInDisplayOrder()) {
            if (serialFieldValues.containsKey(field)) {
                fields.add(new DetailFieldValueTO(field, detailBatchTO.getModel(), serialFieldValues.get(field)));
            }
            else {
                fields.add(new DetailFieldValueTO(field, detailBatchTO.getModel()));
            }
        }
        detailSerialNumberTO.setFields(fields);

        return detailSerialNumberTO;
    }
}
