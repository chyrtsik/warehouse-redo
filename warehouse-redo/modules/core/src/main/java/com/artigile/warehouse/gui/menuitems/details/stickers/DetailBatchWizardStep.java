package com.artigile.warehouse.gui.menuitems.details.stickers;

import com.artigile.warehouse.gui.core.wizard.Wizard;
import com.artigile.warehouse.gui.core.wizard.WizardStep;
import com.artigile.warehouse.utils.SpringServiceContext;
import com.artigile.warehouse.utils.dto.details.DetailBatchTO;
import com.artigile.warehouse.utils.dto.details.DetailFieldTO;
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
 * @author Valery Barysok, 2013-10-16
 */
public class DetailBatchWizardStep implements WizardStep {

    private static final Dimension CAPTION_PREF_SIZE = new Dimension(-1, 50);

    private static final Dimension PREF_SIZE = new Dimension(-1, 100);

    private Wizard wizard;

    @Override
    public String getName() {
        return StickerPrintWizardUtils.getDisplayName(((StickerPrintWizardContext) wizard.getWizardContext()).getDetailBatchTO(), false);
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
        panel.setLayout(new VerticalLayout());
        panel.removeAll();

        final StickerPrintWizardContext wizardContext = (StickerPrintWizardContext) getWizard().getWizardContext();
        java.util.List<DetailBatchTO> availableDetailBatches = getAvailableDetailBatches(wizardContext.getDetailTypeTO().getId(), wizardContext.getFieldValues());
        if (availableDetailBatches.size() == 1 && wizardContext.getDetailBatchTO() == null) {
            wizardContext.setDetailBatchTO(availableDetailBatches.get(0));
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    goForward();
                }
            });
        } else {
            String option = I18nSupport.message("stickers.print.wizard.step.caption", I18nSupport.message("stickers.print.wizard.step.caption.detail.batch"));
            JXLabel detailFieldName = new JXLabel(option, SwingConstants.CENTER);
            detailFieldName.setPreferredSize(CAPTION_PREF_SIZE);
            panel.add(detailFieldName, SwingConstants.CENTER);

            JPanel options = new JXPanel();
            showOptions(options, availableDetailBatches);
            panel.add(options);
        }

        panel.revalidate();
        panel.repaint();
    }

    private List<DetailBatchTO> getAvailableDetailBatches(long detailTypeId, Map<DetailFieldTO, String> fieldValues) {
        SpringServiceContext serviceContext = SpringServiceContext.getInstance();
        return serviceContext.getDetailBatchesService().getAvailableDetailBatches(serviceContext.getDetailTypesService().getDetailTypeById(detailTypeId), fieldValues);
    }

    private void showOptions(JPanel panel, List<DetailBatchTO> detailBatches) {
        panel.setLayout(new GridLayout(5, 0));

        for (final DetailBatchTO detailBatchTO : detailBatches) {
            JXButton button = new JXButton(StickerPrintWizardUtils.getDisplayName(detailBatchTO, true));
            button.setPreferredSize(new Dimension(150, 50));
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    ((StickerPrintWizardContext) getWizard().getWizardContext()).setDetailBatchTO(detailBatchTO);
                    goForward();
                }
            });
            panel.add(button);
        }
    }

    @Override
    public void clearFieldValue() {
        ((StickerPrintWizardContext) wizard.getWizardContext()).setDetailBatchTO(null);
    }

    @Override
    public Dimension getPreferredSize() {
        return PREF_SIZE;
    }

    private void goForward() {
        Wizard wizard = getWizard();
        if (wizard.canForward()) {
            wizard.forward();
        }
        wizard.fireWizardListeners();
    }
}
