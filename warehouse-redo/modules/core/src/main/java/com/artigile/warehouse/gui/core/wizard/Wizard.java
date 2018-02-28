package com.artigile.warehouse.gui.core.wizard;

import java.util.List;

/**
 * @author Valery Barysok, 2013-07-30
 */
public interface Wizard {

    WizardContext getWizardContext();

    List<WizardStep> getSteps();

    void addStep(WizardStep step);

    void removeStep(WizardStep step);

    WizardStep getCurrentStep();

    boolean setCurrentStep(WizardStep step);

    boolean canForward();

    void forward();

    boolean canBackward();

    void backward();

    void addWizardListener(WizardListener wizardListener);

    void removeWizardListener(WizardListener wizardListener);

    void fireWizardListeners();
}
