package com.artigile.warehouse.gui.menuitems.details.stickers;

import com.artigile.warehouse.gui.core.wizard.Wizard;
import com.artigile.warehouse.gui.core.wizard.WizardContext;
import com.artigile.warehouse.gui.core.wizard.WizardListener;
import com.artigile.warehouse.gui.core.wizard.WizardStep;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Valery Barysok, 2013-07-31
 */
public class StickersPrintWizard implements Wizard {

    private List<WizardStep> steps;

    private WizardStep wizardStep;

    private StickerPrintWizardContext wizardContext = new StickerPrintWizardContext();

    private List<WizardListener> wizardListeners = new ArrayList<WizardListener>();

    public StickersPrintWizard(WizardStep... steps) {
        this.steps = new ArrayList<WizardStep>();
        for (WizardStep step : steps) {
            addStep(step);
        }
        this.wizardStep = steps.length > 0 ? steps[0] : null;
    }

    @Override
    public WizardContext getWizardContext() {
        return wizardContext;
    }

    @Override
    public List<WizardStep> getSteps() {
        return new ArrayList<WizardStep>(steps);
    }

    @Override
    public void addStep(WizardStep step) {
        steps.add(step);
        step.setWizard(this);
    }

    @Override
    public void removeStep(WizardStep step) {
        steps.remove(step);
        step.setWizard(null);
        if (getCurrentStep() == step) {
            setCurrentStep(null);
        }
    }

    @Override
    public WizardStep getCurrentStep() {
        return wizardStep;
    }

    @Override
    public boolean  setCurrentStep(WizardStep step) {
        if (wizardStep != step) {
            clearRange(step, wizardStep);
            wizardStep = step;
        }
        return true;
    }

    private void clearRange(WizardStep stepFrom, WizardStep stepTo) {
        int posFrom = steps.indexOf(stepFrom);
        int posTo = steps.indexOf(stepTo);
        for (int i = posFrom; i <= posTo; ++i) {
            steps.get(i).clearFieldValue();
        }
    }

    @Override
    public boolean canForward() {
        int pos = steps.indexOf(getCurrentStep());
        return pos != -1 && pos+1 < steps.size();
    }

    @Override
    public void forward() {
        int pos = steps.indexOf(getCurrentStep());
        setCurrentStep(steps.get(pos+1));
    }

    @Override
    public boolean canBackward() {
        int pos = steps.indexOf(getCurrentStep());
        return pos != -1 && pos-1 >= 0;
    }

    @Override
    public void backward() {
        int pos = steps.indexOf(getCurrentStep());
        setCurrentStep(steps.get(pos-1));
    }

    @Override
    public void addWizardListener(WizardListener wizardListener) {
        wizardListeners.add(wizardListener);
    }

    @Override
    public void removeWizardListener(WizardListener wizardListener) {
        wizardListeners.remove(wizardListener);
    }

    @Override
    public void fireWizardListeners() {
        for (WizardListener wizardListener : wizardListeners) {
            wizardListener.stateChanged(this);
        }
    }
}
